import java.util.ArrayList;

public class BinaryHeap<T extends Comparable<? super T>> {
	
	
	private int currentIndex;
	private ArrayList<T> myArray;
	
	public BinaryHeap(){
		// since a null node will be added under any cases, 
		// therefore the starting index is 1 
		this.myArray= new ArrayList<T>();
		this.myArray.add(0, null);
		this.currentIndex=1;
		
	}
	
	

	public T deleteMin() {
		// if only null exist as a head, deletion will do nothing
		if(this.myArray.size()==1){
			return null;
		}
		// index 1 always has the smallest number 
		T temp= this.myArray.get(1);
		// get the last element's index 
		this.currentIndex=this.myArray.size()-1;
		//copy the last element into the index 1, and remove the orginal copy
		// the old smallest number was replaced by last element's copy 
		this.myArray.set(1, this.myArray.get(currentIndex));
		this.myArray.remove(this.currentIndex);
		// in case for index out of bounds
		if(this.myArray.size()>1){
			percolateDown(1);
		}
		return temp;
		
		
		
	}
	public void percolateDown(int i){
		// followed slides 
		T temp= this.myArray.get(i);
		this.currentIndex=this.myArray.size()-1;
		// while the children of current node's index do not exceeds the size of the arrayList
		while(i*2<this.currentIndex){
			int childIndex= i*2;
			// compare two children's value,
			// and get the index of the larger child
			if(childIndex!=this.currentIndex && 
					this.myArray.get(childIndex+1).compareTo(this.myArray.get(childIndex))<0){
				childIndex++;
			}
			// compare the larger child with the parent
			// and swap if child's value is greater than parent
			if(this.myArray.get(childIndex).compareTo(temp)<0){
				this.myArray.set(i, this.myArray.get(childIndex));
				this.myArray.set(childIndex, temp);
				i = childIndex;
			}
			else{
				break;
			}
			
			
		}	
		
	}
	
	// insert in ascending order 
	public void insert(T i) {
		// add the element to the last index 
		this.myArray.add(i);
		this.currentIndex=this.myArray.size()-1;
		// compare parent's value with child's value and swap 
		int parentindex=this.currentIndex/2;
		while(parentindex>0 && i.compareTo(this.myArray.get(parentindex))<0){
			T temp=this.myArray.get(parentindex);
			this.myArray.set(currentIndex, temp);
			this.myArray.set(parentindex, i);
			parentindex=parentindex/2;
			this.currentIndex=this.currentIndex/2;	
		}
	}
	
	//insert in descending order 
	public void insert2(T i) {
		//same as above 
		this.myArray.add(i);
		this.currentIndex=this.myArray.size()-1;
		int parentindex=this.currentIndex/2;
		while(parentindex>0 && i.compareTo(this.myArray.get(parentindex))>0){
			T temp=this.myArray.get(parentindex);
			this.myArray.set(currentIndex, temp);
			this.myArray.set(parentindex, i);
			parentindex=parentindex/2;
			this.currentIndex=this.currentIndex/2;	
		}
	}
	
	

	public void sort(T[] csLegends) {
		// insert elemnets in descending order 
		for(int i=0; i<csLegends.length; i++){
			this.insert2(csLegends[i]);
		}
		// update 
		this.buildHeap();
		for (int j = 0; j < csLegends.length; j++) {
			csLegends[j] = (T) this.deleteMin();
		}
		
		
	}
		
	
	public void buildHeap(){
		this.currentIndex=this.myArray.size()-1;
		for(int i=this.currentIndex/2; i>0; i--){	
			percolateDown(i);
		}
	}
	
	
	
	
	
	@Override
	public String toString() {
		String str = "[";
		for (int i = 0; i < this.myArray.size(); i++) {
			str += "" + this.myArray.get(i) + ", ";
		}
		str = str.substring(0, str.length() - 2) + "]";
		return str;
	}

}
