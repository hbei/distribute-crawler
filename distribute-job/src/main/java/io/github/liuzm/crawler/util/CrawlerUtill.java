package io.github.liuzm.crawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CrawlerUtill {
	
	private static final Logger log = LoggerFactory.getLogger(CrawlerUtill.class);

	
	/**
	 * 获取当前网页的code
	 * @param httpUrl
	 * @return
	 * @throws IOException
	 */
	public static String getHtmlCode(String httpUrl) throws IOException {
		//定义字符串content
		String content = "";
		//生成传入的URL的对象
		URL url = new URL(httpUrl);
		//获得当前url的字节流（缓冲）
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				url.openStream(),"utf-8"));
		

		String input;
		//当当前行存在数据时
		while ((input = reader.readLine()) != null) {
			//将读取数据赋给content
			content += input;
		}
		//关闭缓冲区
		reader.close();
		//返回content
		return content;
	}
	
	/**
	 * 获取当前网页的code
	 * @param httpUrl
	 * @return
	 * @throws IOException
	 */
	public static String getHtmlCodeByCode(String httpUrl,String code) throws IOException {
		//定义字符串content
		String content = "";
		//生成传入的URL的对象
		URL url = new URL(httpUrl);
		//获得当前url的字节流（缓冲）
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				url.openStream(),code));
		String input;
		//当当前行存在数据时
		while ((input = reader.readLine()) != null) {
			//将读取数据赋给content
			content += input;
		}
		//关闭缓冲区
		reader.close();
		//返回content
		return content;
	}
	
	/**
	 * 判断是否数字
	 * @param httpUrl
	 * @return
	 * @throws IOException
	 */
	public static boolean isNumeric(String str){ 
		Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");
	    Pattern pattern2 = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches()||pattern2.matcher(str).matches();    
	 }
	
	
	
	
}
