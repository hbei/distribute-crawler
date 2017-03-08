package io.github.liuzm.distribute.client.protocol;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.CommandBody;

public class JobCommandHeader implements CommandBody {
	
	private String nodeId;
	
	public JobCommandHeader(){
		
	}
	
	public JobCommandHeader(String nodeId){
		this.nodeId = nodeId;
	}
	private String json;
	
	@Override
	public void checkFields() throws RemotingCommandException {

	}

	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}

	/**
	 * @param json the json to set
	 */
	public void setJson(String json) {
		this.json = json;
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
