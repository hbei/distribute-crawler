package io.github.liuzm.crawler.jobconf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenxinwen
 * @date 2014-7-22
 */
public class PropertyConfigurationHelper implements Serializable{
	
	private static final long serialVersionUID = 7599466817406649028L;
	private static final Logger log = LoggerFactory.getLogger(PropertyConfigurationHelper.class);
	
	private static PropertyConfigurationHelper instance = null;
	private Properties properties;
	
	/**
	 * 构造函数
	 */
	public PropertyConfigurationHelper(String configFilePath){
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(configFilePath));
		} catch (IOException e) {
			log.error("配置文件缺失 ["+configFilePath+"]");
			e.printStackTrace();
		}
	}
	/**
	 * 构造函数
	 */
	public PropertyConfigurationHelper(){
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(GlobalConstants.propertiyFilePath));
		} catch (IOException e) {
			log.error("配置文件缺失 ["+GlobalConstants.propertiyFilePath+"]");
			e.printStackTrace();
		}
	}
	
	
	public String getString(String propertyName,String defaultValue){
		return properties.getProperty(propertyName, defaultValue);
	}
	
	public int getInt(String propertyName,int defaultValue){
		return Integer.parseInt(properties.getProperty(propertyName, String.valueOf(defaultValue)));
	}
	
	public Object getObject(String propertyName){
		return properties.get(propertyName);
	}
	/**
	 * @desc 获取帮助类实例对象
	 */
	public static PropertyConfigurationHelper getInstance(){
		if(instance==null){
			instance = new PropertyConfigurationHelper();
		}
		return instance;
	}
	/**
	 * 根据路径返回配置文件
	 * 
	 * @param path
	 * @return
	 */
	public static final Properties getProperties(String path) {
		Properties properties = new Properties();
		try {
			InputStream inputStream = new FileInputStream(new File(path));
			properties.load(inputStream);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public void destroy(){
		
		properties.clear();
		properties = null;
	}
}
