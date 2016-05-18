package io.github.liuzm.benchmark;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;

import io.github.liuzm.distribute.server.AQServerImpl;
import io.github.liuzm.distribute.common.rpc.RpcFramework;
import io.github.liuzm.distribute.server.AQServerFactory;

/**
 * @author xh-liuzhimin
 *
 */
public class AQServerTest {
	
	public static void main(String[] args) {
		final AQServerImpl server = AQServerFactory.buildServer();
		ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(new Runnable() {
			@Override
			public void run() {
				try {
					RpcFramework.export(server,7788);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Assert.assertNotNull(server);
	}

}
