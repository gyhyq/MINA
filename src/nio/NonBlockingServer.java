package nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class NonBlockingServer {
	public Selector sel = null;
	public ServerSocketChannel server = null;
	public SocketChannel socket = null;
	public int port = 4900;
	String result = null;

	public NonBlockingServer() {
		System.out.println("Inside default ctor");
	}

	public NonBlockingServer(int port) {
		System.out.println("Inside the other ctor");
		this.port = port;
	}

	public void initializeOperations() throws IOException, UnknownHostException {
		System.out.println("Inside initialization");
		sel = Selector.open();
		server = ServerSocketChannel.open();
		server.configureBlocking(false);
		InetAddress ia = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(port);
		server.socket().bind(isa);
	}

	public void startServer() throws IOException {
		System.out.println("Inside startserver");
		initializeOperations();
		System.out.println("Abt to block on select()");
		SelectionKey acceptKey = server.register(sel, SelectionKey.OP_ACCEPT);
		System.out.println("Abt to block on select() end ");
		while (acceptKey.selector().select() > 0) {
			System.out.println("acceptKey.selector().select()");
			Set readyKeys = sel.selectedKeys();
			Iterator it = readyKeys.iterator();

			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				it.remove();

				if (key.isAcceptable()) {
					System.out.println("Key is Acceptable");
					ServerSocketChannel ssc = (ServerSocketChannel) key
							.channel();
					socket = (SocketChannel) ssc.accept();
					socket.configureBlocking(false);
					SelectionKey another = socket.register(sel,
							SelectionKey.OP_READ);
				}
				if (key.isReadable()) {
					System.out.println("Key is readable");
					String ret = readMessage(key);
					if (ret.length() > 0) {
						writeMessage(socket, ret);
					}
				}
				if (key.isWritable()) {
					System.out.println("THe key is writable");
					String ret = readMessage(key);
					socket = (SocketChannel) key.channel();
					if (result.length() > 0) {
						writeMessage(socket, ret);
					}
				}
			}
			try {
				Thread.sleep(5000);
				System.out.println("Thread.sleep(5000);");
			} catch (Exception e) {
				e.toString();
			}
		}
	}

	public void writeMessage(SocketChannel socket, String ret) {
		System.out.println("Inside the loop");

		if (ret.equals("quit") || ret.equals("shutdown")) {
			return;
		}
		try {

			String s = "This is context from server!-----------------------------------------";
			Charset set = Charset.forName("us-ascii");
			CharsetDecoder dec = set.newDecoder();
			CharBuffer charBuf = dec.decode(ByteBuffer.wrap(s.getBytes()));
			System.out.println(charBuf.toString());
			int nBytes = socket.write(ByteBuffer.wrap((charBuf.toString())
					.getBytes()));
			System.out.println("nBytes = " + nBytes);
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String readMessage(SelectionKey key) {
		int nBytes = 0;
		socket = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocate(1024);
		try {
			nBytes = socket.read(buf);
			buf.flip();
			Charset charset = Charset.forName("us-ascii");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(buf);
			result = charBuffer.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String args[]) {
		NonBlockingServer nb;
		if (args.length < 1) {
			nb = new NonBlockingServer();
		} else {
			int port = Integer.parseInt(args[0]);
			nb = new NonBlockingServer(port);
		}

		try {
			nb.startServer();
			System.out.println("the nonBlocking server is started!");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}
}
