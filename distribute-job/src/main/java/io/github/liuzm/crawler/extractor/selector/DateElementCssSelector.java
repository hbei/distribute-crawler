package io.github.liuzm.crawler.extractor.selector;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.select.Elements;

import com.google.common.collect.Sets;

import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;


/**
 * @author chenxinwen
 * @date 2014年7月30日
 * @desc 
 */
public class DateElementCssSelector extends AbstractElementCssSelector<Date> {
	
	private Set<String> patterns = Sets.newHashSet(
			"yyyy.MM.dd HH:mm:ss",
			"yyyy-MM-dd",
			"yyyy-MM-dd",
			"yyyy/MM/dd",
			"yyyy年MM月dd日",
			"yyyy年MM月",
			"yyyy MM dd",
			"yyyyMMdd",
			"yyyy",
			"yy/MM/dd",
			"yyyy.MM.dd G 'at' HH:mm:ss z",
			"EEE, MMM d, ''yy",
			"h:mm a",
			"hh 'o''clock' a, zzzz",
			"K:mm a, z" ,
			"yyyyy.MMMMM.dd GGG hh:mm aaa",
			"EEE, d MMM yyyy HH:mm:ss Z",
			"yyMMddHHmmssZ",
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
			);
	private Date date;
	
	public DateElementCssSelector() {
		super();
	}

	public DateElementCssSelector(String name, String value, String attr,
			boolean isRequired,int index) {
		super(name, value, attr, isRequired, index);
	}
	/**
	 * @param name
	 * @param value
	 * @param attr
	 * @param isRequired
	 * @param index
	 * @param regex
	 * @param parrtarn
	 */
	public DateElementCssSelector(String name, String value, String attr,
			boolean isRequired,int index,String regex,String parrtarn) {
		super(name, value, attr, isRequired,index,regex);
		this.patterns.add(parrtarn);
	}

	@Override
	public Date getContent(){
		try {
			if(null!=this.date && !newDoc){
				return date;
			}
			if(null!=document){
				Elements elements = super.document.select(value);
				if(elements.isEmpty())
					return null;
				String temp;
				switch ($Attr) {
				case text:
					temp = getExtractText(elements);
					break;
				default:
					temp = getExtractAttr(elements, attr);
					break;
				}
				if(StringUtils.isNotBlank(temp)){
					try {
						this.date = DateUtils.parseDate(temp, patterns.toArray(new String[0]));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				newDoc = false;
				return this.date;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, Date> getContentMap() {
		if(newDoc)
			getContent();
		if(null == date)
			return  null;
		HashMap<String, Date> map = new HashMap<>(1);
		map.put(name, date);
		return map;
	}

	public Set<String> getPatterns() {
		return patterns;
	}

	public void setPatterns(Set<String> patterns) {
		this.patterns = patterns;
	}

	public void addPattern(String pattern){
		this.patterns.add(pattern);
	}

	@Override
	public void addAction(SelectorAction<Date> action) {
		// TODO Auto-generated method stub
		
	}

	
	
}
