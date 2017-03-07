package io.github.liuzm.distribute.remoting.netty;

/**
 * @author xh-liuzhimin
 *
 */
public class ClientConfig {
	
	/**
     * Worker thread number
     */
    private int clientWorkerThreads = 4;
    private int clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();
    private long connectTimeoutMillis = 3000;
    private long channelNotActiveInterval = 1000 * 60;
    private int clientChannelMaxIdleTimeSeconds = 120;
    private int clientSocketSndBufSize = SystemConfig.SocketSndbufSize;
    private boolean clientPooledByteBufAllocatorEnable = false;
    
    
	/**
	 * @return the clientWorkerThreads
	 */
	public int getClientWorkerThreads() {
		return clientWorkerThreads;
	}
	/**
	 * @param clientWorkerThreads the clientWorkerThreads to set
	 */
	public void setClientWorkerThreads(int clientWorkerThreads) {
		this.clientWorkerThreads = clientWorkerThreads;
	}
	/**
	 * @return the clientCallbackExecutorThreads
	 */
	public int getClientCallbackExecutorThreads() {
		return clientCallbackExecutorThreads;
	}
	/**
	 * @param clientCallbackExecutorThreads the clientCallbackExecutorThreads to set
	 */
	public void setClientCallbackExecutorThreads(int clientCallbackExecutorThreads) {
		this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
	}
	/**
	 * @return the connectTimeoutMillis
	 */
	public long getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}
	/**
	 * @param connectTimeoutMillis the connectTimeoutMillis to set
	 */
	public void setConnectTimeoutMillis(long connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
	}
	/**
	 * @return the channelNotActiveInterval
	 */
	public long getChannelNotActiveInterval() {
		return channelNotActiveInterval;
	}
	/**
	 * @param channelNotActiveInterval the channelNotActiveInterval to set
	 */
	public void setChannelNotActiveInterval(long channelNotActiveInterval) {
		this.channelNotActiveInterval = channelNotActiveInterval;
	}
	/**
	 * @return the clientChannelMaxIdleTimeSeconds
	 */
	public int getClientChannelMaxIdleTimeSeconds() {
		return clientChannelMaxIdleTimeSeconds;
	}
	/**
	 * @param clientChannelMaxIdleTimeSeconds the clientChannelMaxIdleTimeSeconds to set
	 */
	public void setClientChannelMaxIdleTimeSeconds(int clientChannelMaxIdleTimeSeconds) {
		this.clientChannelMaxIdleTimeSeconds = clientChannelMaxIdleTimeSeconds;
	}
	/**
	 * @return the clientSocketSndBufSize
	 */
	public int getClientSocketSndBufSize() {
		return clientSocketSndBufSize;
	}
	/**
	 * @param clientSocketSndBufSize the clientSocketSndBufSize to set
	 */
	public void setClientSocketSndBufSize(int clientSocketSndBufSize) {
		this.clientSocketSndBufSize = clientSocketSndBufSize;
	}
	/**
	 * @return the clientPooledByteBufAllocatorEnable
	 */
	public boolean isClientPooledByteBufAllocatorEnable() {
		return clientPooledByteBufAllocatorEnable;
	}
	/**
	 * @param clientPooledByteBufAllocatorEnable the clientPooledByteBufAllocatorEnable to set
	 */
	public void setClientPooledByteBufAllocatorEnable(boolean clientPooledByteBufAllocatorEnable) {
		this.clientPooledByteBufAllocatorEnable = clientPooledByteBufAllocatorEnable;
	}
    
    

}
