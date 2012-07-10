package cmpp.sms;


public class WrongLengthOfStringException extends PDUException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5230716137456261476L;

	public WrongLengthOfStringException() {
		super("The string is shorter or longer than required.");
	}

	public WrongLengthOfStringException(int min, int max, int actual) {
		super(
			"The string is shorter or longer than required: "
				+ " min="
				+ min
				+ " max="
				+ max
				+ " actual="
				+ actual
				+ ".");
	}
}
