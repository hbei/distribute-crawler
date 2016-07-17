package io.github.liuzm.crawler.pendingqueue.redis;

import java.io.Serializable;
import java.util.LinkedList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public abstract class AbsRedisPendingQueue<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2681027374438572285L;
	
	private LinkedList<T> Queue = null;
	
	private static Jedis jedis;
	
	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(300);
		config.setMaxIdle(100);
		config.setMaxWaitMillis(2000);
		config.setTestOnBorrow(true);
		
		JedisPool jedisPool = new JedisPool(config,"127.0.0.1",6379,3000);
		jedis=jedisPool.getResource();
		
	}
	/**
	 * 
	 * 1. url  2.page 3. store 4. area
	 * 
	 */
	private String type;
	
	private double count = 0;
	private double success = 0;
	private double failure = 0;
	private double ignored = 0;
	
	
	public AbsRedisPendingQueue(String queueType){
		this.type = queueType;
		
		if(Queue == null){
			Queue = new LinkedList<T>();
		}
	}
	
	public boolean addElement(T t){
		if(t != null){
		}
		return true;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the count
	 */
	public double getCount() {
		return count;
	}


	/**
	 * @param count the count to set
	 */
	public void setCount(double count) {
		this.count = count;
	}


	/**
	 * @return the success
	 */
	public double getSuccess() {
		return success;
	}


	/**
	 * @param success the success to set
	 */
	public void setSuccess(double success) {
		this.success = success;
	}


	/**
	 * @return the failure
	 */
	public double getFailure() {
		return failure;
	}


	/**
	 * @param failure the failure to set
	 */
	public void setFailure(double failure) {
		this.failure = failure;
	}


	/**
	 * @return the ignored
	 */
	public double getIgnored() {
		return ignored;
	}


	/**
	 * @param ignored the ignored to set
	 */
	public void setIgnored(double ignored) {
		this.ignored = ignored;
	}

}
