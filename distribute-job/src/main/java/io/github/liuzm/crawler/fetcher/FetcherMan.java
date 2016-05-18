package io.github.liuzm.crawler.fetcher;

import org.apache.log4j.Logger;

import io.github.liuzm.crawler.jobconf.FetchConfig;
import io.github.liuzm.crawler.vo.JobStatus;



public abstract class FetcherMan {
	
	
	protected Logger log;
	
	
	private Fetcher fetcher;
	private FetchConfig config;
	
	private JobStatus jobStatus;
	
	public FetcherMan(Fetcher fetcher,FetchConfig config){
		this.fetcher = fetcher;
		this.config = config;
		log = Logger.getLogger(config.getJobName() + config.getIndexName());
	}
	
	public abstract void start(JobStatus jobStatus);
	

	public static FetcherMan getInstance(FetchConfig config){
		
		FetcherMan fetcherMan = null;
		
		//使用默认的
		fetcherMan = new DefaultFetchMan(new DefaultFetcher(config),config );
		
		return fetcherMan;
	}
	
	
	
	public Fetcher getFetcher() {
		return fetcher;
	}

	

	public void setFetcher(Fetcher fetcher) {
		this.fetcher = fetcher;
	}

	public JobStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}


	public FetchConfig getConfig() {
		return config;
	}

	public void setConfig(FetchConfig config) {
		this.config = config;
	}
	
}
