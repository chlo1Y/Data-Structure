package priorityQueue;
import java.util.ArrayList;

/**
 * An implementation of the Priority Queue interface using an array list.
 * 
 * @author Matt Boutell and <<TODO: Your name here>>>. Created Mar 29, 2014.
 * 
 * @param <T>
 *            Generic type of PQ elements
 */
public class ArrayListMinPQ<T extends Comparable<T>> {
	// Requirement: You must use this instance variable without changes.
	private ArrayList<T> items;

	public ArrayListMinPQ() {
		ArrayList<T> a = new ArrayList<T>();
		this.items=a;
		
		
		// TODO: implement
	}

	public T findMin() {
		// This is also known as peekMin
		if(this.items.size()==0){
			return null;
		}
		T temp=this.items.get(0);
		for(int i=0; i<this.size(); i++){
			if (this.items.get(i).compareTo(temp)<0){
				temp=this.items.get(i);
			}
		}
		// Done: implement
		return temp;
	}

	public T deleteMin() {
		// Done: implement
		T a=this.findMin();
		this.items.remove(a);
		return a;
		
	}

	public void insert(T item) {
		// Done: implement
		this.items.add(item);
	}

	public int size() {
		// Done: implement
		
		return this.items.size();
	}

	public boolean isEmpty() {
		// Done: implement
		
		return this.items.isEmpty();
		
	}

	public void clear() {
		// Done: implement
		this.items = new ArrayList<>();
	}
}
