package io.github.liuzm.crawler.pendingqueue;

import io.github.liuzm.crawler.page.ExtractedPage;


public class PendingStore extends AbsPendingQueue<ExtractedPage> {

	private static final long serialVersionUID = 7211446103736928404L;

	protected PendingStore(String jobName) {
		super(jobName);
	}
	
}
