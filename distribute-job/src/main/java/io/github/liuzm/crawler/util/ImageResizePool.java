package io.github.liuzm.crawler.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author chenxinwen
 * @date 2014年10月22日
 * @desc 一个简单的线程池，用于提交图片压缩任务的线程
 */
public class ImageResizePool {
	/**
	 * 线程池
	 */
	public ExecutorService pool;
	
	private static ImageResizePool instance;
	
	private ImageResizePool(){
		pool = Executors.newFixedThreadPool(10);
	}
	
	public static ImageResizePool getInstance(){
		if(instance == null){
			instance = new ImageResizePool();
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
