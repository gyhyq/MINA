package cmpp.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmpp.CmppConstant;

import cmpp.pdu.CmppPDU;
import cmpp.sms.ByteBuffer;
import cmpp.sms.StrUtil;
import static cmpp.MinaCmpp.MSG_COUNT;
import static cmpp.MinaCmpp.OPEN;

import java.io.IOException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO: Document me !
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * 
 */
public class CmppClientIoHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(CmppClientIoHandler.class);
	public static AtomicInteger received = new AtomicInteger(0);
	public static AtomicInteger closed = new AtomicInteger(0);
	private final Object LOCK;
	public static boolean Connect = false;
	public static boolean Firstmsg = true;
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	public CmppClientIoHandler(Object lock) {
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
		// 连接
		Connect(session);
		// 启动ActivePDU-Thread
//		ExecutorService exec = Executors.newSingleThreadExecutor();
//		exec.execute(new ActiveThread(session));
		Thread t = new Thread(new ActiveThread(session));
		t.setDaemon(true);
		t.start();
		session.resumeRead();
	}

	public void Connect(IoSession session) {
		// cmpp.pdu.Connect conn = new cmpp.pdu.Connect();
		// conn.setClientId(CmppClient.pu.getValue("CmppGw.server.clientId"));
		// conn.assignSequenceNumber();
		// conn.setSharedSecret(CmppClient.pu.getValue("CmppGw.server.password"));
		// conn.setTimeStamp(conn.genTimeStamp());
		// conn.setAuthClient(conn.genAuthClient());
		// session.write(conn);

		cmpp.pdu.Connect request = new cmpp.pdu.Connect(
				CmppConstant.TRANSMITTER);
		request.setClientId(CmppClient.pu.getValue("CmppGw.server.clientId"));
		request.setSharedSecret(CmppClient.pu
				.getValue("CmppGw.server.password"));
		request.setTimeStamp(request.genTimeStamp());
		request.setAuthClient(request.genAuthClient());
		request.setVersion((byte) 0x30);
		request.assignSequenceNumber();
		logger.info("Connect: " + request.getData().getHexDump());
		logger.info(request.dump());
		session.write(request);
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
			Firstmsg=false;
			switch (pdu.header.getCommandId()) {
			case CmppConstant.CMD_CONNECT_RESP:
				cmpp.pdu.ConnectResp conrsp = (cmpp.pdu.ConnectResp) pdu;
				pdu.dump();
				logger.info("conresp:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				if (conrsp.getStatus() == 0) {
					Connect = true;
//					Thread s = new Thread(new MsgSendThread(session));
//					s.setDaemon(true);
//					s.start();			
//					exec.execute(new MsgSendThread(session));
					//启动发送线程
				} else {
					Connect = false;
					session.close(true);
				}
				break;
			case CmppConstant.CMD_ACTIVE_TEST_RESP:
				cmpp.pdu.ActiveTestResp activeTestRsp = (cmpp.pdu.ActiveTestResp) pdu;
				pdu.dump();
				logger.info("activeTestRsp:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				ActiveThread.lastActiveTime = System.currentTimeMillis();
				break;
			case CmppConstant.CMD_ACTIVE_TEST:
				cmpp.pdu.ActiveTest activeTest = (cmpp.pdu.ActiveTest) pdu;
				cmpp.pdu.ActiveTestResp activeTestResp = (cmpp.pdu.ActiveTestResp) activeTest
						.getResponse();
				session.write(activeTestResp);
				logger.info("active_test:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				break;
			case CmppConstant.CMD_SUBMIT_RESP:
				cmpp.pdu.SubmitResp subresp = (cmpp.pdu.SubmitResp) pdu;
				pdu.dump();
				logger.info("submitresp:" + pdu.header.getSequenceNumber()
						+ " on session " + session.getId());
				break;
			case CmppConstant.CMD_DELIVER:
				logger.info("CMD_DELIVER");
				cmpp.pdu.Deliver cmppDeliver = (cmpp.pdu.Deliver) pdu;
				cmpp.pdu.DeliverResp cmppDeliverResp = (cmpp.pdu.DeliverResp) cmppDeliver
						.getResponse();
				logger.info(cmppDeliver.getSm().getMessage());
				session.write(cmppDeliverResp);

				if (cmppDeliver.getIsReport() == 0) { // 短信
					logger.info("sms_mo");
				} else {// 状态报告
					logger.info("sms_stat");
					ByteBuffer buffer = cmppDeliver.getSm().getData();
					try {
						logger.info("buffer.length=" + buffer.length());
						logger.info("setMsgId:"
								+ (StrUtil.bytesToHex(buffer.removeBytes(8)
										.getBuffer())));
						logger.info("setStat:" + (buffer.removeStringEx(7)));
						logger.info("setSubmitTime:"
								+ (buffer.removeStringEx(10)));
						logger.info("setDoneTime:"
								+ (buffer.removeStringEx(10)));
						logger.info("setUserNumber:"
								+ (buffer.removeStringEx(32)));
						logger.info("setSmscSequence:" + (buffer.removeInt()));
					} catch (Exception e) {
						//
					}
				}
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
}
