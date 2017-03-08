package io.github.liuzm.distribute.remoting.protocol.header;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.CommandBody;

public class BlankCommand implements CommandBody {
	
	private String nodeId;
	
	public BlankCommand(){
		
	}
	
	public BlankCommand(String nodeId){
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
