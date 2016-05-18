package io.github.liuzm.crawler.pendingqueue;

import io.github.liuzm.crawler.page.Page;


public class PendingPages extends AbsPendingQueue<Page> {
	
	private static final long serialVersionUID = -5671808882701246813L;
	
	protected PendingPages(String jobName) {
		super(jobName);
	}
}
