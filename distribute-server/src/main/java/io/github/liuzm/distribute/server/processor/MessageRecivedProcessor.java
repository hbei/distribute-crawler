package io.github.liuzm.distribute.server.processor;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.github.liuzm.distribute.common.util.NamedThreadFactory;
import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.remoting.protocol.header.MessageCommand;
import io.github.liuzm.distribute.server.disruptor.Message;
import io.github.liuzm.distribute.server.disruptor.MessageEventFactory;
import io.github.liuzm.distribute.server.disruptor.MessageEventProducer;
import io.github.liuzm.distribute.server.disruptor.MessageHolder;
import io.github.liuzm.distribute.server.disruptor.StoreMessageEventHandle;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by xh-liuzhimin on 2017/2/8.
 */
public class MessageRecivedProcessor implements Processor {
	
	private MessageEventProducer producer;
	private Disruptor<MessageHolder> messagenDisruptor;
	private Message s;
    
    @SuppressWarnings("unchecked")
	public MessageRecivedProcessor(){
    	messagenDisruptor = new Disruptor<MessageHolder>(new MessageEventFactory(),
                1024,
                new NamedThreadFactory("Storage-writer"),
                ProducerType.MULTI,
                new TimeoutBlockingWaitStrategy(5000, TimeUnit.MILLISECONDS));
    	messagenDisruptor.handleEventsWith(new StoreMessageEventHandle());
    	messagenDisruptor.start();
    	
    	producer = new MessageEventProducer(messagenDisruptor.getRingBuffer());
    }
    
    @Override
    public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
        // 如果接收到的是采集数据，丢给储存线程做储存
    	if(c.getCode() == HeaderMessageCode.MESSAGE_COMMAND){
    		if(c.getCommandBody() instanceof MessageCommand){
    			MessageCommand cc = (MessageCommand)c.getCommandBody();
    			s = new Message(cc.getNodeId(),cc.getBody());
        		try{
        			producer.produce(s);
        		}catch(Exception e){
        			
        		}finally{
        			
        		}
    		}
    		
    	}
        return Command.createResponseCommand(HeaderMessageCode.CONSUMER_SEND_MSG_BACK, "i am a client I have recived your message !!!");
    }


}
