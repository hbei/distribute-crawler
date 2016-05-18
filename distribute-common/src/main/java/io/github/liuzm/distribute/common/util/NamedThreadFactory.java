/**
 * 
 */
package io.github.liuzm.distribute.common.util;

import java.util.concurrent.ThreadFactory;

/**
 * @author xh-liuzhimin
 *
 */
public final class NamedThreadFactory implements ThreadFactory {
	
	private final String name;

	public NamedThreadFactory(String name) {
		this.name = name;
	}

	public Thread newThread(Runnable r) {
		return new Thread(r, name);
	}
}