package io.github.liuzm.crawler.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.crawler.bootsStrap.JobManager;
import io.github.liuzm.crawler.vo.Status;

public abstract class Worker implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(Worker.class);
	
	private static String jobTag;
	private static boolean stop = false;
	private static boolean finish = false;

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

	/*public static void checkFinished(final Configuration config) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				int count = 5;
				while (!stop) {
					try {
						TimeUnit.SECONDS.sleep(100);

						int s1 = PendingManager.getPendingUlr("url_").getQueue().size();
						int s2 = PendingManager.getPendingPages("page_").getQueue().size();
						int s3 = PendingManager.getPendingStore("store_").getQueue().size();

						log.info("检查任务:"+ "是否完成............");
						log.info("PendingUlr\t" + s1);
						log.info("PendingPages\t" + s2);
						log.info("PendingStore\t" + s3);
						System.out.println(Worker.isFinish());
						System.out.println(Worker.isStop());

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (PendingManager.getPendingUlr("url_").isEmpty()
							&& PendingManager.getPendingPages("page_").isEmpty()
							&& PendingManager.getPendingStore("store_").isEmpty()) {
						count--;
						if (count <= 0) {
							Worker.finish();
							log.info("检查任务：" + "已完成");
							return;
						}
					} else {
						count = 5;
					}
				}
			}
		}, "Thread-检查状态");
		t.setDaemon(true);
		t.start();
	}*/

	public String getJobTag() {
		return jobTag;
	}

	public void setJobTag(String jobTag) {
		this.jobTag = jobTag;
	}

}
