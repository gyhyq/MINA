package cmpp.sms;


public class SmsConstant {

	public static final long COMMS_TIMEOUT = 60000;
	public static final long CONNECTION_RECEIVE_TIMEOUT = 10000;
	public static final long QUEUE_TIMEOUT = 60000;
	public static final long RECEIVE_BLOCKING = 0;
	public static final long RECEIVE_ONCE = -1;
	
	//Address_TON
	public static final byte GSM_TON_UNKNOWN = (byte) 0x00;
	public static final byte GSM_TON_INTERNATIONAL = (byte) 0x01;
	public static final byte GSM_TON_NATIONAL = (byte) 0x02;
	public static final byte GSM_TON_NETWORK = (byte) 0x03;
	public static final byte GSM_TON_SUBSCRIBER = (byte) 0x04;
	public static final byte GSM_TON_ALPHANUMERIC = (byte) 0x05;
	public static final byte GSM_TON_ABBREVIATED = (byte) 0x06;
	public static final byte GSM_TON_RESERVED_EXTN = 0x07;

	//Address_NPI
	public static final byte GSM_NPI_UNKNOWN = (byte) 0x00;
	public static final byte GSM_NPI_E164 = (byte) 0x01;
	public static final byte GSM_NPI_ISDN = GSM_NPI_E164;
	public static final byte GSM_NPI_X121 = (byte) 0x03;
	public static final byte GSM_NPI_TELEX = (byte) 0x04;
	public static final byte GSM_NPI_LAND_MOBILE = (byte) 0x06;
	public static final byte GSM_NPI_NATIONAL = (byte) 0x08;
	public static final byte GSM_NPI_PRIVATE = (byte) 0x09;
	public static final byte GSM_NPI_ERMES = (byte) 0x0A;
	public static final byte GSM_NPI_INTERNET = (byte) 0x0E;
	public static final byte GSM_NPI_WAP_CLIENT_ID = (byte) 0x12;
	public static final byte GSM_NPI_RESERVED_EXTN = 0x0F;

	//Port Value
	public static final int MIN_VALUE_PORT = 1024;
	public static final int MAX_VALUE_PORT = 65535;
	
	//Address Length
	public static final int MIN_LENGTH_ADDRESS = 7;
	
	// list of character encodings
	// see http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html
	// from rt.jar

	// American Standard Code for Information Interchange 
	public static final String ENC_ASCII = "ASCII";
	// Windows Latin-1 
	public static final String ENC_CP1252 = "Cp1252";
	// ISO 8859-1, Latin alphabet No. 1 
	public static final String ENC_ISO8859_1 = "ISO8859_1";
	// Sixteen-bit Unicode Transformation Format, big-endian byte order
	// with byte-order mark
	public static final String ENC_UTF16_BEM = "UnicodeBig";
	// Sixteen-bit Unicode Transformation Format, big-endian byte order 
	public static final String ENC_UTF16_BE = "UnicodeBigUnmarked";
	// Sixteen-bit Unicode Transformation Format, little-endian byte order
	// with byte-order mark
	public static final String ENC_UTF16_LEM = "UnicodeLittle";
	// Sixteen-bit Unicode Transformation Format, little-endian byte order 
	public static final String ENC_UTF16_LE = "UnicodeLittleUnmarked";
	// Eight-bit Unicode Transformation Format 
	public static final String ENC_UTF8 = "UTF8";
	// Sixteen-bit Unicode Transformation Format, byte order specified by
	// a mandatory initial byte-order mark 
	public static final String ENC_UTF16 = "UTF-16";

}

