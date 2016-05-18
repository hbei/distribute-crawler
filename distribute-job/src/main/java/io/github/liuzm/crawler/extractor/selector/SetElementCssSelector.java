package io.github.liuzm.crawler.extractor.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;
import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;


public class SetElementCssSelector extends AbstractElementCssSelector<Set<String>> {
	
	private Set<String> content;
	private List<StringSelectorAction> actions = Lists.newArrayList();
	public SetElementCssSelector() {
		super();
	}

	public SetElementCssSelector(String name, String value, String attr,
			boolean isRequired,int index,String regex) {
		super(name, value, attr, isRequired, index,regex);
	}

	@Override
	public Set<String> getContent() throws ExtractException{
		try {
			if(null!=content && !newDoc){
				return content;
			}
			content = Sets.newLinkedHashSet();
			if(document!=null){
				Elements elements = super.document.select(value);
				if(elements.isEmpty())
					return null;
				switch ($Attr) {
				case text:
					for (Element e : elements) {
						content.add(e.text());
					}
					break;
				default:
					for (Element e : elements) {
						content.add(e.attr(attr));
					}
					break;
				}
				if(null!=actions && actions.size()>0){
					Set<String> newSet = Sets.newLinkedHashSet();
					for(String string : content){
						String temp = string;
						for(StringSelectorAction action:actions){
							temp = action.doAction(temp);
						}
						newSet.add(temp);
					}
					this.content = newSet;
				}
				newDoc = false;
				return content;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtractException(SetElementCssSelector.class.getSimpleName()+"信息提取错误:"+e.getMessage());
		}
		return null;
	}

	@Override
	public Map<String, Set<String>> getContentMap() throws ExtractException{
		if(newDoc)
			getContent();
		if(content == null || content.size()==0)
			return null;
		Map<String, Set<String>> map = new HashMap<>(1);
		map.put(name, this.content);
		return map;
	}

	@Override
	public void addAction(SelectorAction action) {
		this.actions.add((StringSelectorAction) action);
	}
	
	public List<StringSelectorAction> getActions() {
		return actions;
	}

	public void setActions(List<StringSelectorAction> actions) {
		this.actions = actions;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SetElementCssSelector [content=");
		builder.append(content);
		builder.append(", actions=");
		builder.append(actions);
		builder.append(", name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append(", attr=");
		builder.append(attr);
		builder.append(", $Attr=");
		builder.append($Attr);
		builder.append(", isRequired=");
		builder.append(isRequired);
		builder.append(", document=");
		builder.append(document);
		builder.append(", newDoc=");
		builder.append(newDoc);
		builder.append("]");
		return builder.toString();
	}
	
	
}
