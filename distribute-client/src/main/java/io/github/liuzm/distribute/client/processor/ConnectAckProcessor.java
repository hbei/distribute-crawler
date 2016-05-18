package io.github.liuzm.distribute.client.processor;

import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.netty.channel.ChannelHandlerContext;

/**
 * client login
 * 
 * @author liuzhimin
 *
 */
public class ConnectAckProcessor implements Processor {

	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		Command cc = Command.createResponseCommand(HeaderMessageCode.ACK_COMMAND, "client ack OK!");
		return cc;
	}

}
