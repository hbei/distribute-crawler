/**
 * 
 */
package io.github.liuzm.distribute.remoting.protocol.header;

/**
 * @author qydpc
 *
 */
public class AckCommandHeader extends AbstractCommandHeader {
	
	private int conn;
	
	public AckCommandHeader(String nodeId,int conn) {
		super(nodeId);
		this.conn = conn;
	}

	/**
	 * @return the conn
	 */
	public int getConn() {
		return conn;
	}

	/**
	 * @param conn the conn to set
	 */
	public void setConn(int conn) {
		this.conn = conn;
	}
	
	

}
