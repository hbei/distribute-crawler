package io.github.liuzm.crawler.extractor.selector;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.extractor.selector.expression.SimpleExpression;
import io.github.liuzm.crawler.extractor.selector.factory.ElementCssSelectorFactory;

/**
 * @author whiteme
 * @date 2013年10月15日
 * @desc 条件判别，满足相应条件，则返回相应的选择器
 */
@SuppressWarnings("rawtypes")
public class IFConditions {
	
	/**
	 * 条件，以AND OR = 分割
	 */
	private String conditions;
	
	public IFConditions(String conditions) {
		super();
		this.conditions = conditions;
	}

	/**
	 * 条件辨别式对应的一组选择器
	 */
	protected List<AbstractElementCssSelector> selectors = Lists.newArrayList();
	/**
	 * 简单操作符号
	 */
	private Set<String> operations = Sets.newHashSet("=","!=", ">", "<", ">=", "<=");
	/**
	 * 简单与或关系
	 */
	private Set<String> cond = Sets.newHashSet(" and ", " or ", " AND ", " OR ");

	/**
	 * 检测依赖的选择器是否满足条件
	 * 
	 * @param depend
	 * @return
	 */
	public boolean test(Map<String, Object> selectContent) throws ExtractException{
		TreeMap<Integer, String> conIndex = Maps.newTreeMap();
		Queue<SimpleExpression> expressionQueue = Queues.newArrayDeque();
		Queue<String> logicQueue = Queues.newArrayDeque();
		// a=b and c=d or c=e or x=y
		int index = 0;
		for (String co : cond) {
			index = 0;
			while ((index = conditions.indexOf(co, index+1)) > -1) {
				int i = index;
				conIndex.put(i, co);
			}
		}
		index = 0;
		for (Entry<Integer, String> entry : conIndex.entrySet()) {
			String subExp = conditions.substring(index, entry.getKey());
			for (String op : operations) {
				int i = subExp.indexOf(op);
				if (i > -1) {
					String[] ss = subExp.split(op);
					if(null==selectContent.get(ss[0].trim())){
						throw new ExtractException("表达式依赖的选择提取内容为空：["+this.conditions + "] 中的"+ss[0]);
					}
					expressionQueue.add(new SimpleExpression(StringUtils.trim((String)selectContent.get(ss[0].trim())), StringUtils.trim(ss[1]), op));
					logicQueue.add(StringUtils.trim(entry.getValue()));
				}
			}
			index = entry.getKey()+entry.getValue().length();
		}
		// 最后一个表达式
		String subExp = conditions.substring(index);
		for (String op : operations) {
			int i = subExp.indexOf(op);
			if (i > -1) {
				String[] ss = subExp.split(op);
				if(null==selectContent.get(ss[0].trim())){
					throw new ExtractException("表达式依赖的选择提取内容为空：["+this.conditions + "] 中的"+ss[0]);
				}
				expressionQueue.add(new SimpleExpression(StringUtils.trim((String)selectContent.get(ss[0].trim())), StringUtils.trim(ss[1]), op));
			}
		}
		boolean b;
		try {
			b = expressionQueue.poll().test();
			while(!expressionQueue.isEmpty()){
				b = cacl(b,logicQueue.poll(),expressionQueue.poll());
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * 一次逻辑运算
	 * @return
	 */
	private boolean cacl(boolean left ,String logic,SimpleExpression right){
		try {
			if("and".equals(logic.toLowerCase())){
				return left && right.test();
			}else {
				return left || right.test();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 获取分支条件下选择器的选择内容
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getContent(Document document) throws ExtractException{
		if(null!=selectors && selectors.size()>0){
			Map<String, Object> content = Maps.newHashMap();
			for(AbstractElementCssSelector<?> selector:selectors){
				if(selector instanceof FileElementCssSelector){
					Map m = ((FileElementCssSelector)selector).setResult(content)
							.setDocument(document)
							.getContentMap();
					if(null==m || m.size()>0 && selector.isRequired())
						return null;
					content.putAll(m);
				}else{
					Map m = selector.setDocument(document).getContentMap();
					if(null==m || m.size()>0 && selector.isRequired())
						return null;
					content.putAll(m);
				}
			}
			return content;
		}
		return Maps.newHashMap();
	}
	
	public List<AbstractElementCssSelector> getSelectors() {
		return selectors;
	}
	public void setSelectors(List<AbstractElementCssSelector> selectors) {
		this.selectors = selectors;
	}
	
	public void addSelector(AbstractElementCssSelector selector){
		this.selectors.add(selector);
	}
	
	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	
	public static void main(String[] args) {
//		String exp = "a= sd ea and  c= c bc d  and c=e and x=y";
//		
//		IFConditions ic = new IFConditions(exp);
//		try {
//			Map<String, Object> map = Maps.newHashMap();
//			map.put("a", "sd");
//			map.put("c", "c bc d");
////			map.put("x", "y");
//			System.out.println(ic.test(map));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		String exp = "category=电视剧 or category=微電影";
		IFConditions ic = new IFConditions(exp);
		try {
			Map<String, Object> map = Maps.newHashMap();
			map.put("category", "微電影");
			System.out.println(ic.test(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	////////////////////////////////////////////////////////////////////
	///			IFC 构造工厂
	///////////////////////////////////////////////////////////////////
	/**
	 * 条件分支选择器创建方法<b>传入该方法的element必须是If分支的配置</br>
	 * 该方法不做检测。
	 * @param document
	 * @return
	 */
	public static IFConditions create(Element element){
		if(element!=null){
			String exp = element.attr("test");
			IFConditions iFconditions = new IFConditions(exp);
			Elements selectElements = element.children();
			for(Element e :selectElements){
				if(e.tagName().equals("element")){
					iFconditions.addSelector(ElementCssSelectorFactory.create(e));
				}
			}
			return iFconditions;
		}
		return null;
	}
}
