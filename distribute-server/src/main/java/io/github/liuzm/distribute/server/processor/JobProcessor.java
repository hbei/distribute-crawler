/**
 * 
 */
package io.github.liuzm.distribute.server.processor;

import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xh-liuzhimin
 *
 */
public class JobProcessor implements Processor {

	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		return null;
	}

}
