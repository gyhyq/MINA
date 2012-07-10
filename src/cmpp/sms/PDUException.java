package cmpp.sms;

import cmpp.sms.SmsException;


public class PDUException extends SmsException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2184183203286999926L;

	public PDUException() {
		super();
	}
	
	public PDUException(String s) {
		super(s);
	}
	
	public PDUException(Exception e) {
		super(e);
	}
}

