package io.github.liuzm.crawler.vo;

import io.github.liuzm.crawler.url.WebURL;


public class ListingUrl extends WebURL{
	
	
	private static final long serialVersionUID = 1L;
	private WebURL city;
	private WebURL area;
	private WebURL block;


	
	
	@Override
	public Object clone() {
		ListingUrl o = null;
		o = (ListingUrl)super.clone();
		o.city = (WebURL)city.clone();
		o.area = (WebURL)area.clone();
		o.block = (WebURL)block.clone();
		return o;
	}

	public ListingUrl() {
		//super();
		// TODO Auto-generated constructor stub
	}

	public ListingUrl(WebURL city) {
		//super();
		this.city = city;
	}

	
	public ListingUrl(WebURL city, WebURL area, WebURL block) {
		//super();
		this.city = city;
		this.area = area;
		this.block = block;
		
	}


	public WebURL getCity() {
		return city;
	}

	public void setCity(WebURL city) {
		this.city = city;
	}

	public WebURL getArea() {
		return area;
	}

	public void setArea(WebURL area) {
		this.area = area;
	}

	public WebURL getBlock() {
		return block;
	}

	public void setBlock(WebURL block) {
		this.block = block;
	}

	@Override
	public String toString() {
		return "SearchListUrl [city=" + city + ", area=" + area + ", block="
				+ block + "]";
	}

	
	

}
