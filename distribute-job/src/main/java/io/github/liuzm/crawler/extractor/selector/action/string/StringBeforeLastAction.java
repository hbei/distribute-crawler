package io.github.liuzm.crawler.extractor.selector.action.string;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;



/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 截取最后一个分隔符之前的字符串
 */
public class StringBeforeLastAction extends StringSelectorAction {
	/**
	 * 分割的字符串
	 */
	String separator;
	/**
	 * 构造器
	 * @param separator
	 */
	public StringBeforeLastAction(String separator){
		this.separator = separator;
	}
	/**
	 * 截取最后一个separator之前的字符串
	 */
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			return StringUtils.substringBeforeLast(content, separator);
		}
		return "";
	}

	public static void main(String[] args) {
		String s = "asdfsfh354^$#^WEEAf ";
		StringBeforeLastAction action = new StringBeforeLastAction("^");
		System.out.println(action.doAction(s));
	}
}
