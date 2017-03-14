package io.github.liuzm.distribute.client.processor;

import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

public class JobStatusProcessor implements Processor {

	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		
		return null;
	}

}
