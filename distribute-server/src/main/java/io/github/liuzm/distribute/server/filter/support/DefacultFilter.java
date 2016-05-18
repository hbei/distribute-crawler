/**
 * 
 */
package io.github.liuzm.distribute.server.filter.support;

import io.github.liuzm.distribute.common.Message;
import io.github.liuzm.distribute.server.filter.Filter;

/**
 * @author lxyq
 *
 */
public class DefacultFilter implements Filter<Message> {

	@Override
	public boolean filter(Message t) {
		return false;
	}

}
