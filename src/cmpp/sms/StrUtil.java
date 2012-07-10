package cmpp.sms;

public class StrUtil extends SmsObject {
	
	public static void main(String[] args) {
		System.out.println(bytesToHex(hexToBytes("abcdef12")));
	}
	
	public static final String bytesToHex(byte[] bs, int off, int length) {
		StringBuffer sb = new StringBuffer(length * 2);
		bytesToHexAppend(bs, off, length, sb);
		return sb.toString();
	}

	public static final void bytesToHexAppend(
		byte[] bs,
		int off,
		int length,
		StringBuffer sb) {
		sb.ensureCapacity(sb.length() + length * 2);
		for (int i = off; i < (off + length) && i < bs.length; i++) {
			sb.append(Character.forDigit((bs[i] >>> 4) & 0xf, 16));
			sb.append(Character.forDigit(bs[i] & 0xf, 16));
		}
	}

	public static final String bytesToHex(byte[] bs) {
		return bytesToHex(bs, 0, bs.length);
	}

	public static final byte[] hexToBytes(String s) {
		return hexToBytes(s, 0);
	}

	public static final byte[] hexToBytes(String s, int off) {
		byte[] bs = new byte[off + (1 + s.length()) / 2];
		hexToBytes(s, bs, off);
		return bs;
	}

	/**
	 * Converts a String of hex characters into an array of bytes.
	 * 
	 * @param s
	 *            A string of hex characters (upper case or lower) of even
	 *            length.
	 * @param out
	 *            A byte array of length at least s.length()/2 + off
	 * @param off
	 *            The first byte to write of the array
	 */
	public static final void hexToBytes(String s, byte[] out, int off)
		throws NumberFormatException, IndexOutOfBoundsException {
		int slen = s.length();
		if ((slen % 2) != 0) {
			s = '0' + s;
		}

		if (out.length < off + slen / 2) {
			throw new IndexOutOfBoundsException(
				"Output buffer too small for input ("
					+ out.length
					+ "<"
					+ off
					+ slen / 2
					+ ")");
		}

		// Safe to assume the string is even length
		byte b1, b2;
		for (int i = 0; i < slen; i += 2) {
			b1 = (byte) Character.digit(s.charAt(i), 16);
			b2 = (byte) Character.digit(s.charAt(i + 1), 16);
			if (b1 < 0 || b2 < 0) {
				throw new NumberFormatException();
			}
			out[off + i / 2] = (byte) (b1 << 4 | b2);
		}
	}

}
