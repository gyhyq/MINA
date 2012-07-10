/*
 * Created on 2005-5-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.sms;

import java.util.EventObject;

/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PDUEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7423431540841213138L;

	private transient Connection connection = null;

	private transient ByteBuffer buffer = null;
	
	public PDUEvent(Object source, Connection connection, ByteBuffer buffer) {
		super(source);
		this.connection = connection;
		this.buffer = buffer;
	}

	/**
	 * @return Returns the buffer.
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}
	/**
	 * @return Returns the connection.
	 */
	public Connection getConnection() {
		return connection;
	}
}
