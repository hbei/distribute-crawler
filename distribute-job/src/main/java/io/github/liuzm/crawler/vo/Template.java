package io.github.liuzm.crawler.vo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Comment;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.google.common.collect.Lists;

public class Template {
	
	private String name = "";
	
	private List<ItemElement> elements = Lists.newArrayList();
	
	private String note = "抓取的模板";

	public Template init(Element temp) throws Exception {
		if(!"template".equalsIgnoreCase(temp.getName()))
			throw new Exception("初始化JobTemplate下的ExtrateTemplate的Template错误!!!传入的Element不是“template”的Element");
		this.name = temp.attributeValue("name");
		Element es =  temp.element("elements");
		int n = es.nodeCount();
		String comment = "";
		for (int i = 0; i < n; i++) {
			/*
			 * 顺序读取 <elements>下的node。
			 * 如果是注释（Comment类型）则把它存在变量里。可以多个
			 */	
			Node node = es.node(i);
			if(node instanceof Comment){
				comment += node.getText()+"\n";
			}
			/*
			 * 如果是<Element>节点则创建一个ItemElement对象保存List<ItemElement> elements里
			 * 最后清空注释的变量。
			 */
			if(node instanceof Element){
				ItemElement item = new ItemElement(node,comment.trim());	
				elements.add(item);
				comment = "";
				item=null;
			}
			
		}
		//this.elements = Element
		return this;
	}
	
	
	public Element createTemplate(Template template) {
		Element template_ = DocumentHelper.createElement("template");
		template_.addAttribute("name", template.getName());
		Element elements = template_.addElement("elements");
		for (ItemElement item : template.getElements()) {
			//由于页面editor.jsp里面通过删除某些element编辑的条目后会导致元素的序号空缺。from表单提交请求，JobTemplate的ExtractTemplate还是会注入空记录
			//通过判断ItemElement的name和value字段来排除不该有的记录
			if(StringUtils.isBlank(item.getName())||StringUtils.isBlank(item.getValue()))
					continue;
			if(StringUtils.isNotBlank(item.getNote()))
				elements.addComment(item.getNote());
			
			elements.add(new ItemElement().createItemElement(item));
		}
		return template_;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ItemElement> getElements() {
		return elements;
	}

	public void setElements(List<ItemElement> elements) {
		this.elements = elements;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
	
}
