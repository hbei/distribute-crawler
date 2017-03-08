/**
 * 
 */
package io.github.liuzm.distribute.client.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.client.AQClient;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端应答处理器，SSSD
 * 
 * @author xh-liuzhimin
 *
 */
public class ClientCtlProcessor extends AbstractSendMessageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ClientCtlProcessor.class);

	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		
		logger.info("processor WARN >>>"+c.toString());
		return null;
	}
	/**
	 * 客户端消息回复处理
	 * 	
	 * @param ctx
	 * @param c
	 * @return
	 */
	private Command clientSendMsgBack(ChannelHandlerContext ctx, Command c) {
		Command cc = new Command();
		// 根据具体的业务来应答消息,例如：crawler 启动成功，失败，给服务端. 应答消息封装
		byte[] command = new byte[1024];
		
		return cc;
	}
	
	@SuppressWarnings("static-access")
	private Command startCrawler(){
		try {
			String path = "D:\\GIT\\crawler\\distribute-clawler\\distribute-job\\src\\main\\resources\\oschina.xml";
			return Command.createResponseCommand(HeaderMessageCode.SERVER_SSSD_COMMAND, "start success");
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
}
