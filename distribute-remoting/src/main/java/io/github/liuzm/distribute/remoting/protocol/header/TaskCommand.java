/**
 * 
 */
package io.github.liuzm.distribute.remoting.protocol.header;

import java.util.List;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.CommandBody;

/**
 * @author xh-liuzhimin
 *
 */
public class TaskCommand implements CommandBody {
	
	private String nodeId;
	
	public TaskCommand(){
		
	}
	
	public TaskCommand(String nodeId) {
		this.nodeId = nodeId;
	}

	private String taskId;
	
	private List<Object> task;
	
	@Override
	public void checkFields() throws RemotingCommandException {

	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the task
	 */
	public List<Object> getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(List<Object> task) {
		this.task = task;
	}

	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}
