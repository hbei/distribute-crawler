package io.github.liuzm.distribute.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lxyq
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3549947862637908997L;
	
	
	private String name;
	private String category;
	private String price;
	private String rating;
	private String url;
	private String image;
	
	private Set<String> related;

	public Message() {
	}

	public Message(String name, String category, String price, String rating, String url, String image) {
		this.name = name;
		this.category = category;
		this.price = price;
		this.rating = rating;
		this.url = url;
		this.image = extractImage(image);
		this.related = new HashSet<String>();
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getPrice() {
		return price;
	}

	public String getRating() {
		return rating;
	}

	public String getUrl() {
		return url;
	}

	/*
	 * don't use directly, just to please object mapper
	 */
	public Set<String> getRelated() {
		return related;
	}

	public void addRelated(Set<String> related) {
		for (Iterator<String> iterator = related.iterator(); iterator.hasNext();) {
			this.addRelated(iterator.next());
		}
	}

	public void addRelated(String related) {
		this.related.add(extractUrl(related));
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	@Override
	public int hashCode() {
		return this.url.hashCode();
	}

	@Override
	public boolean equals(Object arg) {
		if (arg != null && arg instanceof Message) {
			if (((Message) arg).url.equals(this.url)) {
				return true;
			} else {
				return false;
			}
		}
		return super.equals(arg);
	}

	@Override
	public String toString() {
		return String.format("name: %s category: %s image: %s url: %s", name, category, image, url);
	}

	public static String extractCode(String urlParameter) {
		int l = urlParameter.lastIndexOf("/dp/");
		if (l >= 0) {
			// create new to release big
			return new String(urlParameter.substring(l + 4));
		}
		return null;
	}

	public static String extractUrl(String urlParameter) {
		int l = urlParameter.indexOf("com/");
		if (l > 0) {
			// create new to release big
			return new String(urlParameter.substring(l + 4));
		}
		return null;
	}

	public static String extractImage(String imageUrl) {
		int l = imageUrl.indexOf("/images/");
		if (l > 0) {
			// create new to release big
			return new String(imageUrl.substring(l + 8));
		}
		return null;
	}

}
