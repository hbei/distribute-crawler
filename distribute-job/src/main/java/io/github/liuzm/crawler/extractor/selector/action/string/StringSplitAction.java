package io.github.liuzm.crawler.extractor.selector.action.string;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;

/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 根据配置对提取内容进行Split操作，并返回指定位置的截取字串。</br>
 * 如果有多个位置(多个以，分隔)，则多个位置之间的的字串仍然按指定的分隔符连接。
 */
public class StringSplitAction extends StringSelectorAction{
	/**
	 * 分隔符
	 */
	private String split ;
	private String newsplit = "|";
	private Set<Integer> set = Sets.newLinkedHashSet();
	/**
	 * 构造器
	 * @param split
	 * @param index
	 */
	public StringSplitAction(String split,String index){
		if(StringUtils.isNotBlank(split)){
			this.split = split;
		}else {
			this.split = ",";
		}
		if(StringUtils.isNotBlank(index)){
			String is[] = index.split(",");
			for(String i:is){
				if(StringUtils.isNumeric(i)){
					set.add(Integer.parseInt(i));
				}
			}
		}
	}
	
	public StringSplitAction(String split,String index,String newsplit){
		if(StringUtils.isNotBlank(split)){
			this.split = split;
		}else {
			this.split = ",";
		}
		if(StringUtils.isNotBlank(index)){
			String is[] = index.split(",");
			for(String i:is){
				if(StringUtils.isNumeric(i)){
					set.add(Integer.parseInt(i));
				}
			}
			if (set.size()>0) {
				if(StringUtils.isNotBlank(newsplit))
					this.newsplit = newsplit;
			}
		}
	}
	
	/**
	 * 根据配置对提取内容进行Split操作，并返回指定位置的截取字串。</br>
	 * 如果有多个位置(多个以，分隔)，则多个位置之间的的字串仍然按指定的分隔符连接。</br>
	 * 如果没设置位置，默认0。即第一个。
	 */
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			if(set.size()>0){
				String[] subs = StringUtils.split(content, this.split);
				StringBuilder sb = new StringBuilder();
				for (Integer i :set) {
					if(i<subs.length){
						if(i>-1)
							sb.append(subs[i]).append(newsplit);
					}
				}
				if(sb.length()>0)
					return sb.substring(0,sb.length()-1);
			}else {
				return StringUtils.split(content, this.split)[0];
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
		String s = "af!dso!iru43gs!45a!sa";
		StringSplitAction action = new StringSplitAction("!", "1,2,3");
		StringSplitAction action2 = new StringSplitAction("!",null);
		System.out.println(action.doAction(s));
//		/System.out.println(action2.doAction(s));
	}
}
