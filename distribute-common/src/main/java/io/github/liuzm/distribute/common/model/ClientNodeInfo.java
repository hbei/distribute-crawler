/**
 * 
 */
package io.github.liuzm.distribute.common.model;

/**
 * @author xh-liuzhimin
 *
 */
public class ClientNodeInfo {
	
	private String id ;
	
	private String strIp;
	
	
	@Override
	public String toString(){
		return "ClientNode =[" + id + "],ipaddress = [ " + strIp +"] " ;
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
	 * @return the strIp
	 */
	public String getStrIp() {
		return strIp;
	}


	/**
	 * @param strIp the strIp to set
	 */
	public void setStrIp(String strIp) {
		this.strIp = strIp;
	}
	
	

}
