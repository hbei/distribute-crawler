package io.github.liuzm.crawler.extractor.selector.htmlelment;

import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.github.liuzm.crawler.extractor.selector.GCElement;


public abstract class AbstractHtmlElement<E> implements GCElement {
	/**
	 * webClient
	 */
	protected WebClient webClient;
	/**
	 * 页面
	 */
	protected HtmlPage page;
	/**
	 * 选择器名称
	 */
	protected String name;
	/**
	 * 选择器值，对应xpath.
	 */
	protected String value;
	/**
	 * 类型<i>anchor,button,embed,form,image,input....</i>
	 */
	protected String type;
	/**
	 * 动作<i>click,submit,dbclick...</i>
	 */
	protected String action;
	/**
	 * new page
	 */
	protected boolean newPage = true;
	/**
	 * is required
	 */
	protected boolean isRequired;

	/**
	 * 构造器
	 */
	public AbstractHtmlElement() {
	}

	/**
	 * 构造器
	 * 
	 * @param webClient
	 * @param page
	 * @param name
	 * @param value
	 * @param type
	 * @param isRequired
	 */
	public AbstractHtmlElement(WebClient webClient, HtmlPage page, String name,
			String value, String type, boolean isRequired) {
		super();
		this.webClient = webClient;
		this.page = page;
		this.name = name;
		this.value = value;
		this.type = type;
		this.isRequired = isRequired;
	}

	/**
	 * 返回内容
	 * 
	 * @return
	 */
	public abstract E getContent();

	public abstract Map<String, E> getContentMap();

	public HtmlPage getPage() {
		return page;
	}

	public void setPage(HtmlPage page) {
		this.page = page;
		this.newPage = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNewPage() {
		return newPage;
	}

	public void setNewPage(boolean newPage) {
		this.newPage = newPage;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public void setNewPage() {
		this.newPage = true;
	}
}
