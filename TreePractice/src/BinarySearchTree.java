/**
 * Binary Tree practice problems
 * 
 * @author Matt Boutell and <<<YOUR NAME HERE>>>. 2014.
 * @param <T>
 */

/*
 * TODO: 0 You are to implement the four methods below. I took most of them from
 * a CSSE230 exam given in a prior term. These can all be solved by recursion -
 * I encourage you to do so too, since most students find practicing recursion
 * to be more useful.
 */
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
		// DONE: 1 Write this. recurive method at bottom 
		return root.countPositive();
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
		// DONE: 2 Write this.recurive method at bottom
		return this.root.getDepth(item);
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
		// DONE: 3 Write this.recurive method at bottom
		return this.root.numChildrenOfEachNode();
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
		//DONE recurive method at bottom
		return this.root.isZigZag();
	}

	public void insert(T e) {
		root = root.insert(e);
	}

	// /////////////// BinaryNode
	public class BinaryNode {

		public T element;
		public BinaryNode left;
		public BinaryNode right;

		public BinaryNode(T element) {
			this.element = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}
		
		public boolean isZigZag(){
			if(this==NULL_NODE){ //check if there is nothing in the tree
				return true;
			}
			//check if there is only one node in tree
			if(this.left==NULL_NODE && this.right==NULL_NODE){
				return true;
			}
			//if a node has 2 children, it's a false according to instruction
			if(this.left!=NULL_NODE && this.right!=NULL_NODE){
				return false;
			}
			//if the current node is a left node
			if(this.left!=NULL_NODE){
				// if its child is a left child, it 's false 
				if(this.left.left!=NULL_NODE){
					return false;
				}
				//continue checking condition
				return this.left.isZigZag();
			}else if(this.right!=NULL_NODE){ // if the current node is a right node
				// if its child is a right child, it's false
				if(this.right.right!=NULL_NODE){
					return false;
				}
				//continue checking condition
				return this.right.isZigZag();
			}
			//bascially useless, but it needs a return statement. so..
			return false;
			
		}
		
		
		
		public String numChildrenOfEachNode(){
			//establish a string as a result
			String sb= "";
			// if nothing is in the tree, there will be nothing in the string
			if(this==NULL_NODE){
				return sb;
			}
			//if current node has 2 children, put 2 in the string
			if(this.left!=NULL_NODE && this.right!=NULL_NODE){
				return sb+="2"+this.left.numChildrenOfEachNode()+this.right.numChildrenOfEachNode();
			}else if(this.left==NULL_NODE && this.right!=NULL_NODE){
				// if only 1 child, put 1 in string, same below 
				return sb+="1"+this.left.numChildrenOfEachNode()+this.right.numChildrenOfEachNode();
			}else if(this.left!=NULL_NODE && this.right==NULL_NODE){
				return sb+="1"+this.left.numChildrenOfEachNode()+this.right.numChildrenOfEachNode();
			}else{
				//no child, put 0
				return sb+="0";
			}
		}
		
		public int getDepth(T item){
			//if there is nothing in the tree or the tree doesn't have this item 
			//at all, return a -1 as specified
			if(this==NULL_NODE || !this.contains(item)){
				return -1;
			}
			//if the item is found, return 0
			if(this.element.equals(item)){
				return 0;
			}else if (this.element.compareTo(item)<0){
			// if the current data is smaller than item, search the right side of the tree
				return 1+this.right.getDepth(item);
			}else if (this.element.compareTo(item)>0){
			// if the current data is bigger than item, search the left side of the tree
				return 1+this.left.getDepth(item);
			}
			return -1;
		}
		
		// a quick contains method that check whether an element exisit in tree
		// i used it to make getDepth method easier. 
		public boolean contains(T item){
			if(this==NULL_NODE){
				return false;
			}
			if(this.element.equals(item)){
				return true;
			}
			return this.left.contains(item)||this.right.contains(item);
		}
		
		
		public int countPositive(){
			//if nothing is in the tree, simply return a 0 
			if(this==NULL_NODE){
				return 0;
			}
			// if the element is no larger than 0,
			//it continue search the tree to find a postive number 
			//(only the right side is possible for larger value)
			if((int)this.element==0 || (int)this.element<0){
				return this.right.countPositive();
			}
			//if the current number is greater than 0, its left could be larger than 0
			//as well, so search both side to make sure all number are went through
			return 1+this.right.countPositive()+this.left.countPositive();
			
		}

		public BinaryNode insert(T e) {
			if (this == NULL_NODE) {
				return new BinaryNode(e);
			} else if (e.compareTo(element) < 0) {
				left = left.insert(e);
			} else if (e.compareTo(element) > 0) {
				right = right.insert(e);
			} else {
				// do nothing
			}
			return this;
		}
	}
}