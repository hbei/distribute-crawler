/**
 * 
 */
package io.github.liuzm.distribute.server.filter;

/**
 * 
 * @author lxyq
 *
 */
public interface Filter<T> {
	/**
	 * 
	 * 
	 * @param t
	 * @return
	 */
	public boolean filter(T t);
}
