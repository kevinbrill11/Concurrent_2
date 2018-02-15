import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {

	class Node{
		String name;
		int pri;
		Node next;
		ReentrantLock nodeLock;
		public Node(String name, int priority){
			this.name = name;
			this.pri = priority;
			next = null;
			nodeLock = new ReentrantLock();
		}
		public String getName(){
			return name;
		}
		public int getPriority(){
			return pri;
		}
	}
	
	int maxSize;
	int size;
	Node head;
	//Node tail;
	final ReentrantLock lock1 = new ReentrantLock();
	final ReentrantLock lock2 = new ReentrantLock();
	final ReentrantLock sizeLock = new ReentrantLock();

	
	final Condition notFull = lock2.newCondition();
	final Condition notEmpty = lock1.newCondition();
	
	
	public PriorityQueue(int maxSize) {
        // Creates a Priority queue with maximum allowed size as capacity
		this.maxSize = maxSize;
		size = 1;
		head = new Node(" ", -1);
		head.next = null;
		//tail = head;
	}

	// Adds the name with its priority to this queue.
    // Returns the current position in the list where the name was inserted;
    // otherwise, returns -1 if the name is already present in the list.
    // This method blocks when the list is full.
	public int add(String name, int priority) {
        System.out.println(size);
		Node newNode = new Node(name, priority);
		if(head == null){
			head = newNode;
			head.next = null;
			sizeLock.lock();
			size++;
			sizeLock.unlock();
			return 0;
		}
		Node current = head;
		current.nodeLock.lock();
		int i = 0;
		//if duplicate name
		if(findDuplicate(name)){
			current.nodeLock.unlock();
//			current.next.nodeLock.unlock();
			return -1;
		}
		//if first node, doesn't change size
		if(size == 1 && current.getName() == " "){
			current.name = name;
			current.pri=priority;
			current.nodeLock.unlock();
			return 0;
		}
		
		/* priority of new node is higher than priority of head*/
		if(head.getPriority() < priority){
			
			while(size == (maxSize)){
				try {
					System.out.println("is FUll");
					lock2.lock();
					notFull.await();
					lock2.unlock();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			newNode.next = head;
			head.nodeLock.unlock();
			head = newNode;
			sizeLock.lock();
			size++;
			sizeLock.unlock();
			return 0;
		}
		else{
			while(current.next != null && current.getPriority() >= priority && current.next.getPriority() >= priority){
				
				while(size == (maxSize)){
					try {
						System.out.println("is FUll");
						lock2.lock();
						notFull.await();
						lock2.unlock();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				current.nodeLock.lock();
				current.next.nodeLock.lock();
				
				current.nodeLock.unlock();
				current = current.next;
				i++;
			}
			newNode.next = current.next;
	        current.next = newNode;
	        sizeLock.lock();
			size++;
			sizeLock.unlock();
	        lock1.lock();
	        notEmpty.signal();
	        lock1.unlock();
	        
	        current.nodeLock.unlock();
		}
		return i;
	}
	
    // Returns the position of the name in the list;
    // otherwise, returns -1 if the name is not found.
	public int search(String name) {
		if(head == null){
			return -1;
		}
		Node current = head;
		head.nodeLock.lock();
		int i = 0;
		while(current.next != null){
			current.nodeLock.lock();
			current.next.nodeLock.lock();
			if(current.getName().equals(name)){
				current.nodeLock.unlock();
				current.next.nodeLock.unlock();
				
				return i;
			}
			current.nodeLock.unlock();
			current = current.next;
			i++;
		}
		if(current.getName().equals(name)){
			return i;
		}
		return -1;
	}

    // Retrieves and removes the name with the highest priority in the list,
    // or blocks the thread if the list is empty.
	public String getFirst() {
		while(head == null){
			try {
				System.out.println("ain't got no sheeeeit");
				lock1.lock();
				notEmpty.await();
				lock1.unlock();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		head.nodeLock.lock();
//		if(head.next != null){
//			head.next.nodeLock.lock();
//		}
		while(size == 0){
			try {
				System.out.println("no elements");
				lock1.lock();
				notEmpty.await();
				lock1.unlock();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String retStr = head.getName();
		head.nodeLock.unlock();
		head = head.next;
		sizeLock.lock();
		size--;
		sizeLock.unlock();
		lock2.lock();
		notFull.signal();
		lock2.unlock();
		
//		head.nodeLock.unlock();
		return retStr;
	}


	public void print() {
		if(head != null){
		Node current = head;
		head.nodeLock.lock();
		
	
		while(current.next != null){
			System.out.println("name: " + current.getName() + " priority: " + current.getPriority());
			current = current.next;
		
		}
		System.out.println("name: " + current.getName() + " priority: " + current.getPriority());
		}
		
	}
	public int getSize(){
		int size = 1;
		Node current = head;
		while(current.next != null){
			current.nodeLock.lock();
			current.next.nodeLock.lock();
			current.nodeLock.unlock();
			current = current.next;
			size++;
		}
		return size;
	}
	public boolean findDuplicate(String n){
		Node current = head;
		while(current.next != null){
			current.nodeLock.lock();
			current.next.nodeLock.lock();
			
			if(current.name.equals(n)){
				current.nodeLock.unlock();
				System.out.println("found");
				return true;
			}
			current.nodeLock.unlock();
			current = current.next;	
		}
		if(current.name.equals(n)){
			System.out.println("found");
			return true;
		}
		else{
			//current.nodeLock.unlock();
			return false;
		}
	}
}