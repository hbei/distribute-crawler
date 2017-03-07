package io.github.liuzm.benchmark;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.rpc.RpcFramework;
import io.github.liuzm.distribute.common.zookeeper.ZkClient;
import io.github.liuzm.distribute.server.AQServerServiceImpl;

public class RPCCallTest {
private static final ZkClient zkClient;
	
	static {
		zkClient = ZkClient.getClient(Config.ZKPath.CONNECT_STR);
	}
	
	public static void main(String[] args) {
		try {
			AQServerServiceImpl aqserver = RpcFramework.refer(AQServerServiceImpl.class, "127.0.0.1", 7788);
			System.out.println(aqserver);
			aqserver.sendStartCrawler(zkClient.getRandomClientNodeId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
