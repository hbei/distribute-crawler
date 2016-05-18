package io.github.liuzm.crawler.store;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import io.github.liuzm.crawler.jobconf.StoreConfig;
import io.github.liuzm.crawler.page.ExtractedPage;
import io.github.liuzm.crawler.store.StoreResult.Status;
import io.github.liuzm.crawler.util.GeocodingUtil;
import io.github.liuzm.crawler.util.StrUtil;

public class MongodbStorage extends Storage {
	
	private static final Logger log = LoggerFactory.getLogger(MongodbStorage.class);
	
	private  StoreConfig config ;
	
	
	private Mongo mg = null;
	private DB db;
	private DBCollection collection;
	
	

	
	
	public MongodbStorage(StoreConfig config) {
		super();
		this.config = config;
		beforeStore();
	}
	

	@Override
	public synchronized StoreResult beforeStore() {
		try {
			int port = 0;
			if(StringUtils.isEmpty(config.getMongodbConfig().getPort())){
				port = 27017;
			}else {
				port = Integer.parseInt(config.getMongodbConfig().getPort());
			}
			 mg = new Mongo(config.getMongodbConfig().getHost(),port );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		db = mg.getDB(config.getMongodbConfig().getDatabase());
		collection = db.getCollection(config.getMongodbConfig().getCollection());
		
		return null;
	}

	/**
	 * 存储
	 */
	@Override
	public StoreResult onStore(ExtractedPage pageResult) {
		if(null==pageResult) return null;
		StoreResult sResult = new StoreResult();
		
		ExtractedPage  page = pageResult;
		DBObject obj = new BasicDBObject();
		List<DBObject> list = new ArrayList<DBObject>();
		Field[] fields= page.getClass().getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			 if (field.getType().getName().equals(java.lang.String.class.getName())) {
				 try {
					 obj.put(field.getName(), field.get(page));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			 }
		}
		Map<String, Object> resultMap = page.getMessages();
		if(resultMap==null||resultMap.size()==0)return null;
		for (String key : resultMap.keySet()) {
			Object rs = resultMap.get(key);
			if(rs instanceof Map){
				Map<String,Object> sub_map = (Map)rs;
				for (String key2 : sub_map.keySet()) {
					obj.put(key2, StrUtil.toBanjiao(""+sub_map.get(key2)));
				}
			}
			obj.put(key, StrUtil.toBanjiao(""+resultMap.get(key)));
		}
		try {
			
			collection.insert(obj);
			obj = null;
			sResult.setStatus(Status.success);
		} catch (Exception e) {
			System.out.println(page);
			log.error(config.getIndexName() + "\t向Mongodb插入数据时出错：", e);
			e.printStackTrace();
			sResult.setStatus(Status.failed);
			sResult.setMessge(e.getMessage());
		} 
		
		return sResult;
	}
	
	/*
	 * 批量存储
	 * @see com.esf.crawler.store.Storage#onStore(java.util.List)
	 */
	@Override
	public StoreResult onStore(List<ExtractedPage> pageList) {
		StoreResult sResult = new StoreResult();
		if(null==pageList||pageList.isEmpty()){
			sResult.setStatus(Status.ignored);
			sResult.setMessge(config.getIndexName() + "\t没有任务");
		}
		DBObject obj = new BasicDBObject();
		List<DBObject> list = new ArrayList<DBObject>();
		for (ExtractedPage page : pageList) {
			Field[] fields= page.getClass().getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				 if (field.getType().getName().equals(java.lang.String.class.getName())) {
					 try {
						 obj.put(field.getName(), field.get(page));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				 }
			}
			Map<String, Object> resultMap = page.getMessages();
			try {
				for (String key : resultMap.keySet()) {
					if("addr".equalsIgnoreCase(key)){
						String[] geo = GeocodingUtil.getGeocoding(page.getReserve(), ""+resultMap.get(key));
						
						if(null!=geo&&geo.length==2){
							obj.put("lat",geo[0]);
							obj.put("lng",geo[1]);
						}else{
							System.out.println("城市：" +page.getReserve()+"\t地区："+resultMap.get(key));
						}
						
					}
					obj.put(key, resultMap.get(key));
				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(obj);
			obj = null;
		}
		try {
			
			collection.insert(list);
			list = null;
			System.out.println(config.getIndexName() + "\t Mongodb完成"+ pageList.size()+ "个结果的存储");
			sResult.setStatus(Status.success);
		} catch (Exception e) {
			log.error(config.getIndexName() + "\t向Mongodb插入数据时出错：", e);
			e.printStackTrace();
			sResult.setStatus(Status.failed);
			sResult.setMessge(e.getMessage());
		} 
		
		return sResult;
	}

	@Override
	public StoreResult afterStore(ExtractedPage page) {
		// TODO Auto-generated method stub
		return null;
	}

}
