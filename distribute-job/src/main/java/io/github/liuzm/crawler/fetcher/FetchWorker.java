package io.github.liuzm.crawler.fetcher;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import io.github.liuzm.crawler.exception.QueueException;
import io.github.liuzm.crawler.jobconf.FetchConfig;
import io.github.liuzm.crawler.page.Page;
import io.github.liuzm.crawler.page.PageFetchResult;
import io.github.liuzm.crawler.page.Parser;
import io.github.liuzm.crawler.pendingqueue.PendingManager;
import io.github.liuzm.crawler.pendingqueue.PendingPages;
import io.github.liuzm.crawler.pendingqueue.PendingUrls;
import io.github.liuzm.crawler.url.WebURL;
import io.github.liuzm.crawler.util.BloomfilterHelper;
import io.github.liuzm.crawler.util.UrlUtils;
import io.github.liuzm.crawler.vo.KeyValue;
import io.github.liuzm.crawler.worker.Worker;

public abstract class FetchWorker extends Worker {
	
	private static final Logger log = LoggerFactory.getLogger(FetchWorker.class);

	protected UrlUtils urlUtils = new UrlUtils();
	protected BloomfilterHelper bloomfilterHelper = BloomfilterHelper
			.getInstance();
	/**
	 * url队列
	 */
	protected PendingUrls pendingUrls = null;
	/**
	 * Page队列
	 */
	protected PendingPages pendingPages = null;
	/**
	 * 爬取器
	 */
	protected DefaultFetcher fetcher;
	/**
	 * job配置
	 */
	protected FetchConfig config;
	/**
	 * 解析器
	 */
	protected Parser parser;
	/**
	 * robots
	 */

	public List<Pattern> fetchFilters = Lists.newArrayList();

	public List<KeyValue<Pattern, String>> extractFilters = Lists.newArrayList();
	
	

	/**
	 * @param conf
	 *            构造函数，未提供爬取器，需通过setFetcher方法设置Fetcher
	 */
	public FetchWorker(String jobTag, FetchConfig config) {
		this(jobTag,config,new DefaultFetcher(config));
	}

	/**
	 * @param conf
	 * @param fetcher
	 *            推荐使用的构造函数
	 */
	public FetchWorker(String jobTag, FetchConfig config, DefaultFetcher fetcher) {
		super(jobTag);
		this.fetcher = fetcher;
		this.config = config;
		pendingUrls = PendingManager.getPendingUlr(config.getJobName());
		pendingPages = PendingManager.getPendingPages(config.getJobName());
		parser = new Parser(config.isFetchBinaryContent());

		// 过滤器
		List<String> urls1 = config.getFetchUrlFilters();
		List<KeyValue<String, String>> urls2 = config.getExtractUrlfilters();
		for (String s : urls1) {
			fetchFilters.add(Pattern.compile(s));
		}
		for (KeyValue<String, String> s : urls2) {
			extractFilters.add(new KeyValue(Pattern.compile(s.getKey()), s.getValue()));
		}
	}

	public FetchWorker setFetcher(final DefaultFetcher fetcher) {
		this.fetcher = fetcher;
		return this;
	}

	/**
	 * @desc 工作成功
	 */
	public abstract void onSuccessed();

	/**
	 * @desc 工作失败
	 */
	public abstract void onFailed(WebURL url);

	/**
	 * @desc 忽略工作
	 */
	public abstract void onIgnored(WebURL url);

	/**
	 * fetcher filter
	 * 
	 * @param url
	 * @return
	 */
	public boolean fetchFilter(String url) {

		if (null == fetchFilters || fetchFilters.size() == 0) {
			return true;
		}
		for (Pattern p : fetchFilters) {
			if (p.matcher(url).matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * extract filter
	 * 
	 * @param url
	 * @return
	 */
	public boolean extractFilter(String url) {
		// 如果extractFilters为空，说明该条件不设立过滤，所以的url都通过
		if (null == extractFilters || extractFilters.size() == 0) {
			return true;
		}
		for (KeyValue<Pattern, String> p : extractFilters) {
			if (p.getKey().matcher(url).matches()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否是符合带抓取的url。如果是，再看下是否需要替换url
	 * 
	 * @param url
	 * @return
	 */
	public String extractFilterAndChangeUrl(String url) {
		// 如果extractFilters为空，说明该条件不设立过滤，所以的url都通过
		if (null == extractFilters || extractFilters.size() == 0) {
			return url;
		}
		for (KeyValue<Pattern, String> p : extractFilters) {
			if (p.getKey().matcher(url).matches()) {
				if(StringUtils.isNoneBlank(p.getValue())){
					String[] pp = p.getValue().split(",");
					//如果符合需要替换url中的老字符串为新字符串
					if(pp.length==2)
						return url.replace(pp[0], pp[1]);
				}
				return url;
			}
		}
		// 最后如果没有匹配到过滤url的则反回null。不符合过滤url
		return null;
	}

	/**
	 * @param url
	 * @desc 爬网页
	 */
	public void fetchPage(WebURL url) {

		PageFetchResult result = null;
		try {
			if (null != url && StringUtils.isNotBlank(url.getUrl())) {

				result = fetcher.fetch(url, true);
				// 获取状态
				int statusCode = result.getStatusCode();
				if (statusCode == CustomFetchStatus.PageTooBig) {
					onIgnored(url);
					return;
				}
				if (statusCode != HttpStatus.SC_OK) {
					onFailed(url);
				} else {
					Page page = new Page(url);
					onSuccessed();
					if (!result.fetchContent(page)) {
						onFailed(url);
						return;
					}
					if (!parser.parse(page, url.getUrl())) {
						onFailed(url);
						return;
					}
					// 是否加入抽取队列
					String e_url = extractFilterAndChangeUrl(url.getUrl());
					if (StringUtils.isNoneBlank(e_url)) {
						url.setUrl(e_url);
						page.setWebURL(url);
						
						pendingPages.addElement(page);
						
						//return; 抽取页面继续提取
					}

					// 检测depth
					if (url.getDepth() > config.getMaxDepthOfCrawling()&& config.getMaxDepthOfCrawling() != -1) {
						return;
					}
					// 提取Url，放入待抓取Url队列
					Document doc = Jsoup.parse(new String(page.getContentData(), page.getContentCharset()),
							urlUtils.getBaseUrl(page.getWebURL().getUrl()));
					Elements links = doc.getElementsByTag("a");
					if (!links.isEmpty()) {
						for (Element link : links) {
							String linkHref = link.absUrl("href");
							// 满足爬取和抓取规则，并没有处理过的都放到url队列里
							if ((fetchFilter(linkHref) || extractFilter(linkHref))
									&& !bloomfilterHelper.exist(linkHref)) {
								WebURL purl = new WebURL();
								purl.setName(link.text());
								purl.setUrl(linkHref);

								purl.setDepth((short) (url.getDepth() + 1));
								
								if (purl.getDepth() > config.getMaxDepthOfCrawling()&& config.getMaxDepthOfCrawling() != -1)
									return;
								try {
									if (!pendingUrls.addElement(purl)) {
										FileUtils.writeStringToFile(new File("status/_urls.good"),url.getUrl() + "\n", true);
									}
								} catch (QueueException e) {
									log.error(e.getMessage());
								}
							}
						}
					}
				}

			}
		} catch (QueueException e) {
			onFailed(url);
		} catch (Exception e) {
			e.printStackTrace();
			onFailed(url);
		} finally {
			if (null != result)
				result.discardContentIfNotConsumed();
		}
	}

	/**
	 * 爬取网页，不抽取url。用于重新爬去链接
	 * 
	 * @param url
	 */
	public void fetchPageWhitoutExtractUrl(WebURL url) {
		PageFetchResult result = null;
		try {
			if (null != url && StringUtils.isNotBlank(url.getUrl())) {
				// 是否需要爬
				if (fetchFilter(url.getUrl())) {
					// result = fetcher.fetchHeader(url);
					result = fetcher.fetch(url, true);
					// 获取状态
					int statusCode = result.getStatusCode();
					if (statusCode == CustomFetchStatus.PageTooBig) {
						onIgnored(url);
						return;
					}
					if (statusCode != HttpStatus.SC_OK) {
						onFailed(url);
					} else {
						Page page = new Page(url);
						pendingUrls.processedSuccess();
						if (!result.fetchContent(page)) {
							onFailed(url);
							return;
						}
						if (!parser.parse(page, url.getUrl())) {
							onFailed(url);
							return;
						}
						// 是否加入抽取队列
						if (extractFilter(url.getUrl())) {
							pendingPages.addElement(page);
						}
					}
				} else {
					onIgnored(url);
				}
			}
		} catch (Exception e) {
			onFailed(url);
		} catch (QueueException e) {
			onFailed(url);
		} finally {
			if (null != result)
				result.discardContentIfNotConsumed();
		}
	}

	public static void main(String[] args) {
	}
}
