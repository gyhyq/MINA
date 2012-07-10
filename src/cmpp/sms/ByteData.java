package cmpp.sms;


import cmpp.sms.SmsObject;


public abstract class ByteData extends SmsObject {


	public abstract void setData(ByteBuffer buffer)
		throws PDUException;


	public abstract ByteBuffer getData() throws ValueNotSetException;


	public ByteData() {
	}


	protected static short decodeUnsigned(byte signed) {
		if (signed >= 0) {
			return signed;
		} else {
			return (short) (256 + (short) signed);
		}
	}


	protected static int decodeUnsigned(short signed) {
		if (signed >= 0) {
			return signed;
		} else {
			return (int) (65536 + (int) signed);
		}
	}


	protected static byte encodeUnsigned(short positive) {
		if (positive < 128) {
			return (byte) positive;
		} else {
			return (byte) (- (256 - positive));
		}
	}


	protected static short encodeUnsigned(int positive) {
		if (positive < 32768) {
			return (byte) positive;
		} else {
			return (short) (- (65536 - positive));
		}
	}


	public String debugString() {
		return new String("");
	}

}

