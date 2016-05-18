/**
 * 
 */
package io.github.liuzm.distribute.common.zookeeper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.config.Config;

/**
 * 
 * @author xh-liuzhimin
 *
 */
public class ZkClient {

	private final Logger logger = LoggerFactory.getLogger(ZkClient.class);
	
	private CuratorFramework zkClient;
	private static final ConcurrentMap<String,ZkClient> clientCache = new ConcurrentHashMap();
	
	public ZkClient(CuratorFramework zkClient){
        this.zkClient = zkClient;
        this.zkClient.start();
    }
	
	public static final synchronized  ZkClient getClient(String connectionStr){
		ZkClient zkClient = clientCache.get(connectionStr);
		if(zkClient == null){
			final CuratorFramework curatorFramework = newZkClient(connectionStr);
            zkClient = new ZkClient(curatorFramework);
            final ZkClient previous = clientCache.put(connectionStr, zkClient);
            if(previous!=null){//如果包含了,则关闭连接
                previous.getCuratorClient().close();
            }
		}

		return zkClient;
	}
	 /**
     * 为节点添加单个watch，如果已经存在，则不添加
     *
     * @throws Exception
     */
    public synchronized void childrenSingleWatch(String path, CuratorWatcher watcher){
        if(!checkChildrenWatcher(path)){
            try{
                zkClient.getChildren().usingWatcher(watcher).forPath(path);
            }catch(Exception e){
                if(!checkChildrenWatcher(path)){
                    try{
                        zkClient.getChildren().usingWatcher(watcher).forPath(path);
                    }catch(Exception e1){
                        logger.error("ex", e);
                    }
                }
            }
        }else{
            logger.warn("path="+path+" hased children watch,add watch canel");
        }
    }
    
    /**
     * 获取节点下的子节点
     * 
     * @param path
     * @return
     */
    public List<String> getChildByPath(String path){
    	List<String> list = null ;
    	try {
    		list = zkClient.getChildren().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return list;
    }
    /**
     * 检查该节点是否存在 child 节点
     */
    public boolean checkChildrenWatcher(String path){
        try{
            //ZooKeeper zoo = zkClient.getZookeeperClient().getZooKeeper();
            //List<String> dataWatches = zoo.getChildWatches();
            //return dataWatches.contains(path);
        }catch(Exception e){
            logger.error("exceprion", e);
        }
        return false;
    }
    /**
     * 获取第一个client nodeid
     * @return
     */
    public String getRandomClientNodeId(){
    	try{
    		List<String> clients = getChildByPath(Config.ZKPath.REGISTER_CLIENT_PATH);
    		return clients.get(0);
    	}catch(Exception e){
    		logger.error("random get node not extists exceprion", e);
    	}
    	return null;
    }
	/**
     * 获取CuratorFramework
     * @return
     */
    public  final CuratorFramework getCuratorClient(){
        return zkClient;
    }
    /**
     * 根据链接地址来生成一个新的链接对象 </br>
     * 
     * xh-liuzhimin
     * @param connectionString
     * @return
     */
	private static final CuratorFramework newZkClient(String connectionString){
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        final CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
        return curatorFramework;
    }
}
