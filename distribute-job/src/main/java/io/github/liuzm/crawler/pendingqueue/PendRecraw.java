package io.github.liuzm.crawler.pendingqueue;

import io.github.liuzm.crawler.url.WebURL;

public class PendRecraw extends AbsPendingQueue<WebURL> {
	
	private static final long serialVersionUID = -2733220512896685281L;

	protected PendRecraw(String jobName) {
		super(jobName);
	}
	
}
