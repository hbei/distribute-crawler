package io.github.liuzm.crawler.util;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.github.liuzm.crawler.jobconf.FetchConfig;
import io.github.liuzm.crawler.util.DateTimeUtil;
import io.github.liuzm.crawler.util.WrongUrlLog;

public abstract class UrlFetcherUtil {
	private static Log log = LogFactory.getLog(UrlFetcherUtil.class);
	
	
	/*
	 * 获取地址的document
	 
	public static Document goFetchPage(String url, FetchConfig config) {
		int count = 3;
		Document doc = null;
		while(count!=0&&doc==null){
			String proxyIp = setProxy(config.getProxyMap());
			try {
				doc = Jsoup.connect(url).timeout(30000).userAgent(config.getAgent()).get();
				
				count--;
			} catch (IOException e) {
				System.out.println("ERROR:获取网页:" + url + "失败" + "失败的代理ip:"+ proxyIp);
				log.error("获取网页{" + url + "}失败!" + "\t失败的代理ip\t"+ proxyIp);
				//e.printStackTrace();
				
			}
		}
		//3次重试后任然无法获取则失败，记录
		if(count==0&&doc==null){
			WrongUrlLog.writeUrl("all_error_url."+DateTimeUtil.getDate()+".txt", config.getJobName()+"\t"+url+"\t" +new Date().toString());
		}
		
		return doc;

	}*/
	
	/**
	 * 设置一个随机代理iP
	 * @param proxyMap
	 * @return
	 */
	public static String setProxy(final Map<String, String> proxyMap){
		int i = (int) (Math.random() * proxyMap.size());
		String[] ip_port = proxyMap.get(i + "").split(":");
		String ip = ip_port[0];
		String port = ip_port[1];

		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");

		System.setProperty("http.proxyHost", ip);
		System.setProperty("http.proxyPort", port);
		return proxyMap.get(i + "");
	}

}
