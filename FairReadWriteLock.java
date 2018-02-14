//TESTING
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class FairReadWriteLock {
	public int numReaders, numWriters;
	public static Queue<Boolean> waiting;
	//public static int timestamp=0;
	public int myStamp;
	public int turn = 0;
    ReentrantLock readerlock = new ReentrantLock();
	ReentrantLock writerlock = new ReentrantLock();
	/* READER = 0
	 * WRITER = 1
	 */
	public synchronized void beginRead() {
		//timestamp++;
		//myStamp = timestamp;
		//addToQueue()
		numReaders++;
		while(myStamp > timestamp){
			/********WAIT CALL*********/
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/**************************/
		}
		turn++;
	}
	
	public synchronized void endRead() {
		numReaders--;
		notifyAll();	//condition changed, notify
	}
	
	public synchronized void beginWrite() {
		//timestamp++;
		//myStamp = timestamp;
		numWriters++;
		while((numReaders != 0 ) && (myStamp > turn)){ //to account for the recent increase in numWriters
			/********WAIT CALL*********/
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/**************************/
		}
	}
	public synchronized void endWrite() {
		turn++;
		numWriters--;
		notifyAll();	//condition changed, notify
		
	}
	
	private synchronized void addToQueue(boolean val){
		waiting.add(val);
	}
	
}