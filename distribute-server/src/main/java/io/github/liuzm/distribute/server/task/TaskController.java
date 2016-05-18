/**
 * 
 */
package io.github.liuzm.distribute.server.task;

import io.github.liuzm.distribute.common.model.TaskInfo;
import io.github.liuzm.distribute.remoting.protocol.Command;

/**
 * @author xh-liuzhimin
 *
 */
public interface TaskController {
	public int sendTask(Command c,TaskInfo info);
}
