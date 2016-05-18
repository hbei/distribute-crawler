/**
 * 
 */
package io.github.liuzm.distribute.remoting.protocol.header;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;

/**
 * @author xh-liuzhimin
 *
 */
public class SSSDComandHeader extends AbstractCommandHeader {
	
	public SSSDComandHeader(String nodeId) {
		super(nodeId);
	}

	/**
	 * 101 start 102 stop 103 supended 104 detroy  
	 */
	private int sssd;

	@Override
	public void checkFields() throws RemotingCommandException {

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
