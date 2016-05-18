package io.github.liuzm.crawler.vo;

import java.io.Serializable;




import org.jsoup.nodes.Document;

import io.github.liuzm.crawler.page.Page;
import io.github.liuzm.crawler.url.WebURL;

public class CityPage extends Page implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	public static final String PR_OK = "ok";
	public static final String PR_NO = "no";
	private ListingUrl cityList = new ListingUrl();
	private WebURL village;
	
	Document htmlDOM ;
	
	String status ;

	

	public CityPage(WebURL url) {
		super(url);
		// TODO Auto-generated constructor stub
	}

	public Document getHtmlDOM() {
		return htmlDOM;
	}

	public void setHtmlDOM(Document htmlDOM) {
		this.htmlDOM = htmlDOM;
	}

	public CityPage(ListingUrl listingUrl) {
		this.cityList = (ListingUrl)listingUrl.clone();
	}

	public CityPage() {
		super();
	}

	public WebURL getVillage() {
		return village;
	}

	public void setVillage(WebURL village) {
		this.village = village;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public ListingUrl getCityList() {
		return cityList;
	}

	public void setCityList(ListingUrl cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "Page [city="+ cityList.getCity() + ", area=" + cityList.getArea() + ", block=" + cityList.getBlock() + ",village=" + village + ", status=" + status+", HtmlDOM=" + (htmlDOM!=null)+"]";
	}


}
