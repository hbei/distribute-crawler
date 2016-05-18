package io.github.liuzm.crawler.vo;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class StoreTemplate {
	
	//private static Element element = null;
	
	String type = "hbase";  
	String type_comment = "默认为hbase，可以选择mongodb";
	String threadNum = "2";
	String threadNum_comment = "所用线程数";
	String db = "ajk";
	String db_comment = "所用数据库";
	String collection = "villege_ajk";
	String collection_comment = "集合mongodb使用";
	String host = "127.0.0.1";
	String host_comment = "主机地址";
	String port = "27017";
	String port_comment = "端口";
	String table ="villege_ajk";
	String family = "ajk";
	String family_comment = "colum family";    
	
	
	public StoreTemplate() {
		
	}
	
	
	/**
	 * 创建StoreTemplate模板（Dom 元素），从StoreTemplate对象里
	 * @param saveStore
	 * @return  Store部分的Dom 元素
	 */
	public Element createTemplate(StoreTemplate savestore){
		StoreTemplate store = savestore;
		
		Element element = DocumentHelper.createElement("store");
		//type
		element.addComment(StringUtils.isNotBlank(store.getType_comment())?store.getType_comment():this.type_comment);
		element.addElement("type").addText(StringUtils.isNotBlank(store.getType())?store.getType():this.type);
		
		//threadNum
		element.addComment(StringUtils.isNotBlank(store.getThreadNum_comment())?store.getThreadNum_comment():this.threadNum_comment);
		element.addElement("threadNum").addText(StringUtils.isNotBlank(store.getThreadNum())?store.getThreadNum():this.threadNum);
		
		//db
	    element.addComment(StringUtils.isNotBlank(store.getDb_comment())?store.getDb_comment():this.db_comment);
	    Element e = element.addElement("db").addAttribute("name",StringUtils.isNotBlank(store.getDb())?store.getDb():this.db);
		
		e.addComment(StringUtils.isNotBlank(store.getCollection_comment())?store.getCollection_comment():this.collection_comment);
		e.addElement("collection").addText(StringUtils.isNotBlank(store.getCollection())?store.getCollection():this.collection);
		//host
		element.addComment(StringUtils.isNotBlank(store.getHost_comment())?store.getHost_comment():this.host_comment);
		element.addElement("host").addText(StringUtils.isNotBlank(store.getHost())?store.getHost():this.host);
		
		//port
		element.addComment(StringUtils.isNotBlank(store.getPort_comment())?store.getPort_comment():this.port_comment);
		element.addElement("port").addText(StringUtils.isNotBlank(store.getPort())?store.getPort():this.port);
		
		
		//table
		Element e1 = element.addElement("table").addAttribute("name",StringUtils.isNotBlank(store.getTable())?store.getTable():this.table);
		//family
		e1.addComment(StringUtils.isNotBlank(store.getFamily_comment())?store.getFamily_comment():this.family_comment);
		e1.addElement("family").addText(StringUtils.isNotBlank(store.getFamily())?store.getFamily():this.family);

				
		return element;
	}

	/**
	 * 初始化对象，从Dom元素里面
	 * @param e_job
	 */
	public void init(Element e_job) {
		Element element = e_job.element("store");
		this.type =  element.elementTextTrim("type");
		this.threadNum = element.elementText("threadNum");
		Element db = element.element("db");
		if(db != null){
			this.db = db.attribute("name").getValue();
			this.collection =db.elementText("collection");
		}
		Element table = element.element("table");
		if(table != null){
			this.table = table.attribute("name").getValue();
			this.family = table.elementText("family");
		}
		
		if(StringUtils.isNoneBlank(element.elementText("host"))&&StringUtils.isNoneBlank(element.elementText("port"))) {
			this.port = element.elementText("port");
			this.host = element.elementText("host");
		}
	}
			


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getType_comment() {
		return type_comment;
	}


	public void setType_comment(String type_comment) {
		this.type_comment = type_comment;
	}


	public String getThreadNum() {
		return threadNum;
	}


	public void setThreadNum(String threadNum) {
		this.threadNum = threadNum;
	}


	public String getThreadNum_comment() {
		return threadNum_comment;
	}


	public void setThreadNum_comment(String threadNum_comment) {
		this.threadNum_comment = threadNum_comment;
	}


	public String getDb() {
		return db;
	}


	public String getTable() {
		return table;
	}


	public void setTable(String table) {
		this.table = table;
	}


	public void setDb(String db) {
		this.db = db;
	}


	public String getDb_comment() {
		return db_comment;
	}


	public void setDb_comment(String db_comment) {
		this.db_comment = db_comment;
	}


	public String getCollection() {
		return collection;
	}


	public void setCollection(String collection) {
		this.collection = collection;
	}


	public String getCollection_comment() {
		return collection_comment;
	}


	public void setCollection_comment(String collection_comment) {
		this.collection_comment = collection_comment;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public String getHost_comment() {
		return host_comment;
	}


	public void setHost_comment(String host_comment) {
		this.host_comment = host_comment;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	public String getPort_comment() {
		return port_comment;
	}


	public void setPort_comment(String port_comment) {
		this.port_comment = port_comment;
	}


	public String getFamily() {
		return family;
	}


	public void setFamily(String family) {
		this.family = family;
	}


	public String getFamily_comment() {
		return family_comment;
	}


	public void setFamily_comment(String family_comment) {
		this.family_comment = family_comment;
	}

	

	
}
