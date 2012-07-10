package cmpp.sms;


import cmpp.sms.SmsObject;


public abstract class ProcessingThread extends SmsObject implements Runnable {

	private static final String PROCESSING_THREAD_NAME = "ProcThread";

	private static int threadIndex = 0;

	private boolean keepProcessing = true;

	private byte processingStatus = PROC_INITIALISING;

	private static final byte PROC_INITIALISING = 0;

	private static final byte PROC_RECEIVING = 1;

	private static final byte PROC_FINISHED = 2;

	private Object processingStatusLock = new Object();

	private Exception termException = null;

	private Thread processingThread = null;

	public abstract void process();
	
	public abstract void init();

	public void start() {
		logger.debug("ProcessingThread.start enter");
		if (!isProcessing()) { // i.e. is initialising or finished
			setProcessingStatus(PROC_INITIALISING);
			init();
			termException = null;
			keepProcessing = true;
			processingThread = new Thread(this);
			processingThread.setName(generateIndexedThreadName());
			processingThread.start();
			while (isInitialising()) {
				try {
					Thread.sleep(10);
				}	catch (InterruptedException e) {
					//
				}
			}
		}
		logger.debug("ProcessingThread.start exit");
	}

	public void stop() {
		logger.debug("ProcessingThread.stop enter");
		if (isProcessing()) {
			stopProcessing(null);
			while (!isFinished()) {
				try {
					Thread.sleep(10); 
				} catch (InterruptedException e) {
					//
				}
			}
		}
		logger.debug("ProcessingThread.stop exit");
	}

	public void stopProcessing(Exception e) {
		setTermException(e);
		keepProcessing = false;
		synchronized (processingStatusLock) {
			processingStatus = PROC_INITIALISING;
		}
		
	}

	public void run() {
		logger.debug("ProcessingThread.run enter");
		try {
			setProcessingStatus(PROC_RECEIVING);
			while (keepProcessing) {
				process();
				Thread.yield();
			}
		} catch (Exception e) {
			setTermException(e);
			logger.error("ProcessingThread.run() caught exception ", e);
		} finally {
			setProcessingStatus(PROC_FINISHED);
			logger.debug("ProcessingThread.run exit");
		}
	}

	public String getThreadName() {
		return PROCESSING_THREAD_NAME;
	}

	public int getThreadIndex() {
		return ++threadIndex;
	}

	public String generateIndexedThreadName() {
		return getThreadName() + "-" + getThreadIndex();
	}

	protected void setTermException(Exception e) {
		termException = e;
	}

	public Exception getTermException() {
		return termException;
	}

	private void setProcessingStatus(byte value) {
		synchronized (processingStatusLock) {
			processingStatus = value;
		}
	}

	private boolean isInitialising() {
		synchronized (processingStatusLock) {
			return processingStatus == PROC_INITIALISING;
		}
	}

	private boolean isProcessing() {
		synchronized (processingStatusLock) {
			return processingStatus == PROC_RECEIVING;
		}
	}

	private boolean isFinished() {
		synchronized (processingStatusLock) {
			return processingStatus == PROC_FINISHED;
		}
	}
}

