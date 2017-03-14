package io.github.liuzm.distribute.client;

import io.github.liuzm.Job;

/**
 * 协调器，负责client和job间的数据交互
 * 
 * @author xh-liuzhimin
 *
 */
public interface CoordinatorCenter {
	
	boolean startJob(Job job);

}
