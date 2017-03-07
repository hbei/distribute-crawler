package io.github.liuzm.benchmark;

import io.github.liuzm.distribute.client.AQClient;
import io.github.liuzm.distribute.remoting.exception.RemotingConnectException;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;

public class AQClientTest {
	
	public static void main(String[] args) {
		final AQClient client = new AQClient();
		client.start();
		
		try {
			client.send();
		} catch (RemotingConnectException e) {
			e.printStackTrace();
		} catch (RemotingSendRequestException e) {
			e.printStackTrace();
		} catch (RemotingTimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
