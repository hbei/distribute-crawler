/**
 * 
 */
package io.github.liuzm.distribute.remoting.netty;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.common.util.ZKUtil;
import io.github.liuzm.distribute.registy.RegistryNode;
import io.github.liuzm.distribute.registy.RegistryNodeFactory;
import io.github.liuzm.distribute.registy.impl.DefaultRegistryNodeFactory;
import io.github.liuzm.distribute.remoting.InvokeCallback;
import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.RemotingClient;
import io.github.liuzm.distribute.remoting.cache.ChannelManager;
import io.github.liuzm.distribute.remoting.common.Pair;
import io.github.liuzm.distribute.remoting.common.RemotingHelper;
import io.github.liuzm.distribute.remoting.exception.RemotingConnectException;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.exception.RemotingTooMuchRequestException;
import io.github.liuzm.distribute.remoting.netty.codec.NettyDecoder;
import io.github.liuzm.distribute.remoting.netty.codec.NettyEncoder;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.remoting.protocol.header.AckCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author xh-liuzhimin
 *
 */
public class NettyRemotingClient extends AbstractRemoting implements RemotingClient {

	private static final Logger logger = LoggerFactory.getLogger(NettyRemotingClient.class);

	private final ClientConfig nettyClientConfig;
	private final Bootstrap bootstrap = new Bootstrap();
	private final EventLoopGroup eventLoopGroupWorker;
	private final ExecutorService publicExecutor;
	private final Timer timer = new Timer("ClientHouseKeepingService", true);

	public NettyRemotingClient(final ClientConfig nettyClientConfig) {
		super(1, null);
		this.nettyClientConfig = nettyClientConfig;
		this.eventLoopGroupWorker = new NioEventLoopGroup();

		this.publicExecutor = Executors.newFixedThreadPool(4, new ThreadFactory() {
			private AtomicInteger threadIndex = new AtomicInteger(0);

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "ClientPublicExecutor_" + this.threadIndex.incrementAndGet());
			}
		});

	}

	@Override
	public void start() {
		Bootstrap b = this.bootstrap.group(this.eventLoopGroupWorker).channel(NioSocketChannel.class)//
				.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize())
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(
								new NettyEncoder(), 
								new NettyDecoder(),
								new IdleStateHandler(0, 0, nettyClientConfig.getClientChannelMaxIdleTimeSeconds()), //
								new NettyConnetManageHandler(), 
								new ClientHandler());
					}
				});
		// 从zookeeper上获取server的建立连接;目前默认是从服务端第一个节点建立链接
		if (ZKUtil.exists(Config.ZKPath.REGISTER_SERVER_PATH)) {
			List<String> servers;
			try {
				servers = ZKUtil.getChildren(Config.ZKPath.REGISTER_SERVER_PATH);
				if (servers != null && servers.size() > 0) {
					Node node = (Node) JSONObject.parseObject(
							(ZKUtil.getPathData(Config.ZKPath.REGISTER_SERVER_PATH + "/" + servers.get(0))),
							Node.class);
					ChannelFuture future = b.connect(new InetSocketAddress(node.getIpaddress(), node.getPort())).sync();
					// 建立链接完成后
					ChannelManager.put(getRegistryNode().getNode().getId(), future.channel());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 扫描response响应
		this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    NettyRemotingClient.this.scanResponseTable();
                } catch (Exception e) {
                    logger.error("scanResponseTable exception", e);
                }
            }
        }, 1000 * 3, 1000);
	}

	@Override
	public void shutdown() {
		try {
			this.eventLoopGroupWorker.shutdownGracefully();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ExecutorService getOwnExecutor() {
		return this.publicExecutor;
	}

	class ClientHandler extends SimpleChannelInboundHandler<Command> {

		@Override
		public void channelRead0(ChannelHandlerContext ctx, Command request) throws Exception {
				processMessageReceived(ctx, request);
		}
	}

	class NettyConnetManageHandler extends ChannelDuplexHandler {

		@Override
		public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
				ChannelPromise promise) throws Exception {
			final String local = localAddress == null ? "UNKNOW" : localAddress.toString();
			final String remote = remoteAddress == null ? "UNKNOW" : remoteAddress.toString();
			logger.info("NETTY CLIENT PIPELINE: CONNECT  {} => {}", local, remote);
			super.connect(ctx, remoteAddress, localAddress, promise);
		}

		@Override
		public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
			final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
			closeChannel(ctx.channel());
			super.disconnect(ctx, promise);
			logger.info("NETTY CLIENT PIPELINE: disCONNECT  {} => {}", remoteAddress);

		}

		@Override
		public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
			final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
			logger.info("NETTY CLIENT PIPELINE: CLOSE {}", remoteAddress);
			closeChannel(ctx.channel());
			super.close(ctx, promise);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
			logger.warn("NETTY CLIENT PIPELINE: exceptionCaught {}", remoteAddress);
			logger.warn("NETTY CLIENT PIPELINE: exceptionCaught exception.", cause);
			closeChannel(ctx.channel());
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt instanceof IdleStateEvent) {
				IdleStateEvent evnet = (IdleStateEvent) evt;
				if (evnet.state().equals(IdleState.ALL_IDLE)) {
					final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
					logger.warn("NETTY CLIENT PIPELINE: IDLE exception [{}]", remoteAddress);
					ctx.channel().writeAndFlush(Command.createRequestCommand(HeaderMessageCode.ACK_COMMAND,
							new AckCommand(registryNode.getNode().getId(), 2)));
				}
			}

			ctx.fireUserEventTriggered(evt);
		}
	}

	@Override
	public void registerProcessor(int requestCode, Processor processor, ExecutorService executor) {
		ExecutorService executorThis = executor;
		if (null == executor) {
			executorThis = this.publicExecutor;
		}

		Pair<Processor, ExecutorService> pair = new Pair<Processor, ExecutorService>(processor, executorThis);
		this.processorTable.put(requestCode, pair);
	}

	@Override
	public Command invokeSync(String nodeId, Command request, long timeoutMillis) throws InterruptedException,
			RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException {
		final Channel channel = ChannelManager.get(nodeId);
		Command response = this.invokeSyncImpl(channel, request, timeoutMillis);
		return response;
	}

	/**
	 * 异步应答
	 */
	@Override
	public void invokeAsync(String nodeId, Command request, long timeoutMillis, InvokeCallback invokeCallback)
			throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,
			RemotingTimeoutException, RemotingSendRequestException {
		final Channel channel = ChannelManager.get(nodeId);
		this.invokeAsyncImpl(channel, request, timeoutMillis, invokeCallback);
	}

	public void closeChannel(final Channel channel) {
		if (null == channel)
			return;
		ChannelManager.disConnect(registryNode.getNode().getId());
	}

	@Override
	public boolean isChannelWriteable(String addr) {
		if (ChannelManager.get(addr) != null) {
			return true;
		}
		return false;
	}

	@Override
	public RegistryNodeFactory getRegisterNodeFactory() {
		return new DefaultRegistryNodeFactory();
	}

	@Override
	public RegistryNode getRegistryNode() {
		return registryNode;
	}
	
	@Override
    public ExecutorService getCallbackExecutor() {
        return this.publicExecutor;
    }
}
