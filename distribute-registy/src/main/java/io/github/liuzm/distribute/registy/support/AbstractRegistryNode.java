/**
 * 
 */
package io.github.liuzm.distribute.registy.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.common.util.ConcurrentHashSet;
import io.github.liuzm.distribute.common.util.NamedThreadFactory;
import io.github.liuzm.distribute.registy.NotifyListener;
import io.github.liuzm.distribute.registy.RegistryNode;
import io.netty.util.internal.StringUtil;

/**
 * @author xh-liuzhimin
 *
 */
public abstract class AbstractRegistryNode implements RegistryNode{
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractRegistryNode.class);
	
	private final ExecutorService registryCacheExecutor = Executors.newFixedThreadPool(1, new NamedThreadFactory("SaveRegistryNodeCache"));
	
	private Node registryNode;
	
	private final boolean syncSaveFile  = false; 
	
	private File file;
	
	private final AtomicLong lastCacheChangedtime = new AtomicLong();
	
	private final Set<Node> registered = new ConcurrentHashSet<Node>();

    private final ConcurrentMap<Node, Set<NotifyListener>> subscribed = new ConcurrentHashMap<Node, Set<NotifyListener>>();

    private final ConcurrentMap<Node, Map<String, List<Node>>> notified = new ConcurrentHashMap<Node, Map<String, List<Node>>>();
    
    private final Properties properties = new Properties();
    /**
     * 
     * @param node
     */
    public AbstractRegistryNode(Node node){
    	setNode(node);
    	
    	String filename = System.getProperty("user.home") + "/.distribute/distribute-registry-node" + node.getIpaddress() +"-"+ node.getType() + ".cache";
    	File file = null;
        if (!StringUtil.isNullOrEmpty(filename)) {
            file = new File(filename);
            if(! file.exists() && file.getParentFile() != null && ! file.getParentFile().exists()){
                if(! file.getParentFile().mkdirs()){
                    throw new IllegalArgumentException("Invalid registry store file " + file + ", cause: Failed to create directory " + file.getParentFile() + "!");
                }
            }
        }
        this.file = file;
        loadProperties();
       // notify(node.getBackupUrls());
    }
    
    protected void notify(List<Node> nodes) {
        if(nodes == null || nodes.isEmpty()) return;
        
        for (Map.Entry<Node, Set<NotifyListener>> entry : getSubscribed().entrySet()) {
        	Node node = entry.getKey();
            Set<NotifyListener> listeners = entry.getValue();
            if (listeners != null) {
                for (NotifyListener listener : listeners) {
                    try {
                        notify(node, listener, nodes);
                    } catch (Throwable t) {
                        logger.error("Failed to notify registry event, urls: " +  nodes + ", cause: " + t.getMessage(), t);
                    }
                }
            }
        }
    }

    protected void notify(Node node, NotifyListener listener, List<Node> nodes) {
        if (node == null) {
            throw new IllegalArgumentException("notify url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("notify listener == null");
        }
        
        if (logger.isInfoEnabled()) {
            logger.info("Notify urls for subscribe url " + node + ", urls: " + nodes);
        }
        Map<String, List<Node>> result = new HashMap<String, List<Node>>();
        for (Node u : nodes) {
        	String category = String.valueOf(u.getType());
        	List<Node> categoryList = result.get(category);
        	if (categoryList == null) {
        		categoryList = new ArrayList<Node>();
        		result.put(category, categoryList);
        	}
        	categoryList.add(u);
        }
        if (result.size() == 0) {
            return;
        }
        Map<String, List<Node>> categoryNotified = notified.get(node);
        if (categoryNotified == null) {
            notified.putIfAbsent(node, new ConcurrentHashMap<String, List<Node>>());
            categoryNotified = notified.get(node);
        }
        for (Map.Entry<String, List<Node>> entry : result.entrySet()) {
            String category = entry.getKey();
            List<Node> categoryList = entry.getValue();
            categoryNotified.put(category, categoryList);
            saveProperties(node);
            listener.notify(categoryList);
        }
    }

    public void subscribe(Node node, NotifyListener listener) {
        if (node == null) {
            throw new IllegalArgumentException("subscribe url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("subscribe listener == null");
        }
        if (logger.isInfoEnabled()){
            logger.info("Subscribe: " + node);
        }
        Set<NotifyListener> listeners = subscribed.get(node);
        if (listeners == null) {
            subscribed.putIfAbsent(node, new ConcurrentHashSet<NotifyListener>());
            listeners = subscribed.get(node);
        }
        listeners.add(listener);
    }
    
    public void unsubscribe(Node node, NotifyListener listener) {
        if (node == null) {
            throw new IllegalArgumentException("unsubscribe url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("unsubscribe listener == null");
        }
        if (logger.isInfoEnabled()){
            logger.info("Unsubscribe: " + node);
        }
        Set<NotifyListener> listeners = subscribed.get(node);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
    
    protected void recover() throws Exception {
        // register
        Set<Node> recoverRegistered = new HashSet<Node>(getRegistered());
        if (! recoverRegistered.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("Recover register url " + recoverRegistered);
            }
            for (Node node : recoverRegistered) {
                register(node);
            }
        }
        // subscribe
        Map<Node, Set<NotifyListener>> recoverSubscribed = new HashMap<Node, Set<NotifyListener>>(getSubscribed());
        if (! recoverSubscribed.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("Recover subscribe url " + recoverSubscribed.keySet());
            }
            for (Map.Entry<Node, Set<NotifyListener>> entry : recoverSubscribed.entrySet()) {
            	Node node = entry.getKey();
                for (NotifyListener listener : entry.getValue()) {
                    subscribe(node, listener);
                }
            }
        }
    }
    
    public void destroy() {
        if (logger.isInfoEnabled()){
            logger.info("Destroy registry node:" + getNode());
        }
        Set<Node> destroyRegistered = new HashSet<Node>(getRegistered());
        if (! destroyRegistered.isEmpty()) {
            for (Node node : new HashSet<Node>(getRegistered())) {
                try {
                    unregister(node);
                    if (logger.isInfoEnabled()) {
                        logger.info("Destroy unregister url " + node);
                    }
                } catch (Throwable t) {
                    logger.warn("Failed to unregister url " + node + " to registry " + getNode() + " on destroy, cause: " + t.getMessage(), t);
                }
            }
        }
        Map<Node, Set<NotifyListener>> destroySubscribed = new HashMap<Node, Set<NotifyListener>>(getSubscribed());
        if (! destroySubscribed.isEmpty()) {
            for (Map.Entry<Node, Set<NotifyListener>> entry : destroySubscribed.entrySet()) {
            	Node node = entry.getKey();
                for (NotifyListener listener : entry.getValue()) {
                    try {
                        unsubscribe(node, listener);
                        if (logger.isInfoEnabled()) {
                            logger.info("Destroy unsubscribe url " + node);
                        }
                    } catch (Throwable t) {
                        logger.warn("Failed to unsubscribe url " + node + " to registry " + getNode() + " on destroy, cause: " +t.getMessage(), t);
                    }
                }
            }
        }
    }
    
    public void unregister(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("unregister url == null");
        }
        if (logger.isInfoEnabled()){
            logger.info("Unregister: " + node);
        }
        registered.remove(node);
    }
    
    public Map<Node, Set<NotifyListener>> getSubscribed() {
        return subscribed;
    }

    public Map<Node, Map<String, List<Node>>> getNotified() {
        return notified;
    }
    
    public Node getNode(){
    	return registryNode;
    }
    
    public Set<Node> getRegistered() {
        return registered;
    }
    /**
     * 
     * @param node
     */
    protected void setNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("registryNode node == null");
        }
        this.registryNode = node;
    }
    
    private void saveProperties(Node node) {
        if (file == null) {
            return;
        }
        
        try {
            StringBuilder buf = new StringBuilder();
            Map<String, List<Node>> categoryNotified = notified.get(node);
            if (categoryNotified != null) {
                for (List<Node> us : categoryNotified.values()) {
                    for (Node u : us) {
                        if (buf.length() > 0) {
                            buf.append("---");
                        }
                        buf.append(u.toString());
                    }
                }
            }
            properties.setProperty(node.getId(), buf.toString());
            long version = lastCacheChangedtime.incrementAndGet();
            if (syncSaveFile) {
                doSaveProperties(version);
            } else {
                registryCacheExecutor.execute(new SaveProperties(version));
            }
        } catch (Throwable t) {
            logger.warn(t.getMessage(), t);
        }
    }
    /**
     * 
     */
    private void loadProperties() {
        if (file != null && file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                properties.load(in);
                if (logger.isInfoEnabled()) {
                    logger.info("Load registry store file " + file + ", data: " + properties);
                }
            } catch (Throwable e) {
                logger.warn("Failed to load registry store file " + file, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        }
    }
    
    private class SaveProperties implements Runnable{
        private long version;
        private SaveProperties(long version){
            this.version = version;
        }
        public void run() {
            doSaveProperties(version);
        }
    }
    
    
    public void doSaveProperties(long version) {
        if(version < lastCacheChangedtime.get()){
            return;
        }
        if (file == null) {
            return;
        }
        Properties newProperties = new Properties();
        
        InputStream in = null;
        try {
            if (file.exists()) {
                in = new FileInputStream(file);
                newProperties.load(in);
            }
        } catch (Throwable e) {
            logger.warn("Failed to load registry store file, cause: " + e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }     
        try {
			newProperties.putAll(properties);
            File lockfile = new File(file.getAbsolutePath() + ".lock");
            if (!lockfile.exists()) {
            	lockfile.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(lockfile, "rw");
            try {
                FileChannel channel = raf.getChannel();
                try {
                    FileLock lock = channel.tryLock();
                	if (lock == null) {
                        throw new IOException("Can not lock the registry cache file " + file.getAbsolutePath() + ", ignore and retry later, maybe multi java process use the file, please config: dubbo.registry.file=xxx.properties");
                    }
                    try {
                    	if (! file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream outputFile = new FileOutputStream(file);  
                        try {
                            newProperties.store(outputFile, "Dubbo Registry Cache");
                        } finally {
                        	outputFile.close();
                        }
                    } finally {
                    	lock.release();
                    }
                } finally {
                    channel.close();
                }
            } finally {
                raf.close();
            }
        } catch (Throwable e) {
            if (version < lastCacheChangedtime.get()) {
                return;
            } else {
                registryCacheExecutor.execute(new SaveProperties(lastCacheChangedtime.incrementAndGet()));
            }
            logger.warn("Failed to save registry store file, cause: " + e.getMessage(), e);
        }
    }
}
