package io.github.liuzm.crawler.extractor.selector.action;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import io.github.liuzm.crawler.exception.IntegerBetweenExpressionException;
import io.github.liuzm.crawler.extractor.selector.action.SelectorAction;
import io.github.liuzm.crawler.extractor.selector.action.file.DownLoadFileAction;
import io.github.liuzm.crawler.extractor.selector.action.file.DownLoadImageResizeAction;
import io.github.liuzm.crawler.extractor.selector.action.file.FileActionType;
import io.github.liuzm.crawler.extractor.selector.action.integer.IntegerAbsAction;
import io.github.liuzm.crawler.extractor.selector.action.integer.IntegerActionType;
import io.github.liuzm.crawler.extractor.selector.action.integer.IntegerBetweenAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringActionType;
import io.github.liuzm.crawler.extractor.selector.action.string.StringAfterAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringAfterLastAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringBeforeAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringBeforeLastAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringBetweenAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringFilterAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringPerfixAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringRegexAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringReplaceAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringSplitAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringSubAction;
import io.github.liuzm.crawler.extractor.selector.action.string.StringSuffixAction;

/**
 * @author chenxin.wen
 * @date 2014年9月11日
 * @desc Acting工厂
 */
public class ActionFactory {
	
	public static SelectorAction create(Element element,String c){
		if("string".equals(c)){
			StringActionType $type = EnumUtils.getEnum(StringActionType.class, element.attr("operation"));
			if(null == $type){
				try {
					throw new Exception("配置文件错误："+element.tagName()+"请检查元素的operation属性");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			switch ($type) {
			case after:
				return new StringAfterAction(element.attr("split"));
			case afterLast:
				return new StringAfterLastAction(element.attr("split"));
			case before:
				return new StringBeforeAction(element.attr("split"));
			case beforeLast:
				return new StringBeforeLastAction(element.attr("split"));
			case between:
				return new StringBetweenAction(element.attr("exp"));
			case filter:
				return new StringFilterAction(element.attr("filter"), element.attr("charType"));
			case replace:
				/*return new StringReplaceAction(element.attr("exp"),element.attr("replacement"));*/
				String exp = element.attr("exp");
				String[] kv = exp.split(",");
				if(kv.length==2){
					return new StringReplaceAction(kv[0],kv[1]);
				}
				
			case split:
				return new StringSplitAction(element.attr("split"),element.attr("index"));
			case sub:
				return new StringSubAction(element.attr("exp"));
			case suffix:
				return new StringSuffixAction(element.attr("suffix"));
			case perfix:
				return new StringPerfixAction(element.attr("perfix"));
			case regex:
				return new StringRegexAction(element.attr("exp"));
			default:
				break;
			}
		}else if("integer".equals(c)  || "int".equals(c)) {
			IntegerActionType $type = EnumUtils.getEnum(IntegerActionType.class, element.attr("operation"));
			switch ($type) {
			case abs:
				return new IntegerAbsAction();
			case between:
				try {
					return new IntegerBetweenAction(element.attr("exp"),element.attr("default"));
				} catch (IntegerBetweenExpressionException e) {
					e.printStackTrace();
				}
			default:
				break;
			}
		}else if("date".equals(c)){
			
		}else if("numerica".equals(c)){
			IntegerActionType $type = EnumUtils.getEnum(IntegerActionType.class, element.attr("operation"));
			switch ($type) {
			case abs:
				return new IntegerAbsAction();
			case between:
				try {
					return new IntegerBetweenAction(element.attr("exp"),element.attr("default"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			default:
				break;
			}
		}else if ("file".equals(c)) {
			FileActionType $type = EnumUtils.getEnum(FileActionType.class, element.attr("operation"));
			switch ($type) {
			case download:
				String dir = element.attr("dir");
				String temp = element.attr("fileName");
				boolean md5File = false ,asyn;
				if(StringUtils.isNotBlank(temp)){
					if("{md5}".equals(temp)){
						md5File = true;
					}
				}
				else md5File = true;
				
				temp = element.attr("asyn");
				if(StringUtils.isNotBlank(temp)){
					asyn = Boolean.parseBoolean(temp);
				}
				else {
					asyn = true;
				}
				return new DownLoadFileAction(dir, md5File, asyn);
			case download_resize:
				String dir2 = element.attr("dir");
				String temp2 = element.attr("fileName");
				boolean md5File2 = false ,asyn2;
				if(StringUtils.isNotBlank(temp2)){
					if("{md5}".equals(temp2)){
						md5File2 = true;
					}
				}
				else md5File2 = true;
				temp2 = element.attr("asyn");
				
				if(StringUtils.isNotBlank(temp2)){
					asyn2 = Boolean.parseBoolean(temp2);
				}
				else {
					asyn2 = true;
				}
				DownLoadImageResizeAction resizeAction = new DownLoadImageResizeAction(dir2, md5File2, asyn2);
				
				temp2 = element.attr("width");
				if(StringUtils.isNotBlank(temp2)){
					resizeAction.setW(Integer.parseInt(temp2));
				}
				
				temp2 = element.attr("height");
				if(StringUtils.isNotBlank(temp2)){
					resizeAction.setH(Integer.parseInt(temp2));
				}
				temp2 = element.attr("quality");
				if(StringUtils.isNotBlank(temp2)){
					resizeAction.setQuality(Float.parseFloat(temp2));
				}
				temp2 = element.attr("del");
				if(StringUtils.isNotBlank(temp2)){
					resizeAction.setDeleteOldFile(Boolean.parseBoolean(temp2));
				}
				return resizeAction;
			default:
				break;
			}
		}else {
			StringActionType $type = EnumUtils.getEnum(StringActionType.class, element.attr("operation"));
			if(null == $type){
				try {
					throw new Exception("配置文件错误："+element.tagName()+"请检查元素的operation属性");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			switch ($type) {
			case after:
				return new StringAfterAction(element.attr("split"));
			case afterLast:
				return new StringAfterLastAction(element.attr("split"));
			case before:
				return new StringBeforeAction(element.attr("split"));
			case beforeLast:
				return new StringBeforeLastAction(element.attr("split"));
			case between:
				return new StringBetweenAction(element.attr("exp"));
			case filter:
				return new StringFilterAction(element.attr("filter"), element.attr("charType"));
			case replace:
				return new StringReplaceAction(element.attr("search"),element.attr("replacement"));
			case split:
				return new StringSplitAction(element.attr("split"),element.attr("index"));
			case sub:
				return new StringSubAction(element.attr("exp"));
			case suffix:
				return new StringSuffixAction(element.attr("suffix"));
			case perfix:
				return new StringPerfixAction(element.attr("perfix"));
			default:
				break;
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		System.out.println(StringActionType.after);
		System.out.println(StringActionType.after.name().equalsIgnoreCase("after"));
		System.out.println(StringActionType.after.toString());
		System.out.println(StringActionType.after.equals("after"));
		StringActionType[] ss = StringActionType.values();
		for (StringActionType stringActionType : ss) {
			System.out.println();
		}
		
	}
}
