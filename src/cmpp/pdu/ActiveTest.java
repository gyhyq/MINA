/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.pdu;


import cmpp.CmppConstant;
import cmpp.sms.PDUException;
import cmpp.sms.ByteBuffer;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ActiveTest extends Request {

	public ActiveTest() {
		super(CmppConstant.CMD_ACTIVE_TEST);
	}

	protected Response createResponse() {
		return new ActiveTestResp();
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
	}

	public ByteBuffer getData() {
		return header.getData();
	}
	
	public String name() {
		return "CMPP ActiveTest";
	}
}
