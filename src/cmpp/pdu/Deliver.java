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
import cmpp.sms.ShortMessage;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Deliver extends Request {

	private byte[] msgId = new byte[8];
	private String dstId = "";
	private String serviceId = "";
	private byte tpPid = 0;
	private byte tpUdhi = 0;
	private String srcTermId = "";
	private byte srcTermType = 0;
	private byte isReport = 0;
	private ShortMessage sm = new ShortMessage();
	private String linkId = "";
	
	public Deliver() {
		super(CmppConstant.CMD_DELIVER);
	}

	protected Response createResponse() {
		return new DeliverResp();
	}

	public void setBody(ByteBuffer buffer)
		throws PDUException {
		try {
			msgId = buffer.removeBytes(8).getBuffer();
			dstId = buffer.removeStringEx(21);
			serviceId = buffer.removeStringEx(10);
			tpPid = buffer.removeByte();
			tpUdhi = buffer.removeByte();	
			byte msgFormat = buffer.removeByte();
			srcTermId = buffer.removeStringEx(32);
			srcTermType = buffer.removeByte();
			isReport = buffer.removeByte();
			byte signbyte = buffer.removeByte();
			int msgLength = signbyte < 0 ? signbyte + 256 : signbyte;
			if (msgLength>0)
				sm.setData(buffer.removeBuffer(msgLength));
			sm.setMsgFormat(msgFormat);
			linkId = buffer.removeStringEx(20);
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		} 
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(msgId, 8);
		buffer.appendString(dstId, 21);
		buffer.appendString(serviceId, 10);
		buffer.appendByte(tpPid);
		buffer.appendByte(tpUdhi);
		buffer.appendByte(sm.getMsgFormat());
		buffer.appendString(srcTermId,32);
		buffer.appendByte(srcTermType);
		buffer.appendByte(isReport);
		buffer.appendByte((byte)sm.getLength());
		buffer.appendBuffer(sm.getData());
		buffer.appendString(linkId, 20);
		return buffer;
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

	public String getDstId() {
		return dstId;
	}

	public void setDstId(String dstId) {
		this.dstId = dstId;
	}

	public byte getIsReport() {
		return isReport;
	}

	public void setIsReport(byte isReport) {
		this.isReport = isReport;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public byte[] getMsgId() {
		return msgId;
	}

	public void setMsgId(byte[] msgId) {
		this.msgId = msgId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSrcTermId() {
		return srcTermId;
	}

	public void setSrcTermId(String srcTermId) {
		this.srcTermId = srcTermId;
	}

	public byte getSrcTermType() {
		return srcTermType;
	}

	public void setSrcTermType(byte srcTermType) {
		this.srcTermType = srcTermType;
	}

	public byte getTpPid() {
		return tpPid;
	}

	public void setTpPid(byte tpPid) {
		this.tpPid = tpPid;
	}

	public byte getTpUdhi() {
		return tpUdhi;
	}

	public void setTpUdhi(byte tpUdhi) {
		this.tpUdhi = tpUdhi;
	}
	
	public String getMsgContent() {
		return sm.getMessage();
	}

	public byte getMsgFormat() {
		return sm.getMsgFormat();
	}
	
	public byte getMsgLength() {
		return (byte)sm.getLength();
	}
	
	public ShortMessage getSm() {
		return sm;
	}

	public void setSm(ShortMessage sm) {
		this.sm = sm;
	}

	public String dump() {
		String rt =  "\r\nDeliver.dump***************************************"
					+"\r\nseqNo:		"+this.getSequenceNumber()
					+"\r\nmsgId:		"+getMsgId()
					+"\r\ndstId:		"+getDstId()
					+"\r\nserviceId:	"+getServiceId()
					+"\r\ntpPid:		"+getTpPid()
					+"\r\ntpUdhi:		"+getTpUdhi()
					+"\r\nsrcTermId:	"+getSrcTermId()
					+"\r\nsrcTermType:	"+getSrcTermType()
					+"\r\nisReport:		"+getIsReport()
					+"\r\nmsgFormat:	"+getMsgFormat()
					+"\r\nmsgLength:	"+getMsgLength()
					+"\r\nmsgContent:	"+getMsgContent()
					+"\r\nlinkId:		"+getLinkId()
					+"\r\n***************************************Deliver.dump";
		return rt;
	}
	
	public String name() {
		return "CMPP Deliver";
	}
}
