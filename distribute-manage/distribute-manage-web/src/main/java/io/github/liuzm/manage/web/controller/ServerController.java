/**
 * 
 */
package io.github.liuzm.manage.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.server.api.AQServerService;

/**
 * @author qydpc
 *
 */
@Controller
@RequestMapping("/server/")
public class ServerController {
	
	@Autowired
	private AQServerService aqServer;
	
	
	@RequestMapping("detailServer")
	@ResponseBody
	public Node detailServer(){
		return aqServer.getServerNodeDetail();
	}
	
	
	@RequestMapping("startCrawler/{nodeId}.htm")
	@ResponseBody
	public String startCrawler(@PathVariable("nodeId") String nodeId){
		try{
			aqServer.sendStartCrawler(nodeId);
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			return "error";
		}
	}
	
	
	@RequestMapping("getCrawlerStatus/{nodeId}.htm")
	@ResponseBody
	public String getCrawlerStatus(@PathVariable("nodeId") String nodeId){
		try{
			Command response = aqServer.getCrawlerStatusByClientNodeId(nodeId);
			return response.getExtFields().toString();
		}catch(Exception e){
			e.printStackTrace();
			return "error";
		}
	}
	

}
