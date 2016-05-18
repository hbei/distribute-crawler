package io.github.liuzm.crawler.exception;

public class QueueException extends Throwable {

	private static final long serialVersionUID = 1L;

	public QueueException() {
		super();
	}

	public QueueException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public QueueException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public QueueException(String arg0) {
		super(arg0);
	}

	public QueueException(Throwable arg0) {
		super(arg0);
	}
	
	
}
