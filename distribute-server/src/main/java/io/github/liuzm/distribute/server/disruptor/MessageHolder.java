package io.github.liuzm.distribute.server.disruptor;

public class MessageHolder {
	
	private Message message;
	
	public void set(Message message){
		this.message = message;
	}
	
	public Message get(){
		return message;
	}
	
	public void clear(){
		this.message = null;
	}
}
