package io.github.liuzm.benchmark;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class B {
	
	public static void main(String[] args) throws InterruptedException {
		final A a = new A();
		ModifyStatic modifyStatic = new ModifyStatic(a);
		ReadStatic readStatic = new ReadStatic(a);
		
		for(int i = 0 ; i< 10;i++){
			modifyStatic.modifyStatic();
			//readStatic.readStatic();
		}
	}

}
class ModifyStatic{
	
	private Executor s = Executors.newFixedThreadPool(1);
	
	private final A a;
	
	private String str = "aa";
	
	public ModifyStatic(final A a){
		this.a = a;
	}
	public void modifyStatic(){
		s.execute(new Runnable() {
			@Override
			public void run() {
				a.ss += 1;
				a.bb += 1;
				a.setSs(a.ss);
				a.setBb(a.bb);
				//System.out.println("M thread :"+a.ss);
				//System.out.println("M thread :"+A.ss);
				//System.out.println("MB thread :"+a.getBb());
				str+="bb";
				
				System.out.println(str);
			}
		});
	}
}
class ReadStatic{
	
	private Executor s = Executors.newFixedThreadPool(1);
	
	private final A a;
	
	public ReadStatic(final A a){
		this.a = a;
	}
	
	public void readStatic(){
		s.execute(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("R thread :"+a.ss);
				System.out.println("R thread :"+A.ss);
				System.out.println("RB thread :"+a.getBb());
			}
		});
	}
}
