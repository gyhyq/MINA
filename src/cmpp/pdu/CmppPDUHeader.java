/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.pdu;


import cmpp.CmppConstant;
import cmpp.sms.ByteBuffer;
import cmpp.sms.ByteData;
import cmpp.sms.NotEnoughDataInByteBufferException;
import cmpp.sms.PDUException;


/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CmppPDUHeader extends ByteData {
	private int commandLength = CmppConstant.PDU_HEADER_SIZE;
	private int commandId = 0;
	private int sequenceNumber = 0;

	public ByteBuffer getData() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendInt(getCommandLength());
		buffer.appendInt(getCommandId());
		buffer.appendInt(getSequenceNumber());
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		try {
			commandLength = buffer.removeInt();
			commandId = buffer.removeInt();
			sequenceNumber = buffer.removeInt();
		} catch(NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}

	public int getCommandLength() {
		return commandLength;
	}

	public int getCommandId() {
		return commandId;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setCommandLength(int cmdLen) {
		commandLength = cmdLen;
	}

	public void setCommandId(int cmdId) {
		commandId = cmdId;
	}

	public void setSequenceNumber(int seqNr) {
		sequenceNumber = seqNr;
	}

}
