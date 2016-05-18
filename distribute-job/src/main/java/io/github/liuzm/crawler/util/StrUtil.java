package io.github.liuzm.crawler.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class StrUtil {
	/*
	 * 把全角字符转成半角
	 */
	public static String toBanjiao(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}
	
	 public static String  obj2string(Object f) {
		 String returnString ="";
		 if(f instanceof Long){
			 long tmp1 = (Long)f;
			 returnString = String.valueOf(tmp1);
		 }else if(f instanceof Integer){
			 int tmp1 = (Integer)f;
			 returnString = String.valueOf(tmp1);
		 }else if(f instanceof BigDecimal){
			 BigDecimal tmp1 = (BigDecimal)f;
			 returnString = tmp1.toString();
		 }else{
			 returnString =(String) f;
		 }
		 if(StringUtils.isEmpty(returnString)){
			 returnString = "-1";
		 }
		 return returnString;
	 }

	public static void main(String[] args) {
		String QJstr = "";
		String result = toBanjiao(QJstr);
		System.out.println(result+"（）");

	}
}
