package cmpp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.integration.jmx.IoSessionMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmpp.CmppConstant;

import cmpp.ActiveThread;
import cmpp.pdu.CmppPDU;
import cmpp.sms.ByteBuffer;
import cmpp.sms.ShortMessage;
import static cmpp.MinaCmpp.MSG_COUNT;
import static cmpp.MinaCmpp.OPEN;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * TODO: Document me !
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * 
 */
public class CmppIoHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(CmppIoHandler.class);
	public static AtomicInteger received = new AtomicInteger(0);
	public static AtomicInteger closed = new AtomicInteger(0);
	private final Object LOCK;
	public static boolean Connect = false;
	public static boolean Firstmsg = true;
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	public CmppIoHandler(Object lock) {
		LOCK = lock;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		if (!(cause instanceof IOException)) {
			logger.error("Exception: ", cause);
		} else {
			logger.info("I/O error: " + cause.getMessage());
		}
		session.close(true);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("Session " + session.getId() + " is opened");

		// 启动ActivePDU-Thread
		// ExecutorService exec = Executors.newSingleThreadExecutor();
		// exec.execute(new ActiveThread(session));
		Thread t = new Thread(new ActiveThread(session));
		t.setDaemon(true);
		t.start();
		session.resumeRead();

	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("Creation of session " + session.getId());
		session.setAttribute(OPEN);
		session.suspendRead();

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.removeAttribute(OPEN);
		logger.info("{}> Session closed", session.getId());
		final int clsd = closed.incrementAndGet();

		if (clsd == MSG_COUNT) {
			synchronized (LOCK) {
				LOCK.notifyAll();
			}
		}
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		CmppPDU pdu = (CmppPDU) message;
		logger.info("MESSAGE: " + pdu.header.getCommandId() + ":"
				+ pdu.header.getSequenceNumber() + "on session "
				+ session.getId());
		final int rec = received.incrementAndGet();
		if (Firstmsg == true || Connect == true) {
			Firstmsg = false;
			switch (pdu.header.getCommandId()) {
			case CmppConstant.CMD_CONNECT:
				cmpp.pdu.Connect con = (cmpp.pdu.Connect) pdu;
				cmpp.pdu.ConnectResp conresp = (cmpp.pdu.ConnectResp) con
						.getResponse();
				session.write(conresp);
				logger.info("conresp:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				Connect = true;
//				Thread t2 = new Thread(new SendMoThread(session));
//				t2.setDaemon(true);
//				t2.start();
				exec.execute(new SendMoThread(session));
				break;
			case CmppConstant.CMD_ACTIVE_TEST:
				cmpp.pdu.ActiveTest activeTest = (cmpp.pdu.ActiveTest) pdu;
				cmpp.pdu.ActiveTestResp activeTestResp = (cmpp.pdu.ActiveTestResp) activeTest
						.getResponse();
				session.write(activeTestResp);
				logger.info("active_test:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				break;
			case CmppConstant.CMD_ACTIVE_TEST_RESP:
				cmpp.pdu.ActiveTestResp activeTestRsp = (cmpp.pdu.ActiveTestResp) pdu;
				pdu.dump();
				logger.info("activeTestRsp:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				ActiveThread.lastActiveTime = System.currentTimeMillis();
				break;
			case CmppConstant.CMD_SUBMIT:
				cmpp.pdu.Submit submit = (cmpp.pdu.Submit) pdu;
				submit.dump();
				cmpp.pdu.SubmitResp subresp = (cmpp.pdu.SubmitResp) submit
						.getResponse();
				subresp.setMsgId(cmpp.pdu.Tools.GetRspid());
				session.write(subresp);
				logger.info("subresp:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				// 发送状态报告
				cmpp.pdu.Deliver deliver = sendMsgStat(submit);
				session.write(deliver);
				break;
			case CmppConstant.CMD_DELIVER_RESP:
				cmpp.pdu.DeliverResp delresp = (cmpp.pdu.DeliverResp) pdu;
				delresp.dump();
				logger.info("DeliverResp:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				break;
			default:
				logger.warn("Unexpected PDU received! PDU Header: "
						+ pdu.header.getData().getHexDump());
				break;
			}
		}
		if (rec == MSG_COUNT) {
			synchronized (LOCK) {
				LOCK.notifyAll();
			}
		}

		// session.close(true);
	}

	private cmpp.pdu.Deliver sendMsgStat(cmpp.pdu.Submit submit) {
		// TODO Auto-generated method stub
		cmpp.pdu.Deliver delive = new cmpp.pdu.Deliver();
		delive.setMsgId(cmpp.pdu.Tools.GetMsgid());
		delive.setDstId(submit.getMsgSrc());
		delive.setServiceId(submit.getServiceId());
		delive.setSrcTermId(submit.getDestTermId()[0]);
		delive.setIsReport((byte) 1);
		delive.assignSequenceNumber();
		ShortMessage sm = new ShortMessage();
		ByteBuffer messageData = new ByteBuffer();
		messageData.appendBytes(submit.getMsgId(), 8);
		messageData.appendString("0", 7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		messageData.appendString(sdf.format(new Date()), 10);
		messageData.appendString(sdf.format(new Date()), 10);
		messageData.appendString(submit.getDestTermId()[0], 32);
		messageData.appendInt(30);
		sm.setMessage(messageData.getBuffer(), (byte) 04);
		delive.setSm(sm);
		delive.setLinkId(submit.getLinkId());
		return delive;
	}
}
