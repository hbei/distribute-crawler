package io.github.liuzm.crawler.extractor.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.action.IntegerSelectorAction;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;


/**
 * @author chenxinwen
 * @date 2014年8月13日
 * @desc 整型抽取器，如果抽取内容不正确则返回null
 */
public class IntegerElementCssSelector extends AbstractElementCssSelector<Integer> {
	private Logger log = LoggerFactory.getLogger(IntegerElementCssSelector.class);
	private Integer content;
	private List<SelectorAction> actions = Lists.newArrayList();
	
	public IntegerElementCssSelector() {
		super();
	}

	public IntegerElementCssSelector(String name, String value, String attr,
			boolean isRequired, int index,String regex) {
		super(name, value, attr, isRequired, index, regex);
	}

	@Override
	public Integer getContent() throws ExtractException{
		Elements elements = null;
		try {
			// 如果content不为空且不是新文档，则表示是同一个document的2+次调用，不用重新计算
			if(null!=content && !newDoc){
				return content;
			}
			if(null!=document){
				elements = super.document.select(value);
				if(elements.isEmpty())
					return null;
				String temp;
				switch ($Attr) {
				case text:
					temp = CharMatcher.DIGIT.retainFrom(getExtractText(elements));
					break;
				default:
					temp = CharMatcher.DIGIT.retainFrom(getExtractAttr(elements, attr));
					break;
				}
				
				if(StringUtils.isNotBlank(temp)){
					Integer integer = Integer.parseInt(temp);
						this.content = integer;
					newDoc = false;
					return content;
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(elements.toString());
			throw new ExtractException("信息提取错误:"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 如果content为空，且是新文档，则重新计算。
	 */
	@Override
	public Map<String, Integer> getContentMap() throws ExtractException{
		if(newDoc){
			this.content = getContent();
		}
		if(null==this.content)
			return null;
		Map<String, Integer> m = new HashMap<String, Integer>(1);
		m.put(name, this.content);
		return m;
	}

	
	public void setContent(Integer content) {
		this.content = content;
	}

	@Override
	public void addAction(SelectorAction<Integer> action) {
		this.actions.add((IntegerSelectorAction) action);
	}

	
	
}
