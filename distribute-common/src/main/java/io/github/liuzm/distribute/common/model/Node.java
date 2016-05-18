/**
 * 
 */
package io.github.liuzm.distribute.common.model;

import java.io.Serializable;

/**
 * 定义机器节点
 * @author xh-liuzhimin
 *
 */
public class Node implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8562823738543593723L;

	
	private String id;
	
	private String ipaddress;
	
	private int port;
	
	/**
	 * node  0 : server 1: client
	 */
	private int type;
	
	
	public Node(){
		
	}
	
	public Node(int type){
		this.type = type;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
