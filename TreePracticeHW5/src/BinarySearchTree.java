/**
 * More Binary Tree practice problems. This problem creates BSTs of type
 * Integer: 1. Neither problem makes use of the BST ordering property; I just
 * found insert() to be a convenient way to build trees for testing. 2. I used
 * Integer instead of T since the makeTree method sets the data value of each
 * node to be a depth, which is an Integer.
 * 
 * @author Matt Boutell and <<<YOUR NAME HERE>>>.
 * @param <T>
 */

/*
 * TODO: 0 You are to implement the methods below. Use recursion!
 */
public class BinarySearchTree {

	private BinaryNode root;

	private final BinaryNode NULL_NODE = new BinaryNode(null);

	public BinarySearchTree() {
		root = NULL_NODE;
	}

	/**
	 * This constructor creates a full tree of Integers, where the value of each
	 * node is just the depth of that node in the tree.
	 * 
	 * @param maxDepth
	 *            The depth of the leaves in the tree.
	 */
	public BinarySearchTree(int maxDepth) {
		if(maxDepth==-1){
			this.root=NULL_NODE;
		}
		this.root=this.putTree(0,maxDepth);
		
		// DONE: 1 Write this.
		// Hint: You may find it easier if your recursive helper method is
		// outside of the BinaryNode class.

	}
	public BinaryNode putTree(int currentDep ,int maxDepth){
		if(maxDepth==-1){
			return NULL_NODE;
		}
		// if ot reaches the mx depth, create a new node contains maxdepth
		if(currentDep==maxDepth){
			return new BinaryNode(maxDepth);
		}
		//get a node that is in the current depth (current node)
		BinaryNode newNode= new BinaryNode(currentDep);
		//put a node on its left and right with incremented depth
		newNode.left=this.putTree(currentDep+1, maxDepth);
		newNode.right=this.putTree(currentDep+1, maxDepth);
		return newNode;
		
	}

	public int getSumOfHeights() {
		// TODO. 2 Write this.
		// Can you do it in O(n) time instead of O(n log n) by avoiding repeated
		// calls to height()?
		if(this.root==NULL_NODE){
			return -1;
		}
		if(this.root.right==NULL_NODE && this.root.left==NULL_NODE){
			return 0;
		}
		return this.root.getHeight().total+1;
	}
	
	

	// These are here for testing.
	public void insert(Integer e) {
		root = root.insert(e);
	}

	/**
	 * @return A string showing an in-order traversal of nodes with extra
	 *         brackets so that the structure of the tree can be determined.
	 */
	public String toStructuredString() {
		return root.toStructuredString();
	}
	
	public class Wrapper{
		int height;
		int total;
		//helper that can contain both a node's height and store total height
		public Wrapper(int height, int total){
			this.height=height;
			this.total=total;
		}
	}

	// /////////////// BinaryNode
	public class BinaryNode {

		public Integer data;
		public BinaryNode left;
		public BinaryNode right;

		public BinaryNode(Integer element) {
			this.data = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}
		Wrapper getHeight(){
			//if a null node is reached(not the initial root),
			//it's height is 0 (so won't mess up calculaion)
			if(this==NULL_NODE){
				return new Wrapper(0,-1);
			}
			Wrapper temp = new Wrapper(0,0);
			Wrapper left = this.left.getHeight();
			Wrapper right= this.right.getHeight();
			temp.height= Math.max(left.height, right.height)+1;
			temp.total=temp.height+right.total+left.total;
			return temp;
			
		}
		
		
		public BinaryNode insert(Integer e) {
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
		
		

		public String toStructuredString() {
			if (this == NULL_NODE) {
				return "";
			}
			return "[" + left.toStructuredString() + this.data
					+ right.toStructuredString() + "]";
		}

	}
	
	
}