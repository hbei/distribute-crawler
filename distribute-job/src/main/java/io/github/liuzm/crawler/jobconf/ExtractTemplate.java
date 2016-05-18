package io.github.liuzm.crawler.jobconf;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.AbstractElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.FileElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.GCElement;
import io.github.liuzm.crawler.extractor.selector.IFConditions;

public class ExtractTemplate{
	/**
	 * 模板名称
	 */
	private String name;
	/**
	 * 该模板对应的模板模式，如果没有设置则，对所有页面以次模板提取信息
	 */
	private List<Pattern> urlPattern = Lists.newArrayList();
	/**
	 * 该模板对应的css选择器，使用jsoup进行提取。
	 */
	private List<AbstractElementCssSelector> cssSelectors = Lists.newArrayList();
	/**
	 * 该模板对应的css选择器，使用jsoup进行提取。
	 */
	private List<GCElement> gcSelectors = Lists.newArrayList();
	/**
	 * 条件分支
	 */
	private List<IFConditions> conditions = Lists.newArrayList();
	/**
	 * 以该模板的配置提取document的信息
	 * @param document
	 * @return
	 * @throws ExtractException 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getContentResultMap(Document document) throws ExtractException{
		
			Map<String, Object> content = Maps.newHashMap();
			for(AbstractElementCssSelector selector:cssSelectors){
				
				if(selector instanceof FileElementCssSelector){
					FileElementCssSelector s = (FileElementCssSelector)selector;
					Map<String, Object> m = s.setResult(content).setDocument(document).getContentMap();
					if((null==m || m.size()==0) && s.isRequired()){
						return null;
					}else {
						if(null!=m && m.size()>0)
							content.putAll(m);
					}
				}else if(selector instanceof AbstractElementCssSelector){
					Map<String, Object> m = ((AbstractElementCssSelector)selector).setDocument(document).getContentMap();
					
					if((null==m || m.size()==0) && ((AbstractElementCssSelector)selector).isRequired()){
						return null;
						//continue;
					}else {
						if(null!=m && m.size()>0)
							content.putAll(m);
					}
				}
			}
			
			for(IFConditions con:conditions){
				if(con.test(content)){
					for(AbstractElementCssSelector<?> selector:con.getSelectors()){
						if(selector instanceof FileElementCssSelector){
							Map<String, Object> m = ((FileElementCssSelector)selector).setResult(content)
									.setDocument(document)
									.getContentMap();
							
							if((null==m || m.size()==0) && selector.isRequired()){
								return null;
							}else {
								if(null!=m && m.size()>0)
									content.putAll(m);
							}
						}else{
							Map<String, Object> m = selector.setDocument(document).getContentMap();
							
							if((null==m || m.size()==0) && selector.isRequired()){
								return null;
							}else {
								if(null!=m && m.size()>0)
									content.putAll(m);
							}
						}
					}
				}
			}
			
			return content;
	}
	
	
	
	
	/*public Map<String, Object> getContentResultMap(Document document){
		
		Map<String, Object> content = Maps.newHashMap();
		for(AbstractElementCssSelector selector:cssSelectors){
			
			try {
				if(selector instanceof FileElementCssSelector){
					FileElementCssSelector s = (FileElementCssSelector)selector;
					Map<String, Object> m = s.setResult(content)
							.setDocument(document)
							.getContentMap();
					if((null==m || m.size()==0) && s.isRequired()){
						return null;
					}else {
						if(null!=m && m.size()>0)
							content.putAll(m);
					}
				}else if(selector instanceof AbstractElementCssSelector){
					Map<String, Object> m = ((AbstractElementCssSelector)selector).setDocument(document).getContentMap();
					
					if((null==m || m.size()==0) && ((AbstractElementCssSelector)selector).isRequired()){
						//return null;
						continue;
					}else {
						if(null!=m && m.size()>0)
							content.putAll(m);
					}
				}
			} catch (Exception e) {
				System.out.println("提取出错\tAbstractElementCssSelector:" + selector);
				e.printStackTrace();
			}
		}
		
		return content;
}
	*/
	
	
	
	/**
	 * 在提取信息之前过滤Url
	 * @param url
	 * @return
	 */
	public boolean urlFilter(String url){
		for(Pattern pattern :urlPattern){
			if(pattern.matcher(url).matches()){
				return true;
			}
		}
		return false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Pattern> getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(List<Pattern> urlPattern) {
		this.urlPattern = urlPattern;
	}
	
	public void addUrlPattern(Pattern urlPattern){
		this.urlPattern.add(urlPattern);
	}

	public List<AbstractElementCssSelector> getCssSelectors() {
		return cssSelectors;
	}
	
	public void setCssSelectors(List<AbstractElementCssSelector> cssSelectors) {
		this.cssSelectors = cssSelectors;
	}
	
	public void addCssSelector(AbstractElementCssSelector<?> selector){
		this.cssSelectors.add(selector);
	}
	
	/**
	 * @return the gcSelectors
	 */
	public List<GCElement> getGcSelectors() {
		return gcSelectors;
	}

	/**
	 * @param gcSelectors the gcSelectors to set
	 */
	public void setGcSelectors(List<GCElement> gcSelectors) {
		this.gcSelectors = gcSelectors;
	}
	/**
	 * @return the conditions
	 */
	public List<IFConditions> getConditions() {
		return conditions;
	}
	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(List<IFConditions> conditions) {
		this.conditions = conditions;
	}
	
	public void addConditions(IFConditions condition){
		this.conditions.add(condition);
	}



	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	@Override
	public String toString() {
		return "ExtractTemplate [name=" + name + ", urlPattern=" + urlPattern
				+ ", cssSelectors=" + cssSelectors + ", conditions="
				+ "]";
	}
	
	
}
