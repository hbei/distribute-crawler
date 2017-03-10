package org.qyd.aliuge.distrobute.client.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {
	
	private final BlockingQueue quene = new ArrayBlockingQueue<String>(10);
	
	
	class Thread1 implements Runnable{
		@Override
		public void run() {
			final String  str = "hubei";
			System.out.println("Thread1");
			quene.add(str);
		}
	}
	
	class Thread3 implements Runnable{
		@Override
		public void run() {
			final String  str = "tianmen";
			System.out.println("Thread3");
			quene.add(str);
		}
	}
	
	class Thread2 extends Thread{
		
		@Override
		public void run(){
			System.out.println("Thread2");
			try {
				takeStr();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void takeStr() throws InterruptedException{
			final String  str = "hubei";
			
			quene.take();
		}
	}
	
	public static void main(String[] args) {
		
		Thread1 thread1 = new Test().new Thread1();
		Thread2 thread2 = new Test().new Thread2();
		Thread3 thread3 = new Test().new Thread3();
		
		thread1.run();
		
		try {
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
