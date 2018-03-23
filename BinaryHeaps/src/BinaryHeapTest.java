import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests binary heaps.
 * 
 * @author Matt Boutell. Created May 7, 2013.
 */
public class BinaryHeapTest {

	/**
	 * Simple test method for insert, delete, toString, and sort. Enforces the
	 * method signatures.
	 */
	@Test
	public void testSimple() {
		// TODO: Implement the BinaryHeap class according to the spec.
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>();
		// deleteMin returns null if it has no elements.
		assertNull(heap.deleteMin());
		heap.insert(21);
		assertEquals("[null, 21]", heap.toString());
		int min = heap.deleteMin();
		assertEquals(21, min);
		assertEquals("[null]", heap.toString());
		String[] csLegends = new String[] { "Edsger Dijkstra", "Grace Hopper",
				"Donald Knuth", "Ada Lovelace", "Claude Shannon", "Alan Turing" };

		BinaryHeap<String> csLegendsHeap = new BinaryHeap<String>();
		csLegendsHeap.sort(csLegends);
		assertEquals(
				"[Ada Lovelace, Alan Turing, Claude Shannon, Donald Knuth, Edsger Dijkstra, Grace Hopper]",
				Arrays.toString(csLegends));
	}

	// TODO: Add unit tests for each method until you are satisfied your code is
	// correct. I will test your code with more complex tests. Since this
	// assignment is short, I'd like you to give the tests some thought, rather
	// than just always relying on someone else's tests. Professional developers
	// usually write their own unit tests.
	
	@Test
	public void testInsert() {
		// TODO: Implement the BinaryHeap class according to the spec.
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>();
		heap.insert(21);
		assertEquals("[null, 21]", heap.toString());
		
		BinaryHeap<Integer> heap1 = new BinaryHeap<Integer>();
		heap1.insert(6);
		heap1.insert(4);
		heap1.insert(8);
		heap1.insert(1);
		heap1.insert(5);
		heap1.insert(3);
		heap1.insert(2);
		heap1.insert(7);
		assertEquals("[null, 1, 4, 2, 6, 5, 8, 3, 7]", heap1.toString());
		
		
		
	}
	
	
	@Test
	public void testDelete() {
		// TODO: Implement the BinaryHeap class according to the spec.
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>();
		heap.insert(21);
		heap.insert(10);
		heap.insert(5);
		int min =heap.deleteMin();
		assertEquals("[null, 10, 21]", heap.toString());
		assertEquals(5, min);	
	}
	
	
	@Test
	public void testSort() {
		// TODO: Implement the BinaryHeap class according to the spec.
		BinaryHeap<Integer> heap = new BinaryHeap<Integer>();
		
		
		Integer[] intGroup = new Integer[] { 4,3,5,1,2 };

		heap.sort(intGroup);
		assertEquals(
				"[1, 2, 3, 4, 5]",
				Arrays.toString(intGroup));
	}

}
