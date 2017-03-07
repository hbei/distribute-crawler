/**
 * 
 */
package io.github.liuzm.distribute.client;

import org.apache.log4j.Logger;

import io.github.liuzm.distribute.remoting.RemotingClient;
import io.github.liuzm.distribute.remoting.exception.RemotingConnectException;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.netty.ClientConfig;
import io.github.liuzm.distribute.remoting.netty.NettyRemotingClient;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.remoting.protocol.header.AckCommandHeader;

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
	
	public void send() throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException{
		AckCommandHeader requestHeader = new AckCommandHeader(remotingClient.getRegistryNode().getNode().getId(),1);
        Command request = Command.createRequestCommand(HeaderMessageCode.ACK_COMMAND, requestHeader);
		remotingClient.invokeSync(remotingClient.getRegistryNode().getNode().getId(), request, 3000);
	}
	
}
