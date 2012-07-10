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
public class Submit extends Request {

	private byte[] msgId = new byte[8];
	private byte pkTotal = 1;
	private byte pkNumber = 1;
	private byte needReport = 1;
	private byte priority =0;
	private String serviceId = "";
	private byte feeUserType = 0;
	private String feeTermId = "";
	private byte feeTermType = 0;
	private byte tpPid = 0;
	private byte tpUdhi = 0;
	private String msgSrc = "";
	private String feeType = "";
	private String feeCode = "";
	private String validTime = "";
	private String atTime = "";
	private String srcId = "";
	private byte destTermIdCount = 0;
	private String destTermId[] = new String[0];
	private byte destTermIdType = 0;
	private ShortMessage sm = new ShortMessage();
	private String linkId = "";
	private byte[] respMsgId = new byte[8];
	private int respStatus;
	private String dbid = "";

	public int getRespStatus() {
		return respStatus;
	}

	public void setRespStatus(int respStatus) {
		this.respStatus = respStatus;
	}

	public Submit() {
		super(CmppConstant.CMD_SUBMIT);
	}

	protected Response createResponse() {
		return new SubmitResp();
	}

	public void setBody(ByteBuffer buffer)
	throws PDUException {
		try {
			msgId = buffer.removeBytes(8).getBuffer();
			pkTotal = buffer.removeByte();
			pkNumber = buffer.removeByte();
			needReport = buffer.removeByte();
			priority = buffer.removeByte();
			serviceId = buffer.removeStringEx(10);
			feeUserType = buffer.removeByte();
			feeTermId = buffer.removeStringEx(32);
			feeTermType = buffer.removeByte();
			tpPid = buffer.removeByte();
			tpUdhi = buffer.removeByte();
			byte msgFormat = buffer.removeByte();
			msgSrc = buffer.removeStringEx(6);
			feeType = buffer.removeStringEx(2);
			feeCode = buffer.removeStringEx(6);
			validTime = buffer.removeStringEx(17);
			atTime = buffer.removeStringEx(17);
			srcId = buffer.removeStringEx(21);
			destTermIdCount = buffer.removeByte();
			destTermId = new String[destTermIdCount];
			for(int i = 0; i<destTermIdCount; i++)
				destTermId[i] = buffer.removeStringEx(32);
			destTermIdType = buffer.removeByte();
			byte signbyte = buffer.removeByte();
			int msgLength = signbyte < 0 ? signbyte + 256 : signbyte;
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
		buffer.appendByte(pkTotal);
		buffer.appendByte(pkNumber);
		buffer.appendByte(needReport);
		buffer.appendByte(priority);
		buffer.appendString(serviceId, 10);
		buffer.appendByte(feeUserType);
		buffer.appendString(feeTermId, 32);
		buffer.appendByte(feeTermType);
		buffer.appendByte(tpPid);
		buffer.appendByte(tpUdhi);
		buffer.appendByte(sm.getMsgFormat());
		buffer.appendString(msgSrc, 6);
		buffer.appendString(feeType, 2);
		buffer.appendString(feeCode, 6);
		buffer.appendString(validTime, 17);
		buffer.appendString(atTime, 17);
		buffer.appendString(srcId, 21);
		buffer.appendByte((byte)destTermId.length);
		for(int i = 0; i < destTermId.length; i++)
			buffer.appendString(destTermId[i],32);
		buffer.appendByte(destTermIdType);
		buffer.appendByte((byte)sm.getLength());
		buffer.appendBuffer(sm.getData());
		buffer.appendString(linkId, 20);
		return buffer;
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

	public String getAtTime() {
		return atTime;
	}

	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}

	public String[] getDestTermId() {
		return destTermId;
	}

	public void setDestTermId(String[] destTermId) {
		this.destTermId = destTermId;
	}

	public byte getDestTermIdCount() {
		return destTermIdCount;
	}

	public void setDestTermIdCount(byte destTermIdCount) {
		this.destTermIdCount = destTermIdCount;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFeeTermId() {
		return feeTermId;
	}

	public void setFeeTermId(String feeTermId) {
		this.feeTermId = feeTermId;
	}

	public byte getFeeTermType() {
		return feeTermType;
	}

	public void setFeeTermType(byte feeTermType) {
		this.feeTermType = feeTermType;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public byte getFeeUserType() {
		return feeUserType;
	}

	public void setFeeUserType(byte feeUserType) {
		this.feeUserType = feeUserType;
	}

	public byte[] getMsgId() {
		return msgId;
	}

	public void setMsgId(byte[] msgId) {
		this.msgId = msgId;
	}

	public String getMsgSrc() {
		return msgSrc;
	}

	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}

	public byte getNeedReport() {
		return needReport;
	}

	public void setNeedReport(byte needReport) {
		this.needReport = needReport;
	}

	public byte getPkNumber() {
		return pkNumber;
	}

	public void setPkNumber(byte pkNumber) {
		this.pkNumber = pkNumber;
	}

	public byte getPkTotal() {
		return pkTotal;
	}

	public void setPkTotal(byte pkTotal) {
		this.pkTotal = pkTotal;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
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

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public byte getDestTermIdType() {
		return destTermIdType;
	}

	public void setDestTermIdType(byte destTermIdType) {
		this.destTermIdType = destTermIdType;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
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
	
	public void setSm(ShortMessage sm) {
		this.sm = sm;
	}
	
	public ShortMessage getSm() {
		return this.sm;
	}
	
	public String dump() {
		String rt =  "\r\nSubmit.dump**************************************"
					+"\r\nmsgId:			"+cmpp.pdu.Tools.byteArray2HexString(getMsgId())
					+"\r\ntpUdhi:			"+getTpUdhi()
					+"\r\nmsgSrc:			"+getMsgSrc()
					+"\r\nsrcId:			"+getSrcId()
					+"\r\ndestTermId:		"+getDestTermId()[0]
					+"\r\nmsgFormat:		"+getMsgFormat()
					+"\r\nmsgLength:		"+getMsgLength()
					+"\r\nmsgContent:		"+new String(sm.getData().getBuffer())
					+"\r\n***************************************Submit.dump";
		return rt;
	}

	public String name() {
		return "CMPP Submit";
	}

	public byte[] getRespMsgId() {
		return respMsgId;
	}

	public void setRespMsgId(byte[] respMsgId) {
		this.respMsgId = respMsgId;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
}
