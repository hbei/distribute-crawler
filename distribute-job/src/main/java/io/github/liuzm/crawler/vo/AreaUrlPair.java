package io.github.liuzm.crawler.vo;

import java.io.Serializable;

public class AreaUrlPair implements Serializable,Cloneable{
	
	
	private static final long serialVersionUID = 1L;
	String name;
	String url;
	
	
	
	@Override
	protected Object clone()  {
		AreaUrlPair o = null;
		try {
			o = (AreaUrlPair)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	private AreaUrlPair() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AreaUrlPair(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "AreaUrlPair [name=" + name + ", url=" + url + "]";
	}

	
}
