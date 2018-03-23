package editortrees;

// A node in a height-balanced binary tree with rank.
// Except for the NULL_sNODE (if you choose to use one), one node cannot
// belong to two different trees.
/**
 * 
 * A Node used to hold a character in an EditTree.
 *
 * @author kochelmj.
 *         Created Feb 5, 2014.
 */
public class Node {

	enum Code {
		SAME, LEFT, RIGHT
	};

	// The fields would normally be private, but for the purposes of this class,
	// we want to be able to test the results of the algorithms in addition to
	// the
	// "publicly visible" effects

	/**
	 * The element contained by this node.
	 */
	protected char element;
	/**
	 * The left and right children/subtrees of this node.
	 */
	protected Node left, right; // subtrees
	/**
	 * The parent node of this node.
	 */
	protected Node parent;
	/**
	 * The inorder position of this node within its own subtree.
	 */
	protected int rank;
	/**
	 * The balance code of this node, representing if its subtrees have different or same heights.
	 */
	protected Code balance;

	/**
	 * 
	 * Constructs a Node with the given element.
	 * Assigns children as NULL_NODES and parent as null.
	 *
	 * @param c
	 */
	public Node(char c) {
		this.element = c;
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
		this.rank = 0;
		this.balance = Code.SAME;
		this.parent = null;
	}
	
	/**
	 * 
	 * Constructs a NULL_NODE
	 *
	 */
	public Node() {
		this.parent = null;
	}

	/**
	 * 
	 * Constructs a Node with the given element.
	 * Assigns the given parent and sets the children to NULL_NODE.
	 *
	 * @param c
	 * @param parent
	 */
	public Node(char c, Node parent) {
		this.element = c;
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
		this.rank = 0;
		this.balance = Code.SAME;
		this.parent = parent;
	}

	
	/**
	 * 
	 * Returns the height of this node in log(n) time.
	 *
	 * @return height
	 */
	public int height() {
		Node currentNode = this;
		int height = -1;
		while (currentNode != EditTree.NULL_NODE) {
			if (currentNode.balance != Code.RIGHT) {
				currentNode = currentNode.left;
			} else {
				currentNode = currentNode.right;
			}
			height++;
		}
		return height;
	}

	/**
	 * @return this node's in order tree
	 * 
	 */
	@Override
	public String toString() {
		String result;
		if (this.equals(EditTree.NULL_NODE)) {
			return "";
		}
		result = "";
		if (this.left != EditTree.NULL_NODE) {
			result += this.left.toString();
		}
		result += this.element;
		if (this.right != EditTree.NULL_NODE) {
			result += this.right.toString();
		}
		return result;
	}

	/**
	 * 
	 * returns the size of this node and its lineage.
	 *
	 * @return size
	 */
	public int size() {
		if (this == EditTree.NULL_NODE) {
			return 0;
		}
		return this.rank + 1 + this.right.size();
	}

}