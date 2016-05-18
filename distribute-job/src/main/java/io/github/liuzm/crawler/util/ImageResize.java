package io.github.liuzm.crawler.util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;

import io.github.liuzm.crawler.jobconf.PropertyConfigurationHelper;
/**
 * @author chenxin.wen
 * @date 2013年10月22日
 * @desc 图片压缩
 */
public class ImageResize {
	private static final String resize_error_log_file = new PropertyConfigurationHelper().getString("status.save.path.resize.log", "status/reszie-error.log");
	
	/**
	 * 异步等比压缩图像
	 * @param file
	 * @param width
	 * @param quality
	 * @param delOld
	 */
	public void resizeAsynchronous(final File file,final int width,final float quality,final boolean delOld){
		ImageResizePool.getInstance().submit(new Callable<Object>() {
			@Override
			public Boolean call() throws Exception {
				return resize(file, width, quality, delOld);
			}
		});
	}
	/**
	 * 异步固定高宽压缩图像
	 * @param file
	 * @param width
	 * @param height
	 * @param delOld
	 */
	public void resizeAsynchronous(final File file,final int width,final int height,final boolean delOld){
		ImageResizePool.getInstance().submit(new Callable<Object>() {
			@Override
			public Boolean call() throws Exception {
				return resize(file, width, height, delOld);
			}
		});
	}
	/**
	 * 等比例压缩图像，压缩图像将覆盖旧图像
	 * @param file
	 * @param width
	 * @param quality
	 * @param delOld
	 * @return
	 */
	public boolean resize(File file,int width,float quality,boolean delOld){
		try {
			if(delOld){
				Thumbnails.of(file).width(width)
				.outputQuality(quality)
				.toFile(file);
			}else {
				Thumbnails.of(file).width(width)
				.outputQuality(quality)
				.toFile(file.getParent()+"/resize_"+file.getName());
			}
		} catch (IOException e) {
			try {
				FileUtils.write(new File(resize_error_log_file), file.getAbsolutePath() + "\n", true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 按固定高宽压缩
	 * @param file
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean resize(File file,int width,int height,boolean delOld){
		try {
			if(delOld){
				Thumbnails.of(file).forceSize(width, height)
				.outputQuality(0.6f)
				.toFile(file);
			}else {
				Thumbnails.of(file).forceSize(width, height)
				.outputQuality(0.6f)
				.toFile(file.getParent()+"/resize_"+file.getName());
			}
		} catch (IOException e) {
			try {
				FileUtils.write(new File(resize_error_log_file), file.getAbsolutePath() + "\n", true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}
	
	
}
