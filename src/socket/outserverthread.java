package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class outserverthread implements Runnable{
	private PrintWriter out=null;

	public outserverthread(PrintWriter out) {
		// TODO Auto-generated constructor stub
		this.out=out;
	}
	public void run(){
		try{
			int i=0;
			while(true){
				out.println("mt " + i++);
				Thread.yield();
				Thread.sleep(5000);
			}			
		}catch(Exception e){
			System.out.println(e.toString());
		}
			
		}	
}
