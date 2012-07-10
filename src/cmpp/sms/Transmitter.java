package cmpp.sms;

import java.io.IOException;

import cmpp.sms.SmsObject;


public class Transmitter extends SmsObject {

	private Connection connection = null;

	protected Transmitter() {
	}

	public Transmitter(Connection c) {
		connection = c;
	}

	public void send(ByteBuffer buffer) throws IOException {
		logger.debug("TCPIPConnection.send enter");
		try {
			if (buffer == null || buffer.length() == 0) 
				return;
			connection.send(buffer);
			logger.debug("successfully sent pdu's data over connection");
		} finally {
			logger.debug("TCPIPConnection.send exit");
		}
	}

}

