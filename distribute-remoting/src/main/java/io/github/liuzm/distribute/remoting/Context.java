package io.github.liuzm.distribute.remoting;

import java.io.Serializable;

import io.github.liuzm.distribute.remoting.protocol.Command;

public class Context implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 487144547209228773L;
	

	private String clientNodeId;
	
	private String serverNodeId;
	
	private Command requestCommand;
	
	private Command responseCommand;
	
	
	public Context(){
		
	}


	/**
	 * @return the clientNodeId
	 */
	public String getClientNodeId() {
		return clientNodeId;
	}


	/**
	 * @param clientNodeId the clientNodeId to set
	 */
	public void setClientNodeId(String clientNodeId) {
		this.clientNodeId = clientNodeId;
	}


	/**
	 * @return the serverNodeId
	 */
	public String getServerNodeId() {
		return serverNodeId;
	}


	/**
	 * @param serverNodeId the serverNodeId to set
	 */
	public void setServerNodeId(String serverNodeId) {
		this.serverNodeId = serverNodeId;
	}


	/**
	 * @return the requestCommand
	 */
	public Command getRequestCommand() {
		return requestCommand;
	}


	/**
	 * @param requestCommand the requestCommand to set
	 */
	public void setRequestCommand(Command requestCommand) {
		this.requestCommand = requestCommand;
	}


	/**
	 * @return the responseCommand
	 */
	public Command getResponseCommand() {
		return responseCommand;
	}


	/**
	 * @param responseCommand the responseCommand to set
	 */
	public void setResponseCommand(Command responseCommand) {
		this.responseCommand = responseCommand;
	}
	
	

}
