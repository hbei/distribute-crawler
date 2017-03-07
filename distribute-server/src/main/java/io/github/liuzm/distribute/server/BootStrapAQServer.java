package io.github.liuzm.distribute.server;

import io.github.liuzm.distribute.remoting.RemotingServer;
import io.github.liuzm.distribute.remoting.netty.NettyRemotingServer;
import io.github.liuzm.distribute.remoting.netty.ServerConfig;

public final class BootStrapAQServer {
	
	private final RemotingServer remotingServer;
	
	public  BootStrapAQServer(){
		ServerConfig config = new ServerConfig();
		NettyRemotingServer nettyServer = new NettyRemotingServer(config);
		AQServerInitializer.initializerProcessor(nettyServer);
		
		this.remotingServer = nettyServer;
	}
	
	
	public RemotingServer getOneServer(){
		return remotingServer;
	}
	
	public void start(){
		remotingServer.start();
	}
}
