package cmpp.sms;


public class InvalidPDUException extends PDUException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -798250632133116895L;

	public InvalidPDUException(Exception e) {
		super(e);
	}

	public InvalidPDUException(String s) {
		super(s);
	}

}

