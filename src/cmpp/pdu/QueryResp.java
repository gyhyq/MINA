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
public class QueryResp extends Response {

	private String time = "";
	private int queryType = 0;	
	private String queryCode = "";	
	private int mt_tlmsg = 0;	
	private int mt_tlusr = 0;	
	private int mt_scs = 0;	
	private int mt_wt = 0;	
	private int mt_fl = 0;	
	private int mo_scs = 0;	
	private int mo_wt = 0;	
	private int mo_fl = 0;	

	public int getMo_fl() {
		return mo_fl;
	}

	public void setMo_fl(int mo_fl) {
		this.mo_fl = mo_fl;
	}

	public int getMo_scs() {
		return mo_scs;
	}

	public void setMo_scs(int mo_scs) {
		this.mo_scs = mo_scs;
	}

	public int getMo_wt() {
		return mo_wt;
	}

	public void setMo_wt(int mo_wt) {
		this.mo_wt = mo_wt;
	}

	public int getMt_fl() {
		return mt_fl;
	}

	public void setMt_fl(int mt_fl) {
		this.mt_fl = mt_fl;
	}

	public int getMt_scs() {
		return mt_scs;
	}

	public void setMt_scs(int mt_scs) {
		this.mt_scs = mt_scs;
	}

	public int getMt_tlmsg() {
		return mt_tlmsg;
	}

	public void setMt_tlmsg(int mt_tlmsg) {
		this.mt_tlmsg = mt_tlmsg;
	}

	public int getMt_tlusr() {
		return mt_tlusr;
	}

	public void setMt_tlusr(int mt_tlusr) {
		this.mt_tlusr = mt_tlusr;
	}

	public int getMt_wt() {
		return mt_wt;
	}

	public void setMt_wt(int mt_wt) {
		this.mt_wt = mt_wt;
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

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	protected ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(getTime(),8);
		buffer.appendInt(getQueryType());
		buffer.appendString(getQueryCode(), 10);
		buffer.appendInt(getMt_tlmsg());
		buffer.appendInt(getMt_tlusr());
		buffer.appendInt(getMt_scs());
		buffer.appendInt(getMt_wt());
		buffer.appendInt(getMt_fl());
		buffer.appendInt(getMo_scs());
		buffer.appendInt(getMo_wt());
		buffer.appendInt(getMo_fl());
		return buffer;
	}

	public void setBody(ByteBuffer buffer)
	throws PDUException {
		try {
			setTime(buffer.removeStringEx(8));
			setQueryType(buffer.removeInt());
			setQueryCode(buffer.removeStringEx(10));
			setMt_tlmsg(buffer.removeInt());
			setMt_tlusr(buffer.removeInt());
			setMt_scs(buffer.removeInt());
			setMt_wt(buffer.removeInt());
			setMt_fl(buffer.removeInt());
			setMo_scs(buffer.removeInt());
			setMo_wt(buffer.removeInt());
			setMo_fl(buffer.removeInt());
		} catch (NotEnoughDataInByteBufferException e) {
			e.printStackTrace();
			throw new PDUException(e);
		}
	}
	
	public String name() {
		return "CMPP QueryResp";
	}
}
