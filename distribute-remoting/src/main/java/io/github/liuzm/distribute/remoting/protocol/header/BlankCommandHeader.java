package io.github.liuzm.distribute.remoting.protocol.header;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.CommandHeader;

public class BlankCommandHeader implements CommandHeader {
	
	private String nodeId;
	
	public BlankCommandHeader(String nodeId){
		this.nodeId = nodeId;
	}

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
	
	
}
