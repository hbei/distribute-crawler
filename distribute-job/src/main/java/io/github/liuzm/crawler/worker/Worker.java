package io.github.liuzm.crawler.worker;

import io.github.liuzm.crawler.bootsStrap.JobManager;
import io.github.liuzm.crawler.vo.Status;

public abstract class Worker implements Runnable {
	
	private static String jobTag;
	private static boolean stop = false;
	
	@SuppressWarnings("unused")
	private static boolean finish = false;

	@SuppressWarnings("static-access")
	public Worker(String jobTag) {
		this.jobTag = jobTag;
	}

	/**
	 * 停工
	 */
	public static synchronized void stop() {
		stop = true;
	}

	public static synchronized boolean isStop() {
		return stop;
	}

	public static synchronized boolean isFinish() {
		return JobManager.getStatus(jobTag) == Status.STATUS_FINISH;
	}

	public static synchronized void finish() {
		finish = true;
		stop();
	}

	public synchronized void setFinish(boolean f) {
		finish = f;
	}

	public String getJobTag() {
		return jobTag;
	}

	@SuppressWarnings("static-access")
	public void setJobTag(String jobTag) {
		this.jobTag = jobTag;
	}

}
