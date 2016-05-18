package io.github.liuzm.crawler.extractor.selector.action.string;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.extractor.selector.action.StringSelectorAction;

/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc 截取字串的Action<br>
 * 如，"4,4"表示，从第四位起，截取4个字符.<br>
 * "4"表示从4开始截取到最后
 */
public class StringSubAction extends StringSelectorAction {
	/**
	 * 截取位置
	 */
	int pos = 0;
	/**
	 * 字符位置
	 */
	String spos = "";
	/**
	 * 截取长度
	 */
	int lenth = 0;
	/**
	 * 构造器"4,4"表示，从第四位起，截取4个字符.<br>
	 * "4"表示从4开始截取到最后
	 * @param subExpression
	 */
	public StringSubAction(String subExpression){
		if(StringUtils.isNotBlank(subExpression)){
			String[] ss = StringUtils.split(subExpression, ",");
			if(ss.length==1){
				if(StringUtils.isNumeric(ss[0])){
					this.pos = Integer.parseInt(ss[0]);
				}else {
					this.spos=ss[0];
				}
			}else if (ss.length==2) {
				if(StringUtils.isNumeric(ss[0])){
					this.pos = Integer.parseInt(ss[0]);
				}else {
					this.spos=ss[0];
				}
				this.lenth = Integer.parseInt(ss[1]);
			}
		}
	}
	
	/**
	 * 获取字符的特定截取子串
	 */
	@Override
	public String doAction(String content) {
		if(StringUtils.isNotBlank(content)){
			if(StringUtils.isNotBlank(spos)){
				this.pos = content.indexOf(spos) + spos.length();
			}
			if(this.lenth==0 && this.pos>0){
				content = StringUtils.substring(content, this.pos);
			}else if (this.lenth>0 && this.pos>=0) {
				content = StringUtils.substring(content, this.pos, this.pos+this.lenth);
			}
			return content;
		}
		return "";
	}
	
	
	public static void main(String[] args) {
		String string= "234dfs454#$%gasfdjlkas";
		StringSubAction actiong = new StringSubAction("3,4");
		System.out.println(actiong.doAction(string));
		
		StringSubAction action2 = new StringSubAction("gas,4");
		System.out.println(action2.doAction(string));
	}
}
