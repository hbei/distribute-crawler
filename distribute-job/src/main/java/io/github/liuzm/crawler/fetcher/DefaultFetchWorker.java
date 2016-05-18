package io.github.liuzm.crawler.fetcher;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.crawler.bootsStrap.JobManager;
import io.github.liuzm.crawler.exception.QueueException;
import io.github.liuzm.crawler.jobconf.FetchConfig;
import io.github.liuzm.crawler.url.WebURL;




public class DefaultFetchWorker extends FetchWorker {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultFetchWorker.class);
	
	public DefaultFetchWorker(String jobTag,FetchConfig conf) {
		super(jobTag,conf);
	}
	
	public DefaultFetchWorker(String jobTag,FetchConfig conf, DefaultFetcher fetcher) {
		super(jobTag,conf, fetcher);
	}
	
	@Override
	public void onSuccessed() {
		pendingUrls.processedSuccess();
	}

	@Override
	public void onFailed(WebURL url) {
		log.info("抓取失败:"+url.getName()+"\t"+url.getUrl());
		pendingUrls.processedFailure();
	}

	
	@Override
	public void onIgnored(WebURL url) {
		log.info("抓取失败(忽略):"+url.getName()+"\t"+url.getUrl());
	}
	
	@Override
	public void run() {
		int c = 0;
		WebURL url = null ;
		String jobTag = getJobTag();
		try {
			while(JobManager.isRunning(jobTag)){
				if(null==(url=(WebURL)pendingUrls.getElementT())){
					TimeUnit.SECONDS.sleep(1);
					
				}else{
					fetchPage(url);
					//一段时间后休息下
					c++;
					if(c>10000){
						c=0;
						Thread.sleep(2000L);
					}
				}
				
			}
			log.info("爬取线程：" + Thread.currentThread().getName() + "退出");
			log.info("原因:" + JobManager.isRunning(jobTag));
			log.info("url=:" + url);
		} catch (QueueException e) {
			e.printStackTrace();
			 log.error(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			 log.error(e.getMessage());
		}
	}
}
