package editortrees;

import java.util.Stack;

import editortrees.Node.Code;

// A height-balanced binary tree with rank that could be the basis for a text editor.

/**
 * An AVL Tree that aids a text editor
 *
 * @author kochelmj.
 *         Created Feb 5, 2014.
 */
public class EditTree {

	private Node root;
	private int rotationCount;
	
	/**
	 * The "null" node
	 */
	protected static final Node NULL_NODE = new Node();
	private Node newNode = NULL_NODE;
	private Node delNode = NULL_NODE;
	private char delChar;
	private Code delDirect;
	private static final double LOG2 = Math.log(2);

	/**
	 * Construct an empty tree
	 */
	public EditTree() {
		this.root = NULL_NODE;
		this.rotationCount = 0;

	}

	/**
	 * Construct a single-node tree whose element is c
	 * 
	 * @param c
	 */
	public EditTree(char c) {
		this.root = new Node(c);
		this.rotationCount = 0;
	}

	/**
	 * Create an EditTree whose toString is s. This can be done in O(N) time,
	 * where N is the length of the tree (repeatedly calling insert() would be
	 * O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {
		this.root = makeTreeFromStr(s);
	}

	/**
	 * 
	 * Helper method for EditTree(String s) constructor
	 *
	 * @param s
	 * @return
	 */
	private Node makeTreeFromStr(String s) {
		if (s.length() == 0) {
			return NULL_NODE;
		} else if (s.length() == 1) {
			return new Node(s.charAt(0));
		} else {
			Node midNode = new Node(s.charAt(s.length() / 2));
			Node leftNode = makeTreeFromStr(s.substring(0, s.length() / 2));
			Node rightNode = makeTreeFromStr(s.substring(s.length() / 2 + 1,
					s.length()));
			midNode.left = leftNode;
			leftNode.parent = midNode;
			midNode.right = rightNode;
			rightNode.parent = midNode;
			midNode.rank = s.length() / 2;
			// calculation of checking whether the length of the string will
			// result in tilted tree
			int k = (int) (Math.log(s.length()) / LOG2);
			if (Math.pow(2, k) - s.length() == 0) {
				// if the string is a power of 2
				midNode.balance = Code.LEFT;
			} else {
				midNode.balance = Code.SAME;
			}
			return midNode;
		}
	}

	/**
	 * Make this tree be a copy of e, with all new nodes, but the same shape and
	 * contents.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {
		this.root = this.copyTree(e.root, null);
	}

	/**
	 * 
	 * Helper method for EditTree(EditTree e) constructor
	 *
	 * @param copyNode
	 * @param parentNode
	 * @return
	 */
	private Node copyTree(Node copyNode, Node parentNode) {
		Node currentNode = NULL_NODE;
		Node leftNode = NULL_NODE;	//currentNode's left child
		Node rightNode = NULL_NODE;	//currentNode's right child
		if (copyNode != NULL_NODE) {
			currentNode = new Node(copyNode.element, parentNode);
			currentNode.balance = copyNode.balance;
			currentNode.rank = copyNode.rank;
			leftNode = copyTree(copyNode.left, currentNode);
			rightNode = copyTree(copyNode.right, currentNode);
			currentNode.left = leftNode;
			currentNode.right = rightNode;
		}
		return currentNode;
	}

	/**
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		return this.root.height();
	}

	/**
	 * 
	 * returns the total number of rotations done in this tree since it was
	 * created. A double rotation counts as two.
	 * 
	 * @return number of rotations since tree was created.
	 */
	public int totalRotationCount() {
		return this.rotationCount;
	}

	/**
	 * return the string produced by an inorder traversal of this tree
	 */
	@Override
	public String toString() {
		return this.root.toString();

	}

	/**
	 * 
	 * @param pos
	 *            position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		if (pos >= this.size() || pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		return this.getNode(pos, this.root).element;
	}

	/**
	 * 
	 * Helper method for get(int pos) method.
	 * Uses recursion to find the desired node in log(n) time.
	 *
	 * @param pos
	 * @param node
	 * @return
	 */
	private Node getNode(int pos, Node node) {
		if (pos == node.rank) {
			return node;
		}
		if (pos > node.rank) {
			pos = pos - node.rank - 1;
			return this.getNode(pos, node.right);
		} else {
			return this.getNode(pos, node.left);
		}
	}

	/**
	 * 
	 * @param c
	 *            character to add to the end of this tree.
	 */
	public void add(char c) {
		Node node = this.root;
		if (node != EditTree.NULL_NODE) {
			while (node.right != EditTree.NULL_NODE) {
				node = node.right;
			}
			this.newNode = new Node(c, node);
			node.right = this.newNode;
		} else {
			this.newNode = new Node(c);
			this.root = this.newNode;
		}
		this.checkAndRotateAdd(this.newNode);
	}
	
	/**
	 * 
	 * Helper method for add(char c) method.
	 * Uses enum codes for child and parent directions.
	 *
	 * @param node
	 */
	private void checkAndRotateAdd(Node node) {
		Code parentDirection = Code.SAME;
		Code childDirection = Code.SAME;
		while (node.parent != null) {	//node not the root
			childDirection = parentDirection;
			if (node.parent.right == node) {	//node is right child
				parentDirection = Code.RIGHT;
			} else if (node.parent.left == node) {	//node is left child
				parentDirection = Code.LEFT;
			}
			if (node.parent.balance == Code.LEFT
					&& parentDirection == Code.RIGHT) {	//was leaning left, became balanced
				node.parent.balance = Code.SAME;
				break;
			} else if (node.parent.balance == Code.LEFT
					&& parentDirection == Code.LEFT) {	//leaning left, rotation needed
				this.rotate(node, parentDirection, childDirection);
				break;
			} else if (node.parent.balance == Code.RIGHT
					&& parentDirection == Code.LEFT) {	//was leaning right, became balanced
				node.parent.balance = Code.SAME;
				break;
			} else if (node.parent.balance == Code.RIGHT
					&& parentDirection == Code.RIGHT) {	//leaning right, rotation needed
				this.rotate(node, parentDirection, childDirection);
				break;
			} else if (node.parent.balance == Code.SAME) {	//was balanced, new balance updated
				node.parent.balance = parentDirection;
			}
			node = node.parent;
		}
	}

	
	/**
	 * 
	 * Performs a single left rotation
	 *
	 * @param node = the child of the unbalanced node
	 * @return
	 */
	private Node singleLeft(Node node) {
		node.parent.right = node.left;
		if (node.left != NULL_NODE) {
			node.left.parent = node.parent;
		}
		node.left = node.parent;
		node.parent = node.left.parent;
		node.left.parent = node;
		if (node.parent != null) {
			if (node.parent.left == node.left) {
				node.parent.left = node;
			} else if (node.parent.right == node.left) {
				node.parent.right = node;
			}
		} else {
			this.root = node;
		}
		node.rank = node.rank + node.left.rank + 1;
		this.rotationCount++;
		return node;
	}

	/**
	 * 
	 * Performs a single right rotation
	 *
	 * @param node = the child of the unbalanced node
	 * @return
	 */
	private Node singleRight(Node node) {
		node.parent.left = node.right;
		if (node.right != NULL_NODE) {
			node.right.parent = node.parent;
		}
		node.right = node.parent;
		node.parent = node.right.parent;
		node.right.parent = node;
		if (node.parent != null) {
			if (node.parent.right == node.right) {
				node.parent.right = node;
			} else if (node.parent.left == node.right) {
				node.parent.left = node;
			}
		} else {
			this.root = node;
		}
		node.right.rank = node.right.rank - node.rank - 1;
		this.rotationCount++;
		return node;
	}

	/**
	 * 
	 * Uses directions to calculate needed rotations.
	 * Rotations are then performed and balance codes are updated.
	 *
	 * @param node
	 * @param parentDirection
	 * @param childDirection
	 */
	private void rotate(Node node, Code parentDirection, Code childDirection) {
		if (parentDirection == Code.LEFT && childDirection == Code.LEFT) {	//single right situation
			this.singleRight(node);
			node.right.balance = Code.SAME;
			node.balance = Code.SAME;
		} else if (parentDirection == Code.RIGHT
				&& childDirection == Code.RIGHT) {	//single left situation
			this.singleLeft(node);
			node.left.balance = Code.SAME;
			node.balance = Code.SAME;
		} else if (parentDirection == Code.LEFT && childDirection == Code.RIGHT) {	//double right situation
			this.singleLeft(node.right);
			this.singleRight(node.parent);
			updateBalanceCode(node);
			updateBalanceCode(node.parent.right);
			updateBalanceCode(node.parent);// not update to SAME if subtrees are
											// unbalanced
		} else {	//double left situation
			this.singleRight(node.left);
			this.singleLeft(node.parent);
			updateBalanceCode(node);
			updateBalanceCode(node.parent.left);
			updateBalanceCode(node.parent);// not update to SAME if subtrees are
											// unbalanced
		}
	}
	/**
	 * 
	 * Updates the balance code of a single node.
	 *
	 * @param node
	 */
	private void updateBalanceCode(Node node) {
		if (node.left.height() > node.right.height()) {
			node.balance = Code.LEFT;
		} else if (node.left.height() < node.right.height()) {
			node.balance = Code.RIGHT;
		} else {
			node.balance = Code.SAME;
		}
	}

	/**
	 * 
	 * @param c
	 *            character to add
	 * @param pos
	 *            character added in this inorder position
	 * @throws IndexOutOfBoundsException
	 *             id pos is negative or too large for this tree
	 */
	public void add(char c, int pos) throws IndexOutOfBoundsException {
		if (pos > this.size() || pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.root = this.addNode(c, pos, this.root, null);
		this.checkAndRotateAdd(this.newNode);

	}
	
	/**
	 * 
	 * Helper method for add(char c, int pos) method.
	 * 
	 *
	 * @param c
	 * @param pos
	 * @param currentNode
	 * @param parent
	 * @return
	 */
	private Node addNode(char c, int pos, Node currentNode, Node parent) {
		if (currentNode == NULL_NODE) {	//node will be inserted here
			this.newNode = new Node(c, parent);
			return this.newNode;
		}
		if (pos <= currentNode.rank) {	//node will be in left subtree
			currentNode.rank++;
			currentNode.left = this.addNode(c, pos, currentNode.left,
					currentNode);
		} else {	//node will be in right subtree
			pos = pos - currentNode.rank - 1;
			currentNode.right = this.addNode(c, pos, currentNode.right,
					currentNode);
		}
		return currentNode;
	}

	/**
	 * 
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return this.root.size();
	}

	/**
	 * 
	 * @param pos
	 *            position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		if (pos > this.size() - 1 || pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.root = this.deleteNode(pos, this.root, true);
		this.checkAndRotateDel(this.delNode.parent, this.delDirect);
		return this.delChar;
	}

	/**
	 * 
	 * Checks and rotates after a deletion
	 *
	 * @param node
	 * @param direct
	 */
	private void checkAndRotateDel(Node node, Code direct) {
		if (node == null) {
			return;
		}
		// end of checking
		if (node.balance == Code.SAME) {
			if (direct == Code.LEFT) {
				node.balance = Code.RIGHT;
			} else {
				node.balance = Code.LEFT;
			}
			return;
		} else {
			// no rotation case
			if (node.balance == direct) {
				node.balance = Code.SAME;
				if (node != this.root) {
					if (node.parent.left == node) {
						this.checkAndRotateDel(node.parent, Code.LEFT);
					} else {
						this.checkAndRotateDel(node.parent, Code.RIGHT);
					}
				}
			} else {
				// left or right rotation
				if (direct == Code.LEFT) {
					Node rotateNode = node.right;
					// double or single rotation
					if (rotateNode.balance == Code.RIGHT) {
						this.rotate(rotateNode, Code.RIGHT, Code.RIGHT);
						if (rotateNode != this.root) {
							if (rotateNode.parent.left == rotateNode) {
								this.checkAndRotateDel(rotateNode.parent,
										Code.LEFT);
							} else {
								this.checkAndRotateDel(rotateNode.parent,
										Code.RIGHT);
							}
						}
					} else {
						this.rotate(rotateNode, Code.RIGHT, Code.LEFT);
						// if double rotation, then the new root of the subtree
						// is rotateNode.parent
						if (rotateNode.parent != this.root) {
							if (rotateNode.parent.parent.left == rotateNode.parent) {
								this.checkAndRotateDel(
										rotateNode.parent.parent, Code.LEFT);
							} else {
								this.checkAndRotateDel(
										rotateNode.parent.parent, Code.RIGHT);
							}
						}
					}
				} else if (direct == Code.RIGHT) {
					Node rotateNode = node.left;
					// double or single rotation
					if (rotateNode.balance == Code.LEFT) {
						this.rotate(rotateNode, Code.LEFT, Code.LEFT);
						if (rotateNode != this.root) {
							if (rotateNode.parent.left == rotateNode) {
								this.checkAndRotateDel(rotateNode.parent,
										Code.LEFT);
							} else {
								this.checkAndRotateDel(rotateNode.parent,
										Code.RIGHT);
							}
						}
					} else {
						this.rotate(rotateNode, Code.LEFT, Code.RIGHT);
						// if double rotation, then the new root of the subtree
						// is rotateNode.parent
						if (rotateNode.parent != this.root) {
							if (rotateNode.parent.parent.left == rotateNode.parent) {
								this.checkAndRotateDel(
										rotateNode.parent.parent, Code.LEFT);
							} else {
								this.checkAndRotateDel(
										rotateNode.parent.parent, Code.RIGHT);
							}
						}
					}
				} else {
					return; // if the deleted node is the root
				}
			}
		}
	}
	
	/**
	 * 
	 * Helper method for delete(int pos) method
	 *
	 * @param pos
	 * @param currentNode
	 * @param needChar
	 * @return
	 */
	private Node deleteNode(int pos, Node currentNode, boolean needChar) {
		if (pos == currentNode.rank) {
			// find the node and check how many children it has
			if (currentNode.left == NULL_NODE && currentNode.right == NULL_NODE) {
				this.delNode = currentNode;
				// store left child or right child for later check
				if (currentNode.parent != null) {
					if (currentNode.parent.left == currentNode) {
						this.delDirect = Code.LEFT;
					} else {
						this.delDirect = Code.RIGHT;
					}
				} else {
					this.delDirect = Code.SAME;
				}
				// store the char if it is needed
				if (needChar) {
					this.delChar = currentNode.element;
				}
				return NULL_NODE;
			} else if (currentNode.left == NULL_NODE) {
				currentNode.right.parent = currentNode.parent;
				this.delNode = currentNode;
				// store left child or right child for later check
				if (currentNode.parent != null) {
					if (currentNode.parent.left == currentNode) {
						this.delDirect = Code.LEFT;
					} else {
						this.delDirect = Code.RIGHT;
					}
				} else {
					this.delDirect = Code.SAME;
				}
				// store the char if it is needed
				if (needChar) {
					this.delChar = currentNode.element;
				}
				return currentNode.right;
			} else if (currentNode.right == NULL_NODE) {
				currentNode.left.parent = currentNode.parent;
				this.delNode = currentNode;
				// store left child or right child for later check
				if (currentNode.parent != null) {
					if (currentNode.parent.left == currentNode) {
						this.delDirect = Code.LEFT;
					} else {
						this.delDirect = Code.RIGHT;
					}
				} else {
					this.delDirect = Code.SAME;
				}
				// store the char if it is needed
				if (needChar) {
					this.delChar = currentNode.element;
				}
				return currentNode.left;
			} else {
				Node node = currentNode.right;
				while (node.left != NULL_NODE) {
					node = node.left;
				}
				currentNode.element = node.element;
				currentNode.right = this
						.deleteNode(0, currentNode.right, false);
				// store the char if it is needed
				if (needChar) {
					this.delChar = currentNode.element;
				}
				return currentNode;

			}
		} else if (pos < currentNode.rank) {
			currentNode.rank--;
			currentNode.left = this.deleteNode(pos, currentNode.left, true);
		} else {
			pos = pos - currentNode.rank - 1;
			currentNode.right = this.deleteNode(pos, currentNode.right, true);
		}

		return currentNode;
	}

	/**
	 * This method operates in O(length*log N), where N is the size of this
	 * tree.
	 * 
	 * @param pos
	 *            location of the beginning of the string to retrieve
	 * @param length
	 *            length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException
	 *             unless both pos and pos+length-1 are legitimate indexes
	 *             within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		if (pos >= this.size() || pos < 0 || (pos + length - 1) >= this.size()
				|| (pos + length - 1) < 0) {
			throw new IndexOutOfBoundsException("Index out of bound");
		}
		return getString(pos, length);
	}

	private String getString(int pos, int length) {
		if (length == 0) {
			return "";
		} else {
			return get(pos) + getString(pos + 1, length - 1);
		}
	}

	/**
	 * This method is provided for you, and should not need to be changed. If
	 * split() and concatenate() are O(log N) operations as required, delete
	 * should also be O(log N)
	 * 
	 * @param start
	 *            position of beginning of string to delete
	 * 
	 * @param length
	 *            length of string to delete
	 * @return an EditTree containing the deleted string
	 * @throws IndexOutOfBoundsException
	 *             unless both start and start+length-1 are in range for this
	 *             tree.
	 */
	public EditTree delete(int start, int length)
			throws IndexOutOfBoundsException {
		if (start < 0 || start + length >= this.size())
			throw new IndexOutOfBoundsException(
					(start < 0) ? "negative first argument to delete"
							: "delete range extends past end of string");
		EditTree t2 = this.split(start);
		EditTree t3 = t2.split(length);
		this.concatenate(t3);
		return t2;
	}

	/**
	 * Append (in time proportional to the log of the size of the larger tree)
	 * the contents of the other tree to this one. Other should be made empty
	 * after this operation.
	 * 
	 * @param other
	 * @throws IllegalArgumentException
	 *             if this == other
	 */
	public void concatenate(EditTree other) throws IllegalArgumentException {
		if (this == other) {
			throw new IllegalArgumentException("Illegal Argument!");
		}
		int ht = this.height(); // height of this tree
		int hv = other.height(); // height of the other tree
		if (ht == -1) {
			this.root = other.root;
			other.root = NULL_NODE;
			return;
		}
		if (hv == -1) {
			return;
		}
		if (ht >= hv) {
			other.delete(0);
			Node concatNode = other.delNode;
			concatTree(this, concatNode, other, Code.RIGHT);
		} else {
			this.delete(this.size() - 1);
			Node concatNode = this.delNode;
			concatTree(this, concatNode, other, Code.LEFT);
		}
		// make the other tree empty
		other.root = NULL_NODE;
	}
	
	/**
	 * 
	 * Helper method for concatenate(EditTree other) method
	 *
	 * @param left	left in order tree
	 * @param concatNode	removed from the smaller tree
	 * @param right		right in order tree
	 * @param direct	direction from root to concatenate
	 */
	private void concatTree(EditTree left, Node concatNode, EditTree right,
			Code direct) {
		if (left.height() == -1) {
			left.root = right.root;
			left.add(concatNode.element, 0);
			right.root = NULL_NODE;
			return;
		}
		if (right.height() == -1) {
			left.add(concatNode.element);
			return;
		}
		if (direct == Code.RIGHT) {
			Node samehNode = findSameHeight(left.root, left.height(),
					right.height(), direct);
			// update pointers
			concatNode.parent = samehNode.parent;
			if (concatNode.parent != null) {
				concatNode.parent.right = concatNode;
			} else {
				left.root = concatNode;
			}
			samehNode.parent = concatNode;
			concatNode.left = samehNode;
			if (right.root != NULL_NODE) {
				right.root.parent = concatNode;
			}
			concatNode.right = right.root;
			// update rank
			concatNode.rank = concatNode.left.size();
			// check and rotate
			left.checkAndRotateConcat(samehNode, Code.RIGHT);
			// update balance
			if (concatNode.left.height() > concatNode.right.height()) {
				concatNode.balance = Code.LEFT;
			} else if (concatNode.left.height() == concatNode.right.height()) {
				concatNode.balance = Code.SAME;
			} else {
				concatNode.balance = Code.RIGHT;
			}
		} else {
			Node samehNode = findSameHeight(right.root, left.height(),
					right.height(), direct);
			// update pointers
			concatNode.parent = samehNode.parent;
			if (concatNode.parent != null) {
				concatNode.parent.left = concatNode;
			} else {
				right.root = concatNode;
			}
			samehNode.parent = concatNode;
			concatNode.right = samehNode;
			if (left.root != NULL_NODE) {
				left.root.parent = concatNode;
			}
			concatNode.left = left.root;
			// update rank
			concatNode.rank = concatNode.left.size();
			// update parent's rank if in left subtree
			Node currentNode = concatNode;
			while (currentNode.parent != null) {
				if (currentNode.parent.left == currentNode) {
					currentNode.parent.rank = currentNode.parent.rank
							+ concatNode.rank + 1;
				} else {
					break;
				}
				currentNode = currentNode.parent;
			}
			// check and rotate
			right.checkAndRotateConcat(samehNode, Code.LEFT);
			// update balance
			if (concatNode.left.height() > concatNode.right.height()) {
				concatNode.balance = Code.LEFT;
			} else if (concatNode.left.height() == concatNode.right.height()) {
				concatNode.balance = Code.SAME;
			} else {
				concatNode.balance = Code.RIGHT;
			}
			left.root = right.root;
		}

	}

	/**
	 * 
	 * Check and rotate helper method for concatenation
	 *
	 * @param samehNode
	 * @param direct
	 */
	private void checkAndRotateConcat(Node samehNode, Code direct) {
		Code parentDirect = Code.SAME;
		Code childDirect = Code.SAME;
		Node currentNode = samehNode;
		while (currentNode.parent != null) {
			childDirect = parentDirect;
			if (currentNode.parent.left == currentNode) {
				parentDirect = Code.LEFT;
			} else {
				parentDirect = Code.RIGHT;
			}
			if (currentNode.parent.balance == direct) {
				rotate(currentNode, parentDirect, childDirect);
				if (currentNode.parent == null) {
					break;
				}
			} else if (currentNode.parent.balance == Code.SAME) {
				if (currentNode != samehNode) {
					// do not update if its parent is concatNode, whose balance
					// has been updated
					currentNode.parent.balance = direct;
				}
			} else {
				currentNode.parent.balance = Code.SAME;
				break;
			}
			currentNode = currentNode.parent;
		}
	}

	/**
	 * 
	 * Helper method for concatenation.
	 * Finds a node in the taller tree of the same height as the shorter tree.
	 *
	 * @param root	root of taller tree
	 * @param ht	height of first tree
	 * @param hv	height of second tree
	 * @param lowerTree		symbol for which tree is the the shorter tree
	 * @return
	 */
	private Node findSameHeight(Node root, int ht, int hv, Code lowerTree) {
		Node currentNode = root;
		if (lowerTree == Code.RIGHT) {	//shorter height is hv
			while (ht > hv + 1) {
				currentNode = currentNode.right;
				if (currentNode.balance == Code.LEFT) {
					ht = ht - 2;
				} else {
					ht--;
				}
			}
		} else {	//shorter height is ht
			while (hv > ht + 1) {
				currentNode = currentNode.left;
				if (currentNode.balance == Code.RIGHT) {
					hv = hv - 2;
				} else {
					hv--;
				}
			}
		}
		return currentNode;
	}

	/**
	 * This operation must be done in time proportional to the height of this
	 * tree.
	 * 
	 * @param pos
	 *            where to split this tree
	 * @return a new tree containing all of the elements of this tree whose
	 *         positions are >= position. Their nodes are removed from this
	 *         tree.
	 * @throws IndexOutOfBoundsException
	 */
	public EditTree split(int pos) throws IndexOutOfBoundsException {
		if (pos >= this.size() || pos < 0) {
			throw new IndexOutOfBoundsException("Index out of bounds!");
		}
		if (pos == 0) {
			EditTree newTree = new EditTree(this);
			this.root = NULL_NODE;
			return newTree;
		}
		// store every node on the path to the position
		Stack<Node> stack = new Stack<Node>();
		storeNodeToStack(this.root, pos, stack);

		EditTree leftTree = new EditTree();
		EditTree rightTree = new EditTree();
		Node currentNode = stack.pop();
		leftTree.root = copyTree(currentNode.left, null);
		// leftTree.add(currentNode.element);
		rightTree.root = copyTree(currentNode.right, null);
		rightTree.add(currentNode.element, 0);
		while (!stack.isEmpty()) {
			Node childNode = currentNode;
			currentNode = stack.pop();
			if (childNode == currentNode.left) {
				EditTree tempTree = new EditTree();
				tempTree.root = copyTree(currentNode.right, null);
				if (rightTree.height() >= tempTree.height()) {
					concatTree(rightTree, new Node(currentNode.element),
							tempTree, Code.RIGHT);
				} else {
					concatTree(rightTree, new Node(currentNode.element),
							tempTree, Code.LEFT);
				}
			} else {
				EditTree tempTree = new EditTree();
				tempTree.root = copyTree(currentNode.left, null);
				if (tempTree.height() >= leftTree.height()) {
					concatTree(tempTree, new Node(currentNode.element),
							leftTree, Code.RIGHT);
				} else {
					concatTree(tempTree, new Node(currentNode.element),
							leftTree, Code.LEFT);
				}
				leftTree.root = copyTree(tempTree.root, null);
			}
		}

		this.root = copyTree(leftTree.root, null);
		return rightTree;
	}

	/**
	 * 
	 * Stores the root and all nodes on the path to the one at position pos, inclusive.
	 *
	 * @param node
	 * @param pos
	 * @param stack
	 */
	private void storeNodeToStack(Node node, int pos, Stack<Node> stack) {
		stack.push(node);
		if (node.rank == pos) {
			return;
		} else if (pos > node.rank) {
			storeNodeToStack(node.right, pos - node.rank - 1, stack);
		} else {
			storeNodeToStack(node.left, pos, stack);
		}
	}

	/**
	 * Don't worry if you can't do this one efficiently.
	 * 
	 * @param s
	 *            the string to look for
	 * @return the position in this tree of the first occurrence of s; -1 if s
	 *         does not occur
	 */
	public int find(String s) {
		return find(s, 0);
	}

	/**
	 * 
	 * @param s
	 *            the string to search for
	 * @param pos
	 *            the position in the tree to begin the search
	 * @return the position in this tree of the first occurrence of s that does
	 *         not occur before position pos; -1 if s does not occur
	 */
	public int find(String s, int pos) {
		if (s.length() == 0) {
			return 0;
		}
		String treeStr = this.toString();
		int i = 0;
		while (pos != treeStr.length()) {
			i = 0;
			while (i < s.length()) {
				if (treeStr.charAt(pos + i) != s.charAt(i)) {
					break;
				}
				i++;
			}
			if (i == s.length()) {
				return pos;
			}
			pos++;
		}
		return -1;
	}

	/**
	 * @return The root of this tree.
	 */
	public Node getRoot() {
		return this.root;
	}
}
