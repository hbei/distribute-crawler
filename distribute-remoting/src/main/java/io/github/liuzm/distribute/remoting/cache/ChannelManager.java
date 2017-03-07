/**
 * 
 */
package io.github.liuzm.distribute.remoting.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

/**
 * 
 * 
 *
 */
public class ChannelManager {

	private static final Logger logger = LoggerFactory.getLogger(ChannelManager.class);

	public static final ConcurrentMap<String/** nodeId **/, Channel> channelTables = new ConcurrentHashMap<String, Channel>();
	private static final Lock lockChannelTables = new ReentrantLock();
	private static final long LockTimeoutMillis = 3000;
	
	public static Channel get(String nodeId) {
		if (nodeId != null) {
			return channelTables.get(nodeId);
		}
		return null;
	}

	public static void put(String nodeId, Channel channel) {

		try {
			if (channelTables.get(nodeId) != null) {
				// 判断一下该连接是否有效
			}

			if (lockChannelTables.tryLock(LockTimeoutMillis, TimeUnit.MILLISECONDS)) {
				channelTables.put(nodeId, channel);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("channel table error =" + nodeId);
		} finally {
			lockChannelTables.unlock();
		}
	}

	public static Channel disConnect(String nodeId) {
		try {
			final Channel channel = channelTables.get(nodeId);
			if (lockChannelTables.tryLock(LockTimeoutMillis, TimeUnit.MILLISECONDS)) {
				if (channel != null && channel.isActive()) {
					return channel;
				} else if (channel != null && !channel.isActive()) { // 该连接存在，并且连接不被占用
					channelTables.remove(nodeId);
					logger.info("channel table remove =" + nodeId);
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lockChannelTables.unlock();
		}
		return null;
	}

	public static boolean containsKey(String key) {
		return channelTables.containsKey(key);
	}

	public static long size() {
		return channelTables.size();
	}

	public static Map<String, Channel> map() {
		return new HashMap<String, Channel>(channelTables);
	}
}
