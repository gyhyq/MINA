package cmpp;


public class CmppConstant {
	public static final byte COMMAND_NAME_LENGTH=12;
	public static final byte TRANSMITTER = (byte) 0x00;
	public static final byte RECEIVER = (byte) 0x01;
	public static final byte TRANSCEIVER = (byte) 0x02;
	
	public static final int CONNECTION_CLOSED = 0;
	public static final int CONNECTION_OPENED = 1;

	public static final long ACCEPT_TIMEOUT = 60000;
	public static final long RECEIVER_TIMEOUT = 60000;
	public static final long COMMS_TIMEOUT = 60000;
	public static final long QUEUE_TIMEOUT = 60000;
	public static final long RECEIVE_BLOCKING = 0;
	public static final long CONNECTION_RECEIVE_TIMEOUT = 0;
	public static int PDU_HEADER_SIZE = 12;
	
	//CMPP Command Set
	public static final int CMD_CONNECT = 0x00000001;
	public static final int CMD_CONNECT_RESP = 0x80000001;
	public static final int CMD_TERMINATE = 0x00000002;
	public static final int CMD_TERMINATE_RESP = 0x80000002;
	public static final int CMD_SUBMIT = 0x00000004;
	public static final int CMD_SUBMIT_RESP = 0x80000004;
	public static final int CMD_DELIVER = 0x00000005;
	public static final int CMD_DELIVER_RESP = 0x80000005;
	public static final int CMD_QUERY = 0x00000006;
	public static final int CMD_QUERY_RESP = 0x80000006;
	public static final int CMD_CANCEL = 0x00000007;
	public static final int CMD_CANCEL_RESP = 0x80000007;
	public static final int CMD_ACTIVE_TEST = 0x00000008;
	public static final int CMD_ACTIVE_TEST_RESP = 0x80000008;


	

	//Command_Status Error Codes
	public static final int ESME_ROK = 0x00000000;

	//Interface_Version
	public static final byte VERSION = 0x20;

	//Address_TON
	public static final byte GSM_TON_UNKNOWN = 0x00;
	public static final byte GSM_TON_INTERNATIONAL = 0x01;
	public static final byte GSM_TON_NATIONAL = 0x02;
	public static final byte GSM_TON_NETWORK = 0x03;
	public static final byte GSM_TON_SUBSCRIBER = 0x04;
	public static final byte GSM_TON_ALPHANUMERIC = 0x05;
	public static final byte GSM_TON_ABBREVIATED = 0x06;
	public static final byte GSM_TON_RESERVED_EXTN = 0x07;

	//Address_NPI
	public static final byte GSM_NPI_UNKNOWN = 0x00;
	public static final byte GSM_NPI_E164 = 0x01;
	public static final byte GSM_NPI_ISDN = GSM_NPI_E164;
	public static final byte GSM_NPI_X121 = 0x03;
	public static final byte GSM_NPI_TELEX = 0x04;
	public static final byte GSM_NPI_LAND_MOBILE = 0x06;
	public static final byte GSM_NPI_NATIONAL = 0x08;
	public static final byte GSM_NPI_PRIVATE = 0x09;
	public static final byte GSM_NPI_ERMES = 0x0A;
	public static final byte GSM_NPI_INTERNET = 0x0E;
	public static final byte GSM_NPI_WAP_CLIENT_ID = 0x12;
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

