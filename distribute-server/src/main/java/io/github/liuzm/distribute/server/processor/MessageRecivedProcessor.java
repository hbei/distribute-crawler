package io.github.liuzm.distribute.server.processor;

import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by xh-liuzhimin on 2017/2/8.
 */
public class MessageRecivedProcessor implements Processor {

    @Override
    public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
        // 如果接收到的是采集数据，丢给储存线程做储存
        return null;
    }


}
