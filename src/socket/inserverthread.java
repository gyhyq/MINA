package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class inserverthread implements Runnable{
	private BufferedReader in=null;
	public inserverthread(BufferedReader in){
		this.in=in;
	}
	public void run(){
		try{
			while(true){
				String line = in.readLine();
				System.out.println("Cleint send is :" + line);
				if(line==null)line=" ";
				if(line.equals("bye"))
					break;
				Thread.yield();
				Thread.sleep(1000);
			}
			in.close();
		}catch(Exception e){
			System.out.println(e.toString());
		}
			
		}	
}
