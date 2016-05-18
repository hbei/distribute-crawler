package io.github.liuzm.crawler.extractor.selector.action.integer;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.exception.IntegerBetweenExpressionException;
import io.github.liuzm.crawler.extractor.selector.action.IntegerSelectorAction;


/**
 * @author  chenxinwen
 * @date 2014年7月30日
 * @desc 检测数值是否在某个区间内。如果超过区间则有默认值代替
 */
public class IntegerBetweenAction extends IntegerSelectorAction {
	private int max;
	private int min;
	private int def;
	/**
	 * 构造器
	 * @param exp
	 * @param def
	 * @throws IntegerBetweenExpressionException
	 */
	public IntegerBetweenAction(String exp,String def) throws IntegerBetweenExpressionException{
		if(StringUtils.isNotBlank(exp)){
			String ss[] = exp.split(",");
			if(ss.length!=2){
				throw new IntegerBetweenExpressionException("数值区间表示错误");
			}else {
				max = Integer.parseInt(ss[1]);
				min = Integer.parseInt(ss[0]);
				if(max<min){
					int i = max;
					max = min;
					min = i;
				}
			}
		}
		if(StringUtils.isNotBlank(def)){
			this.def = Integer.parseInt(def);
		}
	}
	/**
	 * 检测数值是否在某个区间内。如果超过区间则有默认值代替
	 */
	@Override
	public int doAction(Integer i) {
		if(i>max || i<min){
			i = def;
		}
		return i;
	}

}
