package io.github.liuzm.distribute.remoting.protocol;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSON;

import io.github.liuzm.distribute.remoting.netty.RemotingCommandType;

public class Command implements Serializable,Callable<Command>{
	
	private static final long serialVersionUID = -6529193002034683936L;
	
	public static String VERSION = "remoting-command-version";
	private static AtomicInteger RequestId = new AtomicInteger(0);
	private static final int RPC_TYPE = 0; // 0, REQUEST_COMMAND 1, RESPONSE_COMMAND
    
    private static final int RPC_ONEWAY = 1; //0, RPC 1, Oneway
    
	private int opaque = RequestId.getAndIncrement();
	private int flag = 0; 
	private int code;
	private String remark;
	private HashMap<String, String> extFields;
	private transient CommandHeader customHeader;
	private transient byte[] body;
	
	public Command(){
		
	}
	
	public static Command createRequestCommand(int code, CommandHeader customHeader) {
        Command cmd = new Command();
        cmd.setCode(code);
        cmd.customHeader = customHeader;
        return cmd;
    }
	
	public static Command createResponseCommand(Class<? extends CommandHeader> classHeader) {
		Command cmd =
                createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR, "not set any response code",
                    classHeader);

        return cmd;
    }


    public static Command createResponseCommand(int code, String remark) {
        return createResponseCommand(code, remark, null);
    }


    /**
     * 只有通信层内部会调用，业务不会调用
     */
    public static Command createResponseCommand(int code, String remark,
            Class<? extends CommandHeader> classHeader) {
    	Command cmd = new Command();
        cmd.markResponseType();
        cmd.setCode(code);
        cmd.setRemark(remark);

        if (classHeader != null) {
            try {
                CommandHeader objectHeader = classHeader.newInstance(); // 弱类型只能用无参构造效率低
                cmd.customHeader = objectHeader;
            }
            catch (InstantiationException e) {
            	e.printStackTrace();
                return null;
            }
            catch (IllegalAccessException e) {
                return null;
            }
        }

        return cmd;
    }
	
	
	public boolean isRequest(){
		return flag == 0;
	}
	
	public ByteBuffer encodeHeader() {
        return encodeHeader(this.body != null ? this.body.length : 0);
    }
	
	private byte[] buildHeader() {
        this.makeCustomHeaderToNet();
        return JSON.toJSONString(this).getBytes();
    }
	
	// 反射获取协议头属性
	public void makeCustomHeaderToNet() {
        if (this.customHeader != null) {
            
            if (null == this.extFields) {
                this.extFields = new HashMap<String, String>();
            }
            
            Class<?> clazz = this.customHeader.getClass();
            
            for(; clazz != Object.class ; clazz = clazz.getSuperclass()){
            	Field[] fields = clazz.getDeclaredFields();
            	for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        String name = field.getName();
                        if (!name.startsWith("this")) {
                            Object value = null;
                            try {
                                field.setAccessible(true);
                                value = field.get(this.customHeader);
                            }catch (IllegalArgumentException e){
                            	
                            }catch (IllegalAccessException e) {
                            	
                            }

                            if (value != null) {
                                this.extFields.put(name, value.toString());
                            }
                        }
                    }
                }
            }
            
        }
    }
	/**
     * 只打包Header，body部分独立传输
     */
    public ByteBuffer encodeHeader(final int bodyLength) {
        // 1> header length size
        int length = 4;

        // 2> header data length
        byte[] headerData = this.buildHeader();
        length += headerData.length;

        // 3> body data length
        length += bodyLength;

        ByteBuffer result = ByteBuffer.allocate(4 + length - bodyLength);

        // length
        result.putInt(length);

        // header length
        result.putInt(headerData.length);

        // header data
        result.put(headerData);

        result.flip();

        return result;
    }
	public ByteBuffer encode(){
		 // 1> header length size
        int length = 4;

        // 2> header data length
        byte[] headerData = this.buildHeader();
        length += headerData.length;

        // 3> body data length
        if (this.body != null) {
            length += body.length;
        }

        ByteBuffer result = ByteBuffer.allocate(4 + length);

        // length
        result.putInt(length);

        // header length
        result.putInt(headerData.length);

        // header data
        result.put(headerData);

        // body data;
        if (this.body != null) {
            result.put(this.body);
        }

        result.flip();

        return result;
	}
	
	public static Command decode(final byte[] array) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        return decode(byteBuffer);
    }
	
	public static Command decode(final ByteBuffer byteBuffer) {
        int length = byteBuffer.getInt();

        byte[] data = new byte[length];
        byteBuffer.get(data);

        Command cmd = JSON.parseObject(data, Command.class);
        cmd.body = data;

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
	 * @param opaque the opaque to set
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
	 * @param flag the flag to set
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
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return the body
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body) {
		this.body = body;
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
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the extFields
	 */
	public HashMap<String, String> getExtFields() {
		return extFields;
	}

	/**
	 * @param extFields the extFields to set
	 */
	public void setExtFields(HashMap<String, String> extFields) {
		this.extFields = extFields;
	}
	
	public CommandHeader readCustomHeader() {
	        return customHeader;
    }

    public void writeCustomHeader(CommandHeader customHeader) {
        this.customHeader = customHeader;
    }
	@Override
    public String toString() {
        return "Command [code=" + code 
                + ", opaque=" + opaque
                + ", flag(B)=" + Integer.toBinaryString(flag)
                + ", remark=" + remark
                + ", RPC_TYPE=" + RPC_TYPE
                + ", extFields=" + extFields + "]";
    }
	
}
