package mdpPackage.threadsPunish;

import java.awt.List;

public class ThreadPunish implements Runnable {
	String name;
	Thread thread;
	int num;
	double[] lst;
	public ThreadPunish(String name, int num) {
		this.name = name;
		this.num = num;
		thread = new Thread();
		System.out.println("Punish agent: " + thread);
		
	
	}

		

	@Override
	public void run() {
	
		try {
			for(int i= 0; i < num; i ++) {
				System.out.println(name + ":" + i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(name + " thread interrupted");
		}
		 
		System.out.println(name + " thread existing, ");
	}

}
