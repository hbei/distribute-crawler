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
		final Command response = Command.createResponseCommand(HeaderMessageCode.SERVER_JOB_STATUS, "server query crawler status", JobCommandHeader.class);
		JobCommandHeader header =  new JobCommandHeader(c.getExtFields().get("nodeId"));
		Collection<String> js = JobManager.getJobs().keySet();
		for(String j:js){
			String job = JobManager.getJob(j).getConfiguration().getJobName();
			Map<String, Long> urlstatus = PendingManager.getPendingUlr(job).pendingStatus();
			Map<String, Long> pagestatus = PendingManager.getPendingPages(job).pendingStatus();
			Map<String, Long> storestatus = PendingManager.getPendingStore(job).pendingStatus();
			
			header.setJson(urlstatus.toString());
		}
		response.writeCustomHeader(header);
		response.setBody(JSON.toJSONString(response).getBytes());
		
		return response;
	}

}
