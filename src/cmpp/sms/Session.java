/*
 * Created on 2005-5-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.sms;

/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
abstract public class Session extends SmsObject {

	protected boolean connected = false;
	
	abstract public void start();
	
	abstract public void stop();
	
	abstract public byte getSessionStatus();
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}	

