package io.github.liuzm.crawler.store;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.crawler.bootsStrap.JobManager;
import io.github.liuzm.crawler.jobconf.StoreConfig;
import io.github.liuzm.crawler.page.ExtractedPage;

public class DefaultStoreWorker<V, T> extends StoreWorker<V, T>{
	
	private static final Logger log = LoggerFactory.getLogger(DefaultStoreWorker.class);
	
	public DefaultStoreWorker(String jobTag ,StoreConfig config, Storage storage) {
		super(jobTag,config, storage);
		
		
	}

	@Override
	public void run() {
		while(!JobManager.isStop(getJobTag())&&!JobManager.isFinish(getJobTag())) {
			
			if(pendingStore.isEmpty()){
				log.info("now "+new Date() + "pendingStore null ");
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				try {
					work();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(JobManager.isStop(getJobTag())|| JobManager.isFinish(getJobTag())){
				log.info(config.getIndexName() + "\t存储任务\tStorage线程" + Thread.currentThread().getName()+"退出(if)" );
				break;
			}
			
		}
		System.out.println(config.getIndexName() + "\t存储任务\tStorage线程" + Thread.currentThread().getName()+"退出" );
	}

	@Override
	public void onSuccessed(int count) {
		pendingStore.getSuccess().addAndGet(count);
	}

	@Override
	public void onFailed(int count) {
		pendingStore.getFailure().addAndGet(count);
	}

	@Override
	public void onIgnored(int count) {
		pendingStore.getIgnored().addAndGet(count);
	}

	@Override
	public StoreResult store(ExtractedPage page) {
		return storage.onStore(page);
	}
	

}
