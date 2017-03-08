package io.github.liuzm.distribute.remoting.protocol;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.github.liuzm.distribute.remoting.netty.RemotingCommandType;

public class Command implements Serializable, Callable<Command> {

	private static final long serialVersionUID = -6529193002034683936L;

	private static final int RPC_TYPE = 0; // 0, REQUEST_COMMAND 1,
											// RESPONSE_COMMAND
	private static final int RPC_ONEWAY = 1; // 0, RPC 1, Oneway

	private static AtomicInteger RequestId = new AtomicInteger(0);
	public int VERSION = 0;
	private int opaque = RequestId.getAndIncrement();
	private int flag = 0;
	private int code;
	private String remark;
	private transient CommandBody commandBody;

	public Command() {

	}

	public static Command createRequestCommand(int code, CommandBody commandBody) {
		Command cmd = new Command();
		cmd.setCode(code);
		cmd.setCommandBody(commandBody);
		return cmd;
	}

	public static Command createResponseCommand(Class<? extends CommandBody> classBody) {
		Command cmd = createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR, "not set any response code",
				classBody);
		return cmd;
	}

	public static Command createResponseCommand(int code, String remark) {
		return createResponseCommand(code, remark, null);
	}

	/**
	 * 只有通信层内部会调用，业务不会调用
	 */
	public static Command createResponseCommand(int code, String remark, Class<? extends CommandBody> classHeader) {
		Command cmd = new Command();
		cmd.markResponseType();
		cmd.setCode(code);
		cmd.setRemark(remark);

		if (classHeader != null) {
			try {
				CommandBody objectHeader = classHeader.newInstance(); // 弱类型只能用无参构造效率低
				cmd.commandBody = objectHeader;
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				return null;
			}
		}

		return cmd;
	}

	public boolean isRequest() {
		return flag == 0;
	}

	public ByteBuffer encode() {
		
		int length = 4;

		byte[] headerData = JSON.toJSONString(this).getBytes();
		length += headerData.length;

		byte[] bodyData = null;
		byte[] bodyClass = null;

		if (commandBody != null) {
			bodyData = JSON.toJSONString(commandBody).getBytes();
			length += bodyData.length;

			bodyClass = commandBody.getClass().getName().getBytes();
			length += bodyClass.length;

			length += 4;
		}

		ByteBuffer result = ByteBuffer.allocate(4 + length);

		// length
		result.putInt(length);
		// header length
		result.putInt(headerData.length);
		// header data
		result.put(headerData);

		if (bodyData != null) {
			// body length
			result.putInt(bodyData.length);
			// body data
			result.put(bodyData);
			// body class
			result.put(bodyClass);
		}

		result.flip();
		return result;
	}

	public Command decode(final byte[] buffer) throws ClassNotFoundException {
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		return decode(byteBuffer);
	}

	public static Command decode(final ByteBuffer byteBuffer) throws ClassNotFoundException {
		int length = byteBuffer.limit(); // 已写入数据的总量。数据的总长度,同时position下移,读取到整个写入buffer长度,前4个字节
		int headerLength = byteBuffer.getInt();// 读下面int 4个字节
		byte[] headerData = new byte[headerLength];
		byteBuffer.get(headerData);

		Command cmd = JSONObject.parseObject(headerData, Command.class);

		int remaining = length - 4 - headerLength;

		if (remaining > 0) {

			int bodyLength = byteBuffer.getInt();
			int bodyClassLength = remaining - 4 - bodyLength;

			if (bodyLength > 0) {

				byte[] bodyData = new byte[bodyLength];
				byteBuffer.get(bodyData);

				byte[] bodyClassData = new byte[bodyClassLength];
				byteBuffer.get(bodyClassData);

				cmd.setCommandBody(
						(CommandBody) JSONObject.parseObject(bodyData, Class.forName(new String(bodyClassData))));
			}
		}
		return cmd;
	}

	public void markResponseType() {
		int bits = 1 << RPC_TYPE;
		this.flag |= bits;
	}

	public boolean isResponseType() {
		int bits = 1 << RPC_TYPE;
		return (this.flag & bits) == bits;
	}

	public RemotingCommandType getType() {
		if (this.isResponseType()) {
			return RemotingCommandType.RESPONSE_COMMAND;
		}

		return RemotingCommandType.REQUEST_COMMAND;
	}

	public void markOnewayRPC() {
		int bits = 1 << RPC_ONEWAY;
		this.flag |= bits;
	}

	public boolean isOnewayRPC() {
		int bits = 1 << RPC_ONEWAY;
		return (this.flag & bits) == bits;
	}

	/**
	 * @return the opaque
	 */
	public int getOpaque() {
		return opaque;
	}

	/**
	 * @param opaque
	 *            the opaque to set
	 */
	public void setOpaque(int opaque) {
		this.opaque = opaque;
	}

	/**
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public Command call() throws Exception {
		return this;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the commandBody
	 */
	public CommandBody getCommandBody() {
		return commandBody;
	}

	/**
	 * @param commandBody
	 *            the commandBody to set
	 */
	public void setCommandBody(CommandBody commandBody) {
		this.commandBody = commandBody;
	}

	@Override
	public String toString() {
		return "Command [opaque=" + opaque + ", " + "flag=" + flag + ", " + "code=" + code + ", " + "commandBody="
				+ JSON.toJSONString(commandBody) + ", " + "remark=" + remark + "]";
	}

}
