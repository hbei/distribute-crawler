package io.github.liuzm.crawler.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author chenxin.wen
 * @date 2014年10月20日
 * @desc 用于提交下载任务的线程池
 */
public class DownLoadPool {
	/**
	 * 线程池
	 */
	public ExecutorService pool;
	
	private static DownLoadPool instance;
	
	private DownLoadPool(){
		pool = Executors.newFixedThreadPool(10);
	}
	
	public static DownLoadPool getInstance(){
		if(instance == null){
			instance = new DownLoadPool();
		}
		return instance;
	}
	/**
	 * 提交线程
	 * @return 
	 */
	public Future<?> submit(Callable<?> call){
		return pool.submit(call);
	}
}
