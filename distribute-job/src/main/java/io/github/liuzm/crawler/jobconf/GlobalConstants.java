package io.github.liuzm.crawler.jobconf;

/**
 * @author
 * @date 2014-8-29
 * 描述：系统全局静态常量
 */
public class GlobalConstants {
	/**
	 * conf配置文件路径
	 */
	public static String propertiyFilePath = "conf.properties";
	/**
	 * 待处理URL队列大小
	 */
	public static String pendingUrlsQueueSize = "pending.urls.queue.size";
	/**
	 * 待处理页面队列大小
	 */
	public static String pendingPagesQueueSize = "pending.pages.queue.size";
	/**
	 * 失败页面队列大小
	 */
	public static String failedPagesQueueSize = "failed.pages.queue.size";
	/**
	 * 等待存储的提取信息队列大小
	 */
	public static String pendingStoreMessgeQueueSize = "pending.store.pages.queue.size";
	/**
	 * （解析或者收取）失败页面备份文件路径
	 */
	public static String failedPagesBackupPath = "failed.pages.backup.path";
	/**
	 * 是否忽略错误的或者处理失败的页面
	 */
	public static String ignoreFailedPages = "ignore.failed.pages";
	
}
