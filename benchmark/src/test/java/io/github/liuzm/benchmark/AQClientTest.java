package io.github.liuzm.benchmark;

import org.junit.Assert;

import io.github.liuzm.distribute.client.AQClient;
import io.github.liuzm.distribute.client.AQClientFactory;

public class AQClientTest {
	
	public static void main(String[] args) {
		final AQClient client = AQClientFactory.buildClient();
		Assert.assertNotNull(client);
	}
}
