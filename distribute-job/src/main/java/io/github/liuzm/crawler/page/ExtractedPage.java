package io.github.liuzm.crawler.page;

import java.io.Serializable;
import java.util.HashMap;

import io.github.liuzm.crawler.url.WebURL;

public class ExtractedPage<V, T> implements Serializable {
	
	private static final long serialVersionUID = 3965488944964575676L;
	/**
	 * 保留字段
	 */
	String reserve;
	/**
	 * 附加的WebURL信息
	 */
	WebURL url;
	/**
	 * 提取到信息，与job配置中的select对应，内容可以在扩展接口中重新修改和定义。
	 */
	HashMap<Object, Object> messages = new HashMap<>();

	ExtractResult result;

	public ExtractResult getResult() {
		return result;
	}

	public ExtractedPage<V, T> setResult(ExtractResult result) {
		this.result = result;
		return this;
	}

	public ExtractedPage() {
	}

	public ExtractedPage(WebURL url) {
		super();
		this.url = url;
	}

	public WebURL getUrl() {
		return url;
	}

	public ExtractedPage<V, T> setUrl(WebURL url) {
		this.url = url;
		return this;
	}

	public String getReserve() {
		return reserve;
	}

	public ExtractedPage<V, T> setReserve(String reserve) {
		this.reserve = reserve;
		return this;
	}

	public HashMap getMessages() {
		return messages;
	}

	@SuppressWarnings("unchecked")
	public ExtractedPage<V, T> setMessages(HashMap messages) {
		this.messages = messages;
		return this;
	}
	
	public void setMessage(String k,Object v){
		this.messages.put(k,v);
	}

	public ExtractedPage<V, T> addMessage(V key, T value) {
		messages.put(key, value);
		return this;
	}
}