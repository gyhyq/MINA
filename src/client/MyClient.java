package client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MyClient {
	public static void main(String args[]) {
		// create tcp/ip connector
		IoConnector connector = new NioSocketConnector();
		// 创建接受数据的过滤器
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		// 设定这个过滤器一行一行(/r/n)的读取数据
		chain.addLast("myChin", new ProtocolCodecFilter(
				new TextLineCodecFactory()));
		// 或者如下
		// connector.getFilterChain().addLast("codec", new
		// ProtocolCodecFilter(new
		// TextLineCodecFactory(Charset.forName("UTF-8"),LineDelimiter.WINDOWS.getValue(),LineDelimiter.WINDOWS.getValue())));
		// 设定服务器端的消息处理器：new 一个clinethandler对象
		connector.setHandler(new SamplMinaClientHandler());
		// set connect timeout
		connector.setConnectTimeoutMillis(30000);
		// 或者connector.setConnectTimeout(30);
		// 连接到服务器
		ConnectFuture cf = connector.connect(new InetSocketAddress("localhost",
				9988));
		// wait for the connection attem to be finished
		cf.awaitUninterruptibly();
		cf.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
}
