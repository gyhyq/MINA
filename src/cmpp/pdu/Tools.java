package cmpp.pdu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
	private static int msgi = 1000;
	private static Object obj =new Object();
	private static Object obj2 =new Object();
	public static byte[] GetMsgid() {
		String s_msg = "";
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
		byte[] msgid = null;
		try {
			synchronized(obj){
				msgi = msgi + 1;
			}
			if (msgi >= 9999)
				msgi = 1000;
			s_msg = time.format(nowTime) + msgi;
			// logger.info(s_msg);
			msgid = hexString2ByteArray(s_msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgid;
	}

	public static byte[] GetRspid() {
		String s_msg = "";
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
		byte[] msgid = null;
		try {
			synchronized(obj2){
				msgi = msgi + 1;
			}
			if (msgi >= 9999)
				msgi = 1000;
			s_msg = time.format(nowTime) + msgi;
			msgid = hexString2ByteArray(s_msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgid;
	}

	public static byte[] hexString2ByteArray(String hex) {
		if (hex.length() % 2 != 0)
			return null;
		byte[] buf = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			char c0 = hex.charAt(i);
			char c1 = hex.charAt(i + 1);
			byte b0 = hexChar2byte(c0);
			byte b1 = hexChar2byte(c1);
			if (b0 < 0 || b0 > 15)
				return null;
			if (b1 < 0 || b1 > 15)
				return null;
			buf[i / 2] = (byte) ((b0 << 4) | b1);
		}
		return buf;
	}

	public static byte hexChar2byte(char c) {
		if (c >= '0' && c <= '9')
			return (byte) (c - '0');
		if (c >= 'a' && c <= 'f')
			return (byte) (c - 'a' + 10);
		if (c >= 'A' && c <= 'F')
			return (byte) (c - 'A' + 10);
		return -1;
	}

	public static String byteArray2HexString(byte[] buf) {
		char[] hexTable = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			byte b = buf[i];
			byte b0 = (byte) (b & 0x0F);
			b = (byte) (b >>> 4);
			byte b1 = (byte) (b & 0x0F);
			sb.append(hexTable[b1]).append(hexTable[b0]);
		}
		return sb.toString();
	}

	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (res & 0xff);// 最低位
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
		targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
		return targets;
	}

	public static int byte2int(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

}
