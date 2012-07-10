package cmpp.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import cmpp.pdu.CmppPDU;

/**
 * TODO: Document me !
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class CmppResponseEncoder implements ProtocolEncoder {
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		CmppPDU pdu = (CmppPDU) message;
		byte[] bytes = pdu.getData().getBuffer();
		IoBuffer buf = IoBuffer.allocate(bytes.length, false);

		buf.setAutoExpand(true);
//		buf.putInt(bytes.length);
		buf.put(bytes);

		buf.flip();
		out.write(buf);

	}

	public void dispose(IoSession session) throws Exception {

	}
}