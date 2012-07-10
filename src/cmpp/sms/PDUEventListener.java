/*
 * Created on 2005-5-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.sms;

import java.util.EventListener;

/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface PDUEventListener extends EventListener {
	
	public abstract void handleEvent(PDUEvent event);
	
	public abstract void connectionLost();
}
