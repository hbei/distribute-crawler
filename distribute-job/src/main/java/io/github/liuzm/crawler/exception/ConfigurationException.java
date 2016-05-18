package io.github.liuzm.crawler.exception;
/**
 * @author chenxinwen
 * @date 2014-7-22
 * 配置异常
 */
public class ConfigurationException extends Exception {

	private static final long serialVersionUID = -6215993568347390627L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	
}
