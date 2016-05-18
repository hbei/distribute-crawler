package io.github.liuzm.crawler.extractor.selector.action.string;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;



/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 截取在某个字符（串）之前的部分的action
 */
public class StringBeforeAction extends StringSelectorAction {
	/**
	 * 定位的String
	 */
	private String separator ;
	/**
	 * 构造器
	 * @param befString
	 */
	public StringBeforeAction(String separator){
		this.separator = separator;
	}
	/**
	 * 截取在beforeString之前的部分
	 */
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			String[] ss = content.split(this.separator);
			return ss.length>0?ss[0]:content;
		}
		return content;
	}
	
	public static void main(String[] args) {
		String s = "asdfsfh354^$#^WEEAf ";
		StringBeforeAction action = new StringBeforeAction("^$");
		System.out.println(action.doAction(s));
		
		String s2 = "中原地产 大梅沙分行";
		StringBeforeAction action2 = new StringBeforeAction("\\s+");
		System.out.println(action2.doAction(s2));
		
	}
}
