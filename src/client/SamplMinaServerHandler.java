package client;   
  
import org.apache.mina.core.service.IoHandlerAdapter;   
import org.apache.mina.core.session.IdleStatus;   
import org.apache.mina.core.session.IoSession;   
  
public class SamplMinaServerHandler extends IoHandlerAdapter {   
  
    public SamplMinaServerHandler() {   
        // TODO Auto-generated constructor stub   
    }   
       
    @Override  
    public void sessionOpened(IoSession session )throws Exception{   
        System.out.println("sessionOpened  incomming client :"+session.getRemoteAddress());   
    }   
  
    @Override  
    public void exceptionCaught(IoSession session, Throwable cause)   
            throws Exception {   
        System.out.println("exceptionCaught");   
    }   
  
    @Override  
    public void messageReceived(IoSession session, Object message)   
            throws Exception {   
           
        //需要设定服务器解析消息规则是一行一行的读取，这里可以转为string   
        String str=(String)message;   
        System.out.println("收到客户端信息："+str);   
        //将消息会送到客户端   
        session.write(str);   
           
           
       
           
           
        System.out.println("messageReceived");   
  
    }   
       
  
    @Override  
    public void messageSent(IoSession session, Object message) throws Exception {   
        // TODO Auto-generated method stub   
        System.out.println("messageSent");   
    }   
  
    @Override  
    public void sessionClosed(IoSession session) throws Exception {   
        System.out.println("sessionClose");   
    }   
  
    @Override  
    public void sessionCreated(IoSession session) throws Exception {   
        // TODO Auto-generated method stub   
    System.out.println("sessionCreate");   
    }   
  
    @Override  
    public void sessionIdle(IoSession session, IdleStatus status)   
            throws Exception {   
        // TODO Auto-generated method stub   
        System.out.println("sessionIdle");   
    }   
  
       
  
  
}  
