package io.github.liuzm.distribute.server.disruptor;

public class Message {

	private String nodeId;

	private byte[] messageBody;

	public Message() {

	}

	public Message(String nodeId, byte[] messageBody) {
		this.nodeId = nodeId;
		this.messageBody = messageBody;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public byte[] getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(byte[] messageBody) {
		this.messageBody = messageBody;
	}

}
