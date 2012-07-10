package socket;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThread implements Runnable {

	public static AtomicInteger received = new AtomicInteger(0);
	public MyThread(int num) {
		
	}

	public void run() {		
		received.incrementAndGet();
		try {
			Thread.sleep(1000*3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		received.decrementAndGet();
	}
}
