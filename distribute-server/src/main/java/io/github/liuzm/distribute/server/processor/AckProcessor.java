/**
 * 
 */
package io.github.liuzm.distribute.server.processor;

import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.cache.ChannelManager;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xh-liuzhimin
 *
 */
public class AckProcessor implements Processor {

	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		// 如果是客户端返回的ack消息，进行channel处理
		
		return null;
	}

}
