/**
 * 
 */
package io.github.liuzm.benchmark;

import org.junit.Assert;
import org.junit.Test;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.zookeeper.ZkClient;
import io.github.liuzm.distribute.remoting.cache.ChannelManager;

/**
 * @author xh-liuzhimin
 *
 */
public class RegisterChannelTest {
	
	private static final ZkClient zkClient = ZkClient.getClient(Config.ZKPath.CONNECT_STR);
	
    @Test
	public  void test() {
		Assert.assertNotNull(ChannelManager.get(zkClient.getRandomClientNodeId()));
	}

}
