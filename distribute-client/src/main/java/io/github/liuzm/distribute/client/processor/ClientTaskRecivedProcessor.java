/**
 * 
 */
package io.github.liuzm.distribute.client.processor;

import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * job task response
 * @author xh-liuzhimin
 *
 */
public class ClientTaskRecivedProcessor implements Processor {

	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		Command cc = null;
		if(c.getCode() == HeaderMessageCode.CLIENT_RESV_TASK_COMMAND){
			cc = Command.createResponseCommand(HeaderMessageCode.SERVER_TASK_COMMAND, "i am client . i have done the job");
		}
		return cc;
	}

}
