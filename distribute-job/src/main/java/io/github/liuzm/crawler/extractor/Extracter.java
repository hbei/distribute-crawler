package io.github.liuzm.crawler.extractor;

import io.github.liuzm.crawler.page.Page;

public interface Extracter<T> {
	T parsePageElements(Page page);
}
