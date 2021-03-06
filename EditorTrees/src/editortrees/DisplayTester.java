package editortrees;

import static org.junit.Assert.assertEquals;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author kochelmj. Created Jan 28, 2014.
 */
public class DisplayTester {

	/**
	 * TODO Put here a description of what this method does.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EditTree t2 = new EditTree("abcdefghi");
		DisplayTree.display(t2);

	}

	private static EditTree makeFullTreeWithHeight(int height, char start) {
		EditTree t = new EditTree();
		// This would be much easier to do recursively, if we could
		// depend on having a Node constructor that took two children
		// as arguments!

		for (int i = 0; i <= height; i++) {
			int offset = (int) Math.pow(2, height - i) - 1;
			int increment = (int) Math.pow(2, height + 1 - i);
			for (int j = 0; j < Math.pow(2, i); j++) {
				t.add((char) (start + offset), 2 * j);
				offset += increment;
			}
		}
		return t;
	}

	public static EditTree makeTreeFromSlides() {
		// Level-order insertion so no rotations (we're pretty sure).
		// CONSIDER: a check to make sure there were no rotations?
		EditTree t = new EditTree();
		t.add('I', 0);
		t.add('C', 0);
		t.add('W', 2);
		t.add('A', 0);
		t.add('G', 2);
		t.add('M', 4);
		t.add('Y', 6);
		t.add('B', 1);
		t.add('F', 3);
		t.add('H', 5);
		t.add('L', 7);
		t.add('R', 9);
		t.add('X', 11);
		t.add('a', 13);
		t.add('D', 3);
		t.add('K', 8);
		t.add('O', 11);
		t.add('T', 13);
		t.add('Z', 17);
		t.add('N', 11);
		t.add('P', 13);
		t.add('S', 15);
		t.add('U', 17);
		assertEquals(0, t.totalRotationCount());

		return t;
	}

}
