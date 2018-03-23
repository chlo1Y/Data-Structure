import java.util.TreeSet;

// NEW COMMENTS:
// Note:
// 1) All info passed from method to method is done using parameters and return values. 
// On the exam, you may not add fields to the BST or to the Nodes.
// 2) Note the use of NULL_NODE as the only base case. Makes for elegant code.
// I reserve the right to assign a couple points for elegant code on the test.
public class BinarySearchTree<T extends Comparable<? super T>> {

	private BinaryNode root;

	private final BinaryNode NULL_NODE = new BinaryNode(null);

	public BinarySearchTree() {
		root = NULL_NODE;
	}

	/**
	 * This method counts the number of occurrences of positive Integers in the
	 * tree that is of type Integer. Hint: You may assume this tree contains
	 * integers, so may use a cast.
	 * 
	 * @return The number of positive integers in the tree.
	 */

	public int countPositives() {
		return root.countPositives();
	}

	/**
	 * Recall that the depth of a node is number of edges in a path from this
	 * node to the root. Returns the depth of the given item in the tree. If the
	 * item isn't in the tree, then it returns -1.
	 * 
	 * @param item
	 * @return The depth, or -1 if it isn't in the tree.
	 */
	public int getDepth(T item) {
		return root.getDepth(item, 0);
	}

	/**
	 * This method visits each node of the BST in pre-order and determines the
	 * number of children of each node. It produces a string of those numbers.
	 * If the tree is empty, an empty string is to be returned. For the
	 * following tree, the method returns the string: "2200110"
	 * 
	 * 10 5 15 2 7 18 10
	 * 
	 * @return A string representing the number of children of each node when
	 *         the nodes are visited in pre-order.
	 */

	public String numChildrenOfEachNode() {
		return root.numChildrenOfEachNode();
	}

	/**
	 * This method determines if a BST forms a zig-zag pattern. By this we mean
	 * that each node has exactly one child, except for the leaf. In addition,
	 * the nodes alternate between being a left and a right child. An empty tree
	 * or a tree consisting of just the root are both said to form a zigzag
	 * pattern. For example, if you insert the elements 10, 5, 9, 6, 7 into a
	 * BST in that order. , you will get a zig-zag.
	 * 
	 * @return True if the tree forms a zigzag and false otherwise.
	 */
	public boolean isZigZag() {
		return root.isZigZag(true) || root.isZigZag(false);
	}

	public void insert(T e) {
		root = root.insert(e);
	}

	// /////////////// BinaryNode
	public class BinaryNode {

		public T data;
		public BinaryNode left;
		public BinaryNode right;

		public BinaryNode(T element) {
			this.data = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}

		public BinaryNode insert(T e) {
			if (this == NULL_NODE) {
				return new BinaryNode(e);
			} else if (e.compareTo(data) < 0) {
				left = left.insert(e);
			} else if (e.compareTo(data) > 0) {
				right = right.insert(e);
			} else {
				// do nothing
			}
			return this;
		}

		public int countPositives() {
			if (this == NULL_NODE) {
				return 0;
			}
			return (((Integer) this.data) > 0 ? 1 : 0) + left.countPositives() + right.countPositives();
			// The previous ternary statement, cond ? value_if_true : value_if_false is a shortcut 
			// for an if-else statement whose purpose is to assign a value.
			// It's like:
			// if (((Integer)this.data) > 0)
			//	return 1 + left.countPositives() + right.countPositives();
			// else 
			//	return 0 + left.countPositives() + right.countPositives();
			// but all on 1 line. :)
		}

		public int getDepth(T item, int depth) {
			if (this == NULL_NODE) {
				return -1; // not found
			}
			if (item.compareTo(this.data) < 0) {
				return this.left.getDepth(item, depth + 1);
			} else if (item.compareTo(this.data) > 0) {
				return this.right.getDepth(item, depth + 1);
			} else {
				// Found
				return depth;
			}
		}

		public String numChildrenOfEachNode() {
			if (this == NULL_NODE) {
				return "";
			}
			int count = 0;
			if (left != NULL_NODE) {
				count++;
			}
			if (right != NULL_NODE) {
				count++;
			}
			return count + left.numChildrenOfEachNode() + right.numChildrenOfEachNode();
		}

		public boolean isZigZag(boolean shouldHaveLeftChild) {
			// Leaf
			if (this == NULL_NODE || (left == NULL_NODE && right == NULL_NODE)) {
				return true;
			}
			// 2 children
			if (left != NULL_NODE && right != NULL_NODE) {
				return false;
			}
			// Expected child
			if (shouldHaveLeftChild && left != NULL_NODE) {
				return left.isZigZag(false);
			}
			if (!shouldHaveLeftChild && right != NULL_NODE) {
				return right.isZigZag(true);
			}
			return false;
		}
	}
}