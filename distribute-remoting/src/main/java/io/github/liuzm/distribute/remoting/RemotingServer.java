package io.github.liuzm.distribute.remoting;

import java.util.concurrent.ExecutorService;

import io.github.liuzm.distribute.remoting.common.Pair;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.exception.RemotingTooMuchRequestException;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.netty.channel.Channel;

public interface RemotingServer extends Service {
	
	
	public void registerDefaultProcessor(Processor processor, ExecutorService executor);
	
	public void registerProcessor(final int requestCode, final Processor processor,
            final ExecutorService executor);
	
	public int bindLocalListenerPort();
	
	
	public Command invokeSync(final Channel channel, final Command request,
            final long timeoutMillis) throws InterruptedException, RemotingSendRequestException,
            RemotingTimeoutException;
	
	public Pair<Processor, ExecutorService> getProcessorPair(final int requestCode);
	
	public void invokeAsync(final Channel channel, final Command request, final long timeoutMillis,
            final InvokeCallback invokeCallback) throws InterruptedException,
            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;
}
