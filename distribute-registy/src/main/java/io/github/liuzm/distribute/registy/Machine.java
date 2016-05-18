/**
 * 
 */
package io.github.liuzm.distribute.registy;

import io.github.liuzm.distribute.common.model.Node;

/**
 * @author xh-liuzhimin
 *
 */
public interface Machine {
	
	Node getNode();
	
	boolean isAvailable();
	
	void destroy();
}
