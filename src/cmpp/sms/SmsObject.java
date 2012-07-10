package cmpp.sms;

import org.apache.log4j.Logger;


public class SmsObject {

	static final public String RT = "\n";
	static protected Logger logger = Logger.getLogger(SmsObject.class.getName());
	
	static public Logger getLogger() {
		return logger;
	}

	static public void setLogger(Logger myLogger) {
		logger = myLogger;
	}
}

