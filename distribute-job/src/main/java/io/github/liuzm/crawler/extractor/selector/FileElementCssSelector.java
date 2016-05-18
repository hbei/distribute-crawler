package io.github.liuzm.crawler.extractor.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.action.FileSelectAction;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;


/**
 * @author 
 * @date 
 * @desc 该选择元素需要配置相应的Action完成文件下载功能。该选择元素本身仅返回网页中相应的文本信息。
 * </br>该文本信息应该是一个Url格式的字符串，指向某个网络文件。
 */
public class FileElementCssSelector extends AbstractElementCssSelector<String> {
	
	private String content;
	
	private List<SelectorAction> actions = Lists.newArrayList();
	
	public FileElementCssSelector(String name, String value, String attr,
			boolean isRequired, int index,String regex) {
		super(name, value, attr, isRequired, index, regex);
	}
	
	private Map<String, Object> result = null;
	
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
				switch ($Attr) {
				case text:
					this.content = getExtractText(elements);
					break;
				default:
					this.content = getExtractAttr(elements, attr);
					break;
				}
				newDoc = false;
				
				return this.content;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtractException(StringElementCssSelector.class.getSimpleName()+"信息提取错误:"+e.getMessage());
		}
		return "";
	}

	@Override
	public Map<String, String> getContentMap() throws ExtractException{
		if(newDoc){
			getContent();
		}
		if(StringUtils.isBlank(content))
			return null;
		Map<String, String> m = new HashMap<String, String>(1);
		m.put(name, this.content);
		return m;
	}

	

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void addAction(SelectorAction action) {
		this.actions.add((FileSelectAction) action);
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public FileElementCssSelector setResult(Map<String, Object> result) {
		this.result = result;
		return this;
	}
	
}
