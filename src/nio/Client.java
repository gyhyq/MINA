package nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;



public class Client {    
	   public SocketChannel client = null;    
	 
	   public InetSocketAddress isa = null;    
	 
	   public RecvThread rt = null;    
	  
	   private String host;    
	 
	   private int port;    
	  
	   public Client(String host, int port) {    
	       this.host = host;    
	       this.port = port;    
	   }    
	  
	   public void makeConnection() {    
	       String proxyHost = "127.0.0.1";    
	       String proxyPort = "4900";    
	       System.getProperties().put("socksProxySet", "true");    
	       System.getProperties().put("socksProxyHost", proxyHost);    
	       System.getProperties().put("socksProxyPort", proxyPort);    
	  
	       int result = 0;    
	       try {    
	           client = SocketChannel.open();    
	           isa = new InetSocketAddress(host, port);    
	           client.connect(isa);    
	           client.configureBlocking(false);    
	           receiveMessage();    
	       } catch (UnknownHostException e) {    
	           e.printStackTrace();    
	       } catch (IOException e) {    
	           e.printStackTrace();    
	       }    
	       long begin = System.currentTimeMillis();    
	  
	       sendMessage();    
	  
	       long end = System.currentTimeMillis();    
	       long userTime = end - begin;    
	       System.out.println("use tiem: " + userTime);    
	       try {    
	           interruptThread();    
	           client.close();    
	           System.exit(0);    
	       } catch (IOException e) {    
	           e.printStackTrace();    
	       }    
	   }    
	  
	   public int sendMessage() {    
	               System.out.println("Inside SendMessage");    
	       String msg = null;    
	       ByteBuffer bytebuf;    
	       int nBytes = 0;    
	       try {    
	           msg = "It's message from client!";    
	           System.out.println("msg is "+msg);    
	           bytebuf = ByteBuffer.wrap(msg.getBytes());    
	           for (int i = 0; i < 1000; i++) {    
	               nBytes = client.write(bytebuf);    
	               System.out.println(i + " finished");    
	           }    
	           interruptThread();    
	           try {    
	               Thread.sleep(5000);    
	           } catch (Exception e) {    
	               e.printStackTrace();    
	           }    
	           client.close();    
	           return -1;    
	  
	       } catch (IOException e) {    
	           e.printStackTrace();    
	       }    
	  
	       return nBytes;    
	  
	   }    
	  
	   public void receiveMessage() {    
	       rt = new RecvThread("Receive THread", client);    
	       rt.start();    
	  
	   }    
	  
	   public void interruptThread() {    
	       rt.val = false;    
	   }    
	  
	   public static void main(String args[]) {    
	       if (args.length < 2) {    
	           System.err.println("You should put 2 args: host,port");    
	       } else {    
	           String host = args[0];    
	           int port = Integer.parseInt(args[1]);    
	           Client cl = new Client(host, port);    
	           cl.makeConnection();    
	        }    
	        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));    
	        String msg;    
	   
	    }    
	   
	    public class RecvThread extends Thread {    
	        public SocketChannel sc = null;    
	   
	        public boolean val = true;    
	   
	        public RecvThread(String str, SocketChannel client) {    
	            super(str);    
	            sc = client;    
	        }    
	   
	        public void run() {    
	            int nBytes = 0;    
	            ByteBuffer buf = ByteBuffer.allocate(2048);    
	            try {    
	                while (val) {    
	                    while ((nBytes = nBytes = client.read(buf)) > 0) {    
	                        buf.flip();    
	                        Charset charset = Charset.forName("us-ascii");    
	                        CharsetDecoder decoder = charset.newDecoder();    
	                        CharBuffer charBuffer = decoder.decode(buf);    
	                        String result = charBuffer.toString();    
	                        System.out.println("the server return: " + result);    
	                        buf.flip();    
	   
	                    }    
	                }    
	   
	            } catch (IOException e) {    
	                e.printStackTrace();    
	   
	            }
	        }
	    }
}
