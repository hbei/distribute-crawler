package io.github.liuzm.crawler.extractor.selector.action;
/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 整型选择器的处理接口
 */
public abstract class IntegerSelectorAction implements SelectorAction {
	public abstract int doAction(Integer i);
}
