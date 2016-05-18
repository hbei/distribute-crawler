package io.github.liuzm.benchmark;

import org.junit.Test;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.zookeeper.ZkClient;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.server.AQServerImpl;
import io.github.liuzm.distribute.server.AQServerFactory;

public class SendCommandTest {
	
	private static final ZkClient zkClient = ZkClient.getClient(Config.ZKPath.CONNECT_STR);
	private final AQServerImpl aQServer = AQServerFactory.buildServer();
	
	@Test
	public  void test() {
		try {
			Command response = aQServer.sendStartCrawler(zkClient.getRandomClientNodeId());
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
