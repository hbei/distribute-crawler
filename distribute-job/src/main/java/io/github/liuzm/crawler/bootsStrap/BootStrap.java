package io.github.liuzm.crawler.bootsStrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import io.github.liuzm.Job;
import io.github.liuzm.crawler.classLoader.PluginClassLoader;
import io.github.liuzm.crawler.exception.ConfigurationException;
import io.github.liuzm.crawler.extractor.ExtracterMan;
import io.github.liuzm.crawler.fetcher.Fetcher;
import io.github.liuzm.crawler.fetcher.FetcherMan;
import io.github.liuzm.crawler.jobconf.Configuration;
import io.github.liuzm.crawler.jobconf.ExtractConfig;
import io.github.liuzm.crawler.jobconf.FetchConfig;
import io.github.liuzm.crawler.jobconf.GlobalConstants;
import io.github.liuzm.crawler.jobconf.JobConfigurationManager;
import io.github.liuzm.crawler.jobconf.PropertyConfigurationHelper;
import io.github.liuzm.crawler.jobconf.StoreConfig;
import io.github.liuzm.crawler.pendingqueue.PendingManager;
import io.github.liuzm.crawler.pendingqueue.PendingPages;
import io.github.liuzm.crawler.pendingqueue.PendingStore;
import io.github.liuzm.crawler.pendingqueue.PendingUrls;
import io.github.liuzm.crawler.store.StoreForeman;
import io.github.liuzm.crawler.util.BloomfilterHelper;
import io.github.liuzm.crawler.vo.JobStatus;
import io.github.liuzm.crawler.vo.Status;
import io.github.liuzm.crawler.worker.Worker;

@SuppressWarnings("rawtypes")
public class BootStrap implements Job{
	
	private static final Logger log = LoggerFactory.getLogger(BootStrap.class);
	
	private static PropertyConfigurationHelper propertyHelper = new PropertyConfigurationHelper(GlobalConstants.propertiyFilePath);
	private static BootStrap bootStrap = null;
	
	static{
		try {
			PluginClassLoader.init();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static BootStrap getInstance(){
		if(bootStrap == null){
			synchronized(BootStrap.class){
				if(bootStrap == null){
					bootStrap = new BootStrap();
				}
			}
		}
		return bootStrap;
	}
	
	private static void startStatus(){
		/**
		 * 状态监听
		 */
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Map<String, Integer> times = new HashMap<String, Integer>();
				while(JobManager.getJobs().size()!=0){
					
					int count =5;
					try {
						TimeUnit.SECONDS.sleep(10);
					} catch (InterruptedException e) {
					}
					Collection<String> js = JobManager.getJobs().keySet();
					for(String j:js){
						String job = JobManager.getJob(j).getConfiguration().getJobName();
						Map<String, Long> urlstatus = PendingManager.getPendingUlr(job).pendingStatus();
						Map<String, Long> pagestatus = PendingManager.getPendingPages(job).pendingStatus();
						Map<String, Long> storestatus = PendingManager.getPendingStore(job).pendingStatus();
						log.info("job=urlstatus "+job+"\n"+urlstatus);
						log.info("job=pagestatus "+job+"\n"+pagestatus);
						log.info("job=storestatus "+job+"\n"+storestatus);
						Integer tt = times.get(j);
						
						if(tt!=null&&(Long)urlstatus.get("count")==0&&(Long)pagestatus.get("count")==0&&(Long)pagestatus.get("count")==0){
							if(tt>3){
								log.info("已经重试3次，确定没有任务，程序即将关闭");
								JobManager.getJob(j).setStatus(Status.STATUS_FINISH);
								JobManager.saveJobStatus(j);
								JobManager.removeJob(j);
								
							}else{
								times.put(j, tt+1);
							}
							
						}else{
							times.put(j, 1);
						}
					}
					Map<String, Integer> m_ip = Fetcher.m;
					for (String ip: m_ip.keySet()) {
						System.out.println(ip+":\t"+m_ip.get(ip));
					}
					if(count<0){
						
					}
				}
				log.info("状态监控线程QueueMonitor退出");
			}
		};
		
		Thread monitor = new Thread(runnable,"QueueMonitor");
		monitor.start();
		
		CrawlerStatus.running = true;	
	}
	
	
	/**
	 * @param args
	 * @throws Exception 
	 * @desc 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String args[]) throws Exception {
		try {
			String taskfile = args[0];
			File f = new File(taskfile);
			List list = Lists.newArrayList();
			list.add(Jsoup.parse(f, "utf-8"));
			start(list);
	
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param path
	 * @throws IOException 
	 */
	public static void startByFilePath(String path){
		try {
			File f = new File(path);
			List list = Lists.newArrayList();
			list.add(Jsoup.parse(f, "utf-8"));
			start(list);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (IOException f){
			f.printStackTrace();
		}
	}
	/**
	 * 启动任务
	 * @param jobConf
	 * @throws ConfigurationException 
	 */
	public static void start(Document  document,String jobTag,int id) throws ConfigurationException{
		
		Configuration configuration = new Configuration();
		
		FetchConfig fConfig = new FetchConfig();
		configuration.setFetchConfig(fConfig.loadConfig(document));
		
		//提取爬取的页面信息
		ExtractConfig xconfig = new ExtractConfig();
		configuration.setExtractConfig(xconfig.loadConfig(document));
		
		//存储
		StoreConfig sConfig = new StoreConfig();
		configuration.setStoreConfig(sConfig.loadConfig(document));
		
		JobStatus jobStatus = new JobStatus(id, jobTag,Status.STATUS_RUNNING, configuration);
		
		JobManager.addJob(jobTag, jobStatus);
		
		FetcherMan fetchman = FetcherMan.getInstance(fConfig);
		if(null!=fetchman){
			fetchman.start(jobStatus);
		}else{
			System.out.println("无法获取FetcherMan!!!");
		}
			
		
		ExtracterMan extracterMan = ExtracterMan.getInstance(xconfig);	
		if(extracterMan!=null){
			extracterMan.start(jobStatus);
		}else{
			System.out.println("无法获取ExtracterMan!!!");
		}
			
		
		StoreForeman storeForeman = new StoreForeman();
		storeForeman.start(jobStatus); 
		
		//开启状态监控
		startStatus();
	}
	
	/**
	 * 启动任务
	 * @param jobConf
	 * @throws ConfigurationException 
	 */
	public static void start(List<Document>  configDocs) throws ConfigurationException{
		
		for (Document document : configDocs) {
			Configuration configuration = new Configuration();
			
			FetchConfig fConfig = new FetchConfig();
			configuration.setFetchConfig(fConfig.loadConfig(document));
			FetcherMan fetchman = FetcherMan.getInstance(fConfig);
			
			//提取爬取的页面信息
			ExtractConfig xconfig = new ExtractConfig();
			configuration.setExtractConfig(xconfig.loadConfig(document));
			ExtracterMan extracterMan = ExtracterMan.getInstance(xconfig);
			
			//存储
			StoreConfig sConfig = new StoreConfig();
			configuration.setStoreConfig(sConfig.loadConfig(document));
			StoreForeman storeForeman = new StoreForeman();
			
			
			JobStatus jobStatus = new JobStatus(1, "test-"+fConfig.getJobName(),Status.STATUS_RUNNING, configuration);
			JobManager.addJob("test-"+fConfig.getJobName(), jobStatus);
			
			
			if(null!=fetchman){
				fetchman.start(jobStatus);
			}else{
				System.out.println("无法获取FetcherMan!!!");
			}
			
			if(extracterMan!=null){
				extracterMan.start(jobStatus);
			}else{
				System.out.println("无法获取ExtracterMan!!!");
			}
			
			
			storeForeman.start(jobStatus); 
			
			//开启状态监控
			startStatus();
			
		}
	}
	
	
	/**
	 * 启动任务
	 * @param jobConf
	 * @throws ConfigurationException 
	 * @throws io.github.liuzm.crawler.exception.ConfigurationException 
	 */
	public static void start() throws ConfigurationException,ConfigurationException{
		JobConfigurationManager.init();
		JobConfigurationManager manager = JobConfigurationManager.getInstance();
		List<Document> configDocs = manager.getConfigDoc();
		start(configDocs);	
	}
	
	/**
	 * 停止抓取&保存状态
	 */
	public synchronized static void stop(String jobTag){
		JobManager.removeJob(jobTag);
		Fetcher.proxyerisRuning = false;
		Worker.stop();
		saveStatus(jobTag);
	}
	/**
	 * stop all
	 */
	public synchronized static void stopAll(){
		for(String job:JobManager.getJobs().keySet()){
			stop(job);
		}
		CrawlerStatus.running = false;
	}
	/**
	 * 保存状态，下次启动时可恢复
	 */
	private static void saveStatus(String jobTag){
		JobStatus job = JobManager.getJob(jobTag);
		String jobName = job.getConfiguration().getJobName();
		PendingUrls urls = PendingManager.getPendingUlr(jobName);
		PendingPages pages = PendingManager.getPendingPages(jobName);
		PendingStore stores = PendingManager.getPendingStore(jobName);
		BloomfilterHelper bloomfilter = BloomfilterHelper.getInstance();
		
		File base = new File(propertyHelper.getString("status.save.path", "status"));
		if (!base.exists()) {
			base.mkdir();
		}
		File urlsFile = new File(base,jobName + "/"+urls.getClass().getSimpleName()+".good");
		if(!urlsFile.exists()){
			urlsFile.getParentFile().mkdirs();
		}
		File pagesFile = new File(base,jobName + "/"+urls.getClass().getSimpleName()+".good");
		if(!pagesFile.exists()){
			pagesFile.getParentFile().mkdirs();
		}
		
		File storesFile = new File(base,jobName + "/"+urls.getClass().getSimpleName()+".good");
		if(!storesFile.exists()){
			storesFile.getParentFile().mkdirs();
		}
		File filterFile = new File(base,"filter.good");
		if(!filterFile.exists()){
			filterFile.getParentFile().exists();
		}
			
		try {
			FileOutputStream fosUrl = new FileOutputStream(urlsFile);
			ObjectOutputStream oosUrl = new ObjectOutputStream(fosUrl);
			oosUrl.writeObject(urls);
			oosUrl.close();
			fosUrl.close();
			
			FileOutputStream fosPage = new FileOutputStream(pagesFile);
			ObjectOutputStream oosPage = new ObjectOutputStream(fosPage);
			oosPage.writeObject(pages);
			oosPage.close();
			fosPage.close();
			
			FileOutputStream fosStore = new FileOutputStream(storesFile);
			ObjectOutputStream oosStore = new ObjectOutputStream(fosStore);
			oosStore.writeObject(stores);
			oosStore.close();
			fosStore.close();
			
			FileOutputStream fosFilter = new FileOutputStream(filterFile);
			ObjectOutputStream oosFilter = new ObjectOutputStream(fosFilter);
			oosFilter.writeObject(bloomfilter);
			oosFilter.close();
			fosFilter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String name() {
		return "crawler";
	}

	
}
