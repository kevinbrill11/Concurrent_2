//TESTING
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class FairReadWriteLock {
    int numReaders=0;  //number of readers at front of queue
	int numWriters=0;  //number of writers at front of queue
	
	int nextTurn =0;
	public int currentTurn = 0; // number of total threads in queue
    ReentrantLock readerlock = new ReentrantLock();
    

	ReentrantLock writerlock = new ReentrantLock();
	/* READER = 0
	 * WRITER = 1
	 */
	
	//Reader blocked until all preceding writer
	//threads acquire and release lock 
	public synchronized void beginRead() {
		System.out.println("READER BEGIN: " + Thread.currentThread().getId());	
		int readTurn = currentTurn;
		currentTurn++;
		
		while(readTurn!=nextTurn || numWriters > 0  ){
			try {
//				System.out.println("READER BLOCKED: " + Thread.currentThread().getId());	
				wait();
//				System.out.println("READER UNBLOCKED: " + Thread.currentThread().getId());	


			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//number of readers at front of the queue
		//(unblocked reader)
		numReaders ++;

	}
	
	public synchronized void endRead() {
		System.out.println("READER LEAVES: " +  Thread.currentThread().getId());
		nextTurn ++;
		numReaders--;
		notifyAll();	//condition changed, notify
	}
	
	//blocked until all preceding reader and 
	//writer threads have acquired and released lock
	public synchronized void beginWrite() {
		System.out.println("WRITER BEGIN: " + Thread.currentThread().getId());	
		int writeTurn = currentTurn;
		currentTurn++;

		while(writeTurn != nextTurn || numReaders > 0 || numWriters > 0 ){ 
			/********WAIT CALL*********/
			try {
//			System.out.println("WRITER BLOCKED: " +  Thread.currentThread().getId());	
				this.wait();
//			System.out.println("WRITER UNBLOCKED: " +  Thread.currentThread().getId());	

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/**************************/
		}
		
		//Only increment when the writer is not blocked
		// (it is at front of queue)
		numWriters++;

	}
	public synchronized void endWrite() {
		System.out.println("WRITER LEAVES: " +  Thread.currentThread().getId());	
		nextTurn++;
		numWriters--;
		notifyAll();	//condition changed, notify
		
	}
		
	
}

