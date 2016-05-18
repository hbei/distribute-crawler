package io.github.liuzm.crawler.extractor.selector.action.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;

/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 正则表达式的Action<br>
 * 
 */
public class StringRegexAction extends StringSelectorAction {
	/**
	 * 正则的String
	 */
	private String regex ;
	/**
	 * 构造器
	 * @param 
	 */
	public StringRegexAction(String exp){
		this.regex = exp;
	}
	/**
	 * 截取在beforeString之前的部分
	 */
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			Pattern p = Pattern.compile(this.regex);
			Matcher m = p.matcher(content);
			if(m.find()){
				return m.group(1);
			}
		}
		return null;
	}
	public static void main(String[] args) {
		/*String regex = "([0-9]+).htm";
		String content = "http://guanyuangongyu022.fang.com/photo/1110245827.htm";
		System.out.println(new StringRegexAction(regex).doAction(content));*/
		
		String regex2 = "\\s+";
		String content2 = "中原地产 大梅沙分行";
		//System.out.println(new StringRegexAction(regex2).doAction(content2));
		System.out.println(content2.split("\\s{1,}")[0]);
		
		String s = "GET             /index.html HTTP/1.1";//字符串s由“GET”、“/index.html”和“HTTP/1.1”组成，中间有一个或多个空格
		String tt[] = s.split("\\s{1,}");//按照空格分割字符串，多个空格作为一个空格对字符串进行分割
		for(String str: tt)//增强的for循环
		System.out.println(str);
	}
}
