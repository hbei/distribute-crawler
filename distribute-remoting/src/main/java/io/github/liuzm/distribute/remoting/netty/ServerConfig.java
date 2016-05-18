package io.github.liuzm.distribute.remoting.netty;

/**
 * @author liuzhmin
 *
 */
public class ServerConfig {
	
	private int listenPort = 8888;
    private int serverWorkerThreads = 8;
    private int serverCallbackExecutorThreads = 0;
    private int serverSelectorThreads = 3;
    private int serverOnewaySemaphoreValue = 256;
    private int serverAsyncSemaphoreValue = 64;
    private int serverChannelMaxIdleTimeSeconds = 120;

    private int serverSocketSndBufSize = SystemConfig.SocketSndbufSize;

	/**
	 * @return the listenPort
	 */
	public int getListenPort() {
		return listenPort;
	}

	/**
	 * @param listenPort the listenPort to set
	 */
	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	/**
	 * @return the serverWorkerThreads
	 */
	public int getServerWorkerThreads() {
		return serverWorkerThreads;
	}

	/**
	 * @param serverWorkerThreads the serverWorkerThreads to set
	 */
	public void setServerWorkerThreads(int serverWorkerThreads) {
		this.serverWorkerThreads = serverWorkerThreads;
	}

	/**
	 * @return the serverCallbackExecutorThreads
	 */
	public int getServerCallbackExecutorThreads() {
		return serverCallbackExecutorThreads;
	}

	/**
	 * @param serverCallbackExecutorThreads the serverCallbackExecutorThreads to set
	 */
	public void setServerCallbackExecutorThreads(int serverCallbackExecutorThreads) {
		this.serverCallbackExecutorThreads = serverCallbackExecutorThreads;
	}

	/**
	 * @return the serverSelectorThreads
	 */
	public int getServerSelectorThreads() {
		return serverSelectorThreads;
	}

	/**
	 * @param serverSelectorThreads the serverSelectorThreads to set
	 */
	public void setServerSelectorThreads(int serverSelectorThreads) {
		this.serverSelectorThreads = serverSelectorThreads;
	}

	/**
	 * @return the serverOnewaySemaphoreValue
	 */
	public int getServerOnewaySemaphoreValue() {
		return serverOnewaySemaphoreValue;
	}

	/**
	 * @param serverOnewaySemaphoreValue the serverOnewaySemaphoreValue to set
	 */
	public void setServerOnewaySemaphoreValue(int serverOnewaySemaphoreValue) {
		this.serverOnewaySemaphoreValue = serverOnewaySemaphoreValue;
	}

	/**
	 * @return the serverAsyncSemaphoreValue
	 */
	public int getServerAsyncSemaphoreValue() {
		return serverAsyncSemaphoreValue;
	}

	/**
	 * @param serverAsyncSemaphoreValue the serverAsyncSemaphoreValue to set
	 */
	public void setServerAsyncSemaphoreValue(int serverAsyncSemaphoreValue) {
		this.serverAsyncSemaphoreValue = serverAsyncSemaphoreValue;
	}

	/**
	 * @return the serverChannelMaxIdleTimeSeconds
	 */
	public int getServerChannelMaxIdleTimeSeconds() {
		return serverChannelMaxIdleTimeSeconds;
	}

	/**
	 * @param serverChannelMaxIdleTimeSeconds the serverChannelMaxIdleTimeSeconds to set
	 */
	public void setServerChannelMaxIdleTimeSeconds(int serverChannelMaxIdleTimeSeconds) {
		this.serverChannelMaxIdleTimeSeconds = serverChannelMaxIdleTimeSeconds;
	}

	/**
	 * @return the serverSocketSndBufSize
	 */
	public int getServerSocketSndBufSize() {
		return serverSocketSndBufSize;
	}

	/**
	 * @param serverSocketSndBufSize the serverSocketSndBufSize to set
	 */
	public void setServerSocketSndBufSize(int serverSocketSndBufSize) {
		this.serverSocketSndBufSize = serverSocketSndBufSize;
	}
    
    
    
    
}
