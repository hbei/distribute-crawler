
package io.github.liuzm.crawler.extractor.selector.action.string;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;

/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 该action对StringSelector选择的内容按配置进行替换操作。
 */
public class StringReplaceAction extends StringSelectorAction {
	
	private String searchString;
	private String replacement;
	
	public StringReplaceAction(String searchString, String replacement) {
		super();
		this.searchString = searchString;
		this.replacement = replacement;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	/**
	 * 根据配置的查找字符和替换字符对抽取内容进行替换操作
	 */
	@Override
	public String doAction(String content) {
		return StringUtils.replace(content, searchString, replacement);
	}
	
	public static void main(String[] args) {
		String string  = "@#$%$FGDFGFGHS#@$$Y";
		StringReplaceAction action = new StringReplaceAction("#", ",");
		
		System.out.println(action.doAction(string));
		
		String url = "http://pic1.ajkimg.com/display/anjuke/f5bcb775b47570557790e86d9c7a23dd/820x615.jpg";
		//http://pic1.ajkimg.com/display/inform/f5bcb775b47570557790e86d9c7a23dd/820x615.jpg
		StringReplaceAction action4 = new StringReplaceAction("anjuke","inform");
		System.out.println(action4.doAction(url));
		//http://pic1.ajkimg.com/display/inform/f5bcb775b47570557790e86d9c7a23dd/820x615.jpg
		//http://pic1.ajkimg.com/inform/anjuke/f5bcb775b47570557790e86d9c7a23dd/820x615.jpg
	}

}
