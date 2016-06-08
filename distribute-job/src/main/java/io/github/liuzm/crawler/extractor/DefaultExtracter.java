package io.github.liuzm.crawler.extractor;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.github.liuzm.crawler.exception.ExtractException;
import io.github.liuzm.crawler.jobconf.ExtractConfig;
import io.github.liuzm.crawler.page.ExtractResult;
import io.github.liuzm.crawler.page.ExtractedPage;
import io.github.liuzm.crawler.page.Page;

@SuppressWarnings("rawtypes")
public class DefaultExtracter implements Extracter<ExtractedPage> {

	protected static final Logger log = Logger.getLogger(DefaultExtracter.class);
	
	private ExtractConfig config;

	public DefaultExtracter(ExtractConfig config) {
		super();
		this.config = config;
	}

	@Override
	public ExtractedPage parsePageElements(Page page) {
		if (page == null)
			return null;
		Document doc = null;
		try {
			doc = Jsoup.parse(new String(page.getContentData(), "gbk"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		ExtractedPage pageResult = new ExtractedPage();

		try {
			// 获取所以模板的结果，（其实只有一个模板）
			pageResult.setUrl(page.getWebURL());
			HashMap<Object, Object> m = config.getContentSeprator(doc, page.getWebURL().getUrl());
			pageResult.setMessages(m);
			if (m.size() > 0) {
				pageResult.setResult(ExtractResult.success);
			}
			doc = null;

		} catch (ExtractException e) {
			log.error(config.getIndexName() + "\t页面提取出错：" + page, e);
			e.printStackTrace();
			pageResult.setResult(ExtractResult.failed);
		}

		return pageResult;
	}
}
