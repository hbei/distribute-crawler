package io.github.liuzm.crawler.fetcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import io.github.liuzm.crawler.jobconf.FetchConfig;
import io.github.liuzm.crawler.util.DateTimeUtil;
import io.github.liuzm.crawler.util.ProxyIp;
import io.github.liuzm.crawler.util.WrongUrlLog;

public abstract class Fetcher {

	protected static FetchConfig config;

	private static final Logger log = LoggerFactory.getLogger(Fetcher.class);

	public static volatile Map<String, Integer> m = Maps.newConcurrentMap();
	public static volatile List<String> proxyIps = null;
	public static volatile boolean proxyerisRuning = true;

	public Fetcher() {
	}

	public Fetcher(FetchConfig config) {
		
		this.config = config;

		List<String> ips = config.getProxyIps();
		proxyIps = ips;
		if (ips != null && ips.size() < 10) {
			List fetchIPs = ProxyIp.fetchProxyIps(true);
			proxyIps.addAll(fetchIPs);
			log.info("共爬取代理Ip：" + fetchIPs.size() + "个");
		}
	}

	public static synchronized void runProxyer() throws Exception {
		// 用于维护代理ip的单独线程
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				int count = 0;
				boolean flag_fetchProxyIps = false;
				while (proxyerisRuning) {
					try {
						TimeUnit.SECONDS.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {

						Iterable<String> it = null;
						for (String ip : it = m.keySet()) {
							int t = m.get(ip);

							if (t > 10 && StringUtils.isNotBlank(ip)) {
								m.remove(ip);
								proxyIps.remove(ip);
								log.info("剔除代理ip：" + ip);
							}
						}

						if (proxyIps.size() < 30) {
							log.info("代理ip不足30，开始重新爬取");
							proxyIps.addAll(ProxyIp.fetchProxyIps(true));
							m.clear();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					// 10次后持久化一下代理ip
					if (count == 10) {
						storeProxyIp();
						count = 0;
					} else {
						count++;
					}
				}

				// 最后 持久化一下代理ip
				storeProxyIp();

			}

		}, "代理Ip更新器");
		t.start();

	}

	private static void storeProxyIp() {
		// 最后 ，保存上面优质的代理ip
		Properties pro = new Properties();
		for (int i = 0; i < proxyIps.size(); i++) {
			pro.setProperty("proxy" + i, proxyIps.get(i));
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File(config.getProxyPath()));
			pro.store(out, "更新代理Ip,更新时间[" + DateTimeUtil.getDateTime() + "]");
		} catch (FileNotFoundException e) {
			log.error("代理IP的文件路径不存在！！！:" + new File(config.getProxyPath()).toString(), e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("更新代理Ip错误:", e);
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	public HttpHost getProxyIp() {
		if (proxyIps != null) {
			int size = proxyIps.size();
			if (size <= 0)
				return null;
			int posi = (int) (Math.random() * size);
			String ip = proxyIps.get(posi);
			String[] ip_port = ip.split(":");
			if (ip_port.length != 2)
				return null;
			return new HttpHost(ip_port[0], Integer.valueOf(ip_port[1]));
		}
		return null;
	}

	/*
	 * 获取地址的document
	 */
	public static Document goFetchPage(String url, FetchConfig config) {
		int count = 5;
		Document doc = null;
		while (count > 0 && doc == null) {
			String proxyIp = setProxy(proxyIps);
			try {
				doc = Jsoup.connect(url).timeout(30000).userAgent(config.getAgent()).get();

			} catch (IOException e) {
				count--;
				m.put(proxyIp, m.get(proxyIp) == null ? 1 : m.get(proxyIp) + 1);
				log.error(config.getIndexName() + "\t获取网页{" + url + "}失败!" + "\t失败的代理ip\t" + proxyIp);
				log.error(e.getMessage());
			}
		}
		// 3次重试后任然无法获取则失败，记录
		if (count <= 0 && doc == null) {
			WrongUrlLog.writeUrl(config.getIndexName() + "_error_url." + DateTimeUtil.getDate() + ".txt",
					config.getIndexName() + "\t" + url + "\t" + new Date().toString());
		}

		return doc;

	}

	public static void addFailedProxy(String proxyIp) {
		m.put(proxyIp, m.get(proxyIp) == null ? 1 : m.get(proxyIp) + 1);
	}

	/*
	 * 获取地址的document
	 */
	public static Document goFetchPage(String url, List<String> proxyIps) {
		int count = 5;
		Document doc = null;
		while (count > 0 && doc == null) {
			String proxyIp = setProxy(proxyIps);
			if (proxyIp == null)
				setProxy(proxyIps);
			try {
				doc = Jsoup.connect(url).timeout(5000).userAgent("Agent").get();

			} catch (IOException e) {
				count--;

				log.error("失败的代理ip\t" + proxyIp);
				m.put(proxyIp, m.get(proxyIp) == null ? 1 : m.get(proxyIp) + 1);

			}
		}
		return doc;

	}

	/*
	 * 获取地址的document
	 */
	public static Document goFetchPageNoTry(String url, List<String> proxyIps) {
		Document doc = null;

		String proxyIp = setProxy(proxyIps);
		if (proxyIp == null)
			setProxy(proxyIps);
		try {
			doc = Jsoup.connect(url).timeout(5000).userAgent("Agent").get();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 设置一个随机代理iP
	 * 
	 * @param proxyMap
	 * @return
	 */
	public static String setProxy(final List<String> proxyMap) {
		if (null == proxyMap || proxyMap.isEmpty())
			return "";
		int i = (int) (Math.random() * proxyMap.size());
		/*
		 * String[] ip_port = proxyMap.get(i + "").split(":"); if()
		 */
		String ip_text = proxyMap.get(i);
		String[] ip_port = ip_text.split(":");
		if (ip_port.length != 2)
			return null;
		String ip = ip_port[0];
		String port = ip_port[1];

		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");

		System.setProperty("http.proxyHost", ip);
		System.setProperty("http.proxyPort", port);
		return ip_text;
	}

	/**
	 * 设置一个随机代理iP
	 * 
	 * @param proxyMap
	 * @return
	 */
	public static String setProxy(String ip_str) {
		if (!StringUtils.isNotBlank(ip_str))
			return "";

		String[] ip_port = ip_str.split(":");
		if (ip_port.length != 2)
			return null;
		String ip = ip_port[0];
		String port = ip_port[1];

		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");

		System.setProperty("http.proxyHost", ip);
		System.setProperty("http.proxyPort", port);
		return ip_str;
	}

	/*
	 * 获取地址的document 无代理ip
	 */
	public static Document goFetchPage(String url) {
		// setProxy(proxyIps);
		Document doc = null;
		while (doc == null) {
			try {
				doc = Jsoup.connect(url).timeout(30000).userAgent("Agent").get();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return doc;

	}

}
