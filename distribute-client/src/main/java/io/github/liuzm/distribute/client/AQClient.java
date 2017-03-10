/**
 * 
 */
package io.github.liuzm.distribute.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import io.github.liuzm.distribute.remoting.InvokeCallback;
import io.github.liuzm.distribute.remoting.RemotingClient;
import io.github.liuzm.distribute.remoting.exception.RemotingConnectException;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.exception.RemotingTooMuchRequestException;
import io.github.liuzm.distribute.remoting.netty.ClientConfig;
import io.github.liuzm.distribute.remoting.netty.FutureResponse;
import io.github.liuzm.distribute.remoting.netty.NettyRemotingClient;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.remoting.protocol.header.BlankCommand;
import io.github.liuzm.distribute.remoting.protocol.header.MessageCommand;

/**
 * @author lxyq
 *
 */
public class AQClient{
	
	private static final Logger logger = Logger.getLogger(AQClient.class);
	
	private final RemotingClient remotingClient;
	
	
	
	
	
	public AQClient(){
		ClientConfig config = new ClientConfig();
		NettyRemotingClient nettyClient = new NettyRemotingClient(config);
		AQClientInitializer.buildClient(nettyClient);
		this.remotingClient = nettyClient;
	}
	
	public void start(){
		remotingClient.start();
	}
	
	public void send() throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException, RemotingTooMuchRequestException{
		BlankCommand requestHeader = new BlankCommand(remotingClient.getRegistryNode().getNode().getId());
		while(true){
			Command request = Command.createRequestCommand(HeaderMessageCode.BLANK_COMMAND, requestHeader);
			remotingClient.invokeAsync(remotingClient.getRegistryNode().getNode().getId(), request, 3000,new InvokeCallback() {
				@Override
				public void doComplete(FutureResponse responseFuture) {
					logger.info("do nothing!");
				}
			});
		}
	}
	
	public void sendMessage() throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException{
		MessageCommand requestHeader;
		Command request ;
        String s = "i am OK ";
        for(int i = 0 ;i< 10000;i++){
        	requestHeader = new MessageCommand(remotingClient.getRegistryNode().getNode().getId());
        	requestHeader.setBody((s+i).getBytes());
        	request = Command.createRequestCommand(HeaderMessageCode.MESSAGE_COMMAND, requestHeader);
			remotingClient.invokeSync(remotingClient.getRegistryNode().getNode().getId(), request, 3000);
		}
	}
	
	
	
	
}
