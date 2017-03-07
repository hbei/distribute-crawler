/**
 * 
 */
package io.github.liuzm.distribute.server.processor;

import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端节点控制执行器接收处理
 * 
 * @author xh-liuzhimin
 *
 */
public class NodeProcessor implements Processor {
	
	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		if(c.getCode() == HeaderMessageCode.SERVER_TASK_COMMAND){ // 客户端对控制指令的响应
			// 接收client端写回的信息
			System.out.println(c.getRemark());
		}
        return null;
	}
	
}
