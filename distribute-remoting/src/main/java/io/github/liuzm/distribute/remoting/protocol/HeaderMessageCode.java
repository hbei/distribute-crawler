package io.github.liuzm.distribute.remoting.protocol;

public class HeaderMessageCode {
	
	public static final int SYSTEM_ERROR = 0; // 系统错误
	
	public static final int ACK_COMMAND = 1; // 心跳消息,服务端每隔3000ms向客户端发送心跳,超时则
	
	public static final int ACK_COMMAND_CONNECT =2;
	public static final int ACK_COMMAND_DISCONNECT =3;
	
	public static final int SEND_MESSAGE = 10;
	public static final int CONSUMER_SEND_MSG_BACK = 11;
	
	
	// 200 -> 201
	public static final int CLIENT_RESV_SSSD_COMMAND = 200;// 客户端对服务端控制命令的响应
	public static final int SERVER_SSSD_COMMAND = 201;// 服务端接收客户端处理信息
	
	// head部分指令判断
	public static final int SSSD_COMMAND_START = 101;
	public static final int SSSD_COMMAND_STOP = 102;
	public static final int SSSD_COMMAND_SUPENDED = 103;
	public static final int SSSD_COMMAND_DESTORY = 104;
	
	
	// 300 -> 301
	public static final int CLIENT_RESV_TASK_COMMAND = 300;// 客户端对服务段任务分配的响应
	public static final int SERVER_TASK_COMMAND = 301;// 服务端接收客户端处理信息
	
	
	//400 
	public static final int CLIENT_JOB_STATUS = 400;
	public static final int SERVER_JOB_STATUS = 401;
}
