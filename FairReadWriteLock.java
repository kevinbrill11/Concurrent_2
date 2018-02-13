//TESTING
import java.util.concurrent.*;

public class FairReadWriteLock {
	public int numReaders, numWriters;
	public static int timestamp=0;
	public int myStamp;
	public int turn = 0;
                        
	public synchronized void beginRead() {
		timestamp++;
		myStamp = timestamp;
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
		timestamp++;
		myStamp = timestamp;
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
	
//	private synchronized int getTimestamp(){
//		timestamp++;
//		return timestamp;	//return thread's local timestamp
//	}
	
}