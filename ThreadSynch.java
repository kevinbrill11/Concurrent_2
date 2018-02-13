/*
 * EID's of group members
 * 
 */
import java.util.concurrent.Semaphore; // for implementation using Semaphores

public class ThreadSynch {
	private int parties;
	private Semaphore lock;
	private int numWaiting;
	private int numwaiting2;
	private Semaphore block;
	
	private Semaphore nextBlock;
	
	public ThreadSynch(int parties) {
		this.parties = parties;	
		block = new Semaphore(parties);
		nextBlock = new Semaphore(0);
		lock = new Semaphore(1);
		numWaiting = 0;
		numwaiting2=0;
		
		
		
	}
	
	public int await() throws InterruptedException {
		int index = 0;
		block.acquire();
		lock.acquire();
		numWaiting++;
		index = parties - numWaiting;
		if(index == 0){
			nextBlock.release(parties);
			numWaiting=0;

		}
		lock.release();

		
		nextBlock.acquire();
		
		lock.acquire();
		numwaiting2++;
		nextBlock.release();
		
		if(numwaiting2==parties){
			block.release(parties);
			numwaiting2=0;
			nextBlock.acquire(parties);
		}
		lock.release();
	    return index;
	}
}