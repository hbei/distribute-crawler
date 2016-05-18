package io.github.liuzm.crawler.jobconf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.liuzm.crawler.exception.ConfigurationException;
import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.AbstractElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.IFConditions;
import io.github.liuzm.crawler.extractor.selector.factory.ElementCssSelectorFactory;

public class ExtractConfig extends Configuration {
	/**
	 * 默认使用个线程提取信息
	 */
	private int threadNum = 10;
	/**
	 * 抽取信息的模板列表
	 */
	private final List<ExtractTemplate> templates = Lists.newArrayList();
	/**
	 * 获取所有模板提取的信息
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getContentAll(Document document) throws ExtractException{
		Map<String, Object> content = Maps.newHashMap(); 
		for(ExtractTemplate template:templates){
			Map<String, Object> m = template.getContentResultMap(document);
			if(m!=null && m.size()>0)
				content.putAll(m);
		}
		return content;
	}

	/**
	 * 获取所所有模板提取的信息，分开方式。<br>
	 * 可以通过get(模板名称的方式获取到某个独立的模板提取的信息)
	 * @param document
	 * @param url
	 * @return
	 * @throws ExtractException
	 */
	public HashMap<Object, Object> getContentSeprator(Document document,String url) throws ExtractException{
		HashMap<Object, Object> content = Maps.newHashMap();
		for(ExtractTemplate template : templates){
			if(template.urlFilter(url)){
				Map<String, Object> m = template.getContentResultMap(document);
				if(m!=null && m.size()>0){
					content.put(super.getIndexName(), m);
					return content;
				}
			}
		}
		return content;
	}
	/**
	 * 从配置文件中加载抽取配置信息
	 * @param doc
	 * @return
	 * @throws ConfigurationException
	 */
	public ExtractConfig loadConfig(Document doc) {
		Elements extractElement = doc.select("extract");
		super.setJobName(doc.select("job").attr("name"));
		super.setIndexName(doc.select("job").attr("indexName"));
		String temp = extractElement.select("threadNum").text();
		if(StringUtils.isNotBlank(temp)){
			this.threadNum = Integer.parseInt(temp);
		}
		
		Elements templateElement = extractElement.select("extract").select("template");
		Iterator<Element> it = templateElement.iterator();
		while(it.hasNext()){
			Element template = it.next();
			ExtractTemplate extractTemplate = new ExtractTemplate();
			// 模板对应的Url规则，满足其一就使用该模板进行提取
			Elements urlPatternElement = template.select("url");
			List<Pattern> patterns = Lists.newArrayList();
			for(Element urlElement :urlPatternElement){
				patterns.add(Pattern.compile(urlElement.text()));
			}
			extractTemplate.setUrlPattern(patterns);
			extractTemplate.setName(template.attr("name"));
			// 提取元素
			Elements selectElement = template.select("elements").first().children();
			for(Element element:selectElement){
				if("element".equals(element.tagName())){
					AbstractElementCssSelector<?> selector = ElementCssSelectorFactory.create(element);
					extractTemplate.addCssSelector(selector);
				}else if ("if".equals(element.tagName())) {
					IFConditions ifConditions = IFConditions.create(element);
					extractTemplate.addConditions(ifConditions);
				}
			}
			super.setExtractConfig(this);
			this.templates.add(extractTemplate);
		}
		//super.setExtractConfig(this);
		return this;
	}
	
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public List<ExtractTemplate> getTemplates() {
		return templates;
	}
	
	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("ExtractConfig [threadNum=")
				.append(threadNum)
				.append(", templates=")
				.append(templates != null ? templates.subList(0,
						Math.min(templates.size(), maxLen)) : null)
				.append(", jobName=").append(getJobName()).append("]");
		return builder.toString();
	}
	
}

