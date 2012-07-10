package cmpp.sms;


import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;


public class TCPIPConnection extends Connection {

	private String remoteAddress = null;

	private int remotePort = 0;

	private String localAddress = null;
	
	private Socket socket = null;

	private BufferedInputStream inputStream = null;

	private BufferedOutputStream outputStream = null;

	private boolean opened = false;

	private ServerSocket receiverSocket = null;

	private byte connType = CONN_NONE;

	private static final byte CONN_NONE = 0;

	private static final byte CONN_CLIENT = 1;

	private static final byte CONN_SERVER = 2;

	private static final int DFLT_IO_BUF_SIZE = 2 * 1024;

	private static final int DFLT_RECEIVE_BUFFER_SIZE = 4 * 1024;

	private static final int DFLT_MAX_RECEIVE_SIZE = 128 * 1024;

	private int ioBufferSize = DFLT_IO_BUF_SIZE;

	private int receiveBufferSize;

	private byte[] receiveBuffer;

	private int maxReceiveSize = DFLT_MAX_RECEIVE_SIZE;

	public TCPIPConnection(int port) {
		if ((port >= SmsConstant.MIN_VALUE_PORT) && (port <= SmsConstant.MAX_VALUE_PORT)) {
			this.remotePort = port;
		} else {
			logger.error("Invalid remotePort.");
		}
		connType = CONN_SERVER;
	}

	public TCPIPConnection(String listenIp, int listenPort) {
		if (listenIp.length() >= SmsConstant.MIN_LENGTH_ADDRESS) {
			this.remoteAddress = listenIp;
		} else {
			logger.error("Invalid remoteAddress.");
		}
		if ((listenPort >= SmsConstant.MIN_VALUE_PORT) && (listenPort <= SmsConstant.MAX_VALUE_PORT)) {
			this.remotePort = listenPort;
		} else {
			logger.error("Invalid remotePort.");
		}
		connType = CONN_SERVER;
	}
	
	public TCPIPConnection(String remoteAddress, int remotePort, String localAddress) {
		if (remoteAddress.length() >= SmsConstant.MIN_LENGTH_ADDRESS) {
			this.remoteAddress = remoteAddress;
		} else {
			logger.error("Invalid remoteAddress.");
		}
		if ((remotePort >= SmsConstant.MIN_VALUE_PORT) && (remotePort <= SmsConstant.MAX_VALUE_PORT)) {
			this.remotePort = remotePort;
		} else {
			logger.error("Invalid remotePort.");
		}
		if (localAddress.length() >= SmsConstant.MIN_LENGTH_ADDRESS) {
			this.localAddress = localAddress;
		} else {
			logger.error("Invalid localAddress.");
		}
		connType = CONN_CLIENT;
		setReceiveBufferSize(DFLT_RECEIVE_BUFFER_SIZE);
	}

	public TCPIPConnection(Socket socket) throws IOException {
		connType = CONN_CLIENT;
		this.socket = socket;
		remoteAddress = socket.getInetAddress().getHostAddress();
		remotePort = socket.getPort();
		initialiseIOStreams(socket);
		opened = true;
		setReceiveBufferSize(DFLT_RECEIVE_BUFFER_SIZE);
	}

	public void open() throws IOException {
		logger.debug("TCPIPConnection.open enter: address["+remoteAddress+"],["+remotePort+"]");
		IOException exception = null;
		if (!opened) {
			if (connType == CONN_CLIENT) {
				try {
					socket = new Socket(remoteAddress, remotePort, InetAddress.getByName(localAddress), 0);
					initialiseIOStreams(socket);
					opened = true;
					logger.debug("opened client tcp/ip connection to " + remoteAddress + " on remotePort " + remotePort);
				} catch (IOException e) {
					logger.error("IOException opening TCPIPConnection ", e);
					exception = e;
				}
			} else if (connType == CONN_SERVER) {
				try {
					receiverSocket = new ServerSocket(remotePort,30,InetAddress.getByName(remoteAddress));
					opened = true;
					logger.debug("listening tcp/ip on remotePort " + remotePort);
				} catch (IOException e) {
					logger.error("IOException creating listener socket ", e);
					exception = e;
				}
			} else {
				logger.error("Unknown connection type = " + connType);
			}
		} else {
			logger.error("attempted to open already opened connection ");
		}
		logger.debug("TCPIPConnection.open exit");
		if (exception != null) {
			throw exception;
		}
	}

	public void close() throws IOException {
		logger.debug("TCPIPConnection.close enter");
		IOException exception = null;

		if (connType == CONN_CLIENT) {
			try {
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null) 
					outputStream.close();
				if (socket != null) 
					socket.close();
				socket = null;
				opened = false;
				logger.debug("closed client tcp/ip connection to " + remoteAddress + " on remotePort " + remotePort);
			} catch (IOException e) {
				logger.error("IOException closing socket ", e);
				exception = e;
			}
		} else if (connType == CONN_SERVER) {
			try {
				if (receiverSocket != null)
					receiverSocket.close();
				receiverSocket = null;
				opened = false;
				logger.debug("stopped listening tcp/ip on remotePort " + remotePort);
			} catch (IOException e) {
				logger.error("IOException closing listener socket " + e);
				exception = e;
			}
		} else {
			logger.error("Unknown connection type = " + connType);
		}

		logger.debug("TCPIPConnection.close exit");
		if (exception != null) {
			throw exception;
		}
	}

	public void send(ByteBuffer data) throws IOException {
		logger.debug("TCPIPConnection.send enter");
		IOException exception = null;

		if (outputStream == null) {
			logger.debug("TCPIPConnection.send exit");
			throw new IOException("Not connected");
		}
		if (connType == CONN_CLIENT) {
			try {
				socket.setSoTimeout((int) getCommsTimeout());
				try {
					outputStream.write(data.getBuffer(), 0, data.length());
					logger.debug("sent " + data.length() + " bytes over connection");
				} catch (IOException e) {
					logger.error("IOException sending data ", e);
					exception = e;
				}
				outputStream.flush();
			} catch (IOException e) {
				logger.error("IOException flushing data ", e);
				if (exception == null) {
					exception = e;
				}
			}
		} else if (connType == CONN_SERVER) {
			logger.error("Attempt to send data over server type connection.");
		} else {
			logger.error("Unknown connection type = " + connType);
		}
		logger.debug("TCPIPConnection.send exit");
		if (exception != null) {
			throw exception;
		}
	}

	public ByteBuffer receive() throws IOException {
		logger.debug("TCPIPConnection.receive enter");
		IOException exception = null;

		ByteBuffer data = null;
		if (connType == CONN_CLIENT) {
			data = new ByteBuffer();
			long endTime = System.currentTimeMillis() + getReceiveTimeout();
			//int bytesAvailable = 0;
			int bytesToRead = 0;
			int bytesRead = 0;
			int totalBytesRead = 0;

			try {
				socket.setSoTimeout((int) getCommsTimeout());
				bytesToRead = receiveBufferSize;
				logger.debug("going to read from socket");		
				do {
					bytesRead = 0;
					try {
						bytesRead = inputStream.read(receiveBuffer, 0, bytesToRead);
					} catch (InterruptedIOException e) {
						// comms read timeout expired, no problem
						logger.info("timeout reading from socket");
					}
					if (bytesRead > 0) {
						logger.debug("read " + bytesRead + " bytes from socket");
						data.appendBytes(receiveBuffer, bytesRead);
						totalBytesRead += bytesRead;
					}
					if (bytesRead == -1) {
						throw new EOFException("Reached end of stream");
					}

					bytesToRead = inputStream.available();
					if (bytesToRead > 0) {
						logger.debug("more data (" + bytesToRead + " bytes) remains in the socket");
					} else {
						logger.debug("no more data remains in the socket");
					}
					if (bytesToRead > receiveBufferSize) {
						bytesToRead = receiveBufferSize;
					}
					if (totalBytesRead + bytesToRead > maxReceiveSize) {
						// would be more than allowed
						bytesToRead = maxReceiveSize - totalBytesRead;
					}
				} while (
					((bytesToRead != 0) && (System.currentTimeMillis() <= endTime)) && (totalBytesRead < maxReceiveSize));

				logger.debug("totally read " + data.length() + " bytes from socket");
			} catch (IOException e) {
				exception = e;
			}
		} else if (connType == CONN_SERVER) {
			logger.error("Attempt to receive data from server type connection.");
		} else {
			logger.error("Unknown connection type = " + connType);
		}

		logger.debug("TCPIPConnection.receive exit");
		if (exception != null) {
			throw exception;
		}
		return data;
	}

	public ByteBuffer receiveEx(int bytesToRead) throws IOException,java.net.SocketTimeoutException  {
		logger.debug("TCPIPConnection.receiveEx enter");
		ByteBuffer data = null;
		if (connType == CONN_CLIENT) {
			data = new ByteBuffer();
			long endTime = System.currentTimeMillis() + getReceiveTimeout();
			//int bytesAvailable = 0;
			int bytesRead = 0;
			int totalBytesRead = 0;

			try {
				socket.setSoTimeout((int) getCommsTimeout());
				logger.debug("going to read from socket");		
				do {
					bytesRead = 0;
					bytesRead = inputStream.read(receiveBuffer, 0, bytesToRead);
					logger.debug(""+bytesRead+" bytes received.");
					if (bytesRead == -1) {
						throw new EOFException("Reached end of stream");
					}
					if (bytesRead > 0) {
						logger.debug("read " + bytesRead + " bytes from socket");
						data.appendBytes(receiveBuffer, bytesRead);
						totalBytesRead += bytesRead;
					}
					bytesToRead -= bytesRead;
				} while (
					((bytesToRead != 0) && (System.currentTimeMillis() <= endTime)) && (totalBytesRead < maxReceiveSize));

			} catch (java.net.SocketTimeoutException e) {
				//logger.warn("Socket timeout!", e);
				throw e;
			} catch (EOFException e) {
				//logger.warn("Reach end of stream!", e);
				throw e;
			} catch (IOException e) {
				logger.warn("Socket receive error!", e);
				throw e;
			}
		} else if (connType == CONN_SERVER) {
			logger.error("Attempt to receive data from server type connection.");
		} else {
			logger.error("Unknown connection type = " + connType);
		}
		logger.debug("TCPIPConnection.receive exit");
		return data;
	}
	
	public Connection accept() throws IOException {
		logger.debug("TCPIPConnection.accept enter");
		IOException exception = null;

		Connection newConn = null;
		if (connType == CONN_SERVER) {
			try {
				receiverSocket.setSoTimeout((int) getReceiveTimeout());
			} catch (SocketException e) {
				// don't care, we're just setting the timeout
			}
			Socket acceptedSocket = null;
			try {
				acceptedSocket = receiverSocket.accept();
			} catch (java.net.SocketTimeoutException e) {
				//
			} catch (IOException e) {
				logger.debug("Exception accepting socket (timeout?)", e);
			}
			if (acceptedSocket != null) {
				try {
					newConn = new TCPIPConnection(acceptedSocket);
				} catch (IOException e) {
					logger.error("IOException creating new client connection ", e);
					exception = e;
				}
			}
		} else if (connType == CONN_CLIENT) {
			logger.error("Attempt to receive data from client type connection.");
		} else {
			logger.error("Unknown connection type = " + connType);
		}

		logger.debug("TCPIPConnection.accept exit");
		if (exception != null) {
			throw exception;
		}
		return newConn;
	}
	
	final public Socket acceptEx() throws IOException {
		logger.debug("TCPIPConnection.accept enter");
		Socket acceptedSocket = null;
		if (connType == CONN_SERVER) {
			try {
				acceptedSocket = receiverSocket.accept();
			} catch (IOException e) {
				logger.warn("Exception accepting socket (timeout?)", e);
				throw e;
			}
		} else if (connType == CONN_CLIENT) {
			logger.error("Attempt to receive data from client type connection.");
		} else {
			logger.error("Unknown connection type = " + connType);
		}
		logger.debug("TCPIPConnection.accept exit");
		return acceptedSocket;
	}

	private void initialiseIOStreams(Socket socket) throws IOException {
		if (connType == CONN_CLIENT) {
			inputStream = new BufferedInputStream(socket.getInputStream(), ioBufferSize);
			outputStream = new BufferedOutputStream(socket.getOutputStream(), ioBufferSize);
		} else if (connType == CONN_SERVER) {
			logger.error("Attempt to initialise i/o streams for server type connection.");
		} else {
			logger.error("Unknown connection type = " + connType);
		}
	}

	public void setIOBufferSize(int ioBufferSize) {
		if (!opened) {
			this.ioBufferSize = ioBufferSize;
		}
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
		receiveBuffer = new byte[receiveBufferSize];
	}

	public void setMaxReceiveSize(int maxReceiveSize) {
		this.maxReceiveSize = maxReceiveSize;
	}
	
	public int available() throws IOException {
		return inputStream.available();
	}

}

