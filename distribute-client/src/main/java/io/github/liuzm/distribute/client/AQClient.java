/**
 * 
 */
package io.github.liuzm.distribute.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.remoting.Context;
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
	
	private static final Logger logger = LoggerFactory.getLogger(AQClient.class);
	
	private final RemotingClient remotingClient;
	
	private final Context context = new Context();
	
	
	
	public AQClient(){
		ClientConfig config = new ClientConfig();
		NettyRemotingClient nettyClient = new NettyRemotingClient(config);
		AQClientInitializer.buildClient(nettyClient,context);
		this.remotingClient = nettyClient;
	}
	
	public void start(){
		remotingClient.start();
	}
	
	public void send() throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException, RemotingTooMuchRequestException{
		BlankCommand requestHeader ;
		Command request ;
		String id = remotingClient.getRegistryNode().getNode().getId();
		while(true){
			requestHeader = new BlankCommand(id);
			request = Command.createRequestCommand(HeaderMessageCode.BLANK_COMMAND, requestHeader);
			remotingClient.invokeAsync(id, request, 1000,new InvokeCallback() {
				@Override
				public void doComplete(FutureResponse responseFuture) {
					logger.info("result:"+responseFuture.getResponseCommand().getOpaque());
					logger.info("do nothing!");
				}
			});
		}
	}
	
	public void sendMessage() throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException{
		MessageCommand requestHeader;
		Command request ;
        String s = "i am OK ";
        String id = remotingClient.getRegistryNode().getNode().getId();
        for(int i = 0 ;i< 10000;i++){
        	requestHeader = new MessageCommand(id);
        	requestHeader.setBody((s+i).getBytes());
        	request = Command.createRequestCommand(HeaderMessageCode.MESSAGE_COMMAND, requestHeader);
			Command result = remotingClient.invokeSync(id, request, 1000);
			logger.info("result:"+result);
		}
	}
	
	
	
	
}
