package io.github.liuzm.crawler.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobInfo {

	private int id;
	private String jobId;
	private String jobName;
	private byte[] configure;
	private Date startTime;
	/*private String startTime_str;*/
	private Date endTime;
	/*private String endTime_str;*/
	private String status_str;
	private int status = 1;
	private String info;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public byte[] getConfigure() {
		return configure;
	}
	public void setConfigure(byte[] configure) {
		this.configure = configure;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
		
	public void setStatus_str(String status_str) {
		this.status_str = status_str;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
		setStatus_str(this.status);
	}
	
	public String getStatus_str() {
		return status_str;
	}
	
	
	public String getStartTime_str(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getStartTime());
	}
	
	public String getEndTime_str(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getEndTime());
	}
	
	
	public void setStatus_str(int status) {
		switch (this.status) {
		case Status.STATUS_T: this.status_str = "模板";			
			break;
		case Status.STATUS_INIT: this.status_str = "初始化/编辑状态";			
			break;
		case Status.STATUS_RUNNING: this.status_str = "运行中";			
			break;
		case Status.STATUS_STOP: this.status_str = "停止";			
			break;
		case Status.STATUS_FAILED: this.status_str = "失败";			
			break;
		case Status.STATUS_FINISH: this.status_str = "已完成";			
			break;

		default:this.status_str = "未知";
			break;
		}
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public JobInfo(int id, String jobId, String jobName, byte[] configure,
			Date startTime, Date endTime, int status, String info) {
		super();
		this.id = id;
		this.jobId = jobId;
		this.jobName = jobName;
		this.configure = configure;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.info = info;
	}
	public JobInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "JobInfo [id=" + id + ", jobId=" + jobId + ", jobName="
				+ jobName + ", configure=" + (null!=configure)
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", status_str=" + status_str + ", status=" + status
				+ ", info=" + info + "]";
	}
	
	
	
}
