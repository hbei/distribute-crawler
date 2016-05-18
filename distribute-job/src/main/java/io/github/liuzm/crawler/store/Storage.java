package io.github.liuzm.crawler.store;

import java.util.List;

import io.github.liuzm.crawler.page.ExtractedPage;

public abstract class Storage {

	public Storage() {

	}

	/**
	 * @param object
	 * @return
	 * @desc 存储前
	 */
	public abstract StoreResult beforeStore();

	/**
	 * @param page
	 * @return
	 * @desc 存储时
	 */
	public abstract StoreResult onStore(ExtractedPage page);

	/**
	 * @param page结果集
	 * @return
	 * @desc 批量存储存储
	 */
	public abstract StoreResult onStore(List<ExtractedPage> page);

	/**
	 * @param page
	 * @return
	 * @desc 存储后
	 */
	public abstract StoreResult afterStore(ExtractedPage page);
}
