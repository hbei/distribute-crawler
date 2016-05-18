package io.github.liuzm.crawler.extractor.selector;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.liuzm.crawler.bootsStrap.JobManager;
import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;
import io.github.liuzm.crawler.fetcher.Fetcher;
import io.github.liuzm.crawler.fetcher.FetcherMan;
import io.github.liuzm.crawler.jobconf.Configuration;
import io.github.liuzm.crawler.util.MapUtils;


/**
 * @author chenxinwen
 * @date 2014年9月15日
 * @desc Url类别的选择器。该类别选择器抽取到Url后会进一步抓取该Url内容并根据配置进行再一次抽取。
 */
public class PageElementSelector extends AbstractElementCssSelector<HashMap<String, Object>>  {
	/**
	 * 该Url选择器下的选择器
	 */
	List<AbstractElementCssSelector<?>> selectors = Lists.newArrayList();
	/**
	 * 该Url选择器下提取到的内容
	 */
	HashMap<String, Object> content;

	/**
	 * 返回该Url选择器下子选择器提取到的内容
	 */
	public PageElementSelector() {
		super();
	}

	public PageElementSelector(String name, String value, String attr,
			boolean isRequired,int index,String regex) {
		super(name, value, attr, isRequired, index, regex);
	}
	
	
	public List<AbstractElementCssSelector<?>> getSelectors() {
		return selectors;
	}

	public void setSelectors(List<AbstractElementCssSelector<?>> selectors) {
		this.selectors = selectors;
	}
	
	public void addSelector(AbstractElementCssSelector<?> selector){
		this.selectors.add(selector);
	}
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> getContent() throws ExtractException{
	
		// 抽取document中对应的Selector
		List<String> urls = Lists.newArrayList();
		if (super.document != null) {
			Elements elements = super.document.select(value);
			if(elements.isEmpty())
				return null;
			switch ($Attr) {
			case text:
				for(Element e:elements){
					urls.add(e.text());
				}
				break;
			default:
				for(Element e:elements){
					urls.add(e.attr(attr));
				}
				break;
			}
		}
		if(urls.size()>0){
			content = Maps.newHashMap();
			for(String url:urls){
				Document doc = Fetcher.goFetchPage(url, Fetcher.proxyIps);
				//Document doc = FetcherMan.fetcher.goFetchPage(url);
				if(selectors!=null&&doc!=null)
				for(AbstractElementCssSelector<?> selector :selectors){
					if(selector instanceof FileElementCssSelector){
						Map<String, Object> m = ((FileElementCssSelector)selector).setResult(content)
								.setDocument(doc)
								.getContentMap();
						if((null==m || m.size()==0) && selector.isRequired()){
							return null;
						}else {
							if(null!=m && m.size()>0)
								content = MapUtils.mager(content, (HashMap<String, Object>) m);
						}
					}else{
						Map<String, Object> m = selector.setDocument(doc).getContentMap();
						if((null==m || m.size()==0) && selector.isRequired()){
							return null;
						}else {
							if(null!=m && m.size()>0)
								content = MapUtils.mager(content, (HashMap<String, Object>) m);
						}
					}
				}
			}
			return content;
		}
		newDoc = false;
		return null;
	}
	
	@Override
	public Map<String, HashMap<String, Object>> getContentMap() throws ExtractException{
		if(newDoc){
			getContent();

		}
		if(content == null || content.size()==0)
			return null;
		HashMap<String, HashMap<String, Object>> map = new HashMap<>(1);
		map.put(name, content);
		return map;
	}

	@Override
	public void addAction(SelectorAction action) {
		
	}
}
