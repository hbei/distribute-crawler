package io.github.liuzm.crawler.exception;
public class ExtractException extends Exception{

	private static final long serialVersionUID = 7761968909463699377L;

	public ExtractException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ExtractException(String arg0) {
		super(arg0);
	}
	
}