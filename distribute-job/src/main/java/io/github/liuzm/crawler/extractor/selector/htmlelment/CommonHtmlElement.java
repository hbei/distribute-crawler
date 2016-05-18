package io.github.liuzm.crawler.extractor.selector.htmlelment;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.github.liuzm.crawler.fetcher.AjaxCallFetcher;

public class CommonHtmlElement extends AbstractHtmlElement<Object> {
	
	private AjaxCallFetcher fetch = new AjaxCallFetcher();
	
	private Object content;
	
	@Override
	public Object getContent() {
		if(page!=null){
			if (null != content && !newPage) {
				return content;
			}
			
			if (type.equals(HtmlElementExtractType.xpath)) {
				Object o = fetch.getElement(page, value);
				this.content = o;
				return this.content;
			}else {
				try {
					new Exception("需要使用xpath");
				} catch (Exception e) {
					throw e;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取异步调用的URL
	 * @return
	 */
	public URL getAjaxUrl(){
		if(null!=content && !newPage){
			this.content = fetch.getAjaxCallUrl(page, value, type, action);
			return (URL) this.content;
		}
		return null;
	}
	
	@Override
	public Map<String, Object> getContentMap() {
		if(newPage){
			getContent();
		}
		if(null==content)
			return null;
		Map<String, Object> m = new HashMap<String, Object>(1);
		m.put(name, this.content);
		return m;
	}
}
