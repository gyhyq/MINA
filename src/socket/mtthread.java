package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class mtthread  implements Runnable{
	private PrintWriter out=null;

	public mtthread(PrintWriter out) {
		// TODO Auto-generated constructor stub
		this.out=out;
	}
	public void run(){
		try{
			while(true){
				BufferedReader line = new BufferedReader(new InputStreamReader(
						System.in));
				out.println(line.readLine());
				Thread.yield();
				Thread.sleep(1000);
			}			
		}catch(Exception e){
			System.out.println(e.toString());
		}
			
		}	
}
