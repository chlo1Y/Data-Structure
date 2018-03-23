package buildtree;

import java.util.Stack;

/**
 * 
 * @author Matt Boutell and <<<YOUR NAME HERE>>>.
 * @param <T>
 */

public class BinaryTree {

	private BinaryNode root;

	private final BinaryNode NULL_NODE = new BinaryNode(null);

	public BinaryTree() {
		root = NULL_NODE;
	}

	/**
	 * Constructs a tree (any tree of characters, not just a BST) with the given
	 * values and number of children, given in a pre-order traversal order. See
	 * the HW spec for more details.
	 * 
	 * @param chars
	 *            One char per node.
	 * @param children
	 *            L,R, 2, or 0.
	 */
	public BinaryTree(String chars, String children) {
		// TODO: Implement this constructor. You may not add any other fields to
		// the BinaryTree class, but you may add local variables and helper
		// methods if you like.
		
		if(chars.equals("")){
			this.root=NULL_NODE;
		}else{
			// create a new stack 
			Stack<BinaryNode> a= new Stack<>();
			// renew the stack 
			a= toBinaryTree(chars,children, a);
			root=a.pop();
		}
		
		
		
	}
	
	public Stack<BinaryNode> toBinaryTree(String chars, String children, Stack<BinaryNode> a){
		int length =children.length()-1;
		if(length <0){
			return a;
		}
		BinaryNode resultNode= new BinaryNode(chars.charAt(length));
		if(children.charAt(length)=='0'){
			//it's a leaf don't need to do anything else other than push 
			//node into the stack untill parent is found
		}
		
		if(children.charAt(length)=='L'){
			resultNode.left=a.pop();
			// if it's a left means the current node in the stack is 
			//the left child of the new char
		}
		
		if(children.charAt(length)=='R'){
			resultNode.right=a.pop();
			// if it's a right means the current node in the stack is 
			//the right child of the new char
		}
		if(children.charAt(length)=='2'){
			resultNode.left=a.pop();
			resultNode.right=a.pop();
			//if it has two children the one on the top should be its left child
			//the next one is its right child 
		}
		a.push(resultNode);// for every case push node 
		
		
		children=children.substring(0, length);//reducing the length of children string
		return toBinaryTree(chars, children, a); 
	}

	
	
	/**
	 * In-order traversal of the characters
	 */
	@Override
	public String toString() {
		return root.toString();
	}

	/**
	 * @return A string showing an in-order traversal of nodes with extra
	 *         brackets so that the structure of the tree can be determined.
	 */
	public String toStructuredString() {
		return root.toStructuredString();
	}

	// /////////////// BinaryNode
	public class BinaryNode {

		public Character data;
		public BinaryNode left;
		public BinaryNode right;

		public BinaryNode(Character element) {
			this.data = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}
		

		@Override
		public String toString() {
			if (this == NULL_NODE) {
				return "";
			}
			return left.toString() + data + right.toString();
		}

		public String toStructuredString() {
			if (this == NULL_NODE) {
				return "";
			}
			return "(" + left.toStructuredString() + this.data
					+ right.toStructuredString() + ")";
		}

	}
}