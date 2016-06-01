/**
 * 
 */
package io.github.liuzm.distribute.remoting.protocol.header;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.CommandHeader;

/**
 * @author xh-liuzhimin
 *
 */
public class SSSDComandHeader implements CommandHeader {
	
	private String nodeId;
	
	public SSSDComandHeader(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 101 start 102 stop 103 supended 104 detroy  
	 */
	private int sssd;

	@Override
	public void checkFields() throws RemotingCommandException {

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
	 * @return the sssd
	 */
	public int getSssd() {
		return sssd;
	}

	/**
	 * @param sssd the sssd to set
	 */
	public void setSssd(int sssd) {
		this.sssd = sssd;
	}
	
}
