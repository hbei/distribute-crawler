package io.github.liuzm.crawler.extractor.selector;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;



public abstract class AbstractElementCssSelector<T>{
	
	/**
	 * 选择器名称
	 */
	protected String name;
	/**
	 * css selector
	 */
	protected String value;
	/**
	 * 选择器的属性，img、src、text等
	 */
	protected String attr;
	/**
	 * attr对应的SelectorAttr枚举
	 */
	protected SelectorAttr $Attr;
	/**
	 * 是否required
	 */
	protected boolean isRequired = false;
	
	protected String regex;
	
	//protected Pattern pattern = null;
	/**
	 * 选择器要处理的文档
	 */
	protected Document document;
	/**
	 * 为true表示需要处理新的document<br>
	 * false表示该document已经处理过
	 */
	protected boolean newDoc = true;
	
	/**
	 * 选择器提取出多个结果时选择哪个.默认第一个
	 */
	protected int index = -1;
	/**
	 * 该element之上的action
	 */
	protected List<SelectorAction<T>> actions;
	
	/**
	 * 构造器
	 */
	public AbstractElementCssSelector(){};
	
	/**
	 * 构造器
	 * @param name
	 * @param value
	 * @param atrr
	 * @param isRequired
	 * @param document
	 */
	public AbstractElementCssSelector(String name, String value, String attr,
			boolean isRequired,int index,String regex) {
		super();
		this.name = name;
		this.value = value;
		this.attr = attr;
		this.$Attr = org.apache.commons.lang3.EnumUtils.getEnum(SelectorAttr.class, this.attr);
		if(this.$Attr==null){
			this.$Attr = SelectorAttr.other;
		}
		this.isRequired = isRequired;
		this.index = index;
		this.regex = regex;
		/*if(StringUtils.isNotBlank(regex))
			this.pattern = Pattern.compile(regex);*/
	}
	/**
	 * @param name
	 * @param value
	 * @param attr
	 * @param isRequired
	 * @param index
	 */
	public AbstractElementCssSelector(String name, String value, String attr,boolean isRequired,int index) {
		super();
		this.name = name;
		this.value = value;
		this.attr = attr;
		this.$Attr = org.apache.commons.lang3.EnumUtils.getEnum(SelectorAttr.class, this.attr);
		this.isRequired = isRequired;
		this.index = index;
	}
	/**
	 * 构造器
	 * @param name
	 * @param value
	 * @param attr
	 * @param isRequired
	 */
	public AbstractElementCssSelector(String name, String value, String attr,boolean isRequired) {
		super();
		this.name = name;
		this.value = value;
		this.attr = attr;
		this.$Attr = org.apache.commons.lang3.EnumUtils.getEnum(SelectorAttr.class, this.attr);
		this.isRequired = isRequired;
	}
	
	/**
	 * 获取该选择器从文档中抽取的内容
	 * @return
	 */
	public abstract T getContent() throws ExtractException;
	/**
	 * 获取该选择器从文档中抽取的内容，其中k为选择器名称，即name属性。v是选择器抽取到的内容。
	 * @return
	 */
	public abstract Map<String, T> getContentMap() throws ExtractException;
	
	public abstract void addAction(SelectorAction<T> action);
	
	public String getName() {
		return name;
	}

	public AbstractElementCssSelector setName(String name) {
		this.name = name;
		return this;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public AbstractElementCssSelector setValue(String value) {
		this.value = value;
		return this;
	}

	public String getAttr() {
		return attr;
	}

	public AbstractElementCssSelector setAttr(String attr) {
		this.attr = attr;
		return this;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public AbstractElementCssSelector setRequired(boolean isRequired) {
		this.isRequired = isRequired;
		return this;
	}

	public Document getDocument() {
		return document;
	}

	public AbstractElementCssSelector setDocument(Document document) {
		this.document = document;
		this.newDoc = true;
		return this;
	}


	public SelectorAttr get$Attr() {
		return $Attr;
	}

	public AbstractElementCssSelector set$Attr(SelectorAttr $Attr) {
		this.$Attr = $Attr;
		return this;
	}

	public AbstractElementCssSelector setNewDoc(boolean newDoc) {
		this.newDoc = newDoc;
		return this;
	}
	
	/**
	 * 重置标记，对内容需要重新处理，或者document已经更新
	 */
	protected void isNewDoc(){
		this.newDoc = true;
	}
	/**
	 * 返回提取结果中多个值中指定位置的值
	 * @param elements
	 * @return
	 */
	protected String getExtractText(Elements elements){
		if(elements.size()==0)
			return null;
		String temp = "";
		
		if(attr.equalsIgnoreCase("tostring")){
			return temp = elements.toString();
		}else{
			if(index==-1&&StringUtils.isNotBlank(this.regex)){
				for (Element e : elements) {
					Element element = e;
					if(element.select(this.regex).size()>0){
						return temp = e.text();
					}
				}
				return temp;
			}else{
				if(index>-1&&index<elements.size()){
					return elements.get(index).text(); 
				}
			}
			return elements.first().text(); 
		}
			
		
		/*if(attr.equals("tostring")){
			if(index==0 || index>elements.size())
				temp = elements.first().toString();
			else
				temp = elements.get(index).toString();
		}else{
			if(index==0 || index>elements.size())
				temp = elements.first().text();
			else
				temp = elements.get(index).text();
		}
		
		if(null!=pattern){
			Matcher m = pattern.matcher(temp);
			if(m.find()){
				temp = m.group(1);
			}
		}*/
		//return temp;
	}
	/**
	 * 返回某个提取位置多值中指定位置的属性的值
	 * @param elements
	 * @param attr
	 * @return
	 */
	protected String getExtractAttr(Elements elements,String attr){
		String temp = "";
		if(attr.equalsIgnoreCase("tostring")){
			return temp = elements.attr(attr).toString();
		}else{
			if(index==-1&&StringUtils.isNotBlank(this.regex)){
				for (Element e : elements) {
					Element element = e;
					if(element.select(this.regex).size()>0){
						return temp = e.attr(attr);
					}
				}
				return temp;
			}else{
				if(index>-1&&index<elements.size()){
					return elements.get(index).attr(attr); 
				}
			}
			return elements.first().attr(attr); 
		}
		/*if(null!=pattern){
			Matcher m = pattern.matcher(temp);
			if(m.find()){
				temp = m.group(1);
			}
		}*/
		//return temp;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public String toString() {
		return "AbstractElementCssSelector [name=" + name + ", value=" + value
				+ ", attr=" + attr + ", $Attr=" + $Attr + ", isRequired="
				+ isRequired + ", regex=" + regex + ", newDoc=" + newDoc + ", index=" + index + "]";
	}

}
