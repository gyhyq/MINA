package testcase;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import static testcase.MinaRegressionTest.MSG_SIZE;
import static testcase.MinaRegressionTest.OPEN;

/**
 * TODO: Document me !
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class MyRequestDecoder extends CumulativeProtocolDecoder {
  private static final Logger logger = LoggerFactory.getLogger(MyRequestDecoder.class);

  @Override
  protected boolean doDecode(final IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
    if (!session.containsAttribute(OPEN)) {
      logger.error("!decoding for closed session {}", session.getId());
    }

    logger.info("Done decoding for session {}", session.getId());

    if (in.hasRemaining() && !session.isClosing() && session.isConnected()) {
      logger.info("rev msg for session {}", session.getId());
      IoBuffer tmp = IoBuffer.allocate(in.remaining());
      tmp.put(in);
      tmp.flip();
      out.write(tmp);
      return true;
    }

    return false;
  }
}
