package io.github.liuzm.crawler.extractor.selector.action;


/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 什么也不做
 */
public class EmptyAction implements SelectorAction{
	public Object doAction(Object content) {
		return content;
	}
}
