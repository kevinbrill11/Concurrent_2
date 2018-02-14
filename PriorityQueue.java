import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {

	class Node{
		String name;
		int priority;
		Node next;
		ReentrantLock nodeLock;
		public Node(String name, int priority){
			this.name = name;
			this.priority = priority;
			next = null;
			nodeLock = new ReentrantLock();
		}
		public String getName(){
			return name;
		}
		public int getPriority(){
			return priority;
		}
	}
	
	int size;
	Node head;
	Node tail;
	ReentrantLock lock1, lock2;

	
	public PriorityQueue(int maxSize) {
        // Creates a Priority queue with maximum allowed size as capacity
		size = maxSize;
		head = new Node(null, -1);
		head.next = null;
		tail = head;
	}

	public int add(String name, int priority) {
        // Adds the name with its priority to this queue.
        // Returns the current position in the list where the name was inserted;
        // otherwise, returns -1 if the name is already present in the list.
        // This method blocks when the list is full.
		Node newNode = new Node(name, priority);
		head.nodeLock.lock();
		if(head.next != null){
			head.next.nodeLock.lock();
		}
		if(head.getPriority() < priority){
			newNode.next = head;
			head = newNode;
		}
		

		return 0;
	}

	public int search(String name) {
        // Returns the position of the name in the list;
        // otherwise, returns -1 if the name is not found.
		return 0;
	}

	public String getFirst() {
        // Retrieves and removes the name with the highest priority in the list,
        // or blocks the thread if the list is empty.
		return "";
	}
}