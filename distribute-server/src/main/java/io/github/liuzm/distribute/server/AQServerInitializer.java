package io.github.liuzm.distribute.server;

import java.util.concurrent.Executors;

import io.github.liuzm.distribute.remoting.netty.NettyRemotingServer;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.server.processor.AckProcessor;
import io.github.liuzm.distribute.server.processor.JobProcessor;
import io.github.liuzm.distribute.server.processor.MessageRecivedProcessor;
import io.github.liuzm.distribute.server.processor.NodeProcessor;
import io.github.liuzm.distribute.server.processor.TaskRecivedProcessor;

public class AQServerInitializer {

    public static void initializerProcessor(NettyRemotingServer aqserver) {
        aqserver.registerDefaultProcessor(new AckProcessor(), Executors.newCachedThreadPool());
        aqserver.registerProcessor(HeaderMessageCode.SERVER_SSSD_COMMAND, new NodeProcessor(), Executors.newCachedThreadPool());
        aqserver.registerProcessor(HeaderMessageCode.SERVER_TASK_COMMAND, new TaskRecivedProcessor(), Executors.newCachedThreadPool());
        aqserver.registerProcessor(HeaderMessageCode.SERVER_JOB_STATUS, new JobProcessor(), Executors.newCachedThreadPool());
        aqserver.registerProcessor(HeaderMessageCode.MESSAGE_COMMAND, new MessageRecivedProcessor(), Executors.newCachedThreadPool());
    }

}
