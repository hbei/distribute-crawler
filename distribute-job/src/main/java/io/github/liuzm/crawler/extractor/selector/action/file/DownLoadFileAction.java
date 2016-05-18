package io.github.liuzm.crawler.extractor.selector.action.file;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.DownLoadException;
import io.github.liuzm.crawler.extractor.selector.action.FileSelectAction;
import io.github.liuzm.crawler.util.DateTimeUtil;
import io.github.liuzm.crawler.util.MD5Utils;
import io.github.liuzm.crawler.util.MultiThreadDownload;


/**
 * @author chenxinwen
 * @date 2014年8月3日
 * @desc 下载文件（必须同步进行，如果文件下载不完成，不能执行下面的不走）
 */
public class DownLoadFileAction extends FileSelectAction {
	/**
	 * 下载文件的存储路径
	 */
	File dir = null;
	/**
	 * 动态路径
	 */
	List<String> dynamicPath = null;
	/**
	 * 是否使用Url的md5值作为文件名。false则保留原文件名
	 */
	boolean md5File = true;
	/**
	 * 是否异步处理
	 */
	boolean asynchronous = true;
	/**
	 * 多线程下载时分块的大小
	 */
	long blockSize = 1024*1024L;
	/**
	 * 构造函数
	 * @param dir
	 * @param md5File
	 */
	public DownLoadFileAction(String dir, boolean md5File) {
		if(!dir.contains("{")){
			this.dir = new File(dir);
			if(!this.dir.exists()){
				this.dir.mkdir();
			}
		}else {
			this.dynamicPath = Lists.newArrayList(StringUtils.substringsBetween(dir, "{", "}"));
			String ss[] = StringUtils.split(dir, "/");
			for(String s:ss){
				if(!s.contains("{")){
					this.dynamicPath.add(0, s);
				}
			}
		}
		this.md5File = md5File;
	}
	/**
	 * 构造函数
	 * @param dir
	 * @param md5File
	 * @param asynchronous
	 */
	public DownLoadFileAction(String dir, boolean md5File,boolean asynchronous) {
		super();
		if(!dir.contains("{")){
			this.dir = new File(dir);
			if(!this.dir.exists()){
				this.dir.mkdir();
			}
		}else {
			this.dynamicPath = Lists.newArrayList(StringUtils.substringsBetween(dir, "{", "}"));
			String ss[] = StringUtils.split(dir, "/");
			for(String s:ss){
				if(!s.contains("{")){
					this.dynamicPath.add(0, s);
				}
			}
		}
		this.md5File = md5File;
		this.asynchronous = asynchronous;
	}

	/**
	 * 下载文件，返回文件路径
	 */
	@Override
	public String doAction(Map<String, Object> result,String remoteFile) throws DownLoadException {
		// 首先确认文件存放的本地路径
		String path = getDownDir(result);
		URL url;
		try {
			url = new URL(remoteFile);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new DownLoadException("下载异常："+e.getMessage());
		}
		String fileName = "";
		if(md5File){
			try {
				fileName = MD5Utils.createMD5(remoteFile);
				fileName = fileName +"."+ StringUtils.substringAfterLast(url.getPath(), ".");
			} catch (Exception e) {
			}
		}else {
			fileName = StringUtils.substringAfterLast(url.getPath(), "/");
		}
		MultiThreadDownload download = new MultiThreadDownload(1024*1024L);
		if(asynchronous){
			download.downFile(url, path, fileName, false);
		}else {
			download.downLoad(url, path, fileName);
		}
		// 返回路径
		return StringUtils.replace(path + fileName, "\\", "/");
	}
	
	/**
	 * 首先确认文件存放的本地路径
	 * @param map
	 * @return
	 */
	public String getDownDir(Map<String, Object> map){
		if(null!=map){
			if(null!=dir){
				return dir.getPath();
			}else if(null!=dynamicPath){
				StringBuilder sb = new StringBuilder();
				for(String p:dynamicPath){
					Object o = map.get(p);
					if(null!=o && o instanceof Date){
						sb.append(DateTimeUtil.getYearOfDate((Date)o)).append(File.separator);
					}else {
						sb.append(String.valueOf(map.get(p))).append(File.separator);
					}
				}
				return sb.toString();
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
//		System.out.println(StringUtils.substringAfterLast("sdfasd.asdfaf.gif", "."));
		DownLoadFileAction downLoadFileAction = new DownLoadFileAction("d:/multidown", true);
		try {
			downLoadFileAction.doAction(null,"http://zhangmenshiting.baidu.com/data2/music/65517089/307842151200128.mp3?xcode=53102624c6c63d206dbeaf3b8ae12d9080af3c8af038c7a6");
		} catch (DownLoadException e) {
			e.printStackTrace();
		}
	}
}
