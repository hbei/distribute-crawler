package io.github.liuzm.crawler.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.crawler.jobconf.FetchConfig;
import io.github.liuzm.crawler.page.PageFetchResult;
import io.github.liuzm.crawler.url.WebURL;

public class DefaultFetcher extends Fetcher {

	protected static final Logger log = LoggerFactory.getLogger(DefaultFetcher.class);

	protected static PoolingHttpClientConnectionManager connectionManager;

	protected CloseableHttpClient httpClient;

	protected RequestConfig defaultRequestConfig;

	protected final Object mutex = new Object();

	protected long lastFetchTime = 0;

	protected static IdleConnectionMonitorThread connectionMonitorThread = null;

	public DefaultFetcher() {
		createFetcher(null);
	}

	public DefaultFetcher(FetchConfig config) {

		super(config);
		createFetcher(config);
	}

	public DefaultFetcher createFetcher(FetchConfig config) {
		// 创建连接池
		connectionManager = new PoolingHttpClientConnectionManager();

		BasicCookieStore cookieStore = new BasicCookieStore();
		CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
			public CookieSpec create(HttpContext context) {

				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
						// Oh, I am easy
					}
				};
			}

		};
		Registry<CookieSpecProvider> r = RegistryBuilder.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
				.register("easy", easySpecProvider).build();

		// Create global request configuration
		defaultRequestConfig = RequestConfig.custom().setCookieSpec("easy").setSocketTimeout(10000)
				.setConnectTimeout(10000).build();

		connectionManager.setMaxTotal(config.getMaxTotalConnections());
		connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerHost());

		// Create an HttpClient with the given custom dependencies and configuration
		httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultCookieStore(cookieStore)
				.setDefaultCookieSpecRegistry(r)
				/* .setProxy(new HttpHost("myproxy", 8080)) */
				.setDefaultRequestConfig(defaultRequestConfig).build();

		if (connectionMonitorThread == null) {
			connectionMonitorThread = new IdleConnectionMonitorThread(connectionManager);
		}
		
		return this;
	}

	public PageFetchResult fetch(WebURL webUrl) {
		return fetch(webUrl, false);
	}

	public PageFetchResult fetch(WebURL webUrl, boolean proxy) {
		PageFetchResult fetchResult = new PageFetchResult();

		String toFetchURL = webUrl.getUrl();
		HttpGet get = new HttpGet(toFetchURL);
		get.addHeader("Accept-Encoding", "gzip");
		get.addHeader("User-Agent", config.getAgent());

		RequestConfig requestConfig = null;
		CloseableHttpResponse response = null;

		synchronized (mutex) {
			long now = (new Date()).getTime();
			if (now - lastFetchTime < ((FetchConfig) config).getDelayBetweenRequests()) {
				try {
					Thread.sleep(((FetchConfig) config).getDelayBetweenRequests() - (now - lastFetchTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lastFetchTime = (new Date()).getTime();
		}
		int statusCode = 0;
		int count = 5;

		while (statusCode != HttpStatus.SC_OK && count-- > 0) {
			HttpHost proxyHost = null;
			if (proxy) {
				proxyHost = getProxyIp();

				if (proxyHost != null)
					requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(10000)
							.setConnectTimeout(10000).setProxy(proxyHost).build();
			}

			get.setConfig(requestConfig);

			try {
				response = httpClient.execute(get);
				statusCode = response.getStatusLine().getStatusCode();
				fetchResult.setEntity(response.getEntity());
				fetchResult.setResponseHeaders(response.getAllHeaders());
			} catch (IOException e) {
				
			}
		}

		fetchResult.setStatusCode(statusCode);
		fetchResult.setFetchedUrl(toFetchURL);

		if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {

			long size = fetchResult.getEntity().getContentLength();
			if (size == -1) {
				Header length = response.getLastHeader("Content-Length");
				if (length == null) {
					length = response.getLastHeader("Content-length");
				}
				if (length != null) {
					size = Integer.parseInt(length.getValue());
				} else {
					size = -1;
				}
			}
			if (size > ((FetchConfig) config).getMaxDownloadSizePerPage()) {
				fetchResult.setStatusCode(CustomFetchStatus.PageTooBig);
				get.abort();
				return fetchResult;
			}
			return fetchResult;
		}
		get.abort();

		fetchResult.setStatusCode(CustomFetchStatus.UnknownError);
		return fetchResult;
	}

	public synchronized void shutDown() {
		if (connectionMonitorThread != null) {
			connectionManager.shutdown();
			connectionMonitorThread.shutdown();
		}
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	private static class GzipDecompressingEntity extends HttpEntityWrapper {

		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException, IllegalStateException {

			// the wrapped entity's getContent() decides about repeatability
			InputStream wrappedin = wrappedEntity.getContent();

			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			// length of ungzipped content is not known
			return -1;
		}

	}
}
