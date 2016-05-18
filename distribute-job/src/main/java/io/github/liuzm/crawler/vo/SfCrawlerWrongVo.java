package io.github.liuzm.crawler.vo;

import java.io.Serializable;

public class SfCrawlerWrongVo implements Serializable {
	
private Long id;    
	
	
	private String url;
	
	private String message;
	
	private String hasok;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHasok() {
		return hasok;
	}

	public void setHasok(String hasok) {
		this.hasok = hasok;
	}

	
	
	
}
