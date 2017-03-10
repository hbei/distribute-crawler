package io.github.liuzm.distribute.client;

import java.util.concurrent.Executors;

import io.github.liuzm.distribute.client.processor.ClientCtlProcessor;
import io.github.liuzm.distribute.client.processor.ClientTaskRecivedProcessor;
import io.github.liuzm.distribute.client.processor.ConnectAckProcessor;
import io.github.liuzm.distribute.client.processor.JobStatusProcessor;
import io.github.liuzm.distribute.client.processor.SystemProcessor;
import io.github.liuzm.distribute.remoting.netty.NettyRemotingClient;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;

public class AQClientInitializer {

	public static void buildClient(NettyRemotingClient nettyClient){
		nettyClient.registerProcessor(HeaderMessageCode.ACK_COMMAND, new ConnectAckProcessor(), Executors.newCachedThreadPool());
		nettyClient.registerProcessor(HeaderMessageCode.CLIENT_RESV_SSSD_COMMAND, new ClientCtlProcessor(), Executors.newCachedThreadPool());
		nettyClient.registerProcessor(HeaderMessageCode.CLIENT_JOB_STATUS, new JobStatusProcessor(), Executors.newCachedThreadPool());
		nettyClient.registerProcessor(HeaderMessageCode.CLIENT_RESV_TASK_COMMAND, new ClientTaskRecivedProcessor(), Executors.newCachedThreadPool());
		nettyClient.registerProcessor(HeaderMessageCode.SYSTEM_ERROR, new SystemProcessor(), Executors.newCachedThreadPool());
	}
}
