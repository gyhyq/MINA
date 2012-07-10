package cmpp.sms;


public class UnknownCommandIdException extends PDUException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4269182496314293559L;

	public UnknownCommandIdException() {
	}

	public UnknownCommandIdException(String s) {
		super(s);
	}

}
