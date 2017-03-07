package io.github.liuzm.benchmark;

import org.junit.Assert;

import io.github.liuzm.distribute.server.BootStrapAQServer;

/**
 * @author xh-liuzhimin
 *
 */
public class AQServerTest {
	
	public static void main(String[] args) {
		BootStrapAQServer server = new BootStrapAQServer();
		Assert.assertNotNull(server);
		
		server.start();
	}

}
