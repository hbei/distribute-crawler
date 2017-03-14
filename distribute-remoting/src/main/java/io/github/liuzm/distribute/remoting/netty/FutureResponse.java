/**
 * 
 */
package io.github.liuzm.distribute.remoting.netty;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.liuzm.distribute.remoting.InvokeCallback;
import io.github.liuzm.distribute.remoting.common.SemaphoreReleaseOnlyOnce;
import io.github.liuzm.distribute.remoting.protocol.Command;

/**
 * 请求应答
 * 
 * @author xh-liuzhimin
 *
 */
public class FutureResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2562450290280989622L;

	
	private final long beginTimestamp = System.currentTimeMillis();
	private final InvokeCallback invokeCallback;
	private final long timeoutMillis;
	private final int opaque;
	// 保证信号量至多至少只被释放一次
	private final SemaphoreReleaseOnlyOnce once;
	// 保证回调的callback方法至多至少只被执行一次
	private final AtomicBoolean executeCallbackOnlyOnce = new AtomicBoolean(false);
	private final CountDownLatch countDownLatch = new CountDownLatch(1);
	
	private volatile Throwable cause;
	private volatile Command responseCommand;
	private volatile boolean sendRequestOK = true;
	
	
	public FutureResponse(int opaque, long timeoutMillis, InvokeCallback invokeCallback,
			SemaphoreReleaseOnlyOnce once) {
		this.opaque = opaque;
		this.timeoutMillis = timeoutMillis;
		this.invokeCallback = invokeCallback;
		this.once = once;
	}

	public void executeInvokeCallback() {
		if (invokeCallback != null) {
			if (this.executeCallbackOnlyOnce.compareAndSet(false, true)) {
				invokeCallback.doComplete(this);
			}
		}
	}

	public void release() {
		if (this.once != null) {
			this.once.release();
		}
	}

	public boolean isTimeout() {
		long diff = System.currentTimeMillis() - this.beginTimestamp;
		return diff > this.timeoutMillis;
	}

	public Command waitResponse(final long timeoutMillis) throws InterruptedException {
		this.countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);
		return this.responseCommand;
	}

	public void putResponse(final Command responseCommand) {
		this.responseCommand = responseCommand;
		this.countDownLatch.countDown();
	}

	public long getBeginTimestamp() {
		return beginTimestamp;
	}

	public boolean isSendRequestOK() {
		return sendRequestOK;
	}

	public void setSendRequestOK(boolean sendRequestOK) {
		this.sendRequestOK = sendRequestOK;
	}

	public long getTimeoutMillis() {
		return timeoutMillis;
	}

	public InvokeCallback getInvokeCallback() {
		return invokeCallback;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public Command getResponseCommand() {
		return responseCommand;
	}

	public void setResponseCommand(Command responseCommand) {
		this.responseCommand = responseCommand;
	}

	public int getOpaque() {
        return opaque;
    }

	@Override
	public String toString() {
		return "ResponseFuture [responseCommand=" + responseCommand + ", sendRequestOK=" + sendRequestOK + ", cause="
				+ cause + ", opaque=" + opaque + ", timeoutMillis=" + timeoutMillis + ", invokeCallback="
				+ invokeCallback + ", beginTimestamp=" + beginTimestamp + ", countDownLatch=" + countDownLatch + "]";
	}

}
