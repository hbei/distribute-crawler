/**
 * 
 * copyright@ xinhe99.com 信和大金融
 */
package io.github.liuzm.distribute.server.task;

import io.github.liuzm.distribute.common.model.TaskInfo;

/**
 * 主要功能： 接收和分发任务。</br>
 * 
 * @author xh-liuzhimin
 *
 */
public class TaskManager implements TaskController{

	@Override
	public int sendTask(io.github.liuzm.distribute.remoting.protocol.Command c, TaskInfo info) {
		return 0;
	}


}
