/**
 * 
 */
package io.github.liuzm.distribute.client;

import org.apache.log4j.Logger;

import io.github.liuzm.crawler.bootsStrap.BootStrap;
import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.registy.Registry;
import io.github.liuzm.distribute.remoting.netty.ClientConfig;
import io.github.liuzm.distribute.remoting.netty.NettyRemotingClient;

/**
 * @author lxyq
 *
 */
public class AQClient extends NettyRemotingClient{
	
	private static final Logger logger = Logger.getLogger(AQClient.class);
	
	public static final BootStrap bootstrap = BootStrap.getInstance();
	
	public AQClient(ClientConfig nettyClientConfig, Node node, Registry register) {
		super(nettyClientConfig, node, register);
	}
	
	
}
