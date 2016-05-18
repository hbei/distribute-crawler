package io.github.liuzm.crawler.vo;

import java.io.Serializable;

public class CrawlerVillageUrlVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -497219492403657102L;

	private String name;

	private String url;

	private String status;

	private String cityName;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


}
