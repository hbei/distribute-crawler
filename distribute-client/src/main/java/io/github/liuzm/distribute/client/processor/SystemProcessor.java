package io.github.liuzm.distribute.client.processor;

import io.github.liuzm.distribute.remoting.Context;
import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

public class SystemProcessor implements Processor {
	
	private final Context context;
	
	public SystemProcessor(Context context){
		this.context = context;
	}
	
	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		
		context.setResponseCommand(c);
		return null;
	}

}
