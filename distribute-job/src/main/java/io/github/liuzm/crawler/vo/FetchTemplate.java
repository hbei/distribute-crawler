package io.github.liuzm.crawler.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class FetchTemplate {
	
	//private static Element element = null;
	
	String type = "default";
	String type_comment = "默认为Default，如果有其他实现，填写类的全路径";
	String agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36";
	String agent_comment = "浏览器代理";
	String threadNum = "10";
	String threadNum_comment = "爬取线程数";
	String delayBetweenRequests = "200";
	String delayBetweenRequests_comment = "同一线程两次请求之间的间隔";
	String maxDepthOfCrawling = "-1";
	String maxDepthOfCrawling_comment = "Url爬取深度 -1表示不限深度 ";
	String maxOutgoingLinksToFollow = "100";
	String maxOutgoingLinksToFollow_comment = "";
	String fetchBinaryContent = "false";
	String fetchBinaryContent_comment = "是否下载二进制数据";
	
	String fileSuffix = "gif,jpg,png,zip,rar,aiv,mtk";
	String fileSuffix_comment = "下载文件的后缀";
	String maxDownloadSizePerPage = "1048576";
	String maxDownloadSizePerPage_comment = "无";
	String https = "true";
	String https_comment = "是否支持https";
	String onlyDomain = "true";
	String onlyDomain_comment = "是否仅爬取当前域名";
	String proxy = "proxyips.properties";
	String proxy_comment = "代理设置 文件位置";
	
	Element connection = DocumentHelper.createElement("connection");
	String connection_comment = "爬取链接的参数配置";
	
	
	List<KeyValue> seeds = Lists.newArrayList();
	//爬取的url，正则过滤规则
	List<String> fetchFilters = Lists.newArrayList();
	String fetchFilters_comment = "正则表达式，用于过滤爬取url";
	
	//抓取的url，正则过滤规则
	List<KeyValue> extractFilters = Lists.newArrayList();
	String extractFilters_comment = "正则表达式，用于过滤抓取url";
	
	String seeds_comment = "url种子";
	
	String note = "爬取配置";
	public FetchTemplate() {
		
	}
	
	
	/**
	 * 创建FetchTemplate模板（Dom 元素），从FetchTemplate对象里
	 * @param savefetch
	 * @return  Fetch部分的Dom 元素
	 */
	public Element createTemplate(FetchTemplate savefetch){
		FetchTemplate fetch = savefetch;
		
		Element element = DocumentHelper.createElement("fetch");
		//type
		element.addComment(StringUtils.isNotBlank(fetch.getType_comment())?fetch.getType_comment():this.type_comment);
		element.addElement("type").addText(StringUtils.isNotBlank(fetch.getType())?fetch.getType():this.type);
		
		//agent
		element.addComment(StringUtils.isNotBlank(fetch.getAgent_comment())?fetch.getAgent_comment():this.agent_comment);
		element.addElement("agent").addText(StringUtils.isNotBlank(fetch.getAgent())?fetch.getAgent():this.agent);
		
		//threadNum
		element.addComment(StringUtils.isNotBlank(fetch.getThreadNum_comment())?fetch.getThreadNum_comment():this.threadNum_comment);
		element.addElement("threadNum").addText(StringUtils.isNotBlank(fetch.getThreadNum())?fetch.getThreadNum():this.threadNum);
		
		//delayBetweenRequests
		element.addComment(StringUtils.isNotBlank(fetch.getDelayBetweenRequests_comment())?fetch.getDelayBetweenRequests_comment():this.delayBetweenRequests_comment);
		element.addElement("delayBetweenRequests").addText(StringUtils.isNotBlank(fetch.getDelayBetweenRequests())?fetch.getDelayBetweenRequests():this.delayBetweenRequests);

		
		//maxDepthOfCrawling
		element.addComment(StringUtils.isNotBlank(fetch.getMaxDepthOfCrawling_comment())?fetch.getMaxDepthOfCrawling_comment():this.maxDepthOfCrawling_comment);
		element.addElement("maxDepthOfCrawling").addText(StringUtils.isNotBlank(fetch.getMaxDepthOfCrawling())?fetch.getMaxDepthOfCrawling():this.maxDepthOfCrawling);

		//maxOutgoingLinksToFollow
		element.addComment(StringUtils.isNotBlank(fetch.getMaxOutgoingLinksToFollow_comment())?fetch.getMaxOutgoingLinksToFollow_comment():this.maxOutgoingLinksToFollow_comment);
		element.addElement("maxOutgoingLinksToFollow").addText(StringUtils.isNotBlank(fetch.getMaxOutgoingLinksToFollow())?fetch.getMaxOutgoingLinksToFollow():this.maxOutgoingLinksToFollow);

		//fetchBinaryContent
		element.addComment(StringUtils.isNotBlank(fetch.getFetchBinaryContent_comment())?fetch.getFetchBinaryContent_comment():this.fetchBinaryContent_comment);
		element.addElement("fetchBinaryContent").addText(StringUtils.isNotBlank(fetch.getFetchBinaryContent())?fetch.getFetchBinaryContent():this.fetchBinaryContent);

		//fileSuffix
		element.addComment(StringUtils.isNotBlank(fetch.getFileSuffix_comment())?fetch.getFileSuffix_comment():this.fileSuffix_comment);
		element.addElement("fileSuffix").addText(StringUtils.isNotBlank(fetch.getFileSuffix())?fetch.getFileSuffix():this.fileSuffix);

		//maxDownloadSizePerPage
		element.addComment(StringUtils.isNotBlank(fetch.getMaxDownloadSizePerPage_comment())?fetch.getMaxDownloadSizePerPage_comment():this.maxDownloadSizePerPage_comment);
		element.addElement("maxDownloadSizePerPage").addText(StringUtils.isNotBlank(fetch.getMaxDownloadSizePerPage())?fetch.getMaxDownloadSizePerPage():this.maxDownloadSizePerPage);
		
		//https
		element.addComment(StringUtils.isNotBlank(fetch.getHttps_comment())?fetch.getHttps_comment():this.https_comment);
		element.addElement("https").addText(StringUtils.isNotBlank(fetch.getHttps())?fetch.getHttps():this.https);
		
		//onlyDomain
		element.addComment(StringUtils.isNotBlank(fetch.getOnlyDomain_comment())?fetch.getOnlyDomain_comment():this.onlyDomain_comment);
		element.addElement("onlyDomain").addText(StringUtils.isNotBlank(fetch.getOnlyDomain())?fetch.getOnlyDomain():this.onlyDomain);
		
		//proxy
		element.addComment(StringUtils.isNotBlank(fetch.getProxy_comment())?fetch.getProxy_comment():this.proxy_comment);
		element.addElement("proxy").addText(StringUtils.isNotBlank(fetch.getProxy())?fetch.getProxy():this.proxy);
		
		//connection
		Element e = element.addComment(StringUtils.isNotBlank(fetch.getConnection_comment())?fetch.getConnection_comment():this.connection_comment);	
		if(this.connection!=null){
			e.addComment("http链接超时时间");
			e.addElement("socketTimeoutMilliseconds").addText("20000");
			e.addComment("从http链接池中获取到可用链接的超时时间");
			e.addElement("connectionTimeout").addText("0");
			e.addElement("maxTotalConnections").addText("600");
			e.addElement("maxConnectionsPerHost").addText("600");
		}
		
		//seeds
		element.addComment(StringUtils.isNotBlank(fetch.getSeeds_comment())?fetch.getSeeds_comment():this.seeds_comment);
		Element seeds_e = element.addElement("seeds");
		if(fetch.getSeeds()!=null&&fetch.getSeeds().size()>0){
			
			for (KeyValue<String,String> kv : fetch.getSeeds()) {
				if(StringUtils.isBlank(kv.getValue()))
					continue;
				seeds_e.addElement("seed").addAttribute("area", kv.getKey()).addText(kv.getValue());
			}
		}
		
		//fetchUrlFilters
		element.addComment(StringUtils.isNoneBlank(fetch.getFetchFilters_comment())?fetch.getFetchFilters_comment():this.fetchBinaryContent_comment);
		List<String> ffl_tmp = fetch.getFetchFilters();
		Element fetchUrlFilters_e = element.addElement("fetchUrlFilters");
		if(ffl_tmp!=null&&ffl_tmp.size()>0){
			
			for (String filter : ffl_tmp) {
				if(StringUtils.isNoneBlank(filter))
					fetchUrlFilters_e.addElement("filter").addText(filter);
			}
		}
		//extractUrlfilters
		element.addComment(StringUtils.isNoneBlank(fetch.getExtractFilters_comment())?fetch.getExtractFilters_comment():this.extractFilters_comment);
		List<KeyValue> efl_tmp = fetch.getExtractFilters();
		Element extractUrlfilters_e = element.addElement("extractUrlfilters");
		if(efl_tmp!=null&&efl_tmp.size()>0){
			
			for (KeyValue<String,String> filter : efl_tmp) {
				if(StringUtils.isNoneBlank(filter.getKey()))
					extractUrlfilters_e.addElement("filter").addText(filter.getKey()).addAttribute("replace", filter.getValue());
			}
		}
		
		return element;
	}

	/**
	 * 初始化对象，从Dom元素里面
	 * @param e_job
	 */
	public void init(Element e_job) {
		Element element = e_job.element("fetch");
		this.type =  element.elementTextTrim("type");
		this.agent = element.elementTextTrim("agent");
		this.threadNum = element.elementText("threadNum");
		
		this.delayBetweenRequests = element.elementText("delayBetweenRequests");
		this.maxDepthOfCrawling = element.elementText("maxDepthOfCrawling");
		
		this.maxOutgoingLinksToFollow = element.elementText("maxOutgoingLinksToFollow");
		
		this.fetchBinaryContent = element.elementText("fetchBinaryContent");
		
		this.fileSuffix = element.elementText("fileSuffix");

		this.maxDownloadSizePerPage = element.elementText("maxDownloadSizePerPage");
		
		this.https = element.elementText("https");
		
		this.onlyDomain = element.elementText("onlyDomain");
		
		this.proxy = element.elementText("proxy");
		
		Element connection = element.element("connection");
		
		Element seeds_map = element.element("seeds");
		List<Element> l = seeds_map.elements("seed");
		for (Element e : l) {
			seeds.add(new KeyValue(e.attribute("area").getValue(),e.getTextTrim()));
		}
		l =null;
		
		Element fetechFilters_list = element.element("fetchUrlFilters");
		List<Element> ffl = fetechFilters_list.elements("filter");
		fetechFilters_list = null;
		for (Element e : ffl) {
			String tmp = e.getTextTrim();
			if(StringUtils.isNoneBlank(tmp))
				fetchFilters.add(tmp);
		}
		ffl=null;
		
		Element extractFilters_list = element.element("extractUrlfilters");
		List<Element> efl = extractFilters_list.elements("filter");
		for (Element e : efl) {
			String tmp = e.getTextTrim();
			String tmp_rep = e.attributeValue("replace", "").trim();
			if(StringUtils.isNoneBlank(tmp))
				extractFilters.add(new KeyValue(tmp, tmp_rep));
		}
		efl=null;
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType_comment() {
		return type_comment;
	}

	public void setType_comment(String type_comment) {
		this.type_comment = type_comment;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgent_comment() {
		return agent_comment;
	}

	public void setAgent_comment(String agent_comment) {
		this.agent_comment = agent_comment;
	}

	public String getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(String threadNum) {
		this.threadNum = threadNum;
	}

	public String getThreadNum_comment() {
		return threadNum_comment;
	}

	public void setThreadNum_comment(String threadNum_comment) {
		this.threadNum_comment = threadNum_comment;
	}

	public String getDelayBetweenRequests() {
		return delayBetweenRequests;
	}

	public void setDelayBetweenRequests(String delayBetweenRequests) {
		this.delayBetweenRequests = delayBetweenRequests;
	}

	public String getDelayBetweenRequests_comment() {
		return delayBetweenRequests_comment;
	}

	public void setDelayBetweenRequests_comment(String delayBetweenRequests_comment) {
		this.delayBetweenRequests_comment = delayBetweenRequests_comment;
	}

	public String getMaxDepthOfCrawling() {
		return maxDepthOfCrawling;
	}

	public void setMaxDepthOfCrawling(String maxDepthOfCrawling) {
		this.maxDepthOfCrawling = maxDepthOfCrawling;
	}

	public String getMaxDepthOfCrawling_comment() {
		return maxDepthOfCrawling_comment;
	}

	public void setMaxDepthOfCrawling_comment(String maxDepthOfCrawling_comment) {
		this.maxDepthOfCrawling_comment = maxDepthOfCrawling_comment;
	}

	public String getMaxOutgoingLinksToFollow() {
		return maxOutgoingLinksToFollow;
	}

	public void setMaxOutgoingLinksToFollow(String maxOutgoingLinksToFollow) {
		this.maxOutgoingLinksToFollow = maxOutgoingLinksToFollow;
	}

	public String getMaxOutgoingLinksToFollow_comment() {
		return maxOutgoingLinksToFollow_comment;
	}

	public void setMaxOutgoingLinksToFollow_comment(
			String maxOutgoingLinksToFollow_comment) {
		this.maxOutgoingLinksToFollow_comment = maxOutgoingLinksToFollow_comment;
	}

	public String getFetchBinaryContent() {
		return fetchBinaryContent;
	}

	public void setFetchBinaryContent(String fetchBinaryContent) {
		this.fetchBinaryContent = fetchBinaryContent;
	}

	public String getFetchBinaryContent_comment() {
		return fetchBinaryContent_comment;
	}

	public void setFetchBinaryContent_comment(String fetchBinaryContent_comment) {
		this.fetchBinaryContent_comment = fetchBinaryContent_comment;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getFileSuffix_comment() {
		return fileSuffix_comment;
	}

	public void setFileSuffix_comment(String fileSuffix_comment) {
		this.fileSuffix_comment = fileSuffix_comment;
	}

	public String getMaxDownloadSizePerPage() {
		return maxDownloadSizePerPage;
	}

	public void setMaxDownloadSizePerPage(String maxDownloadSizePerPage) {
		this.maxDownloadSizePerPage = maxDownloadSizePerPage;
	}

	public String getMaxDownloadSizePerPage_comment() {
		return maxDownloadSizePerPage_comment;
	}

	public void setMaxDownloadSizePerPage_comment(
			String maxDownloadSizePerPage_comment) {
		this.maxDownloadSizePerPage_comment = maxDownloadSizePerPage_comment;
	}

	public String getHttps() {
		return https;
	}

	public void setHttps(String https) {
		this.https = https;
	}

	public String getHttps_comment() {
		return https_comment;
	}

	public void setHttps_comment(String https_comment) {
		this.https_comment = https_comment;
	}

	public String getOnlyDomain() {
		return onlyDomain;
	}

	public void setOnlyDomain(String onlyDomain) {
		this.onlyDomain = onlyDomain;
	}

	public String getOnlyDomain_comment() {
		return onlyDomain_comment;
	}

	public void setOnlyDomain_comment(String onlyDomain_comment) {
		this.onlyDomain_comment = onlyDomain_comment;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getProxy_comment() {
		return proxy_comment;
	}

	public void setProxy_comment(String proxy_comment) {
		this.proxy_comment = proxy_comment;
	}

	public Element getConnection() {
		return connection;
	}

	public void setConnection(Element connection) {
		this.connection = connection;
	}

	public String getConnection_comment() {
		return connection_comment;
	}

	public void setConnection_comment(String connection_comment) {
		this.connection_comment = connection_comment;
	}

	
	public List<KeyValue> getSeeds() {
		return seeds;
	}


	public void setSeeds(List<KeyValue> seeds) {
		this.seeds = seeds;
	}


	public String getSeeds_comment() {
		return seeds_comment;
	}

	public void setSeeds_comment(String seeds_comment) {
		this.seeds_comment = seeds_comment;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public List<String> getFetchFilters() {
		return fetchFilters;
	}


	public void setFetchFilters(List<String> fetchFilters) {
		this.fetchFilters = fetchFilters;
	}


	public String getFetchFilters_comment() {
		return fetchFilters_comment;
	}


	public void setFetchFilters_comment(String fetchFilters_comment) {
		this.fetchFilters_comment = fetchFilters_comment;
	}


	public List<KeyValue> getExtractFilters() {
		return extractFilters;
	}


	public void setExtractFilters(List<KeyValue> extractFilters) {
		this.extractFilters = extractFilters;
	}


	public String getExtractFilters_comment() {
		return extractFilters_comment;
	}


	public void setExtractFilters_comment(String extractFilters_comment) {
		this.extractFilters_comment = extractFilters_comment;
	}

	
}
