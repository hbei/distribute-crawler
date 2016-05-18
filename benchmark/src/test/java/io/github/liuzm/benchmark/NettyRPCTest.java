package io.github.liuzm.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.github.liuzm.distribute.client.AQClient;
import io.github.liuzm.distribute.client.AQClientFactory;
import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.zookeeper.ZkClient;
import io.github.liuzm.distribute.remoting.InvokeCallback;
import io.github.liuzm.distribute.remoting.cache.ChannelManager;
import io.github.liuzm.distribute.remoting.exception.RemotingConnectException;
import io.github.liuzm.distribute.remoting.exception.RemotingException;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.netty.FutureResponse;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.remoting.protocol.header.SSSDComandHeader;
import io.github.liuzm.distribute.remoting.protocol.header.TaskCommandHeader;
import io.github.liuzm.distribute.server.AQServerFactory;
import io.github.liuzm.distribute.server.AQServerImpl;
import io.netty.channel.Channel;

/**
 * @author xh-liuzhimin
 *
 */
public class NettyRPCTest {
	
    private static final ZkClient zkClient = ZkClient.getClient(Config.ZKPath.CONNECT_STR);
    
    
    @Test
    public void test_RPC_Sync() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException {
    	AQServerImpl server = AQServerFactory.buildServer();
    	AQClient client = AQClientFactory.buildClient();
        Thread.sleep(3000);
        for (int i = 0; i < 10; i++) {
            SSSDComandHeader requestHeader = new SSSDComandHeader(zkClient.getRandomClientNodeId());
            requestHeader.setSssd(101);
            Command request = Command.createRequestCommand(HeaderMessageCode.CLIENT_RESV_SSSD_COMMAND, requestHeader);
            
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
    	AQServerImpl server = AQServerFactory.buildServer();
    	AQClient client = AQClientFactory.buildClient();
        
        for (int i = 0; i < 10; i++) {
            TaskCommandHeader requestHeader = new TaskCommandHeader(zkClient.getRandomClientNodeId());
            requestHeader.setTaskId("00001");
            List<Object> task = new ArrayList<Object>();
            task.add("123");
            task.add("456");
            task.add("789");
            requestHeader.setTask(task);
            Command request = Command.createRequestCommand(HeaderMessageCode.CLIENT_RESV_TASK_COMMAND, requestHeader);
            
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
        client.shutdown();
        server.shutdown();
    }
    
    @Test
    public void test_server_call_client() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, RemotingException {
    	
    	AQServerImpl server = AQServerFactory.buildServer();
        
    	for(int i = 0;i < 50; i++){
    		@SuppressWarnings("unused")
			AQClient client = AQClientFactory.buildClient();
    	}
    	
        for (int i = 0; i < 1; i++) {
            SSSDComandHeader requestHeader = new SSSDComandHeader(zkClient.getRandomClientNodeId());
            requestHeader.setSssd(101);
            Command request = Command.createRequestCommand(HeaderMessageCode.CLIENT_RESV_SSSD_COMMAND, requestHeader);
            System.out.println(" channel mapper ===>>> " +ChannelManager.map().size()+"===" +ChannelManager.map());
            final Channel channel = ChannelManager.get(zkClient.getRandomClientNodeId());
            System.out.println("channel >> "+channel);
            Command response = server.invokeSync(channel, request, 3000);
            
            System.out.println("<<<===>>>" + response);
        }
        //server.shutdown();
    }
    
}
