package io.github.liuzm.crawler.util;

import io.github.liuzm.crawler.page.ParseData;

public class BinaryParseData implements ParseData {

	private static BinaryParseData instance = new BinaryParseData();
	
	public static BinaryParseData getInstance() {
		return instance;
	}
	
	@Override
	public String toString() {
		return "[Binary parse data can not be dumped as string]";
	}
}
