package io.github.liuzm.crawler.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.crawler.util.BinaryParseData;
import io.github.liuzm.crawler.util.Util;

public class Parser {

	protected static final Logger log = LoggerFactory.getLogger(Parser.class);
	
	private boolean isFetchBinaryContent;
	
	public Parser(Boolean isFetchBinaryContent) {
		this.isFetchBinaryContent = isFetchBinaryContent;
	}

	public boolean parse(Page page, String contextURL) {

		if (Util.hasBinaryContent(page.getContentType())) {
			if (!this.isFetchBinaryContent) {
				return false;
			}

			page.setParseData(BinaryParseData.getInstance());
			return true;

		} else if (Util.hasPlainTextContent(page.getContentType())) {
			try {
				TextParseData parseData = new TextParseData();
				if (page.getContentCharset() == null) {
					parseData.setTextContent(new String(page.getContentData()));
				} else {
					parseData.setTextContent(new String(page.getContentData(), page.getContentCharset()));
				}
				page.setParseData(parseData);
				return true;
			} catch (Exception e) {
				log.error(e.getMessage() + ", while parsing: " + page.getWebURL().getUrl());
			}
			return false;
		}
		return true;
	}

}
