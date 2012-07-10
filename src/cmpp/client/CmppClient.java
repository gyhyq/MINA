package cmpp.client;

import java.net.InetSocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmpp.util.PropertyUtil;



public class CmppClient {
	private final static Object LOCK = new Object();
	public static PropertyUtil pu = new PropertyUtil("ServerIPAddress");
	private static final Logger logger = LoggerFactory
			.getLogger(CmppClient.class);

	public static void main(String args[]) {
		CmppClient client = new CmppClient();
		while (true) {
			client.startup();
			logger.info("client cmpp reconnector");
			try {
				logger.info("Thread.sleep(30*1000)");
				Thread.sleep(30 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void startup() {
		try {
			// create tcp/ip connector
			IoConnector connector = new NioSocketConnector();
			// 创建接受数据的过滤器

			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(
							new cmpp.client.CmppProtocolCodecFactory()));
			connector.setHandler(new cmpp.client.CmppClientIoHandler(LOCK));
			// set connect timeout
			connector.setConnectTimeoutMillis(30000);
			// 或者connector.setConnectTimeout(30);
			// 连接到服务器
			ConnectFuture cf = connector.connect(new InetSocketAddress(
					pu.getValue("CmppGw.server.ip"), Integer.parseInt(pu.getValue("CmppGw.server.port"))));

			// wait for the connection attem to be finished

			cf.awaitUninterruptibly();
			cf.getSession().getCloseFuture().awaitUninterruptibly();
			connector.dispose();
		} catch (Exception e) {
			logger.info(e.toString());
		}
	}
}
