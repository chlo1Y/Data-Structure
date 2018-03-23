import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * 
 * Implementation of most of the Set interface operations using a Binary Search Tree
 *
 * @author Matt Boutell and <<< YOUR NAME HERE >>>.
 * @param <T>
 */

public class BinarySearchTree<T extends Comparable<T>> {
	private BinaryNode root;
	private boolean treeMod;
	private int countMod;

	// Most of you will prefer to use NULL NODES once you see how to use them.
	 private final BinaryNode NULL_NODE = new BinaryNode();

	public BinarySearchTree() { //constructed an empty tree
		root =  NULL_NODE;
		treeMod=false;
	}

	// For manual tests only
	void setRoot(BinaryNode n) {
		this.root = n;
	}
	
	public int size() {
		return this.root.size();
	}
	
	public boolean containsNonBST(T item) {
		return this.root.containsNonBST(item) ;
	}
	
	public boolean contains(T item){
		// this method is more efficient since for BST,we know elements are sorted
		// only one side of the tree need to be searched 
		return this.root.contains(item);
	}
	public ArrayList<T> toArrayList(){
		ArrayList<T> list = new ArrayList<>();
		this.root.toALHelper(list); //helper adds things to the list 
		return list;
	}

	public int height() {
		// helper gets the heright of the tree 
		return this.root.height();
	}
	public boolean isEmpty() {
		// simply checking if tree if empty
		return this.root.equals(NULL_NODE);
	}
	
	public String toString(){
		String result="";
		if(this.root.toString().endsWith(", ")){
			//helper method convert tree to string 
			//remove extra comma at the end
			result=this.root.toString().substring(0, this.root.toString().length()-2);
		}
		return "["+result+"]";
		
	}
	public Object[] toArray() {
		//toarry() uses toArrayList directly
		ArrayList<T> list = new ArrayList<T>();
		this.root.toALHelper(list);
		Object[] arr= new Object[list.size()];
		return list.toArray(arr);
	}
	
	
	public Iterator<T> inefficientIterator() {
		// Iterate over the list.
		return new InefficientIterator();
	}
	 public Iterator<T> preOrderIterator(){
		 //helper method returns a pre ordered tree
		return new PreOrderIterator();
		 
	 }
	 
	 public Iterator<T> iterator() {
			// helper method returns an in-ordered tree
		return new InOrderIterator();
	}
	 

	 public boolean insert(T i) {
		 countMod++;
		 if(i==null){
				throw new IllegalArgumentException();
		}
		 if(this.root==NULL_NODE){
			 this.root=new BinaryNode(i);
			 treeMod=true;
		 }
		this.root.insert(i);
		return this.treeMod;
			// TODO Auto-generated method stub.
			
	}
	 
	
	 
	public boolean remove(T i) {
		countMod++;
		if(i==null){
			throw new IllegalArgumentException();
		}
		if(!this.contains(i)){
			return false;
		}
		if(this.root==NULL_NODE){
		  return false;
		}
		this.root=this.root.remove(i);
		return this.treeMod;
		
			
	}
	 

	
	// Not private, since we need access for manual testing.
	class BinaryNode {
		//delete a Node from tree
		BinaryNode remove(T i){
			treeMod=false;
			if(this==i){
				treeMod=true;
				return NULL_NODE;
				
			}
			if(i.compareTo(this.data)>0){
				this.right=this.right.remove(i);
			}else if(i.compareTo(this.data)<0){
				this.left=this.left.remove(i);
			}else if(i.compareTo(this.data)==0){
				treeMod=true;
				if (this.left==NULL_NODE && this.right==NULL_NODE){
					return NULL_NODE;
				}
				else if(this.left==NULL_NODE){
					return this.right;
				}
				else if(this.right==NULL_NODE){
					return this.left;
				}else{
					BinaryNode temp=this.left.findRight();
					this.left=this.left.remove(temp.data);
					this.data=temp.data;
					return this;
					
				}
				
			}
			return this;
		}
		
		BinaryNode findRight(){
			//to help remove method
			if(this.right==NULL_NODE){
				return this;
			}
			return this.right.findRight();
		}
		
		
		@SuppressWarnings("synthetic-access")
		//insert a node to tree
		boolean insert(T i){
			BinarySearchTree.this.treeMod=false;
			if(this==NULL_NODE){
				treeMod=true;
				this.data=i;
			}
			if(i.compareTo(this.data)>0){
				if(this.right==NULL_NODE){
					this.right=new BinaryNode(i);
					treeMod=true;
					return treeMod;
				}
				return this.right.insert(i);
			}else if(i.compareTo(this.data)<0){
				if(this.left==NULL_NODE){
					this.left=new BinaryNode(i);
					treeMod=true;
					return treeMod;
				}
				return this.left.insert(i);
			}else if (i.compareTo(this.data)==0){
				treeMod=false;
			}
			return treeMod;
			
		}
		
		//convert tree to string
		public String toString(){
			if(this==NULL_NODE){
				return "";
			}
			return this.left.toString()+this.data+", "+this.right.toString();
			
		}
		
		//get tree height
		int height(){
			if(this==NULL_NODE){
				return -1;
			}
			//if one sides' height is more than the other, i go to the 
			//side with larger height
			if(this.left.height()>=this.right.height()){ 
				return 1+this.left.height();
			}
			return 1+this.right.height();
			
		}
		
		void toALHelper(ArrayList<T> list){
			if(this== NULL_NODE){
				return;
			}
			left.toALHelper(list);
			// add this node data to the arry list 
			list.add(this.data);
			right.toALHelper(list);
		}
		//get tree size
		int size(){
			if(this==NULL_NODE){
				return 0;
			}
			return 1+this.left.size()+this.right.size();
		}
		//check if tree contains certain element
		boolean contains(T item){
			if(this==NULL_NODE){
				return false;
			}
			if(this.data.equals(item)){
				return true;
			}
			if(item.compareTo(this.data)>0){
				return this.right.contains(item);
			}
			if((item.compareTo(this.data)<0)){
				return this.left.contains(item);
			}
			return true;
		}
		
		// if the tree is not BST, contain method
		boolean containsNonBST(T item){
			
			if(this==NULL_NODE){
				return false;
			}
			if(this.data.equals(item)){
				return true;
			}
			
			return this.left.containsNonBST(item) || this.right.containsNonBST(item);
			
		}
		
		
		
		
		private T data;
		private BinaryNode left;
		private BinaryNode right;

		public BinaryNode() {
			this.data = null;
			this.left = null;
			this.right = null;
		}
		
		//leaf constructor
		public BinaryNode(T element) {
			this.data = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}

		public T getData() {
			return this.data;
		}

		public BinaryNode getLeft() {
			return this.left;
		}


		public BinaryNode getRight() {
			return this.right;
		}

		// For manual testing
		public void setLeft(BinaryNode left) {
			this.left = left;
		}
		
		public void setRight(BinaryNode right) {
			this.right = right;
		}
		
	}



	
	// TODO: Implement your 3 iterator classes here, plus any other inner helper classes you'd like. 
class InefficientIterator implements Iterator<T>{
		
		private ArrayList<T> list;
		private int pos;
		
		
		public InefficientIterator(){
			list=toArrayList();
			pos=0;
			BinarySearchTree.this.countMod=0;
		}
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub.
			return pos<list.size();
		}

		@Override
		public T next() {
			if(BinarySearchTree.this.countMod!=0){
				throw new ConcurrentModificationException();
			}
			if(!hasNext()){
				throw new NoSuchElementException();
			}
			//advanced and return the next value
			pos++;
			return list.get(pos-1);
		}
		
		
		
	}
class PreOrderIterator implements Iterator<T>{
	private Stack<BinaryNode> sa;
	//used a stack which is the best for pre order
	private boolean calledNext;
	BinaryNode a;
	public PreOrderIterator(){
		sa= new Stack<BinaryNode>();
		BinarySearchTree.this.countMod=0;
		calledNext=false;
		
		if(BinarySearchTree.this.root!=BinarySearchTree.this.NULL_NODE){
			//initilize the stack with the root of the tree
			sa.push(root);
		}
		
	}
	
	@Override
	public boolean hasNext() {
		return !sa.empty();
	}

	@Override
	public T next() {
		calledNext=true;
		if(BinarySearchTree.this.countMod!=0){
			throw new ConcurrentModificationException();
		}
		if(!hasNext() ){
			throw new NoSuchElementException();
		}
		// pop the top of the stack and add its right child frist and 
		//then left child(if any)
		BinaryNode temp=sa.pop();
		a=temp;
		if(temp.right!=NULL_NODE){
			sa.push(temp.right);
		}
		if(temp.left!=NULL_NODE){
			sa.push(temp.left);
		}
		
		return temp.data;
	}
	
	@Override
	public void remove() {
		if (!hasNext() || !calledNext) {
			throw new IllegalStateException();
		}
		
		BinarySearchTree.this.remove(a.data);
		countMod++;
		this.calledNext = false;
		a.data = null;
	}
	
		 
	 }




class InOrderIterator implements Iterator<T>{
	private ArrayList<T> myN;
	//used toArrayList since it's alrady in in-order
	int count=0;
	private boolean calledNext;
	T a;
	
	public InOrderIterator(){
		calledNext=false;
		BinarySearchTree.this.countMod=0;
		myN=BinarySearchTree.this.toArrayList();
		
	}
	
	@Override
	public boolean hasNext() {
		// check if the list doesn;t contain any element and wheter current pionter
		//exceeds the list size
		return !myN.isEmpty() && count<myN.size();
	}

	@Override
	public T next() {
		calledNext=true;
		if(BinarySearchTree.this.countMod!=0){
			throw new ConcurrentModificationException();
		}
		
		if(!hasNext()){
			throw new NoSuchElementException();
		}
		//every time next() is called, get an elemnet from the list
		T temp=myN.get(count);
		a=temp;
		count++;
		return temp;
		
	}
	@Override
	public void remove() {
		if (!hasNext() || !calledNext) {
			throw new IllegalStateException();
		}
		BinarySearchTree.this.remove(a);
		countMod++;
		this.calledNext = false;
		a = null;
	}
	
	
	
}















}
