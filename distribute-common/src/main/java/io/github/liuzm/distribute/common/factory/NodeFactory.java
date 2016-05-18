/**
 * 
 */
package io.github.liuzm.distribute.common.factory;

import io.github.liuzm.distribute.common.model.Node;

/**
 * @author xh-liuzhimin
 *
 */
public class NodeFactory {
	
	/**
	 * 返回当前机器的信息
	 * 
	 * @return
	 */
	public static Node buildNode(){
		Node node = new Node();
		return node;
	}

}
