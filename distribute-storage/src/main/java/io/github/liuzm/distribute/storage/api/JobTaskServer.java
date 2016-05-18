package io.github.liuzm.distribute.storage.api;

/**
 * @author xh-liuzhimin
 *
 */
public interface JobTaskServer {
	
	public int insertJobTask(String domain);
	
	public int delJobtask(String domain);
	
	public int assginJob(String jobId,String clientId);
	
	public JobStats getClientJobStats(String clientId);

}
