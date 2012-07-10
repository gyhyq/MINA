/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.pdu;

import cmpp.CmppConstant;
import cmpp.sms.ByteBuffer;
import cmpp.sms.NotEnoughDataInByteBufferException;
import cmpp.sms.PDUException;



/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubmitResp extends Response {

	private byte[] msgId = new byte[8];
	private int result = 0;

	public SubmitResp() {
		super(CmppConstant.CMD_SUBMIT_RESP);
	}

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
	
	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			msgId = buffer.removeBytes(8).getBuffer();
			result = buffer.removeInt();
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}		
	}
	
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(msgId);
		buffer.appendInt(result);
		return buffer;
	}
	
	public byte[] getMsgId() {
		return msgId;
	}

	public void setMsgId(byte[] msgId) {
		this.msgId = msgId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	public String name() {
		return "CMPP SubmitResp";
	}
	
	public String dump() {
		String rt =  "\r\nSubmitResp********************************"
					+"\r\nmsgId:	"+msgId 
					+"\r\nresult:	"+result
					+"\r\n********************************SubmitResp";
		return rt;
	}
	
}
