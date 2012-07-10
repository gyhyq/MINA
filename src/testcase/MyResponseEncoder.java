package testcase;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * TODO: Document me !
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class MyResponseEncoder implements ProtocolEncoder {
  public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {

  }

  public void dispose(IoSession session) throws Exception {

  }
}