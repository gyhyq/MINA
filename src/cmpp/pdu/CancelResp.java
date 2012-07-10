package cmpp.pdu;

import cmpp.CmppConstant;
import cmpp.sms.ByteBuffer;
import cmpp.sms.NotEnoughDataInByteBufferException;
import cmpp.sms.PDUException;

public class CancelResp extends Response {

	private int successId = 0;
	
	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(CmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendInt(successId);
		return buffer;
	}
	
	public void setBody(ByteBuffer buffer) 
	throws PDUException {
		try {
			successId = buffer.removeInt();
		} catch (NotEnoughDataInByteBufferException e) {
			e.printStackTrace();
			throw new PDUException(e);
		}
	}
	
	public String name() {
		return "CMPP CancelResp";
	}
}
