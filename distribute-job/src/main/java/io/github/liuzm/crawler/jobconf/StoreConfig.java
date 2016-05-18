
package io.github.liuzm.crawler.jobconf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.crawler.exception.ConfigurationException;
import io.github.liuzm.crawler.util.EncryptUtils;
import io.github.liuzm.crawler.util.MD5Utils;

public class StoreConfig extends Configuration {
	
	private static final Logger log = LoggerFactory.getLogger(StoreConfig.class);
	
	private String type = "default";
	private IDPolicy id = IDPolicy.auto;
	private String policyRef = "";
	private int threadNum = 2;
	private String pluginClass = null;
	
	private HBaseConfig hconfig = null;
	
	private MongodbConfig mongodbConfig = null;
	
	private ElasticsearchConfig elasticsearchConfig = null ;
	
	private LocalFileConfig localFileConfig = null;
	
	public StoreConfig() {
	}
	
	public StoreConfig loadConfig(Document confDoc){
		Document doc = confDoc;
		super.setJobName(doc.select("job").attr("name"));
		super.setIndexName(doc.select("job").attr("indexName"));
		Elements e = doc.select("store");
		this.type = e.select("type").text();
		if(StringUtils.isNotBlank(e.select("threadNum").text())){
			this.threadNum = Integer.parseInt(e.select("threadNum").text());
		}
		
		if(this.type.equalsIgnoreCase("hbase")){
			String tName = e.select("table").first().attr("name");
			String fName = e.select("family").first().text();
			this.hconfig = new HBaseConfig(tName, fName);
		}else if(this.type.equalsIgnoreCase("mongodb")){
			String dbName = e.select("db").first().attr("name");
			String collection = e.select("collection").first().text();
			String port = e.select("port").text();
			String host = e.select("host").text();
			this.mongodbConfig = new MongodbConfig(dbName, collection,host,port);
		}else if(this.type.equalsIgnoreCase("es")){
			String url = e.select("url").text();
			String idPolicy = e.select("idPolicy").text();
			String clusterName = e.select("clusterName").text();
			String indexType = e.select("indexType").text();
			
			if(StringUtils.isNotBlank(idPolicy)){
				id = EnumUtils.getEnum(IDPolicy.class, idPolicy);
				if(!IDPolicy.auto .equals(id)){
					String pref = e.select("ref").text();
					if(StringUtils.isNotBlank(pref)){
						this.policyRef = pref;
					}
					if(StringUtils.isBlank(this.policyRef)){
						try {
							throw new ConfigurationException("指定了ID生成策略但未指定参考");
						} catch (Exception e2) {
							e2.printStackTrace();
							log.error(e2.getMessage());
						}
					}
				}
			}
			this.elasticsearchConfig = new ElasticsearchConfig(url,clusterName,indexType);
		}else if(this.type.equalsIgnoreCase("local")){
			String dir = e.select("dir").text();
			this.localFileConfig = new LocalFileConfig(dir);
		}
		
		return this;
	}
	/**
	 * 如果不是自动生成ID则调用此方法生成ID
	 * @return
	 */
	public String genId(HashMap<String, Object> data){
		switch (id) {
		case auto:
			return null;
		case md5:
			return MD5Utils.createMD5((String) data.get(policyRef));
		case base64:
			return EncryptUtils.encodeBase64((String) data.get(policyRef));
		case urlencode:
			try {
				return URLEncoder.encode((String)data.get(policyRef), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		default:
			return null;
		}
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public String getPluginClass() {
		return pluginClass;
	}

	public void setPluginClass(String pluginClass) {
		this.pluginClass = pluginClass;
	}
	
	public HBaseConfig getHconfig() {
		return hconfig;
	}

	public void setHconfig(HBaseConfig hconfig) {
		this.hconfig = hconfig;
	}
	

	

	public MongodbConfig getMongodbConfig() {
		return mongodbConfig;
	}

	public void setMongodbConfig(MongodbConfig mongodbConfig) {
		this.mongodbConfig = mongodbConfig;
	}

	@Override
	public String toString() {
		return "StoreConfig [type=" + type + ", threadNum=" + threadNum
				+ ", pluginClass=" + pluginClass + ", hconfig=" + hconfig + "]";
	}

	
	
	public class HBaseConfig{
		private String tableName ;
		private String familyName ;
		
		public HBaseConfig() {
			super();
		}
		public HBaseConfig(String tableName, String familyName) {
			super();
			this.tableName = tableName;
			this.familyName = familyName;
		}
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public String getFamilyName() {
			return familyName;
		}
		public void setFamilyName(String familyName) {
			this.familyName = familyName;
		}
		@Override
		public String toString() {
			return "HBaseConfig [tableName=" + tableName + ", familyName="
					+ familyName + "]";
		}
	}
	
	public class MongodbConfig{
		private String database ;
		private String collection ;
		private String host;
		private String port;
		
		public MongodbConfig() {
			super();
		}
		public MongodbConfig(String database, String collection,String host,String port) {
			super();
			this.database = database;
			this.collection = collection;
			this.host = host;
			this.port = port;
		}
		
		public String getDatabase() {
			return database;
		}
		public void setDatabase(String database) {
			this.database = database;
		}
		public String getCollection() {
			return collection;
		}
		public void setCollection(String collection) {
			this.collection = collection;
		}
		
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		@Override
		public String toString() {
			return "MongodbConfig [database=" + database + ", collection="
					+ collection + "]";
		}
	}
	
	public class ElasticsearchConfig{
		
		private String clusterName;
		
		private String url;
		
		private String indexType;
		
		public ElasticsearchConfig(String url,String clusterName,String indexType){
			this.url = url;
			this.clusterName = clusterName;
			this.indexType = indexType;
		}
		
		@Override
		public String toString() {
			return "ElasticsearchConfig [url=" + url + " : clusterName"+clusterName +" ]";
		}
		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		/**
		 * @return the clusterName
		 */
		public String getClusterName() {
			return clusterName;
		}

		/**
		 * @param clusterName the clusterName to set
		 */
		public void setClusterName(String clusterName) {
			this.clusterName = clusterName;
		}

		/**
		 * @return the indexType
		 */
		public String getIndexType() {
			return indexType;
		}

		/**
		 * @param indexType the indexType to set
		 */
		public void setIndexType(String indexType) {
			this.indexType = indexType;
		}
		
		
	}
	
	public class LocalFileConfig{
		
		private String dir;
		
		public LocalFileConfig(String dir){
			this.dir = dir;
		}

		/**
		 * @return the dir
		 */
		public String getDir() {
			return dir;
		}

		/**
		 * @param dir the dir to set
		 */
		public void setDir(String dir) {
			this.dir = dir;
		}
		
		
	}
	/**
	 * @return the elasticsearchConfig
	 */
	public ElasticsearchConfig getElasticsearchConfig() {
		return elasticsearchConfig;
	}

	/**
	 * @param elasticsearchConfig the elasticsearchConfig to set
	 */
	public void setElasticsearchConfig(ElasticsearchConfig elasticsearchConfig) {
		this.elasticsearchConfig = elasticsearchConfig;
	}
	
	/**
	 * @return the localFileConfig
	 */
	public LocalFileConfig getLocalFileConfig() {
		return localFileConfig;
	}

	/**
	 * @param localFileConfig the localFileConfig to set
	 */
	public void setLocalFileConfig(LocalFileConfig localFileConfig) {
		this.localFileConfig = localFileConfig;
	}

	/**
	 * 如果不是自动生成ID则调用此方法生成ID
	 * @return
	 */
	public String genId(Map<String, Object> data){
		switch (id) {
		case auto:
			return null;
		case md5:
			return MD5Utils.createMD5((String) data.get(policyRef));
		case base64:
			return EncryptUtils.encodeBase64((String) data.get(policyRef));
		case urlencode:
			try {
				return URLEncoder.encode((String)data.get(policyRef), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		default:
			return null;
		}
	}
	public enum IDPolicy {
		auto,md5,base64,urlencode
	}
}
