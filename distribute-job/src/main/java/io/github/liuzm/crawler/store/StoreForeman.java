package io.github.liuzm.crawler.store;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.liuzm.crawler.vo.JobStatus;

public class StoreForeman {

	public void start(JobStatus jobStatus) {

		int threadNum = jobStatus.getConfiguration().getStoreConfig().getThreadNum();
		String type = jobStatus.getConfiguration().getStoreConfig().getType();

		ExecutorService executor = Executors.newFixedThreadPool(threadNum);

		for (int i = 0; i < threadNum; i++) {

			if (type.equalsIgnoreCase("es")) {
				executor.submit(
						new DefaultStoreWorker(jobStatus.getJobTag(), jobStatus.getConfiguration().getStoreConfig(),
								new ElasticSearchStorage(jobStatus.getConfiguration().getStoreConfig())));
			} else if (type.equalsIgnoreCase("local")) {
				executor.submit(
						new DefaultStoreWorker(jobStatus.getJobTag(), jobStatus.getConfiguration().getStoreConfig(),
								new LocalFileStorage(
										jobStatus.getConfiguration().getStoreConfig().getLocalFileConfig().getDir(),
										jobStatus.getConfiguration().getJobName())));
			} else if (type.equalsIgnoreCase("hbase")) {

			} else if (type.equalsIgnoreCase("mongodb")) {
				executor.submit(
						new DefaultStoreWorker(jobStatus.getJobTag(), jobStatus.getConfiguration().getStoreConfig(),
								new MongodbStorage(jobStatus.getConfiguration().getStoreConfig())));
			}
		}
		executor.shutdown();
	}
}
