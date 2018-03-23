package list;

/**
 * 
 * @author anderson
 * 
 * @param <T>
 *            Any Comparable type
 * 
 *            A linked list whose elements are kept in sorted order.
 */
public class SortedLinkedList<T extends Comparable<T>> extends
		DoublyLinkedList<T> {

	/**
	 * Create an empty list
	 * 
	 */
	public SortedLinkedList() {
		super();
	}

	/**
	 * Creates a sorted list containing the same elements as the parameter
	 * 
	 * @param list
	 *            the input list
	 */
	public SortedLinkedList(DoublyLinkedList<T> list) {
		super();
		Node a =list.head.next;
		while(a.next!=null){
			this.add(a.data);
			a=a.next;
		}
		
		
		// TODO: finish implementing this constructor
	}

	/**
	 * Adds the given element to the list, keeping it sorted.
	 */
	@Override
	public void add(T element) {
		Node a =this.head.next;
		while(a!=this.tail){
			if(a.data.compareTo(element)>0){
				//a.prev.addAfter(element);
				break;
			}
			a=a.next;
		}
		a.prev.addAfter(element);
		
		
		// TODO: implement this method
	}

	@Override
	public void addFirst(T element) {
		// TODO: throw UnsupportedOperationException exception
		if(this.head.next==this.tail){
			throw new UnsupportedOperationException("invalid");
		}
		super.add(element);
	}

	@Override
	public void addLast(T element)  {
		if(this.tail.prev==this.head){
			throw new UnsupportedOperationException("invalid");
		}
		// TODO: throw UnsupportedOperationException exception
		super.add(element);
	}
}
