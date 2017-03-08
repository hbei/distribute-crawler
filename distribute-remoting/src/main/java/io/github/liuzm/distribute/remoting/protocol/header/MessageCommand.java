package io.github.liuzm.distribute.remoting.protocol.header;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.CommandBody;

public class MessageCommand implements CommandBody {
	
	private String nodeId;
	
	private byte[] body;
	
	public MessageCommand(){
		
	}
	
	public MessageCommand(String nodeId){
		this.nodeId = nodeId;
	}
	
	@Override
	public void checkFields() throws RemotingCommandException {
		if(nodeId == null){
			
		}
	}

	/**
	 * @return the body
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body) {
		this.body = body;
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
