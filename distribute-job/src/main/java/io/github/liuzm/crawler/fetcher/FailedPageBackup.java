package io.github.liuzm.crawler.fetcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.liuzm.crawler.exception.QueueException;
import io.github.liuzm.crawler.jobconf.GlobalConstants;
import io.github.liuzm.crawler.jobconf.PropertyConfigurationHelper;
import io.github.liuzm.crawler.page.Page;
import io.github.liuzm.crawler.util.DateTimeUtil;
import io.github.liuzm.crawler.worker.Worker;

public class FailedPageBackup {

	private Log log = LogFactory.getLog(this.getClass());
	private static FailedPageBackup instance = null;
	private PropertyConfigurationHelper config = PropertyConfigurationHelper
			.getInstance();
	/**
	 * 处理失败的页面队列
	 */
	private BlockingQueue<Page> Queue = null;
	/**
	 * 是否忽略处理失败的页面（如果页面解析出错则放掉该也，不加入失败页面队列）
	 */
	private boolean ignoreFailedPage = true;

	private FailedPageBackup() {
		init();
	}

	public static FailedPageBackup getInstace() {
		if (null == instance) {
			instance = new FailedPageBackup();
		}
		return instance;
	}

	public void init() {
		ignoreFailedPage = Boolean.getBoolean(config.getString(
				GlobalConstants.ignoreFailedPages, "true"));
		if (!ignoreFailedPage) {
			Queue = new LinkedBlockingDeque<Page>(config.getInt(
					GlobalConstants.failedPagesQueueSize, 2000));
			// 执行备份
			BackupFailedPages backup = new BackupFailedPages();
			Thread failedPagesBackupThread = new Thread(backup,
					"failed-pages-backup-thread");
			ScheduledExecutorService scheduler = Executors
					.newScheduledThreadPool(1);
			scheduler.scheduleAtFixedRate(failedPagesBackupThread, 60, 60,
					TimeUnit.SECONDS);
		}
	}

	/**
	 * 向队列中添加一个页面
	 * 
	 * @param page
	 * @desc
	 */
	public void addPage(Page page) throws QueueException {
		if (page != null) {
			try {
				Queue.put(page);
			} catch (InterruptedException e) {
				throw new QueueException("待处理页面加入操作中断");
			}
		}
	}

	/**
	 * 从队列中取走一个页面
	 * 
	 * @return
	 * @desc
	 */
	public Page getPage() throws QueueException {
		try {
			return Queue.take();
		} catch (InterruptedException e) {
			throw new QueueException("待处理页面队列取出操作中断");
		}
	}

	public boolean isEmpty() {
		return Queue.isEmpty();
	}

	/**
	 * @author shenbaise(shenbaise@outlook.com)
	 * @date 2013-6-30 备份失败页面
	 */
	private class BackupFailedPages implements Runnable {
		@Override
		public void run() {
			Page page;
			boolean flag = true;
			File backFile = null;
			FileChannel fc = null;
			byte[] b = new byte[] { (byte) 1, (byte) 1 };
			if (Worker.isStop())
				if (!ignoreFailedPage) {
					backFile = new File(config.getString(
							GlobalConstants.failedPagesBackupPath, "")
							+ File.pathSeparator + DateTimeUtil.getDate());
					try {
						fc = new FileOutputStream(backFile, true)
						.getChannel();
						if (flag) {
							while (null != (page = Queue.poll())) {
								fc.write(ByteBuffer.wrap(page.getContentData()));
								fc.write(ByteBuffer.wrap(b));
							}
							fc.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
						log.warn(e.getMessage());
					}
				}
		}
	}
}
