package io.github.liuzm.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.github.liuzm.distribute.client.AQClient;
import io.github.liuzm.distribute.client.AQClientInitializer;
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
import io.github.liuzm.distribute.remoting.protocol.header.SSSDComand;
import io.github.liuzm.distribute.remoting.protocol.header.TaskCommand;
import io.github.liuzm.distribute.server.AQServerInitializer;
import io.github.liuzm.distribute.server.AQServerServiceImpl;
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
        
    }
    
    @Test
    public void test_RPC_Async() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, RemotingException {
    	
    }
    
    @Test
    public void test_server_call_client() throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, RemotingException {
    	
    	
    }
    
}
