package io.github.liuzm.crawler.extractor.selector.action;

/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 字符选择器的处理接口
 */
public abstract class StringSelectorAction implements SelectorAction<String> {
	public abstract String doAction(String content);
}
