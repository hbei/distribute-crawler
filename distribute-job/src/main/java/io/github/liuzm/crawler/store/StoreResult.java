package io.github.liuzm.crawler.store;

public class StoreResult {

	public Status status;
	public String messge;
	public int count = 1;

	public StoreResult() {
	}

	public StoreResult(Status status, String messge, int count) {
		super();
		this.status = status;
		this.messge = messge;
		this.count = count;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessge() {
		return messge;
	}

	public void setMessge(String messge) {
		this.messge = messge;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public enum Status {
		success, failed, ignored
	}
}
