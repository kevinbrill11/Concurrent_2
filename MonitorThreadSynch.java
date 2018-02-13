/*
 * EID's of group members
 * 
 */

public class MonitorThreadSynch {
	private int numWaiting;
	private Object monitor ;
	private int parties;
	
	public MonitorThreadSynch(int parties) {
		this.parties = parties;
		numWaiting = 0;
		monitor = new Object();
		
	}
	
	public int await() throws InterruptedException {
           int index = 0;
           synchronized (monitor){
        	   numWaiting ++;
        	   index = parties - numWaiting;
        	   if(index == 0){
        		   monitor.notifyAll();
        		   numWaiting=0;
        	   }
        	   else{
        		   monitor.wait();
        	   }
           }
		
          // you need to write this code
	    return index;
	}
}
