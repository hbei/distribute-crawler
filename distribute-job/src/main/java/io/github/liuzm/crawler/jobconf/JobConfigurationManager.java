package io.github.liuzm.crawler.jobconf;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.ConfigurationException;

public class JobConfigurationManager {

	private static final Logger log = LoggerFactory.getLogger(JobConfigurationManager.class);
	private static List<File> confFiles = null;
	private static List<Document> configDocuments = Lists.newArrayList();

	private static JobConfigurationManager manager = null;
	private static boolean initial = false;

	private JobConfigurationManager() {
	}

	/**
	 * 初始化配置
	 * 
	 * @param confFile
	 */
	public static void init() {
		if (initial) {
			try {
				throw new ConfigurationException("配置已经初始化，不能再次初始化！");
			} catch (Exception e) {
			}
		} else {
			File confPath = new File("conf");
			File[] files = confPath.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith(".xml")) {
						return true;
					} else
						return false;
				}
			});
			confFiles = Lists.newArrayList(files);
			load();
			manager = new JobConfigurationManager();
			initial = true;
		}
	}

	/**
	 * 返回配置实例
	 * 
	 * @return
	 */
	public static JobConfigurationManager getInstance() {
		if (initial) {
			return manager;
		} else {
			try {
				init();
				// throw new ConfigurationException("请先进行初始化操作！");
			} catch (Exception e) {
			}
			return getInstance();
		}
	}

	/**
	 * 加载配置
	 */
	private static void load() {
		for (File f : confFiles) {
			try {
				Document doc = Jsoup.parse(f, "utf-8");
				configDocuments.add(doc);
			} catch (IOException e) {
				log.error("job配置加载失败");
			}
		}
	}

	public List<Document> getConfigDoc() {
		return configDocuments;
	}

	public static List<File> getConfFiles() {
		return confFiles;
	}

	public static void setConfFiles(List<File> confFiles) {
		JobConfigurationManager.confFiles = confFiles;
	}

}
