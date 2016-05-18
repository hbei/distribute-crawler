package io.github.liuzm.distribute.remoting;

import java.util.concurrent.ExecutorService;

import io.github.liuzm.distribute.remoting.exception.RemotingConnectException;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.exception.RemotingTooMuchRequestException;
import io.github.liuzm.distribute.remoting.protocol.Command;

public interface RemotingClient extends Service {
	
	public Command invokeSync(final String addr, final Command request,
            final long timeoutMillis) throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException;
	
	public void invokeAsync(final String addr, final Command request, final long timeoutMillis,
	            final InvokeCallback invokeCallback) throws InterruptedException, RemotingConnectException,
	            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;
	
	public void registerProcessor(final int requestCode, final Processor processor,
	            final ExecutorService executor);
	
	public boolean isChannelWriteable(final String addr);
}
