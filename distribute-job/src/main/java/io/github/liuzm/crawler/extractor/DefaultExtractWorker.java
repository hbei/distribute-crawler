package io.github.liuzm.crawler.extractor;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.crawler.bootsStrap.JobManager;
import io.github.liuzm.crawler.exception.QueueException;
import io.github.liuzm.crawler.jobconf.ExtractConfig;
import io.github.liuzm.crawler.page.ExtractResult;
import io.github.liuzm.crawler.page.ExtractedPage;
import io.github.liuzm.crawler.page.Page;
import io.github.liuzm.crawler.pendingqueue.PendingManager;
import io.github.liuzm.crawler.pendingqueue.PendingPages;
import io.github.liuzm.crawler.worker.Worker;

public  class DefaultExtractWorker extends Worker {

	private  static final Logger log = LoggerFactory.getLogger(DefaultExtractWorker.class) ;
	
	private Extracter extracter ;
	protected ExtractConfig config;
	protected PendingPages pendingPages ;
	
	public DefaultExtractWorker(String jobTag,Extracter extracter,ExtractConfig config) {
		super(jobTag);
		this.config = config;
		this.extracter = extracter;
		this.pendingPages =  PendingManager.getPendingPages(config.getJobName());
	}

	
	

	@Override
	public void run() {
		while (!JobManager.isStop(getJobTag()) && !JobManager.isFinish(getJobTag())) {

			if (pendingPages.isEmpty()) {
				try {
					TimeUnit.SECONDS.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					
					Page page = (Page)pendingPages.getElementT();
					ExtractedPage result = (ExtractedPage)getExtracter().parsePageElements(page);
					page = null;
					if( null != result && result.getResult()==ExtractResult.success){
						PendingManager.getPendingStore(config.getJobName()).addElement(result);
						pendingPages.processedSuccess();
					}else{
						pendingPages.processedFailure();
					}
					result = null;
					

				} catch (QueueException e) {
					log.error(config.getIndexName() + "\t提取任务\tExtracter从队列PendingUrls获取任务失败!!!");
					e.printStackTrace();

				}catch(Exception e1){
					log.error(config.getIndexName() + "\t提取任务\tExtracter\t线程内异常", e1);
					e1.printStackTrace();
				}
			}

			// 判断是否停止

			if (JobManager.isStop(getJobTag()) || JobManager.isFinish(getJobTag())) {
				log.error(config.getIndexName() + "\tExtracter线程"
						+ Thread.currentThread().getName() + "退出（if）");
				return;

			}

		}
		System.out.println(config.getIndexName() + "\tExtracter线程" + Thread.currentThread().getName()
				+ "退出");
	}
	
	public Extracter getExtracter() {
		return extracter;
	}
	
	

	public void setExtracter(Extracter extracter) {
		this.extracter = extracter;
	}

	public ExtractConfig getConfig() {
		return config;
	}

	public void setConfig(ExtractConfig config) {
		this.config = config;
	}

	public PendingPages getPendingPages() {
		return pendingPages;
	}

	public void setPendingPages(PendingPages pendingPages) {
		this.pendingPages = pendingPages;
	}
	
	
}
