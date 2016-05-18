/**
 * 
 */
package io.github.liuzm.distribute.server;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.config.Config;
import io.github.liuzm.distribute.common.factory.NodeFactory;
import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.common.zookeeper.ZkClient;
import io.github.liuzm.distribute.registy.Registry;
import io.github.liuzm.distribute.registy.impl.DefaultRegistry;
import io.github.liuzm.distribute.remoting.cache.ChannelManager;
import io.github.liuzm.distribute.remoting.exception.RemotingSendRequestException;
import io.github.liuzm.distribute.remoting.exception.RemotingTimeoutException;
import io.github.liuzm.distribute.remoting.netty.NettyRemotingServer;
import io.github.liuzm.distribute.remoting.netty.ServerConfig;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.github.liuzm.distribute.remoting.protocol.header.BlankCommandHeader;
import io.github.liuzm.distribute.remoting.protocol.header.SSSDComandHeader;
import io.github.liuzm.distribute.server.api.AQServer;
import io.github.liuzm.distribute.server.processor.AckControllerProcessor;
import io.github.liuzm.distribute.server.processor.NodeControllerProcessor;
import io.github.liuzm.distribute.server.processor.TaskRecivedProcessor;
import io.netty.channel.Channel;

/**
 * @author lxyq
 *
 */
public class AQServerImpl extends NettyRemotingServer implements AQServer{

	private final static Logger logger = LoggerFactory.getLogger(AQServerImpl.class);

	private final Timer timer = new Timer("ServerWatcherService", true);
	
	private static final ZkClient zkClient;
	
	static {
		zkClient = ZkClient.getClient(Config.ZKPath.CONNECT_STR);
	}
	
	public AQServerImpl(ServerConfig nettyServerConfig,Node node,Registry register) {
		super(nettyServerConfig,node,register);
	}
	
	public static AQServerImpl aqserver = null ;
	
	public static AQServerImpl buildServer(){
		Node node = NodeFactory.buildNode();
		node.setType(0);
		if(aqserver == null){
			synchronized (AQServerFactory.class) {
				aqserver = new AQServerImpl(new ServerConfig(), node, new DefaultRegistry(node));
				aqserver.start();
				aqserver.registerDefaultProcessor(new AckControllerProcessor(), Executors.newCachedThreadPool());
				aqserver.registerProcessor(HeaderMessageCode.SERVER_SSSD_COMMAND, new NodeControllerProcessor(), Executors.newCachedThreadPool());
				aqserver.registerProcessor(HeaderMessageCode.SERVER_TASK_COMMAND, new TaskRecivedProcessor(), Executors.newCachedThreadPool());
				
				logger.info("serve register processor success !");
				return aqserver;
			}
		}
		return aqserver;
	}
	public void start() {
		super.start();
		init();
	}
	/**
	 * 
	 */
	private void init() {
		// 每隔1秒扫描下异步调用超时情况
        this.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
            public void run() {
                try {
					Map<String,Channel> m = ChannelManager.map();
					if(m != null && m.size() > 0){
						for(Entry<String,Channel> entry : m.entrySet()){
							if(zkClient.getCuratorClient().checkExists().forPath(
									Config.ZKPath.REGISTER_CLIENT_PATH+"/"+entry.getKey()) == null){
								ChannelManager.disConnect(entry.getKey());
							}
						}
					}
					 
                }catch (Exception e) {
                    logger.error("channel error exception", e);
                }
            }
        }, 1000 * 3, 1000);
	}
	
	@Override
	public Command sendStartCrawler(String nodeId){
		final Channel channel = ChannelManager.channelTables.get(nodeId);
		SSSDComandHeader requestHeader = new SSSDComandHeader(zkClient.getRandomClientNodeId());
        requestHeader.setSssd(HeaderMessageCode.SSSD_COMMAND_START);
        
        Command request = Command.createRequestCommand(HeaderMessageCode.CLIENT_RESV_SSSD_COMMAND, requestHeader);
		Command c = null;
		try {
			c = invokeSync(channel, request, 3000);
		} catch (RemotingSendRequestException e) {
			e.printStackTrace();
		} catch (RemotingTimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return c;
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
		return this.node;
	}
	
	@Override
	public Command getCrawlerStatusByClientNodeId(String nodeId) {
		final Channel channel = ChannelManager.channelTables.get(nodeId);
		Command cm = Command.createRequestCommand(HeaderMessageCode.CLIENT_JOB_STATUS, new BlankCommandHeader(nodeId));
		Command response = null;
		try {
			 response = invokeSync(channel, cm, 2000);
			System.out.println(response);
		} catch (RemotingSendRequestException e) {
			e.printStackTrace();
		} catch (RemotingTimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	
}
