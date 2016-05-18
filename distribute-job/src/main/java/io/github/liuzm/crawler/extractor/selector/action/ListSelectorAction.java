
package io.github.liuzm.crawler.extractor.selector.action;

import java.util.List;

/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 处理list的Action
 */
public abstract class ListSelectorAction implements SelectorAction {
	public abstract List<?> doAction(List<?> list);
}
