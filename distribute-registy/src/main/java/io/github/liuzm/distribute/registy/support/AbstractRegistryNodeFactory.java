/**
 * 
 */
package io.github.liuzm.distribute.registy.support;

import java.util.concurrent.locks.ReentrantLock;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.registy.RegistryNode;
import io.github.liuzm.distribute.registy.RegistryNodeFactory;

/**
 * @author xh-liuzhimin
 *
 */
public abstract class AbstractRegistryNodeFactory implements RegistryNodeFactory {
	
	private static final ReentrantLock LOCK = new ReentrantLock();
	
	protected RegistryNode registry;
	/**
	 * 
	 * @param node
	 * @return
	 */
	public RegistryNode getRegistryNode(Node node) {
        LOCK.lock();
        try {
            if (registry != null) {
                return registry;
            }
            registry = createRegistry(node);
            if (registry == null) {
                throw new IllegalStateException("Can not create registry " + node);
            }
            return registry;
        } finally {
            LOCK.unlock();
        }
    }

    protected abstract RegistryNode createRegistry(Node node);
}
