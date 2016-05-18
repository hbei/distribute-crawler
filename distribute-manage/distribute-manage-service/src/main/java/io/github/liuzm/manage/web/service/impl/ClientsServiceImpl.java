/**
 * 
 */
package io.github.liuzm.manage.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.common.util.ZKUtil;
import io.github.liuzm.distribute.common.zookeeper.ZkClient;
import io.github.liuzm.manage.api.ClientService;

/**
 * @author xh-liuzhimin
 *
 */
public class ClientsServiceImpl implements ClientService {
	
	private static final ZkClient zkClient = ZkClient.getClient(Config.ZKPath.CONNECT_STR);
	
	@Override
	public List<Node> getClients() {
		
		List<String> zkclietns = zkClient.getChildByPath(Config.ZKPath.REGISTER_CLIENT_PATH);
		
		Node node;
		List<Node> clients = new ArrayList<Node>();
		for(String zkc : zkclietns){
			node = new Node();
			try {
				node = (Node) JSONObject.parseObject((ZKUtil.getPathData(Config.ZKPath.REGISTER_CLIENT_PATH+"/"+zkc)), Node.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			clients.add(node);
		}
		return clients;
	}

}
