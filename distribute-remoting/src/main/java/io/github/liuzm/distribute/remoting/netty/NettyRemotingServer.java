package io.github.liuzm.distribute.remoting.netty;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.registy.Registry;
import io.github.liuzm.distribute.remoting.InvokeCallback;
import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.RemotingServer;
import io.github.liuzm.distribute.remoting.common.Pair;
import io.github.liuzm.distribute.remoting.common.RemotingHelper;
import io.github.liuzm.distribute.remoting.common.RemotingUtil;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.exception.RemotingTooMuchRequestException;
import io.github.liuzm.distribute.remoting.netty.codec.NettyDecoder;
import io.github.liuzm.distribute.remoting.netty.codec.NettyEncoder;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

public class NettyRemotingServer extends AbstractRemoting implements RemotingServer {

	private static final Logger logger = LoggerFactory.getLogger(NettyRemotingServer.class);

	private DefaultEventExecutorGroup defaultEventExecutorGroup;
	private final ServerBootstrap serverBootstrap;
	private final EventLoopGroup eventLoopGroupWorker;
	private final EventLoopGroup eventLoopGroupBoss;
	private final ServerConfig nettyServerConfig;
	
	// 处理Callback应答器
    private final ExecutorService publicExecutor;
    
	protected final HashMap<Integer, Pair<Processor, ExecutorService>> processorTable =
            new HashMap<Integer, Pair<Processor, ExecutorService>>(64);
	
	private int port = 0;
	
	public NettyRemotingServer(final ServerConfig nettyServerConfig,final Node node,final Registry register) {
		super(node,register);
		this.serverBootstrap = new ServerBootstrap();
		this.nettyServerConfig = nettyServerConfig;
		this.eventLoopGroupBoss = new NioEventLoopGroup();
		this.eventLoopGroupWorker = new NioEventLoopGroup();
		
		this.publicExecutor = Executors.newFixedThreadPool(4, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "NettyServerPublicExecutor_" + this.threadIndex.incrementAndGet());
            }
        });
	}
	
	@Override
	public void start() {
		 this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(//
        nettyServerConfig.getServerWorkerThreads(), //
        	new ThreadFactory() {
            	private AtomicInteger threadIndex = new AtomicInteger(0);
            	@Override
            	public Thread newThread(Runnable r) {
            		return new Thread(r, "NettyServerWorkerThread_" + this.threadIndex.incrementAndGet());
            	}
        });
		 
		this.serverBootstrap.group(eventLoopGroupBoss, eventLoopGroupWorker)
		 .channel(NioServerSocketChannel.class)
		 .option(ChannelOption.SO_BACKLOG, 1024)
		 .option(ChannelOption.SO_REUSEADDR, true)
		 .option(ChannelOption.SO_KEEPALIVE, false)
		 .childOption(ChannelOption.TCP_NODELAY, true)
		 .option(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSndBufSize())
		 .localAddress(new InetSocketAddress(this.nettyServerConfig.getListenPort()))
		 .childHandler(new ChannelInitializer<SocketChannel>() {
             @Override
             public void initChannel(SocketChannel ch) throws Exception {
                 ch.pipeline().addLast(
                     defaultEventExecutorGroup,
                     new NettyEncoder(), 
                     new NettyDecoder(), 
                     new IdleStateHandler(0, 0, nettyServerConfig.getServerChannelMaxIdleTimeSeconds()),
                     new NettyConnetManageHandler(),
                     new ServerHandler());
             }
         });

		try {
            ChannelFuture sync = this.serverBootstrap.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
            this.port = addr.getPort();
            logger.info("Server started " + new Date() + "listen ip " + addr +
            		" listen port " +this.bindLocalListenerPort());
        }catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
        }
		
	}
	
	@Override
	public void shutdown() {
		try {

            this.eventLoopGroupBoss.shutdownGracefully();
            this.eventLoopGroupWorker.shutdownGracefully();

            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }
        }catch (Exception e) {
            logger.error("NettyRemotingServer shutdown exception, ", e);
        }

        if (this.defaultExecutor != null) {
            try {
            	this.defaultExecutor.shutdown();
            }catch (Exception e) {
                logger.error("NettyRemotingServer shutdown exception, ", e);
            }
        }
	}
	
	@Override
    public void registerDefaultProcessor(Processor processor, ExecutorService executor) {
        this.defaultProcessor = new Pair<Processor, ExecutorService>(processor, executor);
    }
	
	@Override
	public void registerProcessor(int requestCode, Processor processor, ExecutorService executor) {
		ExecutorService executorThis = executor;
        if (null == executor) {
            executorThis = this.publicExecutor;
        }

        Pair<Processor, ExecutorService> pair =
                new Pair<Processor, ExecutorService>(processor, executorThis);
        this.processorTable.put(requestCode, pair);
	}

	@Override
	public int bindLocalListenerPort() {
		port = nettyServerConfig.getListenPort();
		return port;
	}

	@Override
	public Command invokeSync(Channel channel, Command request, long timeoutMillis)
			throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException {
		return this.invokeSyncImpl(channel, request, timeoutMillis);
	}

	@Override
	public ExecutorService getOwnExecutor() {
		return Executors.newFixedThreadPool(5, new ThreadFactory() {
			private AtomicInteger threadIndex = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "NettyServerRequestExecutor_" + this.threadIndex.incrementAndGet());
			}
		});
	}
	
	
	
	public class ServerHandler extends ChannelInboundHandlerAdapter  {
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object request) throws Exception {
			System.out.println("server >>>> "+request);
			if(request instanceof Command){
				Command c = (Command)request;
				processMessageReceived(ctx, c);
			}
		}
		
		@Override
	    public void channelReadComplete(ChannelHandlerContext ctx) {
	        ctx.flush();
	    }
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.warn("Unexpected exception from downstream.", cause);
			ctx.close();
		}
	}
	
	class NettyConnetManageHandler extends ChannelDuplexHandler {
		
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            logger.info("netty remoting recived connection >>"+remoteAddress);
            super.channelRegistered(ctx);
        }


        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            logger.info("netty remoting recived disconnection >>  " + remoteAddress);
            super.channelUnregistered(ctx);
        }


        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            logger.info("netty remoting recived activeconnection >>  " + remoteAddress);
            super.channelActive(ctx);

        }


        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            super.channelInactive(ctx);

        }


        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent evnet = (IdleStateEvent) evt;
                if (evnet.state().equals(IdleState.ALL_IDLE)) {
                    final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    RemotingUtil.closeChannel(ctx.channel());
                }
            }

            ctx.fireUserEventTriggered(evt);
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());

            RemotingUtil.closeChannel(ctx.channel());
        }
    }
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @return the nettyServerConfig
	 */
	public ServerConfig getNettyServerConfig() {
		return nettyServerConfig;
	}
	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public Pair<Processor, ExecutorService> getProcessorPair(int requestCode) {
		 return processorTable.get(requestCode);
	}

	@Override
	public void invokeAsync(Channel channel, Command request, long timeoutMillis, InvokeCallback invokeCallback)
			throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException,
			RemotingSendRequestException {
		this.invokeAsyncImpl(channel, request, timeoutMillis, invokeCallback);
	}
	
}
