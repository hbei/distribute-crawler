package io.github.liuzm.crawler.fetcher;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IdleConnectionMonitorThread extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);
    
    private final PoolingHttpClientConnectionManager connMgr;
    
    private volatile boolean shutdown;
    
    public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager connMgr) {
    	super();
        this.connMgr = connMgr;
    }
    
    
    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    connMgr.closeExpiredConnections();
                    connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    logger.info("#getAvailable="+connMgr.getTotalStats().getAvailable());
                }
            }
        } catch (InterruptedException ex) {
            this.shutdown();
        }
    }
    
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
    
}
