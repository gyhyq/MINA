package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class serverthread implements Runnable{
	private Socket socket=null;
	private BufferedReader in=null;
	private PrintWriter out=null;
	public serverthread(Socket socket){
		this.socket=socket;
	}
	public void run(){
		try{
			String RemoteIP = socket.getInetAddress().getHostAddress();
			String RemotePort = ":" + socket.getLocalPort();
			//serverthread
			System.out.println("A client come in!IP:" + RemoteIP
					+ RemotePort);	
			while(true){
				in = new BufferedReader(new InputStreamReader(socket
						.getInputStream()));
				String line = in.readLine();
				System.out.println("Cleint send is :" + line);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println("Your Message Received!");
				if(line==null)line=" ";
				if(line.equals("bye"))
					break;
				Thread.yield();
				Thread.sleep(1000);
			}
			out.close();
			in.close();
			socket.close();
		}catch(Exception e){
			System.out.println(e.toString());
		}
			
		}	
}
