package socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(5);
//		ExecutorService exec = Executors.newSingleThreadExecutor();
		for (int i = 0; i < 100; i++)
		{
			exec.execute(new MyThread(i));			
		}
		exec.shutdown();
		System.out.println("end");
		while(!exec.isTerminated()){
			System.out.println(MyThread.received.get());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}