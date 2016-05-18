package io.github.liuzm.crawler.store;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import io.github.liuzm.crawler.page.ExtractedPage;
import io.github.liuzm.crawler.store.StoreResult.Status;


/**
 * @author 
 * @date 2013-6-29
 *  本地文件系统存储实现
 */
public class LocalFileStorage extends Storage {
	
	private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);
	
	public String storeDir;
	String jobName;
	File storeFile;
	
	public LocalFileStorage(String storeDir,String jobName){
		this.storeDir = storeDir;
		this.jobName = jobName;
		File dir = new File(this.storeDir + File.separator + this.jobName);
		if(!dir.exists()){
			dir.mkdirs();
			storeFile = new File(dir.getAbsolutePath(), jobName+".txt");
		}
		if(null==storeFile){
			storeFile = new File(dir.getAbsolutePath(), jobName+".txt");
		}
	}
	
	@Override
	public StoreResult beforeStore() {
		return null;
	}

	@Override
	public StoreResult onStore(ExtractedPage page) {
		StoreResult storeResult = new StoreResult();
		try {
			FileUtils.writeStringToFile(storeFile, JSONObject.toJSONString(page)+"\n\n","utf-8",true);
			storeResult.setStatus(Status.success);
		} catch (IOException e) {
			 log.error(e.getMessage());
			 storeResult.setMessge(e.getMessage());
			 storeResult.setStatus(Status.failed);
		}
		return storeResult;
	}

	@Override
	public StoreResult afterStore(ExtractedPage page) {
		return null;
	}

	@Override
	public StoreResult onStore(List<ExtractedPage> page) {
		return null;
	}
	
}

