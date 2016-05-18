package io.github.liuzm.crawler.pendingqueue;

import java.util.concurrent.ConcurrentHashMap;

public class PendingManager {
	
	private static final String areaPrefix = "area_";
	private static final String urlPrefix = "url_";
	private static final String storePrefix = "store_";
	private static final String pagePrefix = "page_";
	private static final String reCrawPrefix = "recraw_";
	
	private static ConcurrentHashMap<String, AbsPendingQueue<?>> CM = new ConcurrentHashMap<>();
	
	/**
	 * 获取某任务的城市等待队列
	 * @param jobName
	 * @return
	 */
	public static PendingAreas getPendingArea(String jobName){
		String pendingUrlName = areaPrefix + jobName;
		Object p = CM.get(pendingUrlName);
		if(p!=null){
			return (PendingAreas)p;
		}else {
			PendingAreas pu = new PendingAreas(pendingUrlName);
			CM.put(pendingUrlName, pu);
			return (PendingAreas) CM.get(pendingUrlName);
		}
	}
	
	
	/**
	 * 获取某任务的url等待队列
	 * @param jobName
	 * @return
	 */
	public static PendingUrls getPendingUlr(String jobName){
		String pendingUrlName = urlPrefix + jobName;
		Object p = CM.get(pendingUrlName);
		if(p!=null){
			return (PendingUrls)p;
		}else {
			PendingUrls pu = new PendingUrls(pendingUrlName);
			CM.put(pendingUrlName, pu);
			return (PendingUrls) CM.get(pendingUrlName);
		}
	}
	
	/**
	 * 返回某job对应的Page队列
	 * @param jobName
	 * @return
	 */
	public static PendingPages getPendingPages(String jobName){
		String pendingPageName = pagePrefix + jobName;
		Object p = CM.get(pendingPageName);
		if(p!=null){
			return (PendingPages)p;
		}else {
			PendingPages pu = new PendingPages(pendingPageName);
			CM.put(pendingPageName, pu);
			return (PendingPages) CM.get(pendingPageName);
		}
	}
	
	/**
	 * 返回某job对应的Store队列
	 * @param jobName
	 * @return
	 */
	public static PendingStore getPendingStore(String jobName){
		String pendStoreName = storePrefix + jobName;
		Object p = CM.get(pendStoreName);
		if(p!=null){
			return (PendingStore)p;
		}else {
			PendingStore pu = new PendingStore(pendStoreName);
			CM.put(pendStoreName, pu);
			return (PendingStore) CM.get(pendStoreName);
		}
	}
	
	public static PendRecraw getUrlsToRecraw(String jobName){
		String pendRecrawName = reCrawPrefix + jobName;
		Object p = CM.get(pendRecrawName);
		if(p!=null){
			return (PendRecraw)p;
		}else {
			PendRecraw urlsToRecraw = new PendRecraw(pendRecrawName);
			CM.put(pendRecrawName, urlsToRecraw);
			return (PendRecraw)CM.get(pendRecrawName);
		}
	}
	
}
