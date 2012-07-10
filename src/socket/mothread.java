package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class mothread  implements Runnable{
	private BufferedReader in=null;
	public  mothread(BufferedReader in){
		this.in=in;
	}
	public void run(){
		try{
			String strmo="";
			while(true){
				System.out.println(strmo=in.readLine());
				if(strmo==null)strmo=" ";
				if(strmo.equals("bye"))
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
