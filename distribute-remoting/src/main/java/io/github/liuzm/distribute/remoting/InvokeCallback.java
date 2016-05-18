package io.github.liuzm.distribute.remoting;

import io.github.liuzm.distribute.remoting.netty.FutureResponse;

public interface InvokeCallback {
	public void doComplete(final FutureResponse responseFuture);
}
