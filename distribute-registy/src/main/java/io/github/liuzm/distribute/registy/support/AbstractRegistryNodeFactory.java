/**
 * 
 */
package io.github.liuzm.distribute.registy.support;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.registy.RegistryNode;
import io.github.liuzm.distribute.registy.RegistryNodeFactory;

/**
 * @author xh-liuzhimin
 *
 */
public abstract class AbstractRegistryNodeFactory implements RegistryNodeFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractRegistryNodeFactory.class);
	
	private static final Map<String, RegistryNode> REGISTRIES = new ConcurrentHashMap<String, RegistryNode>();
	
	private static final ReentrantLock LOCK = new ReentrantLock();
	/**
	 * 
	 * 
	 * @return
	 */
	public static Collection<RegistryNode> getRegistries() {
        return Collections.unmodifiableCollection(REGISTRIES.values());
    }
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public RegistryNode getRegistryNode(Node node) {
		logger.info(node.toString());
    	String key = node.getId();
        LOCK.lock();
        try {
            RegistryNode registry = REGISTRIES.get(key);
            if (registry != null) {
                return registry;
            }
            registry = createRegistry(node);
            if (registry == null) {
                throw new IllegalStateException("Can not create registry " + node);
            }
            REGISTRIES.put(key, registry);
            return registry;
        } finally {
            LOCK.unlock();
        }
    }

    protected abstract RegistryNode createRegistry(Node node);
}
