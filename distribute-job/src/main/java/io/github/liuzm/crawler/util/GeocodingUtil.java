package io.github.liuzm.crawler.util;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GeocodingUtil {

	private static final Log log = LogFactory.getLog(GeocodingUtil.class);

	public static String[] getGeocoding(String city, String addr) {
		if (city == null || city.equals(""))
			return null;
		if (addr == null || addr.equals(""))
			return null;
		// String url =
		// "http://api.map.baidu.com/geocoder/v2/?ak=KxUYoFFpzgzWRmj0IU1uuUWc&callback=renderOption&output=xml&address=闵行宝城路158弄47号&city=上海";

		String[] aks = new String[] { "KxUYoFFpzgzWRmj0IU1uuUWc",
				"Hv1wsLYZDtzlNKr35YWLzt5Q" };
		String ak = aks[Math.random() > 0.5 ? 1 : 0];
		String url = "http://api.map.baidu.com/geocoder/v2/?ak=" + ak
				+ "&callback=renderOption&output=xml&address=" + addr
				+ "&city= " + city;
		String[] geo = getGeo(url);
		return null!=geo?geo:getGeo(url);
		// return null;
	}

	private static String[] getGeo(String url) {
		try {
			Document doc = Jsoup.connect(url).timeout(30000).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36").get();

			if (null != doc) {
				String[] g = new String[2];
				g[0] = doc.getElementsByTag("lat").text();
				g[1] = doc.getElementsByTag("lng").text();
				if (StringUtils.isNoneBlank(g[0], g[1])) {
					return g;
				}
			}
		} catch (Exception e) {
			log.error("获取百度坐标错误：", e);
			System.out.println("获取百度坐标错误：");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static void main(String[] args) {
		// http://api.map.baidu.com/geocoder/v2/?ak=Hv1wsLYZDtzlNKr35YWLzt5Q&callback=renderOption&output=xml&address=三元桥霄云路36号国航大厦对面&city=
		// 北京
		System.out.println(GeocodingUtil.getGeocoding("上海", "浦东新区科技馆"));
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
