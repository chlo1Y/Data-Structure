import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * A hash set implementation for Strings. Cannot insert null into the set. Other
 * requirements are given with each method.
 *
 * @author Matt Boutell and <<TODO: your name here >>>. Created Oct 6, 2014.
 */
public class StringHashSet {

	// The initial size of the internal array.
	private static final int DEFAULT_CAPACITY = 5;

	
	private int capacity = DEFAULT_CAPACITY;
	private Node[] array;
	private int size;
	Node head;
	private int modifiedCount;

	// You'll want fields for the size (number of elements) and the internal
	// array of Nodes. I also added one for the capacity (the length
	// of the internal array).

	private class Node {
		// DONE: Implement this class . These are just linked-list style
		// nodes, so you will need at least fields for the data and a reference
		// to the next node, plus a constructor. You can choose to use a
		// NULL_NODE at the end, or not. I chose not to do so this time.
		
		
		public Node next;
		public String item;
		
		public Node(String item, Node next){
			this.item=item;
			this.next=next;
		}
	}

	/**
	 * Creates a Hash Set with the default capacity.
	 */
	public StringHashSet() {
		// Recall that using this as a method calls another constructor
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates a Hash Set with the given capacity.
	 */
	public StringHashSet(int initialCapacity) {
		initialize(initialCapacity);
	}

	private void initialize(int initialCapacity) {
		// DONE: Set the capacity to the given capacity, and initialize the
		// other fields.
		// Why did we pull this out into a separate method? Perhaps another
		// method needs to initialize the hash set as well? (Hint)
		this.modifiedCount=0;
		this.capacity=initialCapacity;
		Node[] a= new Node[this.capacity];
		this.array=a;
		this.head=null;
		this.size=0;
	}

	/**
	 * Calculates the hash code for Strings, using the x=31*x + y pattern.
	 * Follow the specification in the String.hashCode() method in the Java API.
	 * Note that the hashcode can overflow the max integer, so it can be
	 * negative. Later, in another method, you'll need to account for a negative
	 * hashcode by adding Integer.MAX_VALUE before you mod by the capacity
	 * (table size) to get the index.
	 *
	 * This method is NOT the place to calculate the index though.
	 *
	 * @param item
	 * @return The hash code for this String
	 */
	public static int stringHashCode(String item) {
		// DONE: Write this.
		// java hasCode uses the 31*x+t pattern. So i just used it 
		return item.hashCode();
	}

	/**
	 * Adds a new node if it is not there already. If there is a collision, then
	 * add a new node to the -front- of the linked list.
	 * 
	 * Must operate in amortized O(1) time, assuming a good hashcode function.
	 *
	 * If the number of nodes in the hash table would be over double the
	 * capacity (that is, lambda > 2) as a result of adding this item, then
	 * first double the capacity and then rehash all the current items into the
	 * double-size table.
	 *
	 * @param item
	 * @return true if the item was successfully added (that is, if that hash
	 *         table was modified as a result of this call), false otherwise.
	 */
	public boolean add(String item) {
		// first get the hashCode of the given string 
		// add the max_vlaue to it in case it has an overflowed code
		int hash= item.hashCode();
		if(hash<0){
			hash+=Integer.MAX_VALUE;
		}
		int index=hash% this.capacity; // mod to gte index 
		
		// create a new node ready to insert
		Node newNode= new Node(item, null);
		// if a collision happened, no change will be made
		if (this.array[index]!=null){
			if(this.array[index].item.equals(item)){
				return false;
			}
			// if not, the new node will be the head and it's next will
			// be the original head that the head holds
			newNode.next=this.array[index];
		}
		this.array[index]=newNode;
		// increase the size of the array 
		this.size++;
		
		// if adter adding, the load exceeds 2, need to resize
		if((double)this.size/this.capacity>2){
			// double the capacity
			this.capacity=this.capacity*2;
			// have a new array to hold values
			Node[] b= new Node[this.capacity];
			for(int i=0; i<this.array.length;i++){
				// loop through and copy elements and aassign them new index 
				Node a=this.array[i];
				while(a!=null){
					int index1 =a.item.hashCode();
					if(index1<0){
						index1+=Integer.MAX_VALUE;
					}
					b[index1% this.capacity]=a;
					a=a.next;
				}
			}
			// change the pointer, take the array back
			this.array=b;
		}
		// each successful add will cause the array to change 
		this.modifiedCount++;
		return true;
	}

	/**
	 * Prints an array value on each line. Each line will be an array index
	 * followed by a colon and a list of Node data values, ending in null. For
	 * example, inserting the strings in the testAddSmallCollisions example
	 * gives "0: shalom hola null 1 bonjour null 2 caio hello null 3 null 4 hi
	 * null". Use a StringBuilder, so you can build the string in O(n) time.
	 * (Repeatedly concatenating n strings onto a growing string gives O(n^2)
	 * time)
	 * 
	 * @return A slightly-formatted string, mostly used for debugging
	 */
	public String toRawString() {
		// a stringbuilder that is used for debugging
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<this.capacity; i++){
			Node a= this.array[i];
			sb.append(i+": ");
			while(a!=null){
				sb.append(a.item+" ");
				a=a.next;
			}
			sb.append("null\n");
		}
		return sb.toString();
	}

	/**
	 * 
	 * Checks if the given item is in the hash table.
	 * 
	 * Must operate in O(1) time, assuming a good hashcode function.
	 *
	 * @param item
	 * @return True if and only if the item is in the hash table.
	 */
	public boolean contains(String item) {
		// same as previous (add)
		int hashCode=item.hashCode();
		if(hashCode<0){
			hashCode+=Integer.MAX_VALUE;
		}
		int index=hashCode%this.capacity;
		// if not exist
		if(this.array[index]==null){
			return false;
		}
		Node a=this.array[index];
		while(a!=null){
			if(a.item.equals(item)){
				return true;
			}
			a=a.next;
		}
		return false;
	}

	/**
	 * Returns the number of items added to the hash table. Must operate in O(1)
	 * time.
	 *
	 * @return The number of items in the hash table.
	 */
	public int size() {
		// Done
		return this.size;
	}

	/**
	 * @return True iff the hash table contains no items.
	 */
	public boolean isEmpty() {
		//check to see if the array is empty
		for(int i=0; i<this.capacity; i++){
			if(this.array[i]!=null){
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes all the items from the hash table. Resets the capacity to the
	 * DEFAULT_CAPACITY
	 */
	public void clear() {
		initialize(DEFAULT_CAPACITY);
		
		// Done: Write this. Should take 1 line if you read carefully above.
	}

	/**
	 * Removes the given item from the hash table if it is there. You do NOT
	 * need to resize down if the load factor decreases below the threshold.
	 * 
	 * @param item
	 * @return True If the item was in the hash table (or equivalently, if the
	 *         table changed as a result).
	 */
	public boolean remove(String item) {
		int code=item.hashCode();
		if(code<0){
			code+=Integer.MAX_VALUE;
		}
		int index= code% this.capacity;
		// to avoid in approciate mutaion, I used a node to represente the desired node 
		Node a=this.array[index];
		// b is the previous node of the current node. 
		// this is for changing references when deleation happens 
		Node b= a;
		while(a!=null){
			// if target is found 
			if(a.item.equals(item)){
				// decrese the size of array 
				this.size--;
				//change node references by skipping current node 
				b.next=a.next;
				// each successful remove cuase array to change
				this.modifiedCount++;;
				return true;
			}
			// if target is not found, update current node and its parent to 
			// continue searching
			b=a;
			a=a.next;
		}
		
		return false;
	}

	/**
	 * Adds all the items from the given collection to the hash table.
	 *
	 * @param collection
	 * @return True if the hash table is modified in any way.
	 */
	public boolean addAll(Collection<String> collection) {
		// Done: Write this.
		// modified is a boolean holder
		boolean modified=false; 
		// process is to see during the add process, does each elemnet being added successfully
		boolean process=false;
		for(String a: collection){
			// add elements form collection
			  process = this.add(a);
			  //used "or". only if one element is successfully added, 
			  //it's still true that the table is modified 
			  modified =modified ||process; 
		}
		return modified;
	}

	/**
	 * 
	 * Challenge Feature: Returns an iterator over the set. Return the items in
	 * any order that you can do efficiently. Should throw a
	 * NoSuchElementException if there are no more items and next() is called.
	 * Should throw a ConcurrentModificationException if next() is called and
	 * the set has been mutated since the iterator was created.
	 *
	 * @return an iterator.
	 */
	public Iterator<String> iterator() {
		// create a new class bacuse I tried to do it in the Iterator class
		// but idk why it doesn't update my local varible value
		// so... :(
		return new NewIterator();
	}
	
	
	class NewIterator<String> implements Iterator<String>{
		private int modifiedCount;
		private int index;
		private Node nodeA;
		
		public NewIterator() {
			// take note of the number modication before iteration 
			this.modifiedCount = StringHashSet.this.modifiedCount;
			// this is the node I am working on 
			this.nodeA = StringHashSet.this.array[index];
			// if the node is empty, 
			while (this.nodeA == null) {
				index++; //check to find the next slot 
				// if all slots are checked and still no element is found
				if (this.index > StringHashSet.this.capacity - 1) {
					break;
				}
				// if anything is found, update the working node 
				this.nodeA = StringHashSet.this.array[this.index];
			}
		}

		@Override
		public boolean hasNext() {
			// if all node are checked and the current working node is still a null
			// it means it doesnlt have a next 
			if(nodeA==null){
				return false;
			}
			return true;
		}

		@Override
		public String next() {
			// if modifcation happens when iteration is doing work,
			// throw exceptions
			if (this.modifiedCount != StringHashSet.this.modifiedCount) {
				throw new ConcurrentModificationException();
			}
			// if all slots are checked and you still want to see if there is a next 
			// sorry no such element exist 
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			// hold the current element for later return 
			String temp = (String) this.nodeA.item;
			//update node since next() is called 
			this.nodeA = this.nodeA.next;
			while (this.nodeA == null) {
				this.index++;
				if (this.index > StringHashSet.this.capacity - 1) {
					break;
				}
				this.nodeA = StringHashSet.this.array[this.index];
			}
			return temp;
		}
			
	}

	// Challenge Feature: If you have an iterator, this is easy. Use a
	// StringBuilder, so you can build the string in O(n) time. (Repeatedly
	// concatenating n strings onto a string gives O(n^2) time)
	// Format it like any other Collection's toString (like [a, b, c])
	@Override
	public String toString() {
		//:)
		StringBuilder sb= new StringBuilder();
		sb.append("[");
		for(int i=0; i<this.capacity; i++){
			Node a= this.array[i];
			while(a!=null){
				sb.append(a.item+", ");
				a=a.next;
			}
		}
		String a= sb.toString().substring(0, sb.length()-2);
		a+="]";
		
		return a;
	}
}
