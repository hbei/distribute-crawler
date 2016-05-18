package io.github.liuzm.crawler.extractor.selector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.select.Elements;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.action.IntegerSelectorAction;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;

/**
 * @author chenxinwen
 * @date 2014年8月1日
 * @desc
 */
public class NumericaElementCssSelector extends AbstractElementCssSelector<Number> {

	NumberFormat format;
	Number content;
	private List<IntegerSelectorAction> actions = Lists.newArrayList();

	public NumericaElementCssSelector(){super();};
	public NumericaElementCssSelector(String name, String value, String attr,
			boolean isRequired,int index,String regex) {
		super(name, value, attr, isRequired, index, regex);
	}
	
	public NumericaElementCssSelector(String name, String value, String attr,
			boolean isRequired,String parttern) {
		super(name, value, attr, isRequired,0);
		format = new DecimalFormat(parttern);
	}
	
	@Override
	public Number getContent() throws ExtractException{
		try {
			// 如果content不为空且不是新文档，则表示是同一个document的2+次调用，不用重新计算
			if (null != content && !newDoc) {
				return content;
			}
			if (null != document) {
				Elements elements = super.document.select(value);
				if (elements.isEmpty())
					return null;
				String temp;
				switch ($Attr) {
				case text:
					//temp = CharMatcher.DIGIT.retainFrom(getExtractText(elements));
					temp = filterDIGITFromString(getExtractText(elements));
					break;
				default:
					temp = filterDIGITFromString(getExtractAttr(elements, attr));
					break;
				}

				if (StringUtils.isNotBlank(temp)) {
					content = NumberUtils.createNumber(temp);
					newDoc = false;
					return content;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtractException("信息提取错误:"+e.getMessage());
		}
		return null;
	}


	@Override
	public Map<String, Number> getContentMap() throws ExtractException{
		if(newDoc){
			this.content = getContent();
		}
		if(null==this.content)
			return null;
		Map<String, Number> m = new HashMap<String, Number>(1);
		m.put(name, this.content);
		return m;
	}
	
	private String filterDIGITFromString(String text){
		Pattern pp = Pattern.compile("\\d+\\.?\\d*");
		Matcher mm = pp.matcher(text);
		if (mm.find()) {
			String temp = mm.group();
			if(temp.endsWith(".")){
				temp = temp.substring(0,temp.length()-1);
			}
			return temp;
		}
		return null;
	}
	
	/**
	 * 返回字符
	 * @return
	 */
	public String getContentString() throws ExtractException{
		if(null==content && newDoc){
			getContent();
		}
		return format.format(this.content);
	}

	public NumberFormat getFormat() {
		return format;
	}

	public void setFormat(NumberFormat format) {
		this.format = format;
	}

	
	
	public static void main(String[] args) {
		String str = "\"8\"";
		System.out.println(new NumericaElementCssSelector().filterDIGITFromString(str));
	}

	@Override
	public void addAction(SelectorAction action) {
		this.actions.add((IntegerSelectorAction) action);
	}
	
}
