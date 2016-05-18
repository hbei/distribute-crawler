package io.github.liuzm.crawler.fetcher;

import java.lang.ref.WeakReference;
import java.net.URL;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author
 * @desc 当ajax调用连接获得后直接返回连接，不去请求。<b>非线程安全</b>
 */
public class ResynchronizingAjaxController extends AjaxController {
	private static final long serialVersionUID = -7294411299347390276L;

	private URL rurl = null;
	private transient WeakReference<Thread> originatedThread_;
	/** 默认超时5秒 **/
	private final long timeOut_ = 5000L;

	public ResynchronizingAjaxController() {
		originatedThread_ = new WeakReference<Thread>(Thread.currentThread());
	}

	/**
	 * always return false
	 */
	@Override
	public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
		if (async && isInOriginalThread()) {
			rurl = request.getUrl();
			return false;
		}
		return false;
	}

	public URL getResynchronizedCallUlr() {
		long time = 0;
		int r = 20;
		while (null == rurl) {
			try {
				if (time >= timeOut_)
					break;
				Thread.sleep(20);
				time += r;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (rurl != null) {
			URL url = rurl;
			rurl = null;
			return url;
		}
		return null;
	}

	public URL getResynchronizedCallUlr(long timeOut) {
		long time = 0;
		int r = 20;
		while (null == rurl) {
			try {
				if (time >= timeOut)
					break;
				Thread.sleep(20);
				time += r;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (rurl != null) {
			URL url = rurl;
			rurl = null;
			return url;
		}
		return null;
	}

	boolean isInOriginalThread() {
		return Thread.currentThread() == originatedThread_.get();
	}
}