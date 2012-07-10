package cmpp;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmpp.sms.ShortMessage;

public class SendMoThread implements Runnable {
	private static int i = 0;
	private IoSession session = null;
	private static final Logger logger = LoggerFactory
			.getLogger(SendMoThread.class);

	public SendMoThread(IoSession s) {
		this.session = s;
	}

	public void run() {
		try {
			while (session.isConnected() & CmppIoHandler.Connect == true) {
				logger.info("session " + session.getId() + " :send mo");
				sendmo();
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	private void sendmo() {
		// TODO Auto-generated method stub
		cmpp.pdu.Deliver delive = new cmpp.pdu.Deliver();
		delive.setMsgId(cmpp.pdu.Tools.GetMsgid());
		delive.setServiceId("abc");
		delive.setSrcTermId("13811032266");
		delive.assignSequenceNumber();
		delive.setDstId("1016");
		delive.setIsReport((byte) 0);
		ShortMessage sm = new ShortMessage();
		String msg = "hi,≤‚ ‘mo:" + i++;
		sm.setMessage(msg.getBytes(), (byte) 15);
		delive.setSm(sm);
		delive.setLinkId("12345678901234567890");
		session.write(delive);
	}
}
