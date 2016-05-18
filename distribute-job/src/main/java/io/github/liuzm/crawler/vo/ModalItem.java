package io.github.liuzm.crawler.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Comment;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.google.common.collect.Lists;

public class ModalItem {
	private String url;
	private String modal_name ;
	private String modal_type ;
	private String modal_value;
	private String modal_attr;
	private String required = "false";
	private String modal_regex = "";
	
	private String modal_index ;
	
	private Action action ;

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getModal_name() {
		return modal_name;
	}

	public void setModal_name(String modal_name) {
		this.modal_name = modal_name;
	}

	public String getModal_type() {
		return modal_type;
	}

	public void setModal_type(String modal_type) {
		this.modal_type = modal_type;
	}

	public String getModal_value() {
		return modal_value;
	}

	public void setModal_value(String modal_value) {
		this.modal_value = modal_value;
	}

	public String getModal_attr() {
		return modal_attr;
	}

	public void setModal_attr(String modal_attr) {
		this.modal_attr = modal_attr;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getModal_regex() {
		return modal_regex;
	}

	public void setModal_regex(String modal_regex) {
		this.modal_regex = modal_regex;
	}

	public String getModal_index() {
		return modal_index;
	}

	public void setModal_index(String modal_index) {
		this.modal_index = modal_index;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "ModalItem [url=" + url + ", modal_name=" + modal_name
				+ ", modal_type=" + modal_type + ", modal_value=" + modal_value
				+ ", modal_attr=" + modal_attr + ", required=" + required
				+ ", modal_regex=" + modal_regex + ", modal_index="
				+ modal_index + ", action=" + action + "]";
	}

	
	/*private List<ModalItem> subItems ;
	
	private String note;*/
	
	
	
	
}