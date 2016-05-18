package io.github.liuzm.crawler.bootsStrap;

import java.util.Map;

import com.google.common.collect.Maps;

import io.github.liuzm.crawler.jobconf.Configuration;
import io.github.liuzm.crawler.vo.JobStatus;
import io.github.liuzm.crawler.vo.Status;

public class JobManager {

private static Map<String, JobStatus> jobs = Maps.newConcurrentMap();
	
	public static void addJob(String jobTag ,JobStatus jobStatus) {
		jobs.put(jobTag, jobStatus);
	}
	
	/**
	 * 删除任务
	 * @param jobTag
	 */
	public static void removeJob(String jobTag) {
		if(jobs.containsKey(jobTag)){
			jobs.remove(jobTag);
		};
	}
	
	/**
	 * 删除任务
	 * @param jobTag
	 */
	public static void saveJobStatus(String jobTag) {
	}
	
	public static Configuration getConfiguration(String jobTag){
		return jobs.get(jobTag).getConfiguration();
	}
	
	public static void setConfiguration(String jobTag,Configuration configuration){
		jobs.get(jobTag).setConfiguration(configuration);
	}
	
	
	public static  Map<String, JobStatus> getJobs(){
		return jobs;
	}
	
	public static  JobStatus getJob(String jobTag){
		return jobs.get(jobTag);
	}
	
	public static int getStatus(String jobTag){
		return getJob(jobTag).getStatus();
	}
	
	public static int setStatus(String jobTag,int status){
		getJob(jobTag).setStatus(status);
		return getJob(jobTag).getStatus();
	}
	
	public static boolean isStop(String jobTag){
		return getStatus(jobTag)==Status.STATUS_STOP;	
	}
	
	public static boolean isFinish(String jobTag){
		return getStatus(jobTag)==Status.STATUS_FINISH;	
	}
	
	public static boolean isRunning(String jobTag){
		return getStatus(jobTag)==Status.STATUS_RUNNING;	
	}
	
	
}
