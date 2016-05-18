package io.github.liuzm.crawler.extractor.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.action.ListSelectorAction;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;


/**
 * @author chenxinwen
 * @param <T>
 * @date 2014年8月3日
 * @desc 将以List<?>的方式返回提取的信息内容
 */
public class ListElementCssSelector extends AbstractElementCssSelector<List<String>> {
	
	private List<String> contenList;
	
	private List<SelectorAction> actions = Lists.newArrayList();
	
	public ListElementCssSelector(){}
	
	public ListElementCssSelector(String name, String value, String attr,
			boolean isRequired,int index,String regex) {
		super(name, value, attr, isRequired, index, regex);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getContent() throws ExtractException{
		try {
			if(null!=contenList && !newDoc){
				return contenList;
			}
			contenList = Lists.newArrayList();
			if(document!=null){
				Elements elements = super.document.select(value);
				if(elements.isEmpty())
					return null;
				switch ($Attr) {
				case text:
					for (Element e : elements) {
						contenList.add(e.text());
					}
					break;
				default:
					for (Element e : elements) {
						contenList.add(e.attr(attr));
					}
					break;
				}
				
				newDoc = false;
				return contenList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtractException("信息提取错误:"+e.getMessage());
		}
		return null;
	}

	@Override
	public Map<String, List<String>> getContentMap() throws ExtractException{
		if(newDoc)
			getContent();
		if(null==contenList || contenList.size()==0)
			return null;
		Map<String, List<String>> map = new HashMap<>(1);
		map.put(name, this.contenList);
		return map;
	}

	@Override
	public void addAction(SelectorAction action) {
			this.actions.add((ListSelectorAction) action);
		}
		
	}
