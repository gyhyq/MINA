package cmpp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmpp.pdu.Submit;
import cmpp.pdu.Tools;
import cmpp.sms.ShortMessage;

public class MsgSendThread extends Thread {
	private static BufferedReader keyboard = new BufferedReader(
			new InputStreamReader(System.in));
	private IoSession session = null;
	private static final Logger logger = LoggerFactory
			.getLogger(MsgSendThread.class);

	public MsgSendThread(IoSession s) {
		setDaemon(true);
		this.session = s;
	}

	public void run() {
		try {
			String option = "1";
			int optionInt;
			while (session.isConnected() & CmppClientIoHandler.Connect == true) {
				logger.info(">Please input your option: ");
				logger.info(">1 shutdown");
				logger.info(">2 test");
				System.out.print(">");
				optionInt = -1;
				try {
					option = keyboard.readLine();
					optionInt = Integer.parseInt(option);
				} catch (Exception e) {
					logger.info("" + e + "\nPlease input an option number");
					optionInt = -1;
				}
				switch (optionInt) {
				case 1:
					shutdown();
					break;
				case 2:
					test();
					break;
				case -1:
					// default option if entering an option went wrong
					break;
				default:
					logger.info("Invalid option. Choose between 0 and 10.");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void shutdown() {
		if (session != null)
			session.close(true);
	}

	private void test() {
		// TODO Auto-generated method stub
		byte[] msgid = Tools.GetMsgid();
		Submit submit = new Submit();
		submit.setServiceId("abc");
		submit.setSrcId("10658830");
		submit.setMsgSrc("1016");
		submit.setDestTermIdCount((byte) 1);
		submit.setDestTermId(new String[] { "13811032266" });
		submit.setFeeTermId("");
		submit.setMsgId(msgid);
		submit.assignSequenceNumber();
		ShortMessage sm = new ShortMessage();
		String msg = "hi,≤‚ ‘";
		sm.setMessage(msg.getBytes(), (byte) 15);
		submit.setSm(sm);
		submit.setLinkId("12345678901234567890");
		session.write(submit);
	}
}
