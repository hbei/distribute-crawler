package io.github.liuzm.distribute.storage.api;

/**
 * @author xh-liuzhimin
 *
 */
public enum JobStats {
	
	JOB_STARTED(0,"STARTED"),
	JOB_FINISHED(1,"FINISHED"),
	JOB_SUPENDED(2,"SUPENDED"),
	JOB_NULL(3,"NULL"),
	JOB_RUNNING(4,"RUNNING");
	
	private int id;
	private String desc;
	
	private JobStats(int id,String desc){
		this.id = id ;
		this.desc = desc;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
