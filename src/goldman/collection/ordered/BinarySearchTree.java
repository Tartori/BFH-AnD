// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import goldman.Objects;
import goldman.collection.*;
import java.util.Comparator;
import java.util.NoSuchElementException;
/**
 * This class implements a standard binary search tree.
 * When elements are inserted into a binary search tree
 * in a random order, then it has the desirable property of having logarithmic
 * height, where the height is the maximum length of a path from the
 * root to a leaf.  The height is important since the cost of most methods is
 * proportional to the height of the tree.  In the best case, a binary
 * search tree has height log<sub>2</sub> (n+1).
 * If the elements are inserted in a random order then the expected height
 * is roughly 1.386 log<sub>2</sub> n.
 * However, the
 * binary search tree can degenerate to a sorted list (which has a height of n).
 * To avoid this situation, most implementations that build upon
 * a binary search tree (red-black tree, B-Tree, B+-Tree)
 * are balanced, meaning that they include
 * methods that reorganize the tree so that the worst-case height is logarithmic.
**/

public class BinarySearchTree<E> extends AbstractSearchTree<E>
	implements OrderedCollection<E>, Tracked<E> {

	protected final BSTNode FRONTIER_L = createFrontierNode();
	protected final BSTNode FRONTIER_R = createFrontierNode();


	final BSTNode FORE = createTreeNode(null);
	final BSTNode AFT = createTreeNode(null);


	protected class BSTNode extends TreeNode {

		protected E data;                     //the data element
		protected BSTNode parent;             //reference to its parent (null for root)
		protected BSTNode left = FRONTIER_L;  //reference to its left child
		protected BSTNode right = FRONTIER_R; //reference to its right child

/**
 * @param data the element to be held in this node
**/

		protected BSTNode(E data) {
			this.data = data;
		}

/**
 * @return the number of elements held in this node
**/

		final protected int size() {
			return 1;
		}

/**
 * @return the maximum number of elements this
 * node can accommodate.
**/

		final protected int capacity() {
			return 1;
		}


		public String toString() {
			return "" + data;
		}
/**
 * @return true  if and only if this node is a frontier node
**/

		final protected boolean isFrontier() {
			return this == FRONTIER_L || this == FRONTIER_R;
		}

/**
 * Marks this node as no longer in use
**/

		final void markDeleted() {
			left = null;
		}

/**
 * @return true  if and only if
 * this node is not in use
**/

		final boolean isDeleted() {
			return left == null;
		}

/**
 * @param index the desired index
 * @return e_{<code>index</code>}
 * @throws IllegalArgumentException <code>index</code> is not 0 since a binary search
 * tree only holds a single element in each node
 * @throws NoSuchElementException this node is not in use
**/

		final protected E data(int index) {
			if (index != 0)
				throw new IllegalArgumentException();
			if (isDeleted())
				throw new NoSuchElementException();
			return data;
		}
		

/**
 * @param index the index for the desired child
 * @return C_{<code>index</code>}
 * @throws IllegalArgumentException index is not 0 or 1
**/

		final protected BSTNode child(int index) {
			if (index == 0)
				return left;
			else if (index == 1)
				return right;
			else
				throw new IllegalArgumentException();		
		}

/**
 * @param x a reference to the node that is to
 * become the left child of this node
 * @return the possibly updated value of <code>x</code>
**/

		final protected BSTNode setLeft(BSTNode x) {
			if (x == FRONTIER_R)  //update x if it was FRONTIER_R
				x = FRONTIER_L;
			left = x;
			x.parent = this;
			return x;
		}

/**
 * @param x a reference to the
 * node that is to become the right child of this node
 * @return the possibly updated value of <code>x</code>
**/

		final protected BSTNode setRight(BSTNode x) {
			if (x == FRONTIER_L)  //update x if it was FRONTIER_L
				x = FRONTIER_R;
			right = x;
			x.parent = this;
			return x;
		}

/**
 * <BR> 
 * REQUIRES: 
 *  it is not called on the root
 * @return true  if and only if this node is a left child.
**/

		final protected boolean isLeftChild() {
			return parent.left == this;
		}

/**
 * <BR> 
 * REQUIRES: 
 *  it is not called on the root
 * @return the grandparent
 * of the node
**/

		final protected BSTNode grandparent() {
			return parent.parent;
		}

/**
 * <BR> 
 * REQUIRES: 
 *  it is not called on the root
 * @return the sibling of this node
**/

		final protected BSTNode sibling() {
			return (isLeftChild()) ? parent.right : parent.left;
		}

/**
 * <BR> 
 * REQUIRES: 
 *  it is not called on the root.
 * @return the left child of
 * this node when it is a left child, and the right child of this node when it is a
 * right child.
**/

		final protected BSTNode sameSideChild() {
			return (isLeftChild()) ? left : right;
		}

/**
 * @param child one of the children of this node
 * @return the other child of this node
**/

		final BSTNode otherChild(BSTNode child) {
			return (left == child) ? right : left;
		}

/**
 * Replaces T(<code>this</code>) by T(<code>x</code>)
 * @param x a reference to a node
 * <BR> 
 * REQUIRES: 
 *  T(<code>left</code>) &le;
 * <code>x.element</code> &le; T(<code>right</code>)
 * @return the possibly updated value of
 * <code>x</code>
**/

		protected BSTNode replaceSubtreeBy(BSTNode x) {
			if (this == root) {	   //if called on the root of the tree
				root = x;		   //   the root changes, and
				x.parent = null;    //   x's parent must be set to null
			} else if (isLeftChild()) {       //otherwise if a left child
				x = parent.setLeft(x);       //   reset the left child
			} else {				            // and if a right child
				x = parent.setRight(x);  	   //    reset the right child
			}
			return x;
		}

/**
 * Replaces T(<code>this</code>) by T(<code>x</code>)
 * @param x a reference to a node
 * <BR> 
 * REQUIRES: 
 *  T(<code>left</code>) &le;
 * <code>x.element</code> &le; T(<code>right</code>)
 * @return the possibly updated value of
 * <code>x</code>
**/

	protected BSTNode deleteAndReplaceBy(BSTNode x) {  
			return replaceSubtreeBy(x);
		}

/**
 * Replaces the node on which this method is called by x
 * @param x a reference to a node
 * <BR> 
 * REQUIRES: 
 *  T(<code>left</code>) &le;
 * <code>x.element</code> &le; T(<code>right</code>)
**/

		protected void substituteNode(BSTNode x) {
			x = replaceSubtreeBy(x);
			x.setLeft(left);
			x.setRight(right);			
		}


	}

	public BinarySearchTree() { 
		this(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp the comparator to use to
 * order the elements
**/

	public BinarySearchTree(Comparator<? super E> comp) {
		super(comp);
		root = FRONTIER_L;
	}

/**
 * @return a reference to a newly
 * created frontier node
**/

	protected BSTNode createFrontierNode() {
		return createTreeNode(null);
	}	

/**
 * @param data the element to
 * be held
 * @return a reference to a new tree node holding <code>data</code>.
**/

	protected BSTNode createTreeNode(E data) {
		return new BSTNode(data);
	}

/**
 * @param element the target
 * @return a reference to a node holding an equivalent element, if one exists.
 * Otherwise it returns a reference to a frontier node at the insert position for
 * <code>element</code> with its parent set to the node
 * that preceded it on the search path.
**/

	protected BSTNode find(E element) {
		BSTNode ptr = (BSTNode) root;						//start at the root
		while (!ptr.isFrontier()) {						//until a frontier node is reached
			FRONTIER_L.parent = FRONTIER_R.parent = ptr;	   //set frontier's parent to ptr
			int comparison = comp.compare(element, ptr.data); //compare element to current one
			if (comparison == 0)							   //if they are equal, return
				return ptr;								   
			else if (comparison < 0)						   //if element is smaller, go left
				ptr = ptr.left;							   
			else											   //if element is larger, go right
				ptr = ptr.right;							   
		}
		return ptr;										//not found, return ptr (frontier node)
	}													

/**
 * @param element the
 * target
 * @return a reference to the frontier node that
 * is the first insert position for <code>element</code>.  The
 * <code>parent</code> field of the returned frontier node is
 * set to the node that preceded it on the search path.
**/

	BSTNode findFirstInsertPosition(E element) {
		BSTNode ptr = (BSTNode) root;                     //start at the root
		while (!ptr.isFrontier()) {						  //until a frontier node is reached
			FRONTIER_L.parent = FRONTIER_R.parent = ptr;     //set frontier's parent			   
			if (comp.compare(element, ptr.data) <= 0)		 //if element <= ptr.data, go left
				ptr = ptr.left;							   
			else											 //if element > ptr.data, go right
				ptr = ptr.right;							   
		}
		return ptr;										//return frontier node at end of search path
	}													

/**
 * @param element the
 * target
 * @return a reference to the frontier node that
 * is the last insert position for <code>element</code>.  The
 * <code>parent</code> field of the returned frontier node is
 * set to the node that preceded it on the search path.
**/

	protected BSTNode findLastInsertPosition(E element) {
		BSTNode ptr = (BSTNode) root;					  //start at the root
		while (!ptr.isFrontier()) {						  //until a frontier node is reached
			FRONTIER_L.parent = FRONTIER_R.parent = ptr;     //set frontier's parent		   
			if (comp.compare(element, ptr.data) < 0)		 //if element < ptr.data, go left
				ptr = ptr.left;							   
			else											 //if element >= ptr.data, go right
				ptr = ptr.right;							   
		}
		return ptr;										 //return frontier node at end of search path
	}													

/**
 * @param x target
 * <BR> 
 * REQUIRES: 
 *  if x is a
 * frontier node, then before the method is called
 * its parent is set to the node that precedes it in the intended search path
 *  if x is a
 * frontier node, then its parent is set to the node that precedes
 * it in x's search path
 * @return the node that holds <code>x</code>'s predecessor, or <code>FORE</code> if x has
 * no predecessor
**/

	BSTNode pred(BSTNode x) {
	if (!x.isFrontier() && !x.left.isFrontier()) //check if T exists and is non-empty
		return (BSTNode) rightmost(x.left);
	BSTNode ptr = x;
	while (ptr != root && ptr.isLeftChild()) 
		ptr = ptr.parent;
	if (ptr == root)
		return FORE;
	else
		return ptr.parent;
}

/**
 * The target need not be in the collection.
 * @param target the element to search for
 * @return the maximum element in this collection that is less than <code>target</code>
 * @throws NoSuchElementException no element in the collection
 * is less than <code>target</code>
**/

	public E predecessor(E target) { 
		BSTNode ptr = pred(findFirstInsertPosition(target));
		if (ptr == FORE)
			throw new NoSuchElementException();
		else
			return ptr.data;
	}

/**
 * @param x the target
 * <BR> 
 * REQUIRES: 
 *  if x is a
 * frontier node, then its parent is set to the node that precedes
 * it in x's search path
 * @return the node that holds <code>x</code>'s successor if one
 * exists, and <code>AFT</code> otherwise
**/

	BSTNode succ(BSTNode x) {
		if (!x.isFrontier() && !x.right.isFrontier())  //check if T exists and is non-empty
			return (BSTNode) leftmost(x.right);
		BSTNode ptr = x;
		while (ptr != root && !ptr.isLeftChild())
			ptr = ptr.parent;
		if (ptr == root)
			return AFT;
		else
			return ptr.parent;
	}

/**
 * This method does not require that <code>target</code>
 * be in the collection.
 * @param target the element to search for
 * @return the smallest
 * element in this collection that is greater than <code>target</code>
 * @throws NoSuchElementException no element in the collection
 * is greater than <code>target</code>
**/

	public E successor(E target) {
		BSTNode ptr = succ(findLastInsertPosition(target));
		if (ptr == AFT)
			throw new NoSuchElementException();
		else
			return ptr.data;
	}

/**
 * @param element the new element
 * @return a
 * reference to the newly added node
**/

	protected BSTNode insert(E element) {
		BSTNode t = createTreeNode(element);              // create a TreeNode t with the given element
		BSTNode ptr = findLastInsertPosition(element);	   // find frontier node at last insert position
		ptr.replaceSubtreeBy(t);						   // replace frontier node reached by t
		return t;
	}

/**
 * Inserts <code>element</code> into the collection.
 * @param element the new element
 * @return a tracker that tracks the new element
**/

	public Locator<E> addTracked(E element) {
		size++;
		return new Tracker(insert(element));
	}

/**
 * @param x a reference to the node to remove
 * @throws NoSuchElementException node <code>x</code> is no longer in the collection (i.e.,
 * x is not in use)
**/

	protected void remove(TreeNode x) {
		BSTNode toRemove = (BSTNode) x;  //to avoid casting each time
		if (toRemove.isDeleted())		   // x already removed
			throw new NoSuchElementException();
		BSTNode successor = (BSTNode) succ(toRemove);	//needed to update trackers
		if (toRemove.left.isFrontier())				       // Case 1
			toRemove.deleteAndReplaceBy(toRemove.right);
		else if (toRemove.right.isFrontier())		       // Case 2
			toRemove.deleteAndReplaceBy(toRemove.left);
		else {							                   // Case 3
			successor.deleteAndReplaceBy(successor.right);    //remove replacement
			toRemove.substituteNode(successor);
		}
		toRemove.parent = successor; // preserved RedirectChain
		toRemove.markDeleted();      // preserves  InUse
		size--;                      // preserves Size
	}


	public void clearNodes(BSTNode x){
		if (x != null && !x.isFrontier()){
			clearNodes(x.left);	
			clearNodes(x.right);
			x.parent = AFT;
			x.markDeleted(); //done with left child so can reset
		}
	}

/**
 * Removes all elements from the collection
**/

	public void clear(){
		clearNodes((BSTNode) root);
		root = FRONTIER_L;
		size = 0;
	}

/**
 * Creates a new tracker at FORE.
**/

	public Locator<E> iterator() {
		return new Tracker((BSTNode) FORE);
	}

/**
 * Creates a new tracker that is at AFT.
**/

	public Locator<E> iteratorAtEnd() {
		return new Tracker((BSTNode) AFT);
	}

/**
 * @param x the target
 * @return a tracker initialized to an element equivalent to x
 * @throws NoSuchElementException there is no equivalent element in the collection
**/

	public Locator<E> getLocator(E x) {
		BSTNode t = find(x);
		if (t.isFrontier())
			throw new NoSuchElementException();
		return new Tracker(t);
	}


	protected class Tracker extends AbstractCollection<E>.AbstractLocator<E> {

		BSTNode node;  //reference to the tracked node

/**
 * @param ptr a reference to
 * the node to track
**/

		Tracker(BSTNode ptr) {
			this.node = ptr;
		}

/**
 * @return true  if and only if the tracked element is
 * currently in the collection.
**/

		public boolean inCollection() {
			return !node.isDeleted();
		}

/**
 * @return the tracked element
 * @throws NoSuchElementException tracker is not at an element in
 * the collection.
**/

		public E get() {
			if (!inCollection())
				throw new NoSuchElementException();
			return node.data;
		}

/**
 * This method performs the optimization
 * of path compression on the redirect chain by letting all of the traversed
 * parent references refer to the returned node.
 * @param ptr reference to a node
 * @return a reference to the first node that is in use
 * reached starting at <code>ptr</code> and following the <code>parent</code> references.
 * If no node in the collection is reached, then AFT is returned.
**/

		private BSTNode skipRemovedElements(BSTNode ptr) {
			if (!ptr.isDeleted()) 
				return ptr;
			if (ptr.parent.isDeleted())
				ptr.parent = skipRemovedElements(ptr.parent);
			return ptr.parent;
		}

/**
 * Moves the tracker to the next element in the
 * iteration order, or AFT if the last element is currently tracked
 * @return true  if and only if the tracker is at
 * an element of the collection after the update.
 * @throws AtBoundaryException the tracker is
 * already at AFT when the method is called, since there is
 * no place to advance.
**/

		public boolean advance() {
			checkValidity();
			if (node == AFT)
				throw new AtBoundaryException();
			else if (isEmpty())
				node = AFT;
			else if (node == FORE)
				node = (BSTNode) leftmost();
			else if (node.isDeleted())
				node = skipRemovedElements(node);
			else
				node = (BSTNode) succ(node);
			return node != AFT;				  //still within collection unless AFT reached
		}

/**
 * Moves the tracker to the previous element in the iteration order,
 * or FORE if the
 * first element is currently tracked.
 * @return true  if and only if after the update, the tracker is at an element of the collection.
 * @throws AtBoundaryException the tracker is at FORE since then there is
 * no place to retreat.
**/

		public boolean retreat() {
			checkValidity();
			E oldData = node.data;
			boolean wasDeleted = node.isDeleted();
			if (node == FORE)
				throw new AtBoundaryException();
			if (wasDeleted)
				node = skipRemovedElements(node);
			if (node == AFT) {				
				if (isEmpty()) 					
					node = FORE;						
				else						  
					node = (BSTNode) rightmost();
			} else {
				node = (BSTNode) pred(node);	        //otherwise move to the predecessor
			}
			if (wasDeleted && node != FORE)	  //move to before any smaller elements added
				while (comp.compare(node.data, oldData) > 0)
					node = (BSTNode) pred(node);
			return node != FORE;				  //still within collection unless AFT reached.
		}

/**
 * @return true  if there is some element in the collection after the currently
 * tracked element.
**/

		public boolean hasNext() {
			checkValidity();
			if (node == FORE)
				return !isEmpty();
			if (node == AFT)
				return false;
			if (node.isDeleted())
				return skipRemovedElements(node) != AFT;
			else
				return succ(node) != AFT;
		}

/**
 * Removes the tracked element and implicitly updates the tracker to be
 * between the predecessor and the successor in the iteration order.
 * @throws NoSuchElementException the tracker is at FORE or AFT
**/

		public void remove() {
			BinarySearchTree.this.remove(node);
			updateVersion();
		}


	}
}
