package io.github.liuzm.crawler.fetcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.liuzm.crawler.bootsStrap.JobManager;
import io.github.liuzm.crawler.exception.QueueException;
import io.github.liuzm.crawler.jobconf.FetchConfig;
import io.github.liuzm.crawler.pendingqueue.PendingAreas;
import io.github.liuzm.crawler.pendingqueue.PendingManager;
import io.github.liuzm.crawler.url.WebURL;
import io.github.liuzm.crawler.vo.JobStatus;

public class DefaultFetchMan extends FetcherMan {

	public DefaultFetchMan(Fetcher fetcher,FetchConfig config) {
		super(fetcher,config);
	}
	
	@Override
	public void start(JobStatus jobStatus) {
		FetchConfig config = jobStatus.getConfiguration().getFetchConfig();
		int threadNum = config.getThreadNum();
		String jobName = jobStatus.getConfiguration().getJobName();
		String jobTag = jobStatus.getJobTag();
		
		//处理城市队列中的任务
		PendingAreas pares = PendingManager.getPendingArea(jobName);

		while (!pares.isEmpty()&&!JobManager.isStop(jobTag)) {
			try {
				WebURL cityUrl = pares.getElementT();
				PendingManager.getPendingUlr(jobName).addElement(cityUrl);
				cityUrl = null;
			} catch (QueueException e) {
				log.error(config.getIndexName() + "\tFetcher从队列PendingAreas获取任务失败!!!" );
				e.printStackTrace();
			}
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		
		for(int i=0;i<threadNum;i++){
			executor.submit(new DefaultFetchWorker(jobTag,config));
		}
		
		executor.shutdown();
		
		try {
		//	Fetcher.runProxyer();
		} catch (Exception e) {
			log.error("错误：", e);
			e.printStackTrace();
		}
	}

}
