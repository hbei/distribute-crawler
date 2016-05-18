package io.github.liuzm.crawler.jobconf;



public class Configuration {

	private String jobName = "";
	public String indexName="";
	
	private FetchConfig fetchConfig;
	
	private ExtractConfig extractConfig;
	
	private StoreConfig storeConfig;
	

	public  FetchConfig getFetchConfig() {
		return fetchConfig;
	}

	public void setFetchConfig(FetchConfig fetchConfig) {
		this.fetchConfig = fetchConfig;
		this.jobName = fetchConfig.getJobName();
		this.indexName = fetchConfig.getIndexName();
	}

	public ExtractConfig getExtractConfig() {
		return extractConfig;
	}

	public  void setExtractConfig(ExtractConfig extractConfig) {
		this.extractConfig = extractConfig;
	}

	public Configuration getConfiguration(){
		return this;
	}

	public StoreConfig getStoreConfig() {
		return storeConfig;
	}

	public void setStoreConfig(StoreConfig storeConfig) {
		this.storeConfig = storeConfig;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	
	
	
}
