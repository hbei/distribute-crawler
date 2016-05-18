package io.github.liuzm.crawler.extractor.selector.action.string;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;




/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 截取字符串中最后一个分隔符之后的部分
 */
public class StringAfterLastAction extends StringSelectorAction{
	
	private String separator;
	
	public StringAfterLastAction(String separator){
		this.separator = separator;
	}
	/**
	 * 截取content中最后一个separator之后的部分
	 */
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			return StringUtils.substringAfterLast(content, separator);
		}
		return "";
	}

	public static void main(String[] args) {
		String s = "asd^$fsfh354^$#^WEEAf ";
		StringAfterLastAction action = new StringAfterLastAction("^$");
		System.out.println(action.doAction(s));
	}
}
