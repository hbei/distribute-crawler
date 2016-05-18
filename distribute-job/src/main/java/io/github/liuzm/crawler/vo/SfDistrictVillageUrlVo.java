package io.github.liuzm.crawler.vo;

import java.io.Serializable;

public class SfDistrictVillageUrlVo implements Serializable {
	
	private String id;
	
	private String district_name;    
	
	private String district_url;
	
	private String city;
	
	private String parent_id;
	
	private String village_count;
	
	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public String getDistrict_url() {
		return district_url;
	}

	public void setDistrict_url(String district_url) {
		this.district_url = district_url;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getVillage_count() {
		return village_count;
	}

	public void setVillage_count(String village_count) {
		this.village_count = village_count;
	}


	
	
	
}
