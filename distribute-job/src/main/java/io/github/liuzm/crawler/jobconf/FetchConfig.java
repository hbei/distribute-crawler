
package io.github.liuzm.crawler.jobconf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.ConfigurationException;
import io.github.liuzm.crawler.exception.QueueException;
import io.github.liuzm.crawler.pendingqueue.PendingManager;
import io.github.liuzm.crawler.url.WebURL;
import io.github.liuzm.crawler.vo.KeyValue;


public class FetchConfig extends Configuration{
	
	public FetchConfig() {
		
	}
	
	private String jobTag="default";
	private String proxyPath ="conf/proxyips.properties" ;
	
	private String type="default";
	/**
	 * job需要启动的线程数
	 */
	private int threadNum=1;
	/**
	 * Socket超时，单位毫秒
	 */
	private int socketTimeoutMilliseconds = 5000;
	/**
	 * connection超时，单位毫秒
	 */
	private int connectionTimeout = 5000;
	/**
	 * 两次请求之间的等待（延迟）时间
	 */
	private int delayBetweenRequests = 200;
	/**
	 * 爬取链接的深度，-1表示无限制
	 */
	private int maxDepthOfCrawling = -1;
	/**
	 * 单个页面处理的最大外链数
	 */
	private int maxOutgoingLinksToFollow = 5000;
	/**
	 * 是否下载二进制文件
	 */
	private boolean fetchBinaryContent = false;
	/**
	 * 可下载文件的后缀
	 */
	private String fileSuffix = "jpg,gif,png,avi,mtk";
	/**
	 * agent
	 */
	private String agent="";
	/**
	 * 是否爬https链接
	 */
	private boolean https = true;
	/**
	 * 是否仅抓取当前域名下内容
	 */
	private boolean onlyDomain = true;
	/**
	 * 是否遵循robots协议
	 */
	private boolean robots = true;
	/**
	 * 最大连接数
	 */
	private int maxTotalConnections = 200;
	/**
	 * 每个远程主机的最大连接数
	 */
	private int maxConnectionsPerHost = 200;
	/**
	 * 页面大小限制，大于该值则不抓取
	 */
	private int maxDownloadSizePerPage = 1048576;
	
	private List<String> proxyIps = null;
	
	/**
	 * 种子地址
	 */
	private List<String> seeds = Lists.newArrayList();
	/**
	 * 收集url的策略
	 */
	private List<String> fetchUrlFilters = Lists.newArrayList();
	/**
	 * 推入下个处理环节的Url处理策略
	 */
	private List<KeyValue<String, String>> extractUrlfilters = Lists.newArrayList();
	
	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public int getSocketTimeoutMilliseconds() {
		return socketTimeoutMilliseconds;
	}

	public void setSocketTimeoutMilliseconds(int socketTimeoutMilliseconds) {
		this.socketTimeoutMilliseconds = socketTimeoutMilliseconds;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getDelayBetweenRequests() {
		return delayBetweenRequests;
	}

	public void setDelayBetweenRequests(int delayBetweenRequests) {
		this.delayBetweenRequests = delayBetweenRequests;
	}

	public int getMaxDepthOfCrawling() {
		return maxDepthOfCrawling;
	}

	public void setMaxDepthOfCrawling(int maxDepthOfCrawling) {
		this.maxDepthOfCrawling = maxDepthOfCrawling;
	}

	public int getMaxOutgoingLinksToFollow() {
		return maxOutgoingLinksToFollow;
	}

	public void setMaxOutgoingLinksToFollow(int maxOutgoingLinksToFollow) {
		this.maxOutgoingLinksToFollow = maxOutgoingLinksToFollow;
	}

	public boolean isFetchBinaryContent() {
		return fetchBinaryContent;
	}

	public void setFetchBinaryContent(boolean fetchBinaryContent) {
		this.fetchBinaryContent = fetchBinaryContent;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public boolean isHttps() {
		return https;
	}

	public void setHttps(boolean https) {
		this.https = https;
	}

	public boolean isOnlyDomain() {
		return onlyDomain;
	}

	public void setOnlyDomain(boolean onlyDomain) {
		this.onlyDomain = onlyDomain;
	}

	public boolean isRobots() {
		return robots;
	}

	public void setRobots(boolean robots) {
		this.robots = robots;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public int getMaxDownloadSizePerPage() {
		return maxDownloadSizePerPage;
	}

	public void setMaxDownloadSizePerPage(int maxDownloadSizePerPage) {
		this.maxDownloadSizePerPage = maxDownloadSizePerPage;
	}

	


	public List<String> getSeeds() {
		return seeds;
	}

	public void setSeeds(List<String> seeds) {
		this.seeds = seeds;
	}

	
	
	public List<String> getFetchUrlFilters() {
		return fetchUrlFilters;
	}

	public void setFetchUrlFilters(List<String> fetchUrlFilters) {
		this.fetchUrlFilters = fetchUrlFilters;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<KeyValue<String, String>> getExtractUrlfilters() {
		return extractUrlfilters;
	}

	public void setExtractUrlfilters(List<KeyValue<String, String>> extractUrlfilters) {
		this.extractUrlfilters = extractUrlfilters;
	}

	/**
	 * 从配置文件中加载配置信息
	 * @param confFile
	 * @return
	 */
	public FetchConfig loadConfig(Document confDoc) throws ConfigurationException{
		try {
			Document doc = confDoc;
			super.setJobName(doc.select("job").attr("name"));
			super.setIndexName(doc.select("job").attr("indexName"));
			Elements e = doc.select("fetch");
			this.type = e.select("type").text();
			this.agent = e.select("agent").text();
			String temp = e.select("threadNum").text();
			if(StringUtils.isNotBlank(temp)){
				this.threadNum = Integer.parseInt(temp);
			}
			
			temp = e.select("delayBetweenRequests").text();
			if(StringUtils.isNotBlank(temp)){
				this.delayBetweenRequests = Integer.parseInt(temp);
			}
			
			temp = e.select("maxDepthOfCrawling").text();
			if(StringUtils.isNotBlank(temp)){
				this.maxDepthOfCrawling = Integer.parseInt(temp);
			}
			
			temp = e.select("fetchBinaryContent").text();
			if(StringUtils.isNotBlank(temp)){
				this.fetchBinaryContent = Boolean.parseBoolean(temp);
			}
			
			if(StringUtils.isNotBlank(e.select("maxOutgoingLinksToFollow").text())){
				this.maxOutgoingLinksToFollow = Integer.parseInt(e.select("maxOutgoingLinksToFollow").text());
			}
			
			temp = e.select("fileSuffix").text();
			if(StringUtils.isNotBlank(temp)){
				this.fileSuffix = temp;
			}
			
			temp = e.select("maxDownloadSizePerPage").text();
			if(StringUtils.isNotBlank(temp)){
				this.maxDownloadSizePerPage = Integer.parseInt(temp);
			}
			
			temp = e.select("https").text();
			if(StringUtils.isNotBlank(temp)){
				this.https = Boolean.parseBoolean(temp);
			}
			
			temp = e.select("onlyDomain").text();
			if(StringUtils.isNotBlank(temp)){
				this.onlyDomain = Boolean.parseBoolean(temp);
			}
			
			temp = e.select("socketTimeoutMilliseconds").text();
			if(StringUtils.isNotBlank(temp)){
				this.socketTimeoutMilliseconds = Integer.parseInt(temp);
			}
			
			temp = e.select("connectionTimeout").text();
			if(StringUtils.isNotBlank(temp)){
				this.connectionTimeout = Integer.parseInt(temp);
			}
			
			temp = e.select("maxTotalConnections").text();
			if(StringUtils.isNotBlank(temp)){
				this.maxTotalConnections = Integer.parseInt(temp);
			}
			
			temp = e.select("maxConnectionsPerHost").text();
			if(StringUtils.isNotBlank(temp)){
				this.maxConnectionsPerHost = Integer.parseInt(e.select("maxConnectionsPerHost").text());
			}
			
			temp = e.select("maxConnectionsPerHost").text();
			if(StringUtils.isNotBlank(temp)){
				this.maxConnectionsPerHost = Integer.parseInt(temp);
			}
			
			temp = e.select("proxy").text();
			if(StringUtils.isNotBlank(temp)){
				Properties p  = PropertyConfigurationHelper.getProperties(temp);
				this.proxyIps = Lists.newLinkedList();
				for (Object o : p.keySet()) {
					proxyIps.add((String)p.get(o));
				}
				
			}
			
			// 加入seed
			Elements seeds = doc.select("fetch seeds seed");
			for(Element element:seeds){
				String url = element.text();
				if(StringUtils.isBlank(url)){
					continue;
				}
				url = url.trim();
				String area = element.attr("area");
				this.seeds.add(url);
				
				WebURL areaUrl = new WebURL(area,url);
				
				try {
					PendingManager.getPendingArea(super.getJobName()).addElement(areaUrl);
				} catch (QueueException e1) {
					e1.printStackTrace();
				}
				
			}
			
			/*
			 * 加载插入待爬取Url队列的过滤规则
			 */
			Elements fetchUrlFilters = doc.select("fetchUrlFilters filter");
			for(Element element:fetchUrlFilters){
				String tmp = element.text();
				if(StringUtils.isNoneBlank(tmp))
					this.fetchUrlFilters.add(element.text());
			}
			
			/*
			 * 加载插入待提取页面Url的过滤规则
			 */
			Elements extractUrlfilters = doc.select("extractUrlfilters filter");
			for(Element element:extractUrlfilters){
				String tmp = element.text();
				String tmp_rep = element.attr("replace");
				if(StringUtils.isNoneBlank(tmp))
					this.extractUrlfilters.add(new KeyValue(tmp,tmp_rep));
			}
		} catch (NumberFormatException e) {
			throw new ConfigurationException("配置文件加载错误："+e.getMessage());
		}
		//super.setFetchConfig(this);
		return this;
	}

	
	public String getJobTag() {
		return jobTag;
	}

	public void setJobTag(String jobTag) {
		this.jobTag = jobTag;
	}

	public String getProxyPath() {
		return proxyPath;
	}

	public void setProxyPath(String proxyPath) {
		this.proxyPath = proxyPath;
	}

	public List<String> getProxyIps() {
		return proxyIps;
	}

	public void setProxyIps(List<String> proxyIps) {
		this.proxyIps = proxyIps;
	}

	@Override
	public String toString() {
		return "FetchConfig [type=" + type + ", threadNum=" + threadNum
				+ ", socketTimeoutMilliseconds=" + socketTimeoutMilliseconds
				+ ", connectionTimeout=" + connectionTimeout
				+ ", delayBetweenRequests=" + delayBetweenRequests
				+ ", maxDepthOfCrawling=" + maxDepthOfCrawling
				+ ", maxOutgoingLinksToFollow=" + maxOutgoingLinksToFollow
				+ ", fetchBinaryContent=" + fetchBinaryContent
				+ ", fileSuffix=" + fileSuffix + ", agent=" + agent
				+ ", https=" + https + ", onlyDomain=" + onlyDomain
				+ ", robots=" + robots + ", maxTotalConnections="
				+ maxTotalConnections + ", maxConnectionsPerHost="
				+ maxConnectionsPerHost + ", maxDownloadSizePerPage=";
	}

	// test
	public static void main(String[] args) {
		FetchConfig fetchConfig = new FetchConfig();
		Document document;
		try {
			document = Jsoup.parse(new File("conf/sfang_conf2.xml"), "utf-8");
			System.out.println(fetchConfig.loadConfig(document).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
}
