package io.github.liuzm.benchmark;

import org.junit.Test;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.zookeeper.ZkClient;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.server.AQServerServiceImpl;
import io.github.liuzm.distribute.server.AQServerInitializer;

public class SendCommandTest {
	
	private static final ZkClient zkClient = ZkClient.getClient(Config.ZKPath.CONNECT_STR);
	
	@Test
	public  void test() {
		
	}

}
