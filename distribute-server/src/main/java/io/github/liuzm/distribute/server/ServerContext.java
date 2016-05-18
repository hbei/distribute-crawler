/**
 * 
 */
package io.github.liuzm.distribute.server;

import io.github.liuzm.distribute.common.BaseContext;

/**
 * 
 * @author lxyq
 *
 */
public class ServerContext extends BaseContext {
	
	private AQServerImpl server;

	/**
	 * @return the server
	 */
	public AQServerImpl getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(AQServerImpl server) {
		this.server = server;
	}
	
	
	
	
}
