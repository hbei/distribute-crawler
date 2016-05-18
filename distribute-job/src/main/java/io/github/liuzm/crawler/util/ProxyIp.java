package io.github.liuzm.crawler.util;


import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

import io.github.liuzm.crawler.fetcher.Fetcher;

public class ProxyIp {
	
	private static final Log log = LogFactory.getLog(ProxyIp.class);
	public static List<String> fetchProxyIps(boolean check){
		String url = "http://www.xici.net.co/nt/";
		Document doc = null;
		List<String> ips  = Lists.newArrayList();
		try {
			doc = Fetcher.goFetchPage(url);
			Elements es = doc.select("table#ip_list tr");
			log.info("爬取代理IP\t 共爬取"+es.size()+"个");
			for (int i = 1; i < es.size(); i++) {
				String p_type = es.get(i).select("td").get(6).ownText();
				if(!"HTTP".equalsIgnoreCase(p_type.trim()))
					continue;
				String p_url = es.get(i).select("td").get(2).ownText();
				String p_port = es.get(i).select("td").get(3).ownText();
				if(StringUtils.isNotBlank(p_url)&&StringUtils.isNumeric(p_port))
					if(check&&checkProxyValid("",p_url,Integer.valueOf(p_port))){
						ips.add(p_url.trim()+":"+p_port.trim());
					}
					
			}
			log.info("爬取代理IP\t 有效代理"+es.size()+"个");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < ips.size(); i++) {
			log.debug("proxy"+i+"="+ips.get(i));
		}
		return ips;
	}

	public static boolean checkProxyValid(String targetURL, String hostName, int port) {  
        HttpHost proxy = new HttpHost(hostName, port);  
        targetURL = StringUtils.isNotBlank(targetURL)?targetURL:"http://www.baidu.com" ; 
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);  
        RequestConfig requestConfig = RequestConfig.custom()  
                .setSocketTimeout(10000)//设置socket超时时间  
                .setConnectTimeout(10000)//设置connect超时时间  
                .build();  
        CloseableHttpClient httpClient = HttpClients.custom()  
                .setRoutePlanner(routePlanner)  
                .setDefaultRequestConfig(requestConfig)  
                .build();  
        HttpGet httpGet = new HttpGet(targetURL);//"http://iframe.ip138.com/ic.asp"  
        CloseableHttpResponse response = null;
        try{  
            response = httpClient.execute(httpGet);  
            int statusCode = response.getStatusLine().getStatusCode();  
            //System.out.println(response.getStatusLine().getStatusCode());  
            if(statusCode == HttpStatus.SC_OK){ 
            	
                return true; 
            }  
            response.close();  
            //System.out.println(html);//打印返回的html  
        } catch(IOException e){  
            System.out.println("****Connection time out****");  
        } finally{
        	try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
          
        return false;  
    }  
	
	public static void main(String[] args) {
		//System.out.println(fetchProxyIps());
	}

}
