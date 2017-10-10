// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.priority;
import java.util.Comparator;
import java.util.NoSuchElementException;
import goldman.Objects;
import goldman.collection.AbstractCollection;
import goldman.collection.Locator;
import goldman.collection.Tracked;
import goldman.collection.Visitor;
import goldman.partition.UnionFindNode;
/**
 * The leftist heap is a fairly simple
 * implementation that supports <code>merge</code> in logarithmic time.
 * A leftist heap can be threaded to support faster iteration, or
 * non-threaded.  We provide only a non-threaded leftist heap implementation,
 * but a threaded version could be implemented
 * as illustrated for a pairing heap.
 * The primary cost of the threading would be the increased space due to
 * two additional
 * references per element. There would also be a small amount of overhead required to
 * maintain the extra references.   In the threaded implementation, all of the methods
 * would have the same time complexity as for the binary heap, except
 * for <code>merge</code> which is improved from linear to logarithmic time, and
 * <code>addAll</code> which changes from constant time per new element to
 * logarithmic time per new element.  In the non-threaded implementation,
 * iteration is limited and less efficient than the threaded implementation.
**/

public class LeftistHeap<E> extends AbstractCollection<E> implements 
		PriorityQueue<E>, Tracked<E> {

	final static LeftistHeapNode FRONTIER = null;      //used for any null child
	LeftistHeapNode<E> root;                           //reference to the root of the leftist heap
	UnionFindNode<LeftistHeap<E>> proxy;               //to support of efficient union


	static class LeftistHeapNode<E> {

		E element;                  //reference to the element
		LeftistHeapNode<E> left;    //reference to left child
		LeftistHeapNode<E> right;   //reference to right child
		LeftistHeapNode<E> parent;  //reference to parent
		int dtf = 1;                //shortest distance to a frontier node

/**
 * @param element the value to be held in this node
**/

		public LeftistHeapNode(E element) {
			this.element = element;
		}


		public String toString() {
			return  "" + element;
		}
/**
 * Modifies this node to indicate
 * that its element, which may be tracked, has been removed from the collection.
**/

		final void markDeleted() {
			left = this;
		}

/**
 * @return true  if and only if
 * this occurrence of the element held in this node has been deleted
**/

		final boolean isDeleted() {
			return left == this;
		}

/**
 * @return the shortest distance
 * to a frontier node in the left subtree
**/

		int dtfLeft() {
			return (left == FRONTIER) ? 0 : left.dtf;
		}

/**
 * @return the shortest distance
 * to a frontier node in the right subtree
**/

		int dtfRight() {
			return (right == FRONTIER) ? 0 : right.dtf;
		}

/**
 * @param x a reference
 * to a heap node
**/

		void setLeft(LeftistHeapNode<E> x) {
			this.left = x;
			x.parent = this;
			swapChildrenAsNeeded();
		}

/**
 * @param x a reference
 * to a heap node
**/

		void setRight(LeftistHeapNode<E> x) {
			this.right = x;
			x.parent = this;
			swapChildrenAsNeeded();
		}

/**
 * @return true  when the children are swapped
**/

		boolean swapChildrenAsNeeded() {
			if (dtfLeft() < dtfRight()) {
				LeftistHeapNode<E> temp = left;
				left = right;
				right = temp;
				dtf = 1 + dtfRight();
				return true;
			}
			return false;
		}


		int size() {
			if (left == null)
				return 0;
			if (right == null)
				return 1;
			else
				return 2;			
		}
	}

	public LeftistHeap(){
		this(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp a user-provided
 * comparator
**/

	public LeftistHeap(Comparator<? super E> comp) {
		super(comp);
		proxy = new UnionFindNode<LeftistHeap<E>>(this); 
	}

/**
 * @param h1 a leftist heap
 * @param h2 a leftist heap
 * @throws IllegalArgumentException <code>h1</code> and <code>h2</code>
 * do not use the same comparator
**/

	public LeftistHeap(LeftistHeap<E> h1, LeftistHeap<E> h2) {
		this(h1.comp);   //create a new leftist heap with comparator from h1
		if (h2.comp != h1.comp)  //check h1 and h2 use the same comparator
			throw new IllegalArgumentException
						("The comparators of the two heaps must be the same.");
		root = merge(h1.root, h2.root); //root set to merge of h1 and h2
		size = h1.size + h2.size;       //size of new heap is sum of sizes of h1 and h2
		
		//The remainder of this method ensures that locators remain valid through our use of
		//  the union-find data structure
		proxy.union(h1.proxy);            //take union of proxies of new heap and h1
		proxy.union(h2.proxy).set(this);  //rep. element for union of this and h2 is this
		h1.reinitialize();                //reinitialize h1 and h2
		h2.reinitialize();                //so new elements can be added to them
	}

/**
 * Reinitializes all parameters so
 * this leftist heap is empty.
**/

	void reinitialize() {
		size = 0;
		root = null;
		proxy = new UnionFindNode<LeftistHeap<E>>(this);
	}

/**
 * @param target the target value
 * @param x the root of the subtree to search
 * @return true  if and only
 * if T(x) contains an element equivalent to the target.
**/

	protected boolean contains(E target, LeftistHeapNode<E> x){
		return x != null && 
			(equivalent(x.element, target) ||
			contains(target, x.left) ||
			contains(target, x.right));
	}

/**
 * @param target the target value
 * @return true  when there is an equivalent element to the target in this
 * collection.  Otherwise, false  is returned.
**/

	public boolean contains(E target) {
		return contains(target, root);
	}

/**
 * @return a highest priority element
 * @throws NoSuchElementException this leftist heap is empty
**/

	public E max() {
		if (isEmpty())
			throw new NoSuchElementException();
		return root.element;
	}

/**
 * Uses a preorder traversal
 * to visit T(x)
 * Any exception thrown by the visitor propagates to the calling method.
 * @param v the visitor
 * @param x a reference to root of the subtree to visit
**/

	void traverseForVisitor(Visitor<? super E> v, LeftistHeapNode<E> x) throws Exception {
		if (x != FRONTIER) {
			v.visit(x.element);
			traverseForVisitor(v, x.left);
			traverseForVisitor(v, x.right);
		}
	}

/**
 * Applies the visitor from the root of the leftist heap
 * @param v the visitor
**/

	protected void traverseForVisitor(Visitor<? super E> v) throws Exception {
		traverseForVisitor(v, root);
	}

/**
 * The method appends each item's string representation to the string buffer, separated
 * by commas.
 * @param sb a string buffer in which to create a string representation of the collection
**/

	protected void writeElements(final StringBuilder sb) { 
		if (!isEmpty()) {		//only visit the collection if it is non-empty
			accept(new Visitor<E>() {
				public void visit(E item) throws Exception {
					sb.append(item);
					sb.append(", ");
				}
			});
			int extraComma = sb.lastIndexOf(","); //remove the comma and space characters
			sb.delete(extraComma,extraComma+2);   //after the last element
		}
	}

/**
 * @param x a reachable heap node,
 * @param element the target
 * @return a reference to a heap node holding an equivalent element, or null\
 * if there is no equivalent element in T(x)
**/

	LeftistHeapNode<E> find(LeftistHeapNode<E> x, E element){
		if (x == FRONTIER || comp.compare(element,x.element) > 0) 
			return null;
		if (equivalent(element, x.element))  //check if x holds an equivalent element
			return x;
		LeftistHeapNode<E> fromLeft = find(x.left,element);  //recursively search left subtree
		if (fromLeft != null)                                //if found in left subtree
			return fromLeft;                                 //return location found
		else                                                 //otherwise
			return find(x.right,element);                    //return location found in right subtree
	}

/**
 * @param element the target
 * @return a reference to a heap node holding an equivalent element, or null\
 * if there is no equivalent element in this collection
**/

	protected LeftistHeapNode<E> find(E element){
		return find(root, element);
	}

/**
 * @param target the target element
 * @return an equivalent element
 * @throws NoSuchElementException there is no equivalent element in this
 * collection.
**/

	public E getEquivalentElement(E target) {
		LeftistHeapNode<E> node = find(target);
		if (node != FRONTIER)
			return node.element;
		else
			throw new NoSuchElementException();
	}

/**
 * Combines them into a single leftist heap
 * @param h1 a reference
 * to the root of one leftist heap
 * @param h2 a reference to the root another leftist heap
 * <BR> 
 * REQUIRES: 
 *  <code>T(h1)</code> and <code>T(h2)</code> are valid
 * leftist heaps
 * @return a reference to
 * the root of the result leftist heap
**/

	LeftistHeapNode<E> merge(LeftistHeapNode<E> h1, LeftistHeapNode<E> h2) {
		if (h2 == FRONTIER)  return h1;  //if one root is null,
		if (h1 == FRONTIER)  return h2;  //then return the other
		if (comp.compare(h1.element, h2.element) < 0) {  //if h2 has higher priority
			LeftistHeapNode<E> temp = h1;    //swap their roles
			h1 = h2;  									   
			h2 = temp;
		}
		h1.setRight(merge(h1.right, h2));  //recursively merge h1.right and h2
		version.increment(); //invalidate all locators
		return h1;
	}

/**
 * @param element the new element to insert into this collection
 * @return a
 * reference to the heap node holding the new element.
**/

	LeftistHeapNode<E> insert(E element) {
		LeftistHeapNode<E> x = new LeftistHeapNode<E>(element);
		root = merge(root, x);
		size++;  //preserve Size
		return x;
	}	

/**
 * @param element the element to add
**/

	public void add(E element) {
		insert(element);
	}

/**
 * @param element the element
 * to add
 * @return a tracker to the newly added element
**/

	public PriorityQueueLocator<E> addTracked(E element) {
		return new Tracker(insert(element), proxy);
	}

/**
 * Breaks the leftist heap into two heaps by
 * making <code>T(x)</code> its own leftist heap.
 * @param x a reference to a heap node for which
 * T(x) is to be detached
 * <BR> 
 * REQUIRES: 
 *  the given node is not the root
**/

	void detachFromParent(LeftistHeapNode<E> x) {
		if (x.parent.left == x) //if node is a left child
			x.parent.left = null;
		else                          //if node is a right child
			x.parent.right = null;
		LeftistHeapNode<E> ptr = x.parent; //preserve DTF and LeftLeaning
		x.parent = null;            //now node is detached from parent
		while (ptr != null && ptr.swapChildrenAsNeeded())
			ptr = ptr.parent;  //move up one level in heap
	}	

/**
 * @param node a reference to the heap node holding
 * element e
 * @param element the new element to replace e
 * <BR> 
 * REQUIRES: 
 *  <code>element</code> &le; <code>node.element</code>
**/

	void decreasePriority(LeftistHeapNode<E> node, E element) {
		node.element = element;               //replace element in node
		LeftistHeapNode<E> leftChild = node.left;    
		LeftistHeapNode<E> rightChild = node.right;
		if (leftChild != FRONTIER &&          //if left child exists and has higher priority            
				comp.compare(leftChild.element, element) > 0)
			detachFromParent(leftChild);      //detach it from current location
		else
			leftChild = null;              //otherwise, remember no left child to merge back
		if (rightChild != FRONTIER &&      //if right child exists and has higher priority           
				comp.compare(rightChild.element, element) > 0)
			detachFromParent(rightChild);  //detach it from current location
		else
			rightChild = null;             //otherwise, remember no right child to merge back
		root = merge(root,leftChild);  //merge with T(root)
		root = merge(root,rightChild); //merge with T(root)
	}

/**
 * @param node a reference to the heap node holding
 * element e
 * @param element the new element to replace e
 * <BR> 
 * REQUIRES: 
 *  <code>element</code> &ge; <code>node.element</code> and
 * that <code>node</code> is not null
**/

	void increasePriority(LeftistHeapNode<E> node, E element){
		node.element = element;  //replace element
		if (node != root &&                                //if node is not the root and 
				comp.compare(node.parent.element, element) < 0 ){ //HeapOrdered violated
			detachFromParent(node);
			root = merge(root,node);
		}
	}

/**
 * @param node a reference to node where element is to be replaced
 * @param element the replacement element
 * <BR> 
 * REQUIRES: 
 *  <code>node</code> is not null
**/

	void update(LeftistHeapNode<E> node, E element){
		if (comp.compare(element,node.element) >= 0)
			increasePriority(node,element);
		else if (comp.compare(element,node.element) < 0)
			decreasePriority(node,element);
	}

/**
 * Removes x from the leftist heap.
 * @param x a reference to the heap node to remove
 * <BR> 
 * REQUIRES: 
 *  x is not null
 * @return the
 * element held in x
**/

	E remove(LeftistHeapNode<E> x) { 
		if (x == root)
			root = merge(root.left, root.right);
		else {
			detachFromParent(x);       //detach node from parent
			root = merge(root, x.left);  //merge left child into T(root)
			root = merge(root, x.right); //merge right child into T(root)
		}
		size--;                             //preserve Size
		x.markDeleted();                 //preserve InUse
		return x.element;
	}


/**
 * Removes an equivalent element from the collection
 * @param target the element to remove
 * @return true  if and only if an equivalent element was found and removed
**/

	public boolean remove(E target) {
		LeftistHeapNode<E> node = find(target);
		if (node == FRONTIER)
			return false;
		remove(node);
		return true;
	}

/**
 * This method also removes
 * the maximum priority element from the collection.
 * @return the highest priority
 * element from the collection
 * @throws NoSuchElementException the collection is empty.
**/

	public E extractMax() {
		if (isEmpty())
			throw new NoSuchElementException();
		return remove(root);
	}

/**
 * Removes all elements in T(x)
 * @param x a reachable heap node
**/

	void clear(LeftistHeapNode<E> x) {
		if (x != FRONTIER) {
			clear(x.left);
			clear(x.right);
			x.markDeleted();
		}		
	}

/**
 * Removes all elements from the collection
**/

	public void clear() {
		clear(root);
		size = 0;
		root = FRONTIER;
	}

/**
 * Creates a new tracker that is at FORE.
**/

	public Locator<E> iterator() {
		return new VisitingIterator();
	}

/**
 * @param element the target
 * @return a tracker positioned at the given element.
 * @throws NoSuchElementException there is no equivalent element
 * in this collection.
**/

	public PriorityQueueLocator<E> getLocator(E element) {
		LeftistHeapNode<E> node = find(element);
		if (node == null)
			throw new NoSuchElementException();
		else
			return new Tracker(node, proxy);
	}


	public String showStructure() {
		return showStructureVisit(root);
	}
	
	String showStructureVisit(LeftistHeapNode<E> x) {
		String out = "\n***node " + x + " has depth of " + x.dtf + "\n";
		out += "       - left child " + x.left + "\n";
		out += "       - right child " + x.right + "\n";
		if (x.left != null)
			out += showStructureVisit(x.left);
		if (x.right != null)
			out += showStructureVisit(x.right);
		return out;
	}

	protected class Tracker extends AbstractCollection<E>.AbstractLocator<E>
				implements PriorityQueueLocator<E> {

		LeftistHeapNode<E> node;
		UnionFindNode<LeftistHeap<E>> proxy;  //preserves trackers after merge 

/**
 * @param node a reference to the node to track
 * @param proxy the union-find node that serves as a proxy for the leftist heap
 * to which this tracker belongs
**/

		Tracker(LeftistHeapNode<E> node, UnionFindNode<LeftistHeap<E>> proxy) {
			this.node = node;
			this.proxy = proxy;
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
 * @throws NoSuchElementException the tracker is at FORE, at AFT, or is
 * tracking an element that has been removed.
**/

		public E get() {
			if (node.isDeleted())
				throw new NoSuchElementException();
			return node.element;
		}


		public boolean advance() {
			throw new UnsupportedOperationException();
		}


		public boolean retreat() {
			throw new UnsupportedOperationException();
		}


		public boolean hasNext() {
			throw new UnsupportedOperationException();
		}

/**
 * Removes the tracked element from the collection
**/

		public void remove() {
			if (node.isDeleted())
				throw new NoSuchElementException();
			proxy.findRepresentative().get().remove(node);
		}

/**
 * Replaces the tracked element by
 * <code>element</code>
**/

		public void update(E element) {
			if (node.isDeleted())
				throw new NoSuchElementException();
			proxy.findRepresentative().get().update(node,element);
		}

/**
 * Replaces the tracked element by
 * <code>element</code>
 * <BR> 
 * REQUIRES: 
 *  the given parameter is greater than e, or that
 * e is the parameter being passed and its value has been mutated to have a higher priority than
 * it had previously.
**/

		public void increasePriority(E element) {
			if (node.isDeleted())
				throw new NoSuchElementException();
			proxy.findRepresentative().get().increasePriority(node,element);
		}

/**
 * Replaces the tracked element by
 * <code>element</code>
 * <BR> 
 * REQUIRES: 
 *  the given parameter is less than e, or that
 * e is the parameter being passed and its value has been mutated to have a lower priority than
 * it had previously.
**/

		public void decreasePriority(E element) {
			if (node.isDeleted())
				throw new NoSuchElementException();
			proxy.findRepresentative().get().decreasePriority(node,element);
		}


	}
}
