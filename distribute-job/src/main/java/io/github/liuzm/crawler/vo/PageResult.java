package io.github.liuzm.crawler.vo;

import java.util.Map;

import com.google.common.collect.Maps;

public class PageResult  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String city;
	private String area;
	private String block;
	private String villege;
	Map<String, Object> result = Maps.newConcurrentMap();
	
	
	public PageResult() {
		super();
		// TODO Auto-generated constructor stub
	}


	public PageResult(String city, String area, String block, String villege) {
		super();
		this.city = city;
		this.area = area;
		this.block = block;
		this.villege = villege;
		
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getBlock() {
		return block;
	}


	public void setBlock(String block) {
		this.block = block;
	}


	public String getVillege() {
		return villege;
	}


	public void setVillege(String villege) {
		this.villege = villege;
	}


	public Map<String, Object> getResult() {
		return result;
	}


	public void setResult(Map<String, Object> result) {
		this.result.putAll(result);
	}	
	
	public void setResult(String key,Object value) {
		this.result.put(key, value);
	}


	@Override
	public String toString() {
		return "PageResult [city=" + city + ", area=" + area + ", block="
				+ block + ", villege=" + villege + ", result=" + result.values().toString() + "]";
	}
	
	

}
