package io.github.liuzm.crawler.vo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.google.common.collect.Lists;

public class ExtratTemplate {
	
	String type = "default";
	String type_comment = "默认为Default，如果有其他实现，填写类的全路径";
	
	String threadNum = "10";
	String threadNum_comment = "解析线程数";
	
	List<Template>  templates = Lists.newArrayList();
	
	String note = "解析配置";

	public void init(Element e_job) {
		Element element = e_job.element("extract");
		this.type = element.elementText("type");
		this.threadNum = element.elementText("threadNum");
		
		List<Element> template_list = (List<Element>)element.elements("template");
		for (Element temp : template_list) {
			try {
				templates.add(new Template().init(temp));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public Element createTemplate(ExtratTemplate saveExtract) {
		ExtratTemplate extrat = saveExtract;
		Element element = DocumentHelper.createElement("extract");
		
		//type
		element.addComment(StringUtils.isNotBlank(extrat.getType_comment())?extrat.getType_comment():this.type_comment);
		element.addElement("type").addText(StringUtils.isNotBlank(extrat.getType())?extrat.getType():this.type);
		
		//threadNum
		element.addComment(StringUtils.isNotBlank(extrat.getThreadNum_comment())?extrat.getThreadNum_comment():this.threadNum_comment);
		element.addElement("threadNum").addText(StringUtils.isNotBlank(extrat.getThreadNum())?extrat.getThreadNum():this.threadNum);
		List<Template> templates = extrat.getTemplates();
		for (Template temp : templates) {
			element.addComment(temp.getNote());
			element.add(new Template().createTemplate(temp));
		}
		
		return element;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType_comment() {
		return type_comment;
	}

	public void setType_comment(String type_comment) {
		this.type_comment = type_comment;
	}

	public String getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(String threadNum) {
		this.threadNum = threadNum;
	}

	public String getThreadNum_comment() {
		return threadNum_comment;
	}

	public void setThreadNum_comment(String threadNum_comment) {
		this.threadNum_comment = threadNum_comment;
	}

	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplate(List<Template> templates) {
		this.templates = templates;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
	
	
}
