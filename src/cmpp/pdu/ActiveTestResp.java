/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.pdu;


import cmpp.CmppConstant;
import cmpp.sms.NotEnoughDataInByteBufferException;
import cmpp.sms.PDUException;
import cmpp.sms.ByteBuffer;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ActiveTestResp extends Response {

	private byte reserve = 0x00;
	
	public ActiveTestResp() {
		super(CmppConstant.CMD_ACTIVE_TEST_RESP);
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);		
	}

	public ByteBuffer getData() {
		return header.getData();
	}
	
	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			reserve = buffer.removeByte();
		} catch (NotEnoughDataInByteBufferException e) {
			e.printStackTrace();
			throw new PDUException(e);
		}
	}
	
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendInt(reserve);
		return buffer;
	}
	
	public String name() {
		return "CMPP ActiveTestResp";
	}
}
