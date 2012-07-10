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
public class ConnectResp extends Response {
	
	private int status = 0;
	private String authServer = "";
	private byte version = 0;
	
	public ConnectResp() {
		super(CmppConstant.CMD_CONNECT_RESP);
	}
	
	/**
	 * @return Returns the authServer.
	 */
	public String getAuthServer() {
		return authServer;
	}
	/**
	 * @param authServer The authServer to set.
	 */
	public void setAuthServer(String authServer) {
		this.authServer = authServer;
	}
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return Returns the version.
	 */
	public byte getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(byte version) {
		this.version = version;
	}
	

	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			setStatus(buffer.removeInt());
			setAuthServer(buffer.removeStringEx(16));
			setVersion(buffer.removeByte());
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendInt(getStatus());
		buffer.appendString(getAuthServer(), 16);
		buffer.appendByte(getVersion());
		return buffer;
	}

	/* (non-Javadoc)
	 * @see cmpp.sms.ByteData#setData(cmpp.sms.util.ByteBuffer)
	 */
	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	/* (non-Javadoc)
	 * @see cmpp.sms.ByteData#getData()
	 */
	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(CmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}
	
	public String name() {
		return "CMPP ConnectResp";
	}
	
	public String dump() {
		String rt =  "\r\nnConnectResp******************************************"
					+"\r\nstatus:		"+status
					+"\r\nauthServer:	"+authServer
					+"\r\nversion:		"+version
					+"\r\nConnectResp******************************************";
		return rt;
	}
}
