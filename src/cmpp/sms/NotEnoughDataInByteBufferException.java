package cmpp.sms;

import cmpp.sms.SmsException;


public class NotEnoughDataInByteBufferException extends SmsException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5084315197182138348L;
	private int available;
	private int expected;

	public NotEnoughDataInByteBufferException(int p_available, int p_expected) {
		super("Not enough data in byte buffer. " + "Expected " + p_expected + ", available: " + p_available + ".");
		available = p_available;
		expected = p_expected;
	}

	public NotEnoughDataInByteBufferException(String s) {
		super(s);
		available = 0;
		expected = 0;
	}

	public int getAvailable() {
		return available;
	}

	public int getExpected() {
		return expected;
	}
}

