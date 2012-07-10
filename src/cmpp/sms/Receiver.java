package cmpp.sms;

import java.io.IOException;


public class Receiver extends ProcessingThread {

	private static final String RECEIVER_THREAD_NAME = "Receiver";
	
	private Transmitter transmitter = null;

	private Connection connection = null;

	private Unprocessed unprocessed = new Unprocessed();

	private PDUEventListener pduListener = null;
	
	private long receiveTimeout = 0;
	
	public Receiver(Connection connection) {
		this.connection = connection;
	}
	public void process() {
		logger.debug("Receiver.process enter");
		try {
			ByteBuffer buffer = receivePDUFromConnection(connection);
			if (buffer != null) {
				PDUEvent pduEvent = new PDUEvent(this, connection, buffer);
				pduListener.handleEvent(pduEvent);
			}
		} catch (TimeoutException e) {
			logger.info("TimeoutException in Receiver.process().",e);
		} catch (IOException e) {
			logger.warn("IOException in Receiver.process().", e);
			stopProcessing(e);
			logger.error("stoped Receiver.process().");
			pduListener.connectionLost();
		}
		logger.debug("Receiver.process exit");
	}

	final public ByteBuffer receivePDUFromConnectionEx(Connection connection) throws TimeoutException, IOException {
		ByteBuffer buffer = null;
		do {
			buffer = receivePDUFromConnection(connection);
			Thread.yield();
		} while (buffer == null);
		return buffer;
	} 
	
	final protected ByteBuffer receivePDUFromConnection(Connection connection)
	throws IOException, TimeoutException {
		logger.debug("Receiver.receivePDUFromConnection enter");
		logger.debug("has unprocessed bytes: "+unprocessed.getUnprocessed().length());
		ByteBuffer pduBuffer = null;
		ByteBuffer buffer;
		// first check if there is something left from the last time
		if (unprocessed.hasUnprocessed()) {
			pduBuffer = getCompletePDU(unprocessed.getUnprocessed());
		}
		if (pduBuffer == null) { // only if we didn't manage to get pdu from unproc
			logger.debug("Receiver.receivePDUFromConnection");
			buffer = connection.receive();
			// if received something now or have something from the last receive
			if ((buffer != null) && (buffer.length() != 0)) {
				unprocessed.appendBuffer(buffer);
				unprocessed.setLastTimeReceived();
				pduBuffer = getCompletePDU(unprocessed.getUnprocessed());
			} else {
				logger.debug("no data received this time.");
				// check if it's not too long since we received any data
				long timeout = getReceiveTimeout();
				if ((unprocessed.getUnprocessed().length() > 0)
						&& ((unprocessed.getLastTimeReceived() + timeout) < System.currentTimeMillis())) {
					logger.debug("and it's been very long time.");
					unprocessed.reset();
					throw new TimeoutException(timeout, unprocessed.getExpected(), unprocessed.getUnprocessed().length());
				}
			}
		}
		logger.debug("has unprocessed bytes: "+unprocessed.getUnprocessed().length());
		logger.debug("Receiver.receivePDUFromConnection exit");
		return pduBuffer;
	}
	
	final protected ByteBuffer getCompletePDU(ByteBuffer buffer) {
		logger.debug("Receiver.getCompletePDU enter");
		ByteBuffer pduBuffer = null;
		if (buffer.length() >= 4) {
			try {
				int pduLength = buffer.readInt(0);
				if (pduLength <= buffer.length()) {
					pduBuffer = buffer.removeBuffer(pduLength);
				}
			} catch (NotEnoughDataInByteBufferException e) {
				//should never happen
			}
		}
		logger.debug("Receiver.getCompletePDU exit");
		return pduBuffer;
	}

	final protected ByteBuffer receivePDU(Connection conn) {
		logger.debug("Receiver.receivePDU enter");
		ByteBuffer pduBuffer = null;
		try {
			pduBuffer = conn.receiveEx(4);
			int pduLength = pduBuffer.readInt(0);
			pduBuffer.appendBuffer(conn.receiveEx(pduLength-4));
		} catch (java.net.SocketTimeoutException e) {
			pduBuffer = null;
		} catch (IOException e) {
			pduBuffer = null;
		} catch (NotEnoughDataInByteBufferException e) {
			//should never happen
		}
		logger.debug("Receiver.receivePDU exit");
		return pduBuffer;
	}
	
	private boolean canContinueReceiving(long startTime, long timeout) {
		return timeout == SmsConstant.RECEIVE_BLOCKING ? true : System.currentTimeMillis() <= (startTime + timeout);
	}

	public long getReceiveTimeout() {
		return receiveTimeout;
	}

	public void setReceiveTimeout(long receiveTimeout) {
		this.receiveTimeout = receiveTimeout;
	}
	
	public PDUEventListener getPduListener() {
		return pduListener;
	}
	
	public void setPduListener(PDUEventListener pduListener) {
		this.pduListener = pduListener;
	}
	public void connectionLost() {
		this.pduListener.connectionLost();
	}
	public void init() {
		unprocessed.reset();
	}
}

