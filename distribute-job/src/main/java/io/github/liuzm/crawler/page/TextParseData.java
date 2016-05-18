package io.github.liuzm.crawler.page;


public class TextParseData implements ParseData {

	private String textContent;

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	
	@Override
	public String toString() {
		return textContent;
	}
	
}