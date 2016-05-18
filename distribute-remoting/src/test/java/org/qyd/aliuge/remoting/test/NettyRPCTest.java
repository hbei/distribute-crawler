/**
 * 
 */
package org.qyd.aliuge.remoting.test;

import java.util.concurrent.Executors;

import org.junit.Test;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.common.zookeeper.ZkClient;
import io.github.liuzm.distribute.registy.Registry;
import io.github.liuzm.distribute.registy.impl.DefaultRegistry;
import io.github.liuzm.distribute.remoting.InvokeCallback;
import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.RemotingClient;
import io.github.liuzm.distribute.remoting.RemotingServer;
import io.github.liuzm.distribute.remoting.cache.ChannelManager;
import io.github.liuzm.distribute.remoting.exception.RemotingConnectException;
import io.github.liuzm.distribute.remoting.exception.RemotingException;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.netty.ClientConfig;
import io.github.liuzm.distribute.remoting.netty.FutureResponse;
import io.github.liuzm.distribute.remoting.netty.NettyRemotingClient;
import io.github.liuzm.distribute.remoting.netty.NettyRemotingServer;
import io.github.liuzm.distribute.remoting.netty.ServerConfig;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.remoting.protocol.header.SSSDComandHeader;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xh-liuzhimin
 *
 */
public class NettyRPCTest {
	
	public static RemotingClient createRemotingClient() {
        ClientConfig config = new ClientConfig();
        Node node = new Node();
        node.setType(1);
        Registry register = new DefaultRegistry(node);
        
        RemotingClient client = new NettyRemotingClient(config,node,register);
        
        client.registerProcessor(HeaderMessageCode.ACK_COMMAND, new Processor() {
			@Override
			public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
				System.out.println("client processor >>>>" + c);
				// 客户端接收到ack消息，进行channel处理.响应服务端的请求
				return Command.createResponseCommand(HeaderMessageCode.CONSUMER_SEND_MSG_BACK, "i am a client I have recived your message !!!");
			}
		}, Executors.newCachedThreadPool());
        client.start();
        
        return client;
    }


    public static RemotingServer createRemotingServer() throws InterruptedException {
        ServerConfig config = new ServerConfig();
        Node node = new Node();
        node.setType(0);
        Registry register = new DefaultRegistry(node);
        RemotingServer remotingServer = new NettyRemotingServer(config,node,register);
        
        remotingServer.registerDefaultProcessor(new Processor() {
			@Override
			public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
				// 如果是客户端返回的ack消息，进行channel处理
				if(c.getCode() == HeaderMessageCode.CONSUMER_SEND_MSG_BACK){
					System.out.println(ctx.channel());
				}
				return Command.createResponseCommand(HeaderMessageCode.CONSUMER_SEND_MSG_BACK, "i am a server i recived");
			}
		}, Executors.newCachedThreadPool());
         
        remotingServer.start();
        return remotingServer;
    }
    
    private static final ZkClient zkClient = ZkClient.getClient(Config.ZKPath.CONNECT_STR);
    
    @Test
    public void test_RPC_Sync() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException {
    	RemotingServer server = createRemotingServer();
        RemotingClient client = createRemotingClient();
        
        for (int i = 0; i < 10; i++) {
            SSSDComandHeader requestHeader = new SSSDComandHeader(zkClient.getRandomClientNodeId());
            requestHeader.setSssd(101);
            Command request = Command.createRequestCommand(HeaderMessageCode.ACK_COMMAND, requestHeader);
            
            final Channel channel = ChannelManager.get(zkClient.getRandomClientNodeId());
            System.out.println("channel >> "+channel);
            Command response = client.invokeSync(zkClient.getRandomClientNodeId(),request, 3000);
            
            System.out.println("invoke result = " + response);
        }
        
        client.shutdown();
        server.shutdown();
        
    }
    
    @Test
    public void test_RPC_Async() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, RemotingException {
    	RemotingServer server = createRemotingServer();
        RemotingClient client = createRemotingClient();
        
        for (int i = 0; i < 10; i++) {
            SSSDComandHeader requestHeader = new SSSDComandHeader(zkClient.getRandomClientNodeId());
            requestHeader.setSssd(101);
            Command request = Command.createRequestCommand(HeaderMessageCode.ACK_COMMAND, requestHeader);
            
            final Channel channel = ChannelManager.get(zkClient.getRandomClientNodeId());
            System.out.println("channel >> "+channel);
            // tcp是双工的，应该可以写数据给client端
            client.invokeAsync(zkClient.getRandomClientNodeId(),request, 3000,new InvokeCallback() {
				@Override
				public void doComplete(FutureResponse responseFuture) {
					
					System.out.println("invoke result = " + responseFuture);
				}
			});
        }
        //client.shutdown();
        //server.shutdown();
    }
    
    @Test
    public void test_server_call_client() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, RemotingException {
    	RemotingServer server = createRemotingServer();
        RemotingClient client = createRemotingClient();
        
        for (int i = 0; i < 1; i++) {
            SSSDComandHeader requestHeader = new SSSDComandHeader(zkClient.getRandomClientNodeId());
            requestHeader.setSssd(101);
            Command request = Command.createResponseCommand(HeaderMessageCode.ACK_COMMAND, "服务端答复请求");
            
            final Channel channel = ChannelManager.get(zkClient.getRandomClientNodeId());
            System.out.println("channel >> "+channel);
            Command response = server.invokeSync(channel, request, 3000);
            
            System.out.println("<<<===>>>" + response);
        }
        
        client.shutdown();
        server.shutdown();
    }
    
}
