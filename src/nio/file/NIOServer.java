package nio.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;

/**
 * 测试文件下载的NIOServer
 * 
 * @author tenyears.cn
 */
public class NIOServer {
  static int BLOCK = 512;
  // 处理与客户端的交互
  public class HandleClient {
    protected FileChannel channel;
    protected ByteBuffer buffer;
    public HandleClient() throws IOException {    	
      this.channel = new FileInputStream(filename).getChannel();
      this.buffer = ByteBuffer.allocate(BLOCK);
    }
    public ByteBuffer readBlock() {
      try {
        buffer.clear();
        int count = channel.read(buffer);
//        System.out.println(count);
        buffer.flip();
        if (count <= 0)
          return null;
      } catch (IOException e) {
        e.printStackTrace();
      }
      return buffer;
    }
    public void close() {
      try {
        channel.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  protected Selector selector;
  protected String filename = "e:\\log4"; // a big file
  protected ByteBuffer clientBuffer = ByteBuffer.allocate(BLOCK);
  protected CharsetDecoder decoder;

  public NIOServer(int port) throws IOException {
    selector = this.getSelector(port);
    Charset charset = Charset.forName("GB2312");
    decoder = charset.newDecoder();
  }

  // 获取Selector
  protected Selector getSelector(int port) throws IOException {
    ServerSocketChannel server = ServerSocketChannel.open();
    Selector sel = Selector.open();
    server.socket().bind(new InetSocketAddress(port));
    server.configureBlocking(false);
    server.register(sel, SelectionKey.OP_ACCEPT);
    return sel;
  }

  // 监听端口
  public void listen() {
    try {
      for (;;) {
        selector.select();
        Iterator iter = selector.selectedKeys().iterator();
        while (iter.hasNext()) {
          SelectionKey key = (SelectionKey) iter.next();
          iter.remove();
          handleKey(key);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 处理事件
  protected void handleKey(SelectionKey key) throws IOException {
    if (key.isAcceptable()) { // 接收请求
      ServerSocketChannel server = (ServerSocketChannel) key.channel();
      SocketChannel channel = server.accept();
      channel.configureBlocking(false);
      channel.register(selector, SelectionKey.OP_READ);
    } else if (key.isReadable()) { // 读信息
      SocketChannel channel = (SocketChannel) key.channel();
      int count = channel.read(clientBuffer);
      if (count > 0) {
        clientBuffer.flip();
        CharBuffer charBuffer = decoder.decode(clientBuffer);
        System.out.println("Client >>" + charBuffer.toString());
        SelectionKey wKey = channel.register(selector,
            SelectionKey.OP_WRITE);
        wKey.attach(new HandleClient());
      } else
        channel.close();
      clientBuffer.clear();
//      System.out.println("key.isReadable()");
    } else if (key.isWritable()) { // 写事件
//      System.out.println("key.isWritable()");
      SocketChannel channel = (SocketChannel) key.channel();
      HandleClient handle = (HandleClient) key.attachment();
      ByteBuffer block = handle.readBlock();
      if (block != null)
        channel.write(block);
      else {
        handle.close();
        channel.close();
      }
      
    }
  }

  public static void main(String[] args) {
    int port = 12345;
    try {
      NIOServer server = new NIOServer(port);
      System.out.println("Listernint on " + port);
      while (true) {
        server.listen();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
} 