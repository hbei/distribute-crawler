package io.github.liuzm.distribute.server;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.factory.NodeFactory;
import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.registy.impl.DefaultRegistry;
import io.github.liuzm.distribute.remoting.netty.ServerConfig;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.server.processor.AckControllerProcessor;
import io.github.liuzm.distribute.server.processor.NodeControllerProcessor;
import io.github.liuzm.distribute.server.processor.TaskRecivedProcessor;

public class AQServerFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(AQServerFactory.class);
	
	public static AQServerImpl aqserver = null ;
	
	public static AQServerImpl buildServer(){
		Node node = NodeFactory.buildNode();
		node.setType(0);
		if(aqserver == null){
			synchronized (AQServerFactory.class) {
				aqserver = new AQServerImpl(new ServerConfig(), node, new DefaultRegistry(node));
				aqserver.start();
				aqserver.registerDefaultProcessor(new AckControllerProcessor(), Executors.newCachedThreadPool());
				aqserver.registerProcessor(HeaderMessageCode.SERVER_SSSD_COMMAND, new NodeControllerProcessor(), Executors.newCachedThreadPool());
				aqserver.registerProcessor(HeaderMessageCode.SERVER_TASK_COMMAND, new TaskRecivedProcessor(), Executors.newCachedThreadPool());
				
				logger.info("serve register processor success !");
				return aqserver;
			}
		}
		return aqserver;
	}
	
}
