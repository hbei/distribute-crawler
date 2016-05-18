package io.github.liuzm.crawler.extractor.selector.action.integer;

import io.github.liuzm.crawler.extractor.selector.action.IntegerSelectorAction;


/**
 * @author  chenxinwen
 * @date 2014年7月30日
 * @desc 求绝对值
 */
public class IntegerAbsAction extends IntegerSelectorAction {

	/**
	 * 求绝对值
	 */
	@Override
	public int doAction(Integer i) {
		return Math.abs(i);
	}

}
