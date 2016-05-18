/**
 * 
 */
package io.github.liuzm.distribute.server;

/**
 * @author lxyq
 *
 */
public class ServerException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5514235380676169424L;

	public ServerException(Throwable cause){
		super(cause);
	}
	
	public ServerException(String ipaddress , Throwable cause){
		super("server ip = " + ipaddress + " is exception", cause);
	}
}
