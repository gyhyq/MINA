package cmpp.sms;

import cmpp.sms.SmsException;


public class TimeoutException extends SmsException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3787008913827216723L;

	/** The expired timeout. */
	public long timeout = 0;

	/** The expected bytes. */
	public int expected = 0;

	/** The received bytes. */
	public int received = 0;

	/** Don't allow default constructor */
	private TimeoutException() {
	}

	/**
	 * Construct with provided timeout and expected and received amount
	 * of data.
	 */
	public TimeoutException(long timeout, int expected, int received) {
		super(
			"The rest of pdu not received for "
				+ (timeout / 1000)
				+ " seconds. "
				+ "Expected "
				+ expected
				+ " bytes, received "
				+ received
				+ " bytes.");
		this.timeout = timeout;
		this.expected = expected;
		this.received = received;
	}

}
