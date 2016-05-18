package io.github.liuzm.crawler.extractor;

import io.github.liuzm.crawler.jobconf.ExtractConfig;
import io.github.liuzm.crawler.vo.JobStatus;

public abstract class ExtracterMan {

	private ExtractConfig config;

	public ExtracterMan(){
	}

	public static ExtracterMan getInstance(ExtractConfig config) {
		return new DefaultExtracterMan(config);
	}

	public ExtractConfig getConfig() {
		return config;
	}

	public void setConfig(ExtractConfig config) {
		this.config = config;
	}

	public abstract void start(JobStatus jobStatus);

}
