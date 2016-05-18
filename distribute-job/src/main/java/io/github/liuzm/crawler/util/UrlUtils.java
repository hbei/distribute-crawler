package io.github.liuzm.crawler.util;
public class UrlUtils {
	
	/**
	 * @param url
	 * @return
	 * @desc 从Url中提取域名
	 */
	public String getDomain(String url){
		int domainStartIdx = url.indexOf("//") + 2;
		int domainEndIdx = url.indexOf('/', domainStartIdx);
		return url.substring(domainStartIdx, domainEndIdx);
	}
	/**
	 * @param url
	 * @return
	 * @desc 从Url中取得base url
	 */
	public String getBaseUrl(String url){
		return url.substring(0, url.lastIndexOf("/")+1);
	}

}