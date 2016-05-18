package io.github.liuzm.crawler.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrongUrlLog {
	
	private static final Logger log = LoggerFactory.getLogger(WrongUrlLog.class);
	
	private static String dir = new File("").getAbsolutePath();
	
	private static String lineSeq = System.getProperty("line.separator");
	
	public WrongUrlLog() {
		super();
		log.info("info未抓取的地址：" + dir);
	}
	
	public static void writeUrl(String fileName ,String url){
		File file = new File(dir+ File.separator + fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(file,true);
			fw.write(url);
			fw.write(lineSeq);
			log.info("爬取失败!在文件"+file.getAbsolutePath()+"写入地址："+ url);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
