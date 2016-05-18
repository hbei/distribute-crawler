package io.github.liuzm.distribute.remoting.netty;

/**
 * @author liuzhimin
 *
 */
public class SystemConfig {
	
	public static final String SocketSize = "org.qyd.aliuge.remoting.socket.send.size";
	
	 public static int SocketSndbufSize = Integer.parseInt(System.getProperty(SocketSize, "2048"));

}
