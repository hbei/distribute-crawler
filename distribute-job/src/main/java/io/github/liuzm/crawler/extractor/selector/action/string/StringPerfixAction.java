package io.github.liuzm.crawler.extractor.selector.action.string;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;



/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 前加字符串
 */
public class StringPerfixAction extends StringSelectorAction {
	/**
	 * 字符串
	 */
	String perfix = "";
	
	public StringPerfixAction(String perfix){
		this.perfix = perfix;
	}
	
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			return this.perfix + content;
		}
		return "";
	}
}
