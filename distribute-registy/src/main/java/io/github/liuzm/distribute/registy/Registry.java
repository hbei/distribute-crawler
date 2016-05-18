/**
 * 
 */
package io.github.liuzm.distribute.registy;

import io.github.liuzm.distribute.common.model.Node;

/**
 * @author xh-liuzhimin
 *
 */
public interface Registry {
	/**
	 * 
	 * @param node
	 */
	Node register(Node node);
	
	// void subscribe(Node node, NotifyListener listener);
	
	// void unsubscribe(Node node, NotifyListener listener);
}
