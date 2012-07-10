package cmpp.sms;


public class SmsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3117037333965625264L;

	public SmsException() {
		super();
	}

	public SmsException(Exception e) {
		super(e);
	}

	public SmsException(String s) {
		super(s);
	}

	public SmsException(String s, Exception e) {
		super(s, e);
	}
}

