package cmpp.sms;



public class Unprocessed {

	private ByteBuffer unprocessed = new ByteBuffer();

	private int expected = 0;

	private long lastTimeReceived = 0;


	public Unprocessed appendBuffer(ByteBuffer buffer) {
		unprocessed.appendBuffer(buffer);
		return this;
	}

	public void reset() {
		unprocessed.setBuffer(null);
		expected = 0;
	}

	public void setExpected(int value) {
		expected = value;
	}
	public void setLastTimeReceived(long value) {
		lastTimeReceived = value;
	}
	public void setLastTimeReceived() {
		lastTimeReceived = System.currentTimeMillis();
	}

	public ByteBuffer getUnprocessed() {
		return unprocessed;
	}
	public boolean hasUnprocessed() {
		return unprocessed.length() > 0;
	}
	public int getExpected() {
		return expected;
	}
	public long getLastTimeReceived() {
		return lastTimeReceived;
	}
}

