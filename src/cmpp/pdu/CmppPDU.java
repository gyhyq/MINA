/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cmpp.pdu;

import cmpp.sms.PDU;


/**
 * @author lucien
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class CmppPDU extends PDU  {

	private static int sequenceNumber = 0;

	private boolean sequenceNumberChanged = false;

	public CmppPDUHeader header = null;

	public CmppPDU() {
		header = new CmppPDUHeader();
	}

	public CmppPDU(int commandId) {
		header = new CmppPDUHeader();
		header.setCommandId(commandId);
	}

	/** Checks if the header field is null and if not, creates it. */
	private void checkHeader() {
		if (header == null) {
			header = new CmppPDUHeader();
		}
	}

	public int getCommandLength() {
		checkHeader();
		return header.getCommandLength();
	}

	public int getCommandId() {
		checkHeader();
		return header.getCommandId();
	}

	public int getSequenceNumber() {
		checkHeader();
		return header.getSequenceNumber();
	}

	public void setCommandLength(int cmdLen) {
		checkHeader();
		header.setCommandLength(cmdLen);
	}

	public void setCommandId(int cmdId) {
		checkHeader();
		header.setCommandId(cmdId);
	}

	public void setSequenceNumber(int seqNr) {
		checkHeader();
		header.setSequenceNumber(seqNr);
	}

	public void assignSequenceNumber() {
		assignSequenceNumber(false);
	}

	public void assignSequenceNumber(boolean always) {
		if ((!sequenceNumberChanged) || always) {
			synchronized (this) {
				setSequenceNumber(++sequenceNumber);
			}
			sequenceNumberChanged = true;
		}
	}

	public void resetSequenceNumber() {
		setSequenceNumber(0);
		sequenceNumberChanged = false;
	}

	public boolean equals(Object object) {
		if ((object != null) && (object instanceof CmppPDU)) {
			CmppPDU pdu = (CmppPDU) object;
			return pdu.getSequenceNumber() == getSequenceNumber();
		} else {
			return false;
		}
	}

	public String getSequenceNumberAsString() {
		int data = header.getSequenceNumber();
		byte[] intBuf = new byte[4];
		intBuf[3] = (byte) (data & 0xff);
		intBuf[2] = (byte) ((data >>> 8) & 0xff);
		intBuf[1] = (byte) ((data >>> 16) & 0xff);
		intBuf[0] = (byte) ((data >>> 24) & 0xff);
		return new String(intBuf);
	}

	public abstract boolean isRequest();

	public abstract boolean isResponse();

	public String dump() {
		return name() + " dump() unimplemented";
	}


}
