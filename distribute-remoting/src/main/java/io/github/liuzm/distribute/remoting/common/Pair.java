package io.github.liuzm.distribute.remoting.common;

public class Pair<T1,T2> {
	
	private T1 object1;
	private T2 object2;

	public Pair(T1 object1, T2 object2) {
		this.object1 = object1;
		this.object2 = object2;
	}

	/**
	 * @return the object1
	 */
	public T1 getObject1() {
		return object1;
	}

	/**
	 * @param object1 the object1 to set
	 */
	public void setObject1(T1 object1) {
		this.object1 = object1;
	}

	/**
	 * @return the object2
	 */
	public T2 getObject2() {
		return object2;
	}

	/**
	 * @param object2 the object2 to set
	 */
	public void setObject2(T2 object2) {
		this.object2 = object2;
	}
	
}
