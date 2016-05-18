/**
 * 
 */
package io.github.liuzm.distribute.registy;

import java.util.List;

import io.github.liuzm.distribute.common.model.Node;

/**
 * @author xh-liuzhimin
 *
 */
public interface NotifyListener {
	/**
	 * 
	 * @param nodes
	 */
	void notify(List<Node> nodes);
}
