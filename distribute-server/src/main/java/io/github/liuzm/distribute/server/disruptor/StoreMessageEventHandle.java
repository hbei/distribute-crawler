package io.github.liuzm.distribute.server.disruptor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

public class StoreMessageEventHandle implements EventHandler<MessageHolder>{
	
	private static final Logger logger = LoggerFactory.getLogger(StoreMessageEventHandle.class);
	
	private List<Message> messageList;
	private final int bufferSize = 10; // 每10条刷盘或者存储一次
	
	public StoreMessageEventHandle(){
		messageList = new ArrayList<>();
	}

	@Override
	public void onEvent(MessageHolder event, long sequence, boolean endOfBatch) throws Exception {
		try{
			messageList.add(event.get());
			if(endOfBatch || messageList.size() == bufferSize){
				try{
					for(Message m : messageList ){
						logger.info("接收到来自NODEID为："+m.getNodeId()+" 的数据:"+new String(m.getMessageBody()));
					}
				}catch(Exception e){
					logger.error("刷盘储存异常,s%",e);
				}finally{
					messageList.clear();
				}
			}
			
		}catch(Exception e){
			logger.error("EventHandle 异常,s%",e);
		}finally {
			event.clear();
		}
	}
	
}
