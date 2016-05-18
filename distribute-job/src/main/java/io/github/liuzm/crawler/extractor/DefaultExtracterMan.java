package io.github.liuzm.crawler.extractor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.liuzm.crawler.jobconf.ExtractConfig;
import io.github.liuzm.crawler.vo.JobStatus;

public class DefaultExtracterMan extends ExtracterMan {
	
	DefaultExtracterMan extracterMan = null;
	private ExtractConfig config = null;
	private DefaultExtracter extracter = null;
	
	public DefaultExtracterMan(ExtractConfig config) {
		this.config = config;
		this.extracter = new DefaultExtracter(config);
	}

	@Override
	public void start(JobStatus jobStatus) {
		int nThreads = config.getThreadNum();
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);

		for (int i = 0; i < nThreads; ++i) {
			executor.submit(new DefaultExtractWorker(jobStatus.getJobTag(), extracter, config));
		}
		executor.shutdown();
	}

}
