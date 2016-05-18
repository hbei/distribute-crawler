package io.github.liuzm.crawler.extractor.selector.action;

import java.util.Map;

import io.github.liuzm.crawler.exception.DownLoadException;


/**
 * @author chenxin.wen
 * @date 2014年10月20日
 * @desc FileSelectAction接口
 */
public abstract class FileSelectAction implements SelectorAction {
	/**
	 * 返回文件下载后的本地路径
	 * @param remoteFile
	 * @return
	 * @throws DownLoadException
	 */
	public abstract String doAction(Map<String, Object> result,String remoteFile) throws DownLoadException;
}
