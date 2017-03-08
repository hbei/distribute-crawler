package io.github.liuzm.distribute.client.processor;

import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.github.liuzm.crawler.bootsStrap.CrawlerStatus;
import io.github.liuzm.crawler.bootsStrap.JobManager;
import io.github.liuzm.crawler.pendingqueue.PendingManager;
import io.github.liuzm.distribute.client.protocol.JobCommandHeader;
import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.netty.channel.ChannelHandlerContext;

public class JobStatusProcessor implements Processor {

	@Override
	public Command process(ChannelHandlerContext ctx, Command c) throws Exception {
		
		return null;
	}

}
