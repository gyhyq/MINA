/*
 * Created on 2005-5-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.pdu;


import cmpp.CmppConstant;
import cmpp.sms.PDUException;
import cmpp.sms.ByteBuffer;
import cmpp.sms.NotEnoughDataInByteBufferException;


/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Query extends Request {

	private String time = "";
	private byte queryType = 0x00;
	private String queryCode = "";
	private String reserve = "";
	
	protected Response createResponse() {
		return new QueryResp();
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(CmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}
	
	private ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(time, 8);
		buffer.appendByte(queryType);
		buffer.appendString(queryCode, 10);
		buffer.appendString(reserve, 8);
		return buffer;
	}

	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			time = buffer.removeStringEx(8);
			queryType = buffer.removeByte();
			queryCode = buffer.removeStringEx(10);
			reserve = buffer.removeStringEx(8);
		} catch (NotEnoughDataInByteBufferException e) {
			e.printStackTrace();
			throw new PDUException(e);
		}
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(byte queryType) {
		this.queryType = queryType;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public String name() {
		return "CMPP Query";
	}
}
