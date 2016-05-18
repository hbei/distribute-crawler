package io.github.liuzm.crawler.extractor.selector.factory;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.github.liuzm.crawler.extractor.selector.AbstractElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.DateElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.FileElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.IntegerElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.ListElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.NumericaElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.PageElementSelector;
import io.github.liuzm.crawler.extractor.selector.SelectorType;
import io.github.liuzm.crawler.extractor.selector.StringElementCssSelector;
import io.github.liuzm.crawler.extractor.selector.action.ActionFactory;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;

/**
 * @author  chenxinwen
 * @date 2014年7月30日
 * @desc css选择器工厂类
 */
public class ElementCssSelectorFactory {
	
	/**
	 * 创建各种选择器 
	 * @param name
	 * @param type
	 * @param value
	 * @param attr
	 * @param isRequired
	 * @param regex
	 * @return
	 */
	private static AbstractElementCssSelector<?> create(String name, String type,String value, String attr,
			boolean isRequired,int index,String regex,String pattenr){
		SelectorType $type = SelectorType.valueOf("$"+type);
		switch ($type) {
		case $int:
			return new IntegerElementCssSelector(name, value, attr, isRequired, index,regex);
		case $string:
			return new StringElementCssSelector(name, value, attr, isRequired, index,regex);
		case $list:
			return new ListElementCssSelector(name, value, attr, isRequired, index,regex);
		/*case $set:
			return new SetElementCssSelector(name, value, attr, isRequired, index,regex);*/
		case $url:
			return new PageElementSelector(name, value, attr, isRequired, index,regex);
		case $numerica:
			return new NumericaElementCssSelector(name, value, attr, isRequired, index,regex);
		case $date:
			return new DateElementCssSelector(name, value, attr, isRequired, index,regex,pattenr);
		case $file:
			return new FileElementCssSelector(name, value, attr, isRequired, index,regex);
		case $ajax:
//			return new AjaxElementCssSelector(name, value, attr, isRequired);
		default:
			return new StringElementCssSelector(name, value, attr, isRequired, index,regex);
		}
	}
	/**
	 * 构造器<b>该方法对Element不做检测，传递的Element必须是描述select的元素
	 * @param element
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static AbstractElementCssSelector create(Element element){
		String name = element.attr("name");
		String value = element.attr("value");
		String type = element.attr("type");
		
		String attr = element.attr("attr");
		String pattern = element.attr("pattern");
		String regex = element.attr("regex");
		String required = element.attr("required");
		String sIndex = element.attr("index");
		boolean isRequired = false;
		if(StringUtils.isNotBlank(required)){
			isRequired = Boolean.parseBoolean(required);
		}
		int index = -1;
		if(StringUtils.isNotBlank(sIndex)){
			index = Integer.parseInt(sIndex)-1;
		}
		AbstractElementCssSelector selector = ElementCssSelectorFactory.create(name, type, value, attr, isRequired,index,regex,pattern);
		// 检测子元素
		Elements children = element.children();
		for(Element e : children){
			if("action".equals(e.tagName())){
				SelectorAction action = ActionFactory.create(e, element.attr("type"));
				if(action!=null)
					selector.addAction(action);
			}
			// 只有Url类型的选择器嵌套自选择器
			else if("element".equals(e.tagName())){
				((PageElementSelector)selector).addSelector(create(e));
			}
		}
		return selector;
	}
}
