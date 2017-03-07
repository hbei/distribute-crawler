package io.github.liuzm.distribute.registy.impl;

import java.net.SocketException;
import java.util.UUID;

import com.alibaba.fastjson.JSON;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.common.util.LocalIPUtil;
import io.github.liuzm.distribute.common.util.ZKUtil;
import io.github.liuzm.distribute.registy.support.AbstractRegistryNode;

public class DefaultRegistry extends AbstractRegistryNode{
	
	
	public DefaultRegistry(Node node){
		super(node);
	}
	
	@Override
	public Node register(Node node) {
		
		UUID id = UUID.randomUUID();
		String Mid = id.toString();
		try {
			node = this.buildNode(node,Mid);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if(node.getType()  == 0){
			if(!ZKUtil.exists(Config.ZKPath.REGISTER_SERVER_PATH)){
				ZKUtil.createPath(Config.ZKPath.REGISTER_SERVER_PATH);
			}
			if(!ZKUtil.exists(Config.ZKPath.REGISTER_SERVER_PATH+"/"+Mid)){
				String path = Config.ZKPath.REGISTER_SERVER_PATH+"/"+Mid;
				ZKUtil.createTempPath(path);
				ZKUtil.createOrUpdateDataByPath(path,JSON.toJSONBytes(node));
			}
		}else{
			if(!ZKUtil.exists(Config.ZKPath.REGISTER_CLIENT_PATH)){
				ZKUtil.createPath(Config.ZKPath.REGISTER_CLIENT_PATH);
			}
			if(!ZKUtil.exists(Config.ZKPath.REGISTER_CLIENT_PATH+"/"+Mid)){
				String path = Config.ZKPath.REGISTER_CLIENT_PATH ;
				ZKUtil.createTempPath(path+"/"+Mid);
				ZKUtil.createOrUpdateDataByPath(path+"/"+Mid,JSON.toJSONBytes(node));
			}
		}
		super.registryNode = node;
		return node;
	}

	@Override
	public boolean isAvailable() {
		return false;
	}
	
	/**
	 * store node info
	 * @return
	 * @throws SocketException 
	 */
	private Node buildNode(Node node,String id) throws SocketException{
		node.setId(id);
		node.setIpaddress(LocalIPUtil.getIpAddress());
		node.setPort(node.getPort());
		return node;
	}
}
