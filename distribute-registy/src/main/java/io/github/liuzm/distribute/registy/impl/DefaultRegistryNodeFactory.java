/**
 * 
 */
package io.github.liuzm.distribute.registy.impl;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.registy.RegistryNode;
import io.github.liuzm.distribute.registy.support.AbstractRegistryNodeFactory;

/**
 * @author xh-liuzhimin
 *
 */
public class DefaultRegistryNodeFactory extends AbstractRegistryNodeFactory {

	@Override
	protected RegistryNode createRegistry(Node node) {
		return new DefaultRegistry(node);
	}

}
