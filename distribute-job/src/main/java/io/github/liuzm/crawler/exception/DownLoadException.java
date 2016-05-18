package io.github.liuzm.crawler.exception;



public class DownLoadException extends Exception {
	private static final long serialVersionUID = 6548227413938390848L;

	public DownLoadException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DownLoadException(String arg0) {
		super(arg0);
	}
}