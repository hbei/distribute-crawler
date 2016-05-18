/**
 * 
 */
package io.github.liuzm.distribute.server;

/**
 * 监听器接口
 * 
 * @author xh-liuzhimin
 *
 */
public interface Listener<T> {
	
	T executeListener();
}
