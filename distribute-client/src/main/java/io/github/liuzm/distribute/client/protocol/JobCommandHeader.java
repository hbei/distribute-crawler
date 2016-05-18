package io.github.liuzm.distribute.client.protocol;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.header.AbstractCommandHeader;

public class JobCommandHeader extends AbstractCommandHeader {
	
	public JobCommandHeader(){
		
	}
	
	public JobCommandHeader(String nodeId){
		super(nodeId);
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
	

}
