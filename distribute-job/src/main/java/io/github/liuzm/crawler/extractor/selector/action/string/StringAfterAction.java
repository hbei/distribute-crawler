package io.github.liuzm.crawler.extractor.selector.action.string;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;



/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 截取给定字符串中某个字符串之后的部分
 */
public class StringAfterAction extends StringSelectorAction {
	
	/**
	 * 分隔符
	 */
	private String separator;
	/**
	 * 构造器
	 * @param separator
	 */
	public StringAfterAction(String separator){
		this.separator = separator;
	}
	
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			return StringUtils.substringAfter(content, separator);
		}
		return "";
	}
	
	public static void main(String[] args) {
		String s = "asdfsfh354^$#^WEEAf ";
		StringAfterAction action = new StringAfterAction("^$");
		System.out.println(action.doAction(s));
	}
}
