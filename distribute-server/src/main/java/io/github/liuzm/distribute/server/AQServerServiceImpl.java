/**
 *
 */
package io.github.liuzm.distribute.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.server.api.AQServerService;

/**
 * <br>
 * 1. 启动服务端供worker连接以及数据传输</br>
 * <br>
 * 2. 启动dubbo服务供admin管理</br>
 *
 * @author liuzhimin
 */
public class AQServerServiceImpl implements AQServerService {

	private final static Logger logger = LoggerFactory.getLogger(AQServerServiceImpl.class);

	@Override
	public Command sendStartCrawler(String nodeId) {
		return null;
	}

	@Override
	public Command sendStopCrawler(String nodeId) {
		return null;
	}

	@Override
	public Command sendSupendedCrawler(String nodeId) {
		return null;
	}

	@Override
	public Command sendTask(String nodeId, List<String> task) {
		return null;
	}

	@Override
	public Node getServerNodeDetail() {
		return null;
	}

	@Override
	public Command getCrawlerStatusByClientNodeId(String nodeId) {
		return null;
	}

}
