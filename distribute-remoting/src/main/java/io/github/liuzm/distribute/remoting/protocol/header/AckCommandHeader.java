/**
 * 
 */
package io.github.liuzm.distribute.remoting.protocol.header;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.CommandHeader;

/**
 * @author qydpc
 *
 */
public class AckCommandHeader implements CommandHeader {
	
	private int conn;
	
	private String nodeId;
	
	public AckCommandHeader(String nodeId,int conn) {
		this.nodeId = nodeId;
		this.conn = conn;
	}

	/**
	 * @return the conn
	 */
	public int getConn() {
		return conn;
	}

	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @param conn the conn to set
	 */
	public void setConn(int conn) {
		this.conn = conn;
	}

	@Override
	public void checkFields() throws RemotingCommandException {
		
	}
	
	

}
