package io.github.liuzm.crawler.extractor.selector.action.string;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;

/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 追加字符串
 */
public class StringSuffixAction extends StringSelectorAction {
	
	/**
	 * 追加字符
	 */
	String suffix = "";
	
	
	public StringSuffixAction(String suffix){
		this.suffix = suffix;
	}
	
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			return content + this.suffix;
		}
		return "";
	}
}
