// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import goldman.Objects;
import goldman.collection.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
/**
 * A B-tree is a balanced binary search tree in which each
 * node can hold between t-1 and 2t-1 elements, where integer t &gt; 1
 * is provided as a parameter to the constructor.   Our implementation of the B-tree
 * uses bottom-up insertion and deletion.
 * The B-tree is designed for collections that are
 * so large that secondary storage must be used.  The larger node
 * size (chosen with knowledge of the page size) helps minimize the number of
 * disk pages that must be read to locate an element.
**/

public class BTree<E> extends AbstractSearchTree<E> implements OrderedCollection<E> {

	int t = 2;  //min branching factor


	final TreeNode FRONTIER = new BTreeNode();


	int curIndex;  //index of last element located by find


	TreeNode FORE = new BTreeNode();
	TreeNode AFT = new BTreeNode();


	class BTreeNode extends TreeNode {

		SortedArray<E> data;  //holds elements held in the node
		BTreeNode[] children;
		BTreeNode parent;


		int pIndex; // parent index most recently used to access this node


		BTreeNode() {
			children = (BTreeNode[]) new BTree.BTreeNode[2*t];  
			data = new SortedArray<E>(comp, 2*t-1);
		}	


		public String toString(){
			return "" + data;
		}
/**
 * @return true  if and only if this node is a frontier node
**/

		protected boolean isFrontier(){
			return this == FRONTIER;
		}

/**
 * @return true  if and only if this node is a leaf node.
**/

		final boolean isLeaf(){
			return child(0) == FRONTIER;
		}

/**
 * @return the number of elements held in this node
**/

		protected int size() {
			return data.getSize();
		}

/**
 * @return the maximum number of elements
 * that can be held in a node.
**/

		protected int capacity(){
			return 2*t-1;
		}

/**
 * @return true if and only if this node is full.
**/

		final boolean atMaxSize(){
			return (size() == 2*t-1);
		}

/**
 * @return true if and only if this node is at
 * the minimum size of a non-root node.
**/

		final boolean atMinSize(){
			return (size() == t-1);
		}

/**
 * @return the parent of this node
**/

		protected BTreeNode parent() {
			return parent;
		}

/**
 * @param i the index of
 * the desired element
 * @return the element with the given
 * index
 * @throws PositionOutOfBoundsException i is not a valid index
**/

		protected E data(int i) {
			if (this == AFT || this == FORE)
				throw new NoSuchElementException();
			return data.get(i);
		}

/**
 * @param i the index for the
 * desired child
 * @return a reference to the i<sup>th</sup> child
 * of this node
 * @throws ArrayOutofBoundsException i is not between 0 and
 * 2t-1
**/

		protected BTreeNode child(int i) {
			children[i].pIndex = i;
			return children[i];
		}

/**
 * @param i the index for the
 * new child
 * @param child a reference to the node to make
 * <code>child[index]</code>
 * <BR> 
 * REQUIRES: 
 *  the given child
 * is not already a child of another node
**/

		final void setChild(int i, BTreeNode child) {
			children[i] = child;
			child.parent = this;
		}

/**
 * The node on which this method is called, becomes the left
 * child of <code>element</code>.  This method creates a new
 * root, when it is called on the root
 * @param element the element to
 * add to the parent of this node
 * @param right the reference to
 * a B-tree node to add as the right child of the new element
**/

		protected void addToParent(E element, BTreeNode right){
			if (this == root){  //splitting the root
				BTreeNode newRoot = new BTreeNode(); //create a new root
				newRoot.setChild(0,this); 		   //leftmost child is this
				newRoot.addElement(0,element,right);        //add element and right child
				root = newRoot;					   //reset root
			} else {			    //otherwise (not splitting root)
				parent.addElement(pIndex,element,right);	    //add element and right child
			}
		}

/**
 * Splits this B-tree node by moving
 * the median element to the parent
 * <BR> 
 * REQUIRES: 
 *  the node on which it is called is a full
 * node
 * @return a new B-tree node
 * that is created to hold the elements right of the median
**/

		BTreeNode split(){
			BTreeNode left = this;
			BTreeNode right = new BTreeNode(); //will hold right half
			move(left,t,right,0,t-1);  //move last t-1 elements to new node
			E moveUp = left.data.remove(t-1); //the median element to move up to parent
			addToParent(moveUp, right); //adjust elements and children of parent
			return right;			
		}

/**
 * It merges this node with its neighboring sibling to the right
 * This method requires that this node, and
 * its neighboring sibling to the right are both minimum-sized.
 * @param parent the parent of the two nodes that
 * are to be merged
 * @param index the index of the child reference for this node
**/

		void merge(BTreeNode parent, int index) {
			BTreeNode rightSibling = parent.child(index+1);
			E moveDown = (E) parent.data.get(index); //element from parent to move down
			parent.remove(index);                    //remove from parent
			data.add(t-1,moveDown);	                 //add to this node
			move(rightSibling, 0, this, t, t-1);     //move over elements in right sibling
			if (root.size() == 0){
				root = root.child(0);               //preserve InOrder and Reachable
				((BTreeNode) root).parent = null;   //preserve Parent
			}
		}

/**
 * @param parent the parent of this
 * node
 * @param i the index of this
 * node in its parent's <code>children</code> array
 * <BR> 
 * REQUIRES: 
 *  the right sibling of this node is not minimum sized,
 * and that this node is not full
**/

		void shiftLeft(BTreeNode parent, int i) {
			BTreeNode left = this;
			BTreeNode right = parent.child(i+1);  
			E moveDown = (E) parent.data.get(i);  //element to move down to left
			left.data.add(size(),moveDown);		//add moveDown to end of left
			E moveUp = (E) right.data.remove(0);  //element to move up to parent
			parent.data.set(i,moveUp);	 		//move into parent
			left.setChild(size(),(BTreeNode) right.leftmostChild());  //move subtree and
			System.arraycopy(right.children,1,right.children,0,right.size()+1); //right's children
			right.children[right.size()+1] = null;  //reset child in right not in use to null
		}

/**
 * @param parent the
 * parent of this node
 * @param i the index of
 * this node in its parent's <code>children</code> array
 * <BR> 
 * REQUIRES: 
 *  this node is not full, and that
 * its left sibling is not minimum sized
**/

		void shiftRight(BTreeNode parent, int i) {
			BTreeNode left = parent.child(i-1);
			BTreeNode right = this;
			BTreeNode moveToRight = (BTreeNode) left.rightmostChild();
			E moveDown = (E) parent.data.get(i-1);          //element to move down to right
			right.data.add(0,moveDown);                     //add moveDown to end of left
			E moveUp = (E) left.data.remove(left.size()-1); //element to move up to parent
			parent.data.set(i-1,moveUp);                    //move into parent
			System.arraycopy(right.children,0,right.children,1,right.size()+1); //right's children
			right.setChild(0,moveToRight); //move subtree from left to right
			left.children[left.size()+1] = null;    //reset child in left not in use to null
		}

/**
 * @param i the index
 * where the new element is to be added
 * @param element the element to add
 * @param rightChild the right child for the new element
**/

		BTreeNode addElement(int i, E element, BTreeNode rightChild){
			BTreeNode x = this; //pointer to node where element will be added
			if (atMaxSize()) { //if node is full split
				BTreeNode newNode = split();
				if (i >= t) { //if it was on the right side
					i -= t;       //adjust index and
					x = newNode;  //the node to add to
				}
			}
			if (x.size()-i > 0)  //need to make room for rightChild
				System.arraycopy(x.children,i+1,x.children,i+2,x.size()-i);
			x.data.add(i, element);  //add new element
			x.setChild(i+1,rightChild); //add new child
			return x;   //return the node where the new element was placed
		}

/**
 * Removes it from this node
 * @param i the index of the
 * element to be removed
 * <BR> 
 * REQUIRES: 
 *  this node is not minimum sized.
**/

		void extract(int i) {
			data.remove(i);
		}

/**
 * @param i the index of the
 * element to remove
**/

		void remove(int i) { 
			BTreeNode x = this;
			if (x.atMinSize() && x != root) { //can't remove prior to restructing
				BTreeNode parent = x.parent();
				if (pIndex > 0 && !parent.child(pIndex-1).atMinSize()){ //Case 1
					shiftRight(parent,pIndex);
					i++;  //element to be removed shifted right by one
				} else if (pIndex < parent.size() && 
						!parent.child(pIndex+1).atMinSize()) { //Case 2
					shiftLeft(parent,pIndex);
				} else {
					if (pIndex != parent.size()) //Case 3a
						x.merge(parent,pIndex);
					else {     				   //Case 3b
						x = parent.child(pIndex-1);        //move x to left sibling
						x.merge(parent, pIndex-1);
						int offsetFromEnd = t-1-i;
						i = x.size() - offsetFromEnd; //update to index in joined node
					}
				}
			}
			x.extract(i);
			if (x.size() - i > 0) //shift children right of index back by one
				System.arraycopy(x.children,i+2,x.children,i+1,x.size()-i);
			x.children[x.size()+1] = null; //reset child not in use to null
		}


	}

	public BTree() { 
		this(Objects.DEFAULT_COMPARATOR,2);
	}

/**
 * @param t the order
 * for the B-tree
**/

	public BTree(int t) { 
		this(Objects.DEFAULT_COMPARATOR,t);
	}

/**
 * @param comp the function used
 * to compare two elements
 * @param t the order of the B-tree
**/

	public BTree(Comparator<? super E> comp, int t) {
		super(comp);
		this.t = t;
		root = FRONTIER;
	}

/**
 * Creates and initializes a new empty root node
**/

	protected void createRoot() {
		root = new BTreeNode();
	}

/**
 * This method
 * sets the global variable <code>curIndex</code> to hold the
 * index for an occurrence of the target (if it is in the collection),
 * or otherwise the insert position
 * for the target in the last non-frontier node on the search path
 * @param target the value to search for
 * @return a reference to a B-tree node that contains an
 * occurrence of the target when there is an equivalent element in the
 * collection.  Otherwise it returns the frontier node after setting its
 * <code>parent</code> field set to predecessor of the target on the search path.
**/

	protected BTreeNode find(E target) {
		BTreeNode ptr = (BTreeNode) root;       //start at the root
		while (!ptr.isFrontier()) {				//until a frontier node is reached
			curIndex = ptr.data.find(target);	   //find the element in the node
			if (curIndex < ptr.size() && comp.compare(target, ptr.data.get(curIndex)) == 0)
				return ptr;						   //return the ptr to the current node
			((BTreeNode) FRONTIER).parent = ptr;   //set frontiers' parent to ptr
			ptr = ptr.child(curIndex);			   //go to the appropriate child		   
		}
		return ptr;	//not found, return ptr to frontier node with parent set
	}

/**
 * <BR> 
 * REQUIRES: 
 *  it is called only after a successful search
 * @return the index, within its
 * tree node, of the element returned in the most recent search.
**/

	protected int getLastNodeSearchIndex() {
		return curIndex;
	}

/**
 * It sets the global variable <code>curIndex</code> to the target's insert
 * position within the returned node.
 * @param target the target element
 * @return a reference to the B-tree node where the target would
 * be inserted to precede any equivalent elements in the iteration order
**/

	BTreeNode findFirstInsertPosition(E target) {
		BTreeNode ptr = (BTreeNode) root;			//start at the root
		BTreeNode returnNode = null;
		int returnIndex = 0;
		while (!ptr.isFrontier()) {				   //until a frontier node is reached
			curIndex = ptr.data.findFirstInsertPosition(0,ptr.size()-1,target);	
			if (curIndex < ptr.size() && comp.compare(target, ptr.data.get(curIndex)) == 0) {
				returnNode = ptr;                       //remember node and
				returnIndex = curIndex;                 //index where found
			}
			((BTreeNode) FRONTIER).parent = ptr; //set frontier's parent to ptr
			ptr = ptr.child(curIndex);						   
		}
		if (returnNode == null)  //no equivalent element found
			return ptr.parent; 
		curIndex = returnIndex;  //use saved values for index and node
		return returnNode;	
	}

/**
 * It sets the global variable <code>curIndex</code> to hold the insert
 * position for the target in the returned node.
 * @param target the target element
 * @return a reference to the B-tree node where the target would
 * be inserted to follow any equivalent elements in the iteration order
**/

	BTreeNode findLastInsertPosition(E target) {
		BTreeNode ptr = (BTreeNode) root;			//start at the root
		BTreeNode returnNode = null;
		int returnIndex = 0;
		while (!ptr.isFrontier()) {				 //until a frontier node is reached
			curIndex = ptr.data.findLastInsertPosition(0,ptr.size()-1,target);	
			((BTreeNode) FRONTIER).parent = ptr; //set frontiers' parent to ptr
			if (curIndex < ptr.size() && comp.compare(target, ptr.data.get(curIndex)) == 0) {
				returnNode = ptr;
				returnIndex = curIndex+1;
				ptr = ptr.child(returnIndex);
			}
			else
				ptr = ptr.child(curIndex);				   
		}
		if (returnNode == null)  //no equivalent element found
			return ptr.parent;           
		else {
			curIndex = returnIndex;  //use saved values for index and node
			return returnNode;	
		}
	}

/**
 * This method sets the
 * global variable <code>curIndex</code> to hold the
 * index of the predecessor of the target element, if it exists.
 * @param x a reference to a B-tree node holding the
 * target element
 * @param index the index of the target element within the parent
 * @return a reference to the B-tree node holding the predecessor of the target element,
 * or FORE if it has no predecessor
**/

	TreeNode pred(BTreeNode x, int index) {
		BTreeNode ptr = x;
		if (!x.isLeaf()){ 					//Case 1
			ptr = (BTreeNode) rightmost(x.child(index));
			curIndex = ptr.size()-1;  
		} else if (index > 0) { 		    //Case 2a
				curIndex = index-1; 
		} else if (x == root){ 				//Case 2b
			return FORE; 
		} else {               				//Case 2c
			BTreeNode parent = (BTreeNode) x.parent();
			while (parent != root && parent.child(0) == ptr) { //move up tree as long
				ptr = parent;							//as ptr is a leftmost
				parent = (BTreeNode) ptr.parent(); 		//child of parent
			}			
			curIndex = ptr.pIndex;  //reset global var curIndex
			ptr = parent; 
			if (ptr == root && curIndex == 0) 
				return FORE;
			else
				curIndex--;
		}
		return ptr;
	}

/**
 * This method does not require that <code>target</code>
 * be in the collection.
 * @param target the element for which to find
 * the predecessor
 * @return the largest
 * element in the ordered collection that is less than <code>target</code>.
 * @throws NoSuchElementException no element in the collection
 * is less than <code>target</code>
**/

	public E predecessor(E target) { 
		BTreeNode x = findFirstInsertPosition(target); //curIndex is set
		if (x.isLeaf() && curIndex > 0)            //if insert position not left of first element 
			return x.data(curIndex-1);
		return pred(x,curIndex).data(curIndex); //curIndex is updated by pred
	}

/**
 * This method sets the global variable <code>curIndex</code> to hold the
 * index of the predecessor of the target element, if it exists.
 * @param x a reference to a B-tree node holding the
 * target element
 * @param index the index of the target element
 * @return a reference to the B-tree node holding the successor of the target element,
 * or AFT if it has no successor
**/

	TreeNode succ(BTreeNode x, int index){
		BTreeNode ptr = x;
		if (!x.isLeaf()){  				//Case 1
			ptr = (BTreeNode) leftmost(x.child(index+1));
			curIndex = 0;
		} else if (index < x.size() - 1){ //Case 2a
				curIndex = index+1;
		} else if (x == root){ 			//Case 2b
			return AFT;
		} else {  						//Case 2c
			BTreeNode parent = ptr.parent();
			while (parent != root && parent.child(parent.size()) == ptr) { //move up tree as long
				ptr = ptr.parent();								 //as ptr is a rightmost
				parent = ptr.parent(); 						     //child of parent
			}
			curIndex= ptr.pIndex; //reset global variable curIndex
			ptr = parent;							
			if (ptr == root && curIndex == ptr.size()) 
				return AFT;
		}
		return ptr;
	}	

/**
 * This method does not require that <code>target</code>
 * be in the collection.
 * @param target the element for which to find
 * the successor
 * @return the least
 * element in the ordered collection that is greater than <code>target</code>
 * @throws NoSuchElementException no element in the collection
 * is greater than <code>target</code>
**/

	public E successor(E target) {
		BTreeNode x = findLastInsertPosition(target);  //curIndex is set
		if (curIndex < x.size())    //if insert position not after last element
			return x.data(curIndex);      
		return succ(x,curIndex).data(curIndex);  //curIndex is updated by succ
	}

/**
 * @param from a reference to
 * the B-tree node from which elements
 * are to be moved
 * @param fromIndex the index of the first element to be moved
 * @param to a reference to the B-tree node where the elements are to be moved
 * @param toIndex the destination index for the first element
 * @param num the number of elements (and their surrounding children) to be moved.
**/

	void move(BTreeNode from, int fromIndex, BTreeNode to, int toIndex, int num){
		for (int i = 0; i < num; i++){
			to.data.add(toIndex+i,from.data.remove(fromIndex));			  
			to.setChild(toIndex+i,from.child(fromIndex+i));
		}
		to.setChild(toIndex+num,from.child(fromIndex+num)); //move last child
		Arrays.fill(from.children, fromIndex, fromIndex+num+1, null);
	}

/**
 * @param element the new element
 * @return a reference to the newly added B-tree node
**/

	protected TreeNode insert(E element){
		if (isEmpty()) {
			createRoot();
			((BTreeNode) root).data.add(0, element);
			((BTreeNode) root).children[0] = (BTreeNode) FRONTIER;
			((BTreeNode) root).children[1] = (BTreeNode) FRONTIER;
			return root;
		} else {
			BTreeNode ptr = (BTreeNode) root;			//start search at the root
			while (!ptr.isLeaf()) {				//until a frontier node is reached
				curIndex = ptr.data.findLastInsertPosition(0,ptr.size()-1,element);
				ptr = ptr.child(curIndex);						   
			}
			curIndex = ptr.data.findLastInsertPosition(0,ptr.size()-1,element);
			version.increment();   //invalidate all markers for iteration
			return ptr.addElement(curIndex,element,(BTreeNode) FRONTIER);
		}
	}

/**
 * @param x a reference
 * to the B-tree node holding the element to remove
 * @param index the index in x of the element to remove
**/

	void remove(BTreeNode x, int index) {
		if (x.isLeaf()) {    //Case 1
			x.remove(index);
			size--;
		} else {             //Case 2
				BTreeNode pred = (BTreeNode) rightmost(x.child(index));
				x.data.set(index,pred.data(pred.size()-1));
				remove(pred,pred.size()-1);
		}
	}

/**
 * Removes the element at <code>curIndex</code>
 * of x
 * @param x a reference to
 * an existing tree node
**/

	protected void remove(TreeNode x) {
		remove((BTreeNode) x,curIndex); //curIndex was set by find in public remove
		version.increment();            //invalidate all markers for iteration
	}

/**
 * Removes all elements from the collection
**/

	public void clear(){
		root = FRONTIER;
		size = 0;
		version.increment();   //invalidate all markers for iteration
	}

/**
 * Creates a new tracker that is at FORE.
**/

	public Locator<E> iterator() {
		return new Marker((BTreeNode) FORE, -1);
	}

/**
 * Creates a new tracker that is at AFT.
**/

	public Locator<E> iteratorAtEnd() {
		return new Marker((BTreeNode) AFT, -1);
	}

/**
 * @param x the element to track
 * @return a locator initialized at x.
 * @throws NoSuchElementException there is no element equivalent to
 * x in the collection.
**/

	public Locator<E> getLocator(E x) {
		BTreeNode t = find(x);
		if (t == FRONTIER)
			throw new NoSuchElementException();
		return new Marker(t,curIndex);
	}


	protected class Marker extends AbstractCollection<E>.AbstractLocator<E> {

		BTreeNode node;
		int index;

/**
 * @param x a reference
 * the node to track
 * @param index the index of the element to track in x
**/

		Marker(BTreeNode x, int index) {
			node = x;
			this.index = index;
		}

/**
 * @return true  if and only if the tracked element is
 * currently in the collection.
**/

		public boolean inCollection() {
			checkValidity();
			return (node != FORE && node != AFT);
		}

/**
 * @return the marked element
 * @throws NoSuchElementException marker is not at an element in
 * the collection.
**/

		public E get() {
			checkValidity();
			return node.data(index);
		}

/**
 * Moves the tracker to the next element in the iteration order,
 * or {\texttt AFT} if the last element is currently tracked.
 * @return true  if and only if after the update, the tracker is at
 * an element of the collection.
 * @throws AtBoundaryException the tracker is at AFT since there is
 * no place to advance.
**/

		public boolean advance() {
			checkValidity();
			if (node == AFT)                    
				throw new AtBoundaryException();
			if (node == FORE && isEmpty())
				node = (BTreeNode) AFT;
			else if (node == FORE)	{				//when the marker is at FORE	  
				node = (BTreeNode) leftmost();	//first element in iteration order
				index = 0;                      //is element 0 of the leftmost node
			} else {
				node = (BTreeNode) succ(node, index);  //move to successor
				index = curIndex;
			}
			return node != AFT;				     //still within collection unless AFT reached.
		}

/**
 * Moves the tracker to the previous element in the iteration order, or FORE if the
 * first element is currently tracked.
 * @return true  if and only if after the update, the tracker is at an element of the collection.
 * @throws AtBoundaryException the tracker is at FORE since then there is
 * no place to retreat.
**/

		public boolean retreat() {
			checkValidity();
			if (node == FORE)
				throw new AtBoundaryException();
			if (node == AFT && isEmpty())
				node = (BTreeNode) FORE;
			else if (node == AFT)	{				//otherwise, if tracker is at AFT
				node = (BTreeNode) rightmost();		 // go to last element
				index = node.size()-1;
			} else {									//otherwise, move to the predecessor
				node = (BTreeNode) pred(node,index);
				index = curIndex;
			}
			return node != FORE;					//still within collection unless FORE reached
		}

/**
 * @return true  if there is some element after the current locator position.
**/

		public boolean hasNext() {
			checkValidity();
			if (node == FORE)
				return !isEmpty();
			if (node == AFT)
				return false;
			return succ(node,index) != AFT;
		}


		public void remove() {
			throw new UnsupportedOperationException();
		}


	}
}
