package io.github.liuzm.benchmark;

public class A {
	
	public static int ss = 0;
	
	public volatile int bb = 0;
	
	

	/**
	 * @return the ss
	 */
	public static int getSs() {
		return ss;
	}

	/**
	 * @param ss the ss to set
	 */
	public static void setSs(int ss) {
		A.ss = ss;
	}

	/**
	 * @return the bb
	 */
	public int getBb() {
		return bb;
	}

	/**
	 * @param bb the bb to set
	 */
	public void setBb(int bb) {
		this.bb = bb;
	}
	
	
	
}
