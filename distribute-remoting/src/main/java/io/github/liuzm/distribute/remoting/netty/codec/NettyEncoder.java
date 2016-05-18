package io.github.liuzm.distribute.remoting.netty.codec;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoder extends MessageToByteEncoder<Command> {
	
	private static final Logger logger = LoggerFactory.getLogger(NettyEncoder.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Command command, ByteBuf out) throws Exception {
		try{
			ByteBuffer header = command.encodeHeader();
            out.writeBytes(header);
            byte[] body = command.getBody();
            if (body != null) {
                out.writeBytes(body);
            }
		}catch(Exception e){
			e.printStackTrace();
			logger.error(command.toString());
		}
	}

}
