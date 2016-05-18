/**
 * 
 */
package io.github.liuzm.distribute.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author xh-liuzhimin
 *
 */
public class TaskInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378180592832520980L;
	
	private String id;
	
	private List<String> urls;

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
	 * @return the urls
	 */
	public List<String> getUrls() {
		return urls;
	}

	/**
	 * @param urls the urls to set
	 */
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	
}
