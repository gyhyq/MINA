package cmpp.sms;

import java.util.LinkedList;
import java.util.ListIterator;

import cmpp.sms.SmsObject;


public class Queue extends SmsObject {
	private int maxQueueSize = 0;
	private LinkedList queueData = new LinkedList();
	private Object mutex;

	public Queue() {
		mutex = this;
	}

	public Queue(int maxSize) {
		maxQueueSize = maxSize;
		mutex = this;
	}

	/**
	 * Current count of the elements in the queue.
	 */
	public int size() {
		synchronized (mutex) {
			return queueData.size();
		}
	}

	/**
	 * If there is no element in the queue.
	 */
	public boolean isEmpty() {
		synchronized (mutex) {
			return queueData.isEmpty();
		}
	}

	/**
	 * Removes first element form the queue and returns it.
	 * If the queue is empty, returns null.
	 */
	public Object dequeue() {
		synchronized (mutex) {
			Object first = null;
			if (size() > 0) {
				first = queueData.removeFirst();
			}
			return first;
		}
	}

	/**
	 * Tries to find the provided element in the queue and if found,
	 * removes it from the queue and returns it.
	 * If the element is not found returns null.
	 */
	public Object dequeue(Object obj) {
		Object found = null;
		synchronized (mutex) {
			found = find(obj);
			if (found != null) {
				queueData.remove(found);
			}
		}
		if (found == null) 
			logger.info("Queue.dequeue: Nothing found");
		return found;
	}

	/**
	 * Appends an element to the end of the queue. If the queue
	 * has set limit on maximum elements and there is already specified
	 * max count of elements in the queue throws IndexOutOfBoundsException.
	 */
	public void enqueue(Object obj) throws IndexOutOfBoundsException {
		synchronized (mutex) {
			queueData.add(obj);
		}
	}

	/**
	 * Searches the queue to find the provided element.
	 * Uses <code>equals</code> method to compare elements.
	 */
	public Object find(Object obj) {
		synchronized (mutex) {
			Object current;
			ListIterator iter = queueData.listIterator(0);
			while (iter.hasNext()) {
				current = iter.next();
				if (current.equals(obj)) {
					return current;
				}
			}
		}
		return null;
	}
	
	public PDU removePDUFromQueue(PDU pdu) {
		PDU found = null;
		synchronized (mutex) {
			found = null;
			PDU current;
			ListIterator iter = queueData.listIterator(0);
			while (iter.hasNext()) {
				current = (PDU) iter.next();
				if (current.getSequenceNumber() == pdu.getSequenceNumber()) {
					found = current;
					break;
				}
			}
			if (found != null) {
				queueData.remove(found);
			}
		}
		if (found == null) 
			logger.info("Queue.removePDUFromQueue: Nothing found");
		return found;
	}
	
	public void resetTimeStamp() {
		long currentTime = System.currentTimeMillis();
		synchronized (mutex) {
			PDU pdu;
			ListIterator iter = queueData.listIterator(0);
			while (iter.hasNext()) {
				pdu = (PDU) iter.next();
				pdu.timeStamp = currentTime;
			}
		}
	}
	
	public void removeTimeoutPDU(long timeoutInterval) {
		long currentTime = System.currentTimeMillis();
		synchronized (mutex) {
			PDU pdu;
			ListIterator iter = queueData.listIterator(0);
			while (iter.hasNext()) {
				pdu = (PDU) iter.next();
				if ((currentTime - pdu.timeStamp) > timeoutInterval) {
					logger.info("REMOVE TIMED OUT PDU: "+pdu.name());
					logger.info(pdu.dump());
					iter.remove();
				}
			}
		}
	}
	
	public void clear() {
		synchronized(mutex) {
			queueData.clear();
		}
	}
	
	public Object get(int i) {
		synchronized (mutex) {
			return queueData.get(i);
		}
	}
	
	public void set(int i, Object obj) {
		synchronized(mutex) {
			queueData.set(i, obj);
		}
	}
}

