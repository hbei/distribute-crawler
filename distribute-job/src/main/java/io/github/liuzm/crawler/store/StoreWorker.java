package io.github.liuzm.crawler.store;

import io.github.liuzm.crawler.exception.QueueException;
import io.github.liuzm.crawler.jobconf.StoreConfig;
import io.github.liuzm.crawler.page.ExtractedPage;
import io.github.liuzm.crawler.pendingqueue.PendingManager;
import io.github.liuzm.crawler.pendingqueue.PendingStore;
import io.github.liuzm.crawler.worker.Worker;

public abstract class StoreWorker<V, T> extends Worker {

	protected PendingStore pendingStore = null;
	protected StoreConfig config;
	protected Storage storage;

	/**
	 * 构造函数
	 */
	public StoreWorker(String jobTag, StoreConfig conf, Storage storage) {
		super(jobTag);
		this.config = conf;
		this.storage = storage;
		this.pendingStore = PendingManager.getPendingStore(conf.getJobName());
	}

	/**
	 * @desc 工作成功
	 */
	public abstract void onSuccessed(int count);

	/**
	 * @desc 工作失败
	 */
	public abstract void onFailed(int count);

	/**
	 * @desc 忽略工作
	 */
	public abstract void onIgnored(int count);

	/**
	 * @param page
	 * @desc 存储
	 */
	public abstract StoreResult store(ExtractedPage page);

	public void work(){
		
		ExtractedPage page = null;
		
		try {
			page = this.pendingStore.getElementT();
			if (null == page)
				return;

		} catch (QueueException e) {
			e.printStackTrace();
		}
		
		StoreResult result = store(page);
		
		page = null;
		if (null != result && null != result.status) {
			switch (result.status) {
			case ignored:
				onIgnored(result.getCount());
				break;
			case success:
				onSuccessed(result.getCount());
				break;
			case failed:
				onFailed(result.getCount());
				break;
			default:
				break;
			}
		} else {
			onFailed(1);
		}
	}

	
	public PendingStore getPendingStore() {
		return pendingStore;
	}

	public void setPendingStore(PendingStore pendingStore) {
		this.pendingStore = pendingStore;
	}

	public StoreConfig getConfig() {
		return config;
	}

	public void setConfig(StoreConfig conf) {
		this.config = conf;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

}
