// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;

import java.util.Comparator;
import goldman.Objects;
import goldman.collection.Locator;
import goldman.collection.Visitor;
/**
 * The B+-tree is variation of a B-tree in which the internal nodes
 * are used only for navigation.
 * The leaves (from left to right) form a sorted list of
 * all elements in the ordered collection, and
 * contain a pointer from each leaf to its successor to enable fast navigation within
 * the sorted order.  The B+-tree is also designed for collections
 * so large that secondary storage is required.  It aims to minimize the number of disk
 * access required to both search for elements and find the set of elements that fall in a desired
 * range of values.  A B+-tree also supports efficient bulk loading which is important
 * for real-time applications when a large number of insertions must be performed.  If each
 * element inserted is larger than all elements in the collection, then it can be inserted
 * at the end of the leaf list and the rest of the structure can be built later.
 * A B+-tree uses more space than a B-tree since the elements held
 * in the internal nodes are duplicated in the leaves.
**/

public class BPlusTree<E> extends BTree<E> {

	public class LeafNode extends BTreeNode {
		LeafNode prev;  
		LeafNode next;

/**
 * @param ptr a reference to the leaf node to place
 * after this leaf node in the leaf chain
**/

		final void setNext(LeafNode ptr) {
			next = ptr;
			ptr.prev = this;
		}

/**
 * Splits a leaf node
 * <BR> 
 * REQUIRES: 
 *  this node is full
 * @return a reference to the newly created node
**/

		LeafNode split(){
			LeafNode left = this;
			LeafNode right = new LeafNode();    //create new right child
			move(left,t,right,0,t-1);           //move last t-1 elements to new leaf
			children[t] = (BTreeNode) FRONTIER; //set child t to FRONTIER
			E copyUp = data.get(t-1);           //median element, to copy up (don't remove)
			addToParent(copyUp, right);
			right.setNext(left.next);           //preserve SortedLeafChain
			left.setNext(right);
			return right;
		}

/**
 * It merges this node with its neighboring sibling to the right
 * @param parent the parent of the two nodes that
 * are to be merged
 * @param index the index of this node in its parent's <code>children</code>
 * array
 * <BR> 
 * REQUIRES: 
 *  this node and its right sibling
 * are minimum sized.
**/

		 void merge(BTreeNode parent, int index) {
			LeafNode rightSibling = (LeafNode) parent.child(index+1);
			parent.remove(index);
			move(rightSibling, 0, this, t-1, t-1);
			setNext(rightSibling.next);   //preserve SortedLeafChain
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
			parent.data.set(i,parent.child(i+1).data.get(0));
			super.shiftLeft(parent,i);
		}

/**
 * This method requires that the left child is not at its minimum size,
 * and that the right child is not at its maximum size
 * @param parent the parent of this node
 * @param i the index of this node within the parent
**/

		void shiftRight(BTreeNode parent, int i) {
			BTreeNode left = parent.child(i-1);
			parent.data.set(i-1,left.data.get(left.size()-1));
			super.shiftRight(parent,i);
			LeafNode leftSibling = (LeafNode) parent.child(i-1);
			parent.data.set(i-1,leftSibling.data.get(leftSibling.size()-1));	
		}

/**
 * @param i the index of the
 * element to be removed
 * <BR> 
 * REQUIRES: 
 *  this node is not minimum sized.
**/

		void extract(int i) {
			E removed = data(i);
			super.extract(i);
			if (this != root && i == size()) {  //removing the rightmost element in this node
				E pred = data(i-1);
				if (comp.compare(pred, removed) < 0)  //violation only occurs when pred different
					for (BTreeNode ptr = this; ptr != root; ptr = ptr.parent)
						if (ptr.pIndex < ptr.parent.size() &&
								comp.compare(ptr.parent.data(ptr.pIndex), removed) == 0) {
							ptr.parent.data.set(ptr.pIndex, pred);
							return;
						}
			}
		}


	}

	public BPlusTree() { 
		this(Objects.DEFAULT_COMPARATOR,2);
	}

/**
 * @param t the order
 * for the B+-tree
**/

	public BPlusTree(int t) { 
		this(Objects.DEFAULT_COMPARATOR,t);
	}

/**
 * @param comp the function used
 * to compare two elements
 * @param t the order of the B+-tree
**/

	public BPlusTree(Comparator<? super E> comp, int t) {
		super(comp,t);
		FORE = new LeafNode();
		AFT = new LeafNode();
		((LeafNode) FORE).setNext((LeafNode) AFT);
	}
	

/**
 * Creates and initializes a new empty root node
**/

	protected void createRoot(){
		root = new LeafNode();
		((LeafNode) root).setNext((LeafNode) AFT);
		((LeafNode) FORE).setNext((LeafNode) root);
	}

/**
 * @return the
 * leftmost node in the B+-tree
**/

	TreeNode leftmost() {
		return ((LeafNode) FORE).next;
	}

/**
 * @return the
 * rightmost node in the B+-tree
**/

	TreeNode rightmost() {
		return ((LeafNode) AFT).prev;
	}

/**
 * If there is no equivalent element the collection,
 * then <code>find</code> returns the frontier node where the target would be inserted with the
 * <code>parent</code> field set to the node that preceded it on the search path.
 * This method sets the global instance variable <code>curIndex</code> to hold the
 * position for an occurrence of the target, if any, or otherwise the position where
 * it would be inserted in the node
 * @param target the target element
 * @return a reference to the leaf node where
 * the search ends (which would be <code>FRONTIER</code> when there is no
 * equivalent element in the collection
**/

	protected BTreeNode find(E target) {
		BTreeNode ptr = (BTreeNode) root;	     //start at the root
		while (!ptr.isFrontier()) {				 //until a frontier node is reached
			curIndex = ptr.data.find(target);	 //look for the target in the node 
			if (curIndex < ptr.size() && ptr.isLeaf() &&
						comp.compare(target, ptr.data.get(curIndex)) == 0)
				return ptr;					      //return the ptr to the current node
			((BTreeNode) FRONTIER).parent = ptr;  //set frontiers' parent to ptr
			ptr = ptr.child(curIndex);            //go to the appropriate child
		}
		return ptr;	//not found, return ptr to frontier node with parent set
	}

/**
 * It sets the global variable <code>curIndex</code> to hold the insert
 * position for the target in the returned node.
 * @param target the target element
 * @return a reference to the B-tree node where the target would
 * be inserted to precede any equivalent elements in the iteration order
**/

	BTreeNode findFirstInsertPosition(E target) {
		BTreeNode ptr = (BTreeNode) root;			//start at the root
		while (!ptr.isFrontier()) {				    //until a frontier node is reached
			curIndex = ptr.data.findFirstInsertPosition(0,ptr.size()-1,target);
			if (curIndex < ptr.size() && ptr.isLeaf() &&
					comp.compare(target, ptr.data.get(curIndex)) == 0)
				return ptr;
			((BTreeNode) FRONTIER).parent = ptr;    //set frontiers' parent to ptr
			ptr = ptr.child(curIndex);              //go to the appropriate child
	    	}
		return ptr.parent;  //not found, return ptr to frontier node with parent set
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
		while (!ptr.isFrontier()) {				    //until a frontier node is reached
			curIndex = ptr.data.findLastInsertPosition(0,ptr.size()-1,target);
			if (curIndex < ptr.size() && ptr.isLeaf() &&
					comp.compare(target, ptr.data.get(curIndex)) == 0)
				return ptr;
			((BTreeNode) FRONTIER).parent = ptr;    //set frontiers' parent to ptr
			ptr = ptr.child(curIndex);				//go to the appropriate child
	    	}
		return ptr.parent;  //not found, return ptr to frontier node with parent set
	}

/**
 * This method  sets the global variable <code>curIndex</code> to hold the
 * index of the predecessor of the target element, if it exists.
 * @param x a reference to a leaf node holding the
 * target element
 * @param index the index of the target element
 * @return a reference to the leaf node holding the predecessor of the target element,
 * or FORE if it has no predecessor
**/

	TreeNode pred(BTreeNode x, int index) {
		if (index > 0) 
			curIndex = index-1; 
		else {
			x = ((LeafNode) x).prev;
			curIndex = x.size()-1;
		}
		return x;
	}

/**
 * In
 * addition, this method has the side effect of setting <code>curIndex</code> to
 * the index of the successor in the BTreeNode returned.
 * @param x the BTreeNode holding the element for
 * which the successor in the iteration order is to be found
 * @param index the index of the element in <code>x</code>
 * @return a reference to the BTreeNode holding the successor or AFT if
 * there is no successor
**/

	TreeNode succ(BTreeNode x, int index){
		if (index < x.size()-1) 
			curIndex = index+1; 
		else {
			x = ((LeafNode) x).next;
			curIndex = 0;
		}
		return x;
	}

/**
 * Traverses the collection applying v to each element
 * @param v a visitor
**/

	protected void traverseForVisitor(Visitor<? super E> v) throws Exception {
		for (E e : this)
			v.visit(e);
	}

/**
 * @param sb the string builder
 * to fill with a comma-separated string of the elements in the collection in sorted order
**/

	protected void writeElements(StringBuilder sb) {
		Locator<E> loc = iterator();
		while (loc.advance()) {
			sb.append(loc.get());
			if (loc.hasNext())
				sb.append(", ");
		}
	}

/**
 * The node <code>x</code> is guaranteed to be a leaf.
 * @param x the tree node holding
 * the element to remove
 * @param index the index of the element to remove
**/

	void remove(BTreeNode x, int index) {
		super.remove(x, index);
		if (isEmpty())   //remove empty node that was the root
			 ((LeafNode) FORE).setNext((LeafNode) AFT);
	}

/**
 * Removes all elements from the collection
**/

	public void clear(){
		super.clear();
		 ((LeafNode) FORE).setNext((LeafNode) AFT);
	}


}
