package io.github.liuzm.distribute.server.api;

import java.util.List;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.remoting.protocol.Command;


public interface AQServerService {
	
	public Command sendStartCrawler(String nodeId);
	
	public Command sendStopCrawler(String nodeId);
	
	public Command sendSupendedCrawler(String nodeId);
	
	
	public Command sendTask(String nodeId,List<String> task);
	
	public Node getServerNodeDetail();
	
	/**
	 * client job task status
	 * 
	 * @param nodeId
	 */
	public Command getCrawlerStatusByClientNodeId(String nodeId);

	
}
