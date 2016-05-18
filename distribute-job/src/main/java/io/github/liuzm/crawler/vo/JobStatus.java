package io.github.liuzm.crawler.vo;

import io.github.liuzm.crawler.jobconf.Configuration;

public class JobStatus {
	
	private int id ;
	private String jobTag;
	private int status;
	private Configuration configuration;
	
	
	
	public JobStatus(int id, String jobTag,int status, Configuration configuration) {
		super();
		this.id = id;
		this.jobTag = jobTag;
		this.status = status;
		this.configuration = configuration;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJobTag() {
		return jobTag;
	}
	public void setJobTag(String jobTag) {
		this.jobTag = jobTag;
	}
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "JobStatus [id=" + id + ", jobTag=" + jobTag
				+ ", configuration=" + configuration + "]";
	}
	
	
	

}
