package io.github.liuzm.distribute.remoting;

import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

public interface Processor {
	
	Command process(ChannelHandlerContext ctx,Command c) throws Exception;
}
