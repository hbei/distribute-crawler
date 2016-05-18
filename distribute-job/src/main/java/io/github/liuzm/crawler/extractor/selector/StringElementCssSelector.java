package io.github.liuzm.crawler.extractor.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;
import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;



public class StringElementCssSelector extends AbstractElementCssSelector<String> {
	private String content;
	private List<StringSelectorAction> actions = Lists.newArrayList();
	
	public StringElementCssSelector() {
		super();
	}

	public StringElementCssSelector(String name, String value, String attr,
			boolean isRequired, int index,String regex) {
		super(name, value, attr, isRequired, index,regex);
	}

	/**
	 * 提取内容，并根据Action对内容做加工
	 */
	@Override
	public String getContent() throws ExtractException{
		try {
			// 同一个文档的2+次调用不用重新计算。
			if(StringUtils.isNotBlank(this.content) && !newDoc){
				return content;
			}
			// 抽取document中对应的Selector
			if (super.document != null) {
				Elements elements = super.document.select(value);
				if(elements.isEmpty())
					return null;
				String value =null;
				switch ($Attr) {
				case text:
					value = trimInvisibleChar(getExtractText(elements));
					break;
				default:
					value = trimInvisibleChar(getExtractAttr(elements, attr));
					break;
				}
				if(StringUtils.isNotBlank(value)){
					if(null!=actions && actions.size()>0){
						String temp = value.substring(0,value.length());
						for(StringSelectorAction action:actions){
							temp = action.doAction(temp);
						}
						this.content = temp;
					}else {
						this.content = value.toString();
					}
					newDoc = false;
					return this.content ;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtractException(StringElementCssSelector.class.getSimpleName()+"信息提取错误:"+e.getMessage());
		}
		return "";
	}

	@Override
	public Map<String, String> getContentMap(){
		if(newDoc){
			try {
				this.content = getContent();
			} catch (ExtractException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(StringUtils.isBlank(this.content))
			return null;
		Map<String, String> m = new HashMap<String, String>(1);
		m.put(name, this.content);
		return m;
	}

//	public List<SelectorAction> getActions() {
//		return actions;
//	}
//
//	public void setActions(List<SelectorAction> actions) {
//		this.actions = actions;
//	}

	public void setContent(String content) {
		this.content = content;
	}
	
	private String trimInvisibleChar(String text){
		if(StringUtils.isNotBlank(text))
			return CharMatcher.INVISIBLE.trimFrom(text);
		return text;
	}

	@Override
	public void addAction(SelectorAction<String> action) {
		this.actions.add((StringSelectorAction) action);
	}
	
}
