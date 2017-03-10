package io.github.liuzm.distribute.server.processor;

import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

public class BlankProcessor implements Processor {

	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		System.out.println("i do!"+System.nanoTime());
		return Command.createResponseCommand(0, "i recv!");
	}

}
