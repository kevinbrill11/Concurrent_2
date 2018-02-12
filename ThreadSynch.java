/*
 * EID's of group members
 * 
 */
import java.util.concurrent.Semaphore; // for implementation using Semaphores

public class ThreadSynch {
	private int parties;
	private Semaphore lock;
	private int numWaiting;
	private Semaphore block;
	
	public ThreadSynch(int parties) {
		this.parties = parties;
				
		block = new Semaphore(0);
		lock = new Semaphore(1);
		
		numWaiting = 0;
		
		
		
	}
	
	public int await() throws InterruptedException {
		// Waits until all parties have invoked await on this ThreadSynch.
		// If the current thread is not the last to arrive then it is
		// disabled for thread scheduling purposes and lies dormant until the last thread arrives.
		// Returns: the arrival index of the current thread, where index
		// (parties - 1) indicates the first to arrive and zero indicates
		// the last to arrive.
		int index = 0;
   		lock.acquire();
   		numWaiting++;
   		index = parties - numWaiting;
   		if(index == 0){
   			block.release(parties);
			numWaiting=0;
   		}

   		lock.release();
   		block.acquire();
           
        // you need to write this code
	    return index;
	}
}