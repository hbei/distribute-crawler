/**
 * 
 */
package io.github.liuzm.manage.web.model;

import java.io.Serializable;

/**
 * @author xh-liuzhimin
 *
 */
public class Menu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String name;
	
	private String uri;
	
	private String icon;
	
	private Menu submenu;
	
	
	public Menu(){
		
	}
	
	public Menu(int id,String name,String uri,String icon){
		this.id = id;
		this.name = name;
		this.uri = uri;
		this.icon = icon;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the submenu
	 */
	public Menu getSubmenu() {
		return submenu;
	}

	/**
	 * @param submenu the submenu to set
	 */
	public void setSubmenu(Menu submenu) {
		this.submenu = submenu;
	}
	
}
