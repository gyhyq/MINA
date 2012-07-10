package client;   
  
import java.io.IOException;   
import java.net.InetSocketAddress;   
  
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;   
import org.apache.mina.filter.codec.ProtocolCodecFilter;   
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;   
import org.apache.mina.transport.socket.SocketAcceptor;   
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;   

  
public class MainServer {   
    public static void main(String args[]) throws IOException{   
        //创建一个非阻塞的Server端socket 用NIO   
        SocketAcceptor acceptor = new NioSocketAcceptor();   
        
        //创建接收数据的过滤器   
        DefaultIoFilterChainBuilder chain= acceptor.getFilterChain();   
        //设定这个过滤器一行一行的(/r/n)的读取数据   
        chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));   
        //设定服务器端的消息处理器 new 一个对象   
        acceptor.setHandler(new SamplMinaServerHandler());   
        //服务器绑定的端口   
        int bindport=9988;   
        //绑定端口，启动服务器   
        acceptor.bind(new InetSocketAddress(bindport));   
        System.out.println("start ok,listen on:="+bindport);   
           
    }   
  
}  
