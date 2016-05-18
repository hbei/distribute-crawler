package io.github.liuzm.schedule;

import io.github.liuzm.Job;

public class QuartzSchedule implements Job{
	
	private static QuartzSchedule quartz;
	
	@Override
	public String name() {
		return "quartzJob";
	}

	public Object getInstance() {
		if(quartz == null){
			synchronized(QuartzSchedule.class){
				if(quartz == null){
					quartz = new QuartzSchedule();
				}
			}
		}
		return quartz;
	}

}
