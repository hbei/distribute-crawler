package io.github.liuzm.distribute.client;

import java.util.concurrent.Executors;

import io.github.liuzm.distribute.client.processor.ClientCtlProcessor;
import io.github.liuzm.distribute.client.processor.ClientTaskRecivedProcessor;
import io.github.liuzm.distribute.client.processor.ConnectAckProcessor;
import io.github.liuzm.distribute.client.processor.JobStatusProcessor;
import io.github.liuzm.distribute.common.factory.NodeFactory;
import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.registy.impl.DefaultRegistry;
import io.github.liuzm.distribute.remoting.netty.ClientConfig;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;

public class AQClientFactory {

	public static AQClient buildClient(){
		Node node = NodeFactory.buildNode();
		node.setType(1);
		final AQClient client = new AQClient(new ClientConfig(),node,new DefaultRegistry(node));
		client.start();
		
		client.registerProcessor(HeaderMessageCode.ACK_COMMAND, new ConnectAckProcessor(), Executors.newCachedThreadPool());
        client.registerProcessor(HeaderMessageCode.CLIENT_RESV_SSSD_COMMAND, new ClientCtlProcessor(), Executors.newCachedThreadPool());
        client.registerProcessor(HeaderMessageCode.CLIENT_JOB_STATUS, new JobStatusProcessor(), Executors.newCachedThreadPool());
        client.registerProcessor(HeaderMessageCode.CLIENT_RESV_TASK_COMMAND, new ClientTaskRecivedProcessor(), Executors.newCachedThreadPool());
		return client;
	}
}
