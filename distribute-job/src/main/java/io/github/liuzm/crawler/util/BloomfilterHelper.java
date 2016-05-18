package io.github.liuzm.crawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import io.github.liuzm.crawler.jobconf.GlobalConstants;
import io.github.liuzm.crawler.jobconf.PropertyConfigurationHelper;

/**
 * @author 
 * @date 2014-8-30
 * Bloomfilter的帮助类
 */
public class BloomfilterHelper implements Serializable{
	
	private static final long serialVersionUID = -160403070863080075L;

	private BloomFilter<String> bf = null;
	
	private static BloomfilterHelper instance = null;
	
	private BloomfilterHelper(){
		init();
	};
	
	public static BloomfilterHelper getInstance(){
		if(null==instance)
			instance = new BloomfilterHelper();
		return instance;
	}
	
	/**
	 * @desc 初始化队列
	 */
	private void init() {
		File file = new File(new PropertyConfigurationHelper(GlobalConstants.propertiyFilePath)
				.getString("status.save.path", "status")
				+ File.separator
				+ "filter.good");
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				instance = (BloomfilterHelper) ois.readObject();
				ois.close();
				fis.close();
				bf = instance.bf;
				System.out.println("recovery Bloomfilter...");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} 
		if(null==bf){
			bf = new BloomFilter<String>(0.001, 1000000);
		}
	}
	
	/**
	 * @param s
	 * @return
	 * @desc 检测是否存在该Url
	 */
	public synchronized boolean exist(String url){
		if(StringUtils.isBlank(url))
			return true;
		return bf.containsOradd(url.getBytes());
	}
	/**
	 * add
	 * @param url
	 */
	public void add(String url){
		bf.add(url);
	}
	
	/**
	 * @param args
	 * @desc 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
