package cmpp.sms;


public class WrongDateFormatException extends PDUException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8742352839745298149L;

	public WrongDateFormatException() {
		super("Date must be either null or of format YYMMDDhhmmsstnnp");
	}

	public WrongDateFormatException(String dateStr) {
		super("Date must be either null or of format YYMMDDhhmmsstnnp and not " + dateStr + ".");
	}

	public WrongDateFormatException(String dateStr, String msg) {
		super("Invalid date " + dateStr + ": " + msg);
	}
}

