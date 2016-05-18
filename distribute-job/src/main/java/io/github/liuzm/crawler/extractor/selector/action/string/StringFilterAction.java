package io.github.liuzm.crawler.extractor.selector.action.string;

import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Sets;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;



/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 负责过滤字符中的特定字符
 */
public class StringFilterAction extends StringSelectorAction{
	/**
	 * 需要过滤的字符
	 */
	private String filterString;
	/**
	 * 过滤字符集名称，见<b>CharType</b>
	 */
	private Set<CharType> set = Sets.newLinkedHashSet();
	/**
	 * 构造器
	 * @param filterString
	 * @param charType
	 */
	public StringFilterAction(String filterString, String charType) {
		super();
		this.filterString = filterString;
		if(StringUtils.isNotBlank(charType)){
			String is[] = charType.split(",");
			for(String i:is){
				CharType ct = EnumUtils.getEnum(CharType.class, i);
					set.add(ct);
			}
		}
	}
	
	/**
	 * 对字符进行过滤
	 */
	@Override
	public String doAction(String content) {
		for(CharType ct :set){
			switch (ct) {
			case INVISIBLE:
				content = CharMatcher.INVISIBLE.removeFrom(content);
				
			case BREAKING_WHITESPACE:
				content = CharMatcher.BREAKING_WHITESPACE.removeFrom(content);
			case DIGIT:
				content = CharMatcher.DIGIT.removeFrom(content);
			case LETTER:
				content = CharMatcher.JAVA_LETTER.removeFrom(content);
			default:
				break;
			}
		}
		if(StringUtils.isNotBlank(filterString)){
			content = CharMatcher.anyOf(filterString).removeFrom(content);
		}
		return content;
	}
	
	public enum CharType{
		/**
		 * Determines whether a character is invisible; that is, if its Unicode category is any of SPACE_SEPARATOR, LINE_SEPARATOR, PARAGRAPH_SEPARATOR, CONTROL, FORMAT, SURROGATE, and PRIVATE_USE according to ICU4J.
		 */
		INVISIBLE,
		/**
		 * Determines whether a character is a breaking whitespace (that is, a whitespace which can be interpreted as a break between words for formatting purposes). See WHITESPACE for a discussion of that term.
		 */
		BREAKING_WHITESPACE,
		DIGIT,
		LETTER
	}
	public static void main(String[] args) {
		String string = "sssf</>dfdfdsfasfafdadgsadgsadafsdfgsdeisdfl";
		StringFilterAction action = new StringFilterAction("</", CharType.LETTER.name());
		System.out.println(action.doAction(string));
		System.out.println(CharType.LETTER.name());
	}
}
