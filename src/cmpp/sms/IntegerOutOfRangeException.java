package cmpp.sms;


public class IntegerOutOfRangeException extends PDUException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1347624981736409051L;

	public IntegerOutOfRangeException() {
		super("The integer is lower or greater than required.");
	}

	public IntegerOutOfRangeException(int min, int max, int val) {
		super("The integer is lower or greater than required: " + " min=" + min + " max=" + max + " actual=" + val + ".");
	}
}
