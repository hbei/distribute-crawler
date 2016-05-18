package io.github.liuzm.crawler.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Comment;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.google.common.collect.Lists;

import io.github.liuzm.crawler.extractor.selector.action.string.StringActionType;

public class ItemElement {
	private String name = "";
	private String type = "string";
	private String value= "";
	private String attr = "text";
	private String required = "false";
	private String regex = "";
	
	private String index = "";
	
	private Action action ;
	
	private List<ItemElement> subItems = Lists.newArrayList();
	
	private String note;

	
	/*
	 * <element name="v_id" type="string" value="a#xqwxqy_B02_07" attr="href" required="true">
				 	<action operation="regex" exp="([0-9]+).htm"></action> <action operation="regex" exp="regex"/>
				 </element>
	 */
	
	public ItemElement(){super();};
	
	public ItemElement(Node node, String note) {
		Element item = (Element)node;
		this.name = item.attributeValue("name");
		this.type = item.attributeValue("type");
		this.value = item.attributeValue("value");
		this.attr = item.attributeValue("attr");
		this.required = item.attributeValue("required");
		this.regex = item.attributeValue("regex");
		this.index = item.attributeValue("index");
		this.note = note;
		
		Element action_e = item.element("action");

		//处理<action>
		if(action_e!=null){
			
			this.action = new Action();
			this.action.setOperation(action_e.attributeValue("operation"));
			if(StringUtils.isNotBlank(action_e.attributeValue("split")))
				this.action.setExp(action_e.attributeValue("split"));
			if(StringUtils.isNotBlank(action_e.attributeValue("exp")))
				this.action.setExp(action_e.attributeValue("exp"));
			if(StringUtils.isNotBlank(action_e.attributeValue("suffix")))
				this.action.setExp(action_e.attributeValue("suffix"));
			if(StringUtils.isNotBlank(action_e.attributeValue("perfix")))
				this.action.setExp(action_e.attributeValue("perfix"));
		}
		
		//处理子<element>
		if("url".equalsIgnoreCase(this.type)){
			subItems = Lists.newArrayList();
			//注意是当前<element>的branch下的node个数
			int n = item.nodeCount();
			String comment = "";
			for (int i = 0; i < n; i++) {
				/*
				 * 顺序读取 <elements>下的node。
				 * 如果是注释（Comment类型）则把它存在变量里。可以多个
				 */	
				Node sub_node = item.node(i);
				if(sub_node instanceof Comment){
					comment += node.getText()+"\n";
				}
				/*
				 * 如果是<Element>节点则创建一个ItemElement对象保存List<ItemElement> elements里
				 * 最后清空注释的变量。
				 */
				if(sub_node instanceof Element){
					ItemElement sub_item = new ItemElement(sub_node,comment);	
					subItems.add(sub_item);
					comment = "";
					sub_item=null;
				}
				
			}
			
		}
	}
	
	
	public Element createItemElement(ItemElement item) {
		Element ie = DocumentHelper.createElement("element");
		ie.addAttribute("name", StringUtils.isNotBlank(item.name)?item.getName():"key_"+new Date().getTime());
		ie.addAttribute("type", StringUtils.isNoneBlank(item.getType())?item.getType():this.type);
		ie.addAttribute("value", StringUtils.isNoneBlank(item.getValue())?item.getValue():this.value);
		ie.addAttribute("attr", StringUtils.isNoneBlank(item.getAttr())?item.getAttr():this.attr);
		ie.addAttribute("required", StringUtils.isNoneBlank(item.getRequired())?item.getRequired():this.required);
		if(StringUtils.isNoneBlank(item.getRegex())){
			ie.addAttribute("regex", item.getRegex());
		}
		
		if(StringUtils.isNumeric(item.getIndex())){
			ie.addAttribute("index", item.getIndex());
		}
		
		
		if("url".equalsIgnoreCase(item.getType())){
			
			for (ItemElement it : item.getSubItems()) {
				if(StringUtils.isNotBlank(it.getNote()))
					ie.addComment(it.getNote());
				ie.add(createItemElement(it));
			}
		}
		
		if(!item.getAction().isNull()){
			Element a_e = ie.addElement("action");
			String op = item.getAction().getOperation();
			String value = item.getAction().getExp();
			a_e.addAttribute("operation", op);
			StringActionType[] types = StringActionType.values();
			for (StringActionType t : types) {
				String tmp = t.name();
				if(!tmp.equalsIgnoreCase(op))continue;
				if(tmp.equalsIgnoreCase("after")||tmp.equalsIgnoreCase("afterLast")||
						tmp.equalsIgnoreCase("before")||
						tmp.equalsIgnoreCase("beforeLast"))
					a_e.addAttribute("split", value);
				if(tmp.equalsIgnoreCase("between")||tmp.equalsIgnoreCase("sub")||tmp.equalsIgnoreCase("regex")||tmp.equalsIgnoreCase("replace"))	
					a_e.addAttribute("exp", value);
				if(tmp.equalsIgnoreCase("suffix"))	
					a_e.addAttribute("suffix", value);
				if(tmp.equalsIgnoreCase("perfix"))	
					a_e.addAttribute("perfix", value);
			}
			//未完
		}
		
		
		return ie;
	}
	
	

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type.toLowerCase();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	

	public List<ItemElement> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<ItemElement> subItems) {
		this.subItems = subItems;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "ItemElement [name=" + name + ", type=" + type + ", value="
				+ value + ", attr=" + attr + ", required=" + required
				+ ", regex=" + regex + ", index=" + index + ", action="
				+ action + ", sub_url=" + subItems + ", note=" + note + "]";
	}
	
}
