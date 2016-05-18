/**
 * 
 */
package io.github.liuzm.distribute.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lxyq
 *
 */
public class MessageConverter {
	
	/**
	 * 把message转化为String
	 * @param message
	 * @return
	 */
	public static String write(Message message){
		return JSONObject.toJSONString(message);
	}
	/**
	 * 把String转为Message对象
	 * @param message
	 * @return
	 */
	public static Message read(String message){
		return (Message)JSON.parseObject(message,Message.class);
	}
}
