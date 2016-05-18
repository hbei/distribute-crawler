/**
 * 
 */
package io.github.liuzm.distribute.client.processor;

import io.github.liuzm.distribute.common.message.MessageContext;
import io.github.liuzm.distribute.remoting.Processor;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xh-liuzhimin
 *
 */
public abstract class AbstractSendMessageProcessor implements Processor {
	
	/**
	 * 
	 * @param ctx
	 * @param code
	 * @return
	 */
	protected MessageContext buildMessageContext(ChannelHandlerContext ctx,int code){
		MessageContext context = new MessageContext();
		context.setCode(code);
		return context;
	}

}
