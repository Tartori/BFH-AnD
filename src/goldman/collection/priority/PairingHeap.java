// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.priority;
import java.util.Comparator;
import java.util.NoSuchElementException;
import goldman.Objects;
import goldman.collection.AbstractCollection;
import goldman.collection.AtBoundaryException;
import goldman.collection.Tracked;
import goldman.collection.positional.Queue;
/**
 * The pairing heap is a simple
 * self-organizing data structure in which the
 * amortized cost for <code>add</code>, <code>merge</code>, and
 * <code>remove</code> through a tracker are all logarithmic.
 * The insert and merge methods have worst-case constant cost.
 * When an element is removed, the data structure is re-structured,
 * leading to a worst-case linear cost.
 * Increasing the priority for a tracked element has worst-case
 * constant cost, but the amortized cost of this operation is
 * logarithmic due to an increase in the potential.  Intuitively,
 * each <code>add</code> method is charged to compensate for the future
 * reorganization cost.  To decrease the priority of a tracked element,
 * the amortized cost is logarithmic, although the worst-case cost
 * is linear.  The space usage of the pairing heap is
 * similar to that of the leftist heap.   While we provide only
 * a threaded version of a pairing heap,
 * a non-threaded version could be created as illustrated in the leftist
 * heap implementation.
**/

public class PairingHeap<E> extends AbstractCollection<E> implements 
	PriorityQueue<E>, Tracked<E> {

	HeapNode<E> root;
	final HeapNode<E> FORE = new HeapNode<E>(null);
	final HeapNode<E> AFT = new HeapNode<E>(null); 
	Queue<HeapNode<E>> detached = new Queue<HeapNode<E>>();  


	static class HeapNode<E> {

		E element;
		HeapNode<E> child;  //reference to the leftmost child
		HeapNode<E> sibL;   //references left sibling in sibling list
		HeapNode<E> sibR;   //references right sibling in sibling list
		HeapNode<E> next;   //references next node in the iteration list
		HeapNode<E> prev;   //references previous node in the iteration list

/**
 * @param element the element to be held in this node
**/

		public HeapNode(E element) {
			this.element = element;
			makeRoot();
		}

/**
 * Sets <code>sibL</code> and <code>sibR</code> so that this node becomes the root.
 * Since the Java runtime system initializes
 * <code>child</code> to null the property Children is satisfied.
**/

		void makeRoot() {
			sibL = this;  //satisfy Parent
			sibR = null; //satisfy SiblingChain
		}

/**
 * @param ptr a reference to the heap
 * node that is to be placed left of this node in the sibling chain
**/

		final void setLeft(HeapNode<E> ptr) {
			sibL = ptr;
			ptr.sibR = this;
		}

/**
 * @param ptr a reference to the node to place
 * next in the iteration list
**/

		final void setNext(HeapNode<E> ptr) {
			next = ptr;
			ptr.prev = this;
		}

/**
 * @return true  exactly
 * when this node is a leftmost sibling.
**/

		final boolean isLeftmostChild() {
			return sibL.child == this;
		}

/**
 * @return true  exactly
 * when this node is a rightmost sibling.
**/

		final boolean isRightmostChild() {
			return sibR == null;
		}

/**
 * Modifies this node to indicate
 * that the element it holds has been removed from the collection.
**/

		final void markDeleted() {
			sibL = null;
		}

/**
 * @return true  if and only if
 * this occurrence of the element held is no longer in the collection
**/

		final boolean isDeleted() {
			return sibL == null;
		}

/**
 * @param newChild a reference
 * to the heap node to be added as a child to this node
**/

		void addChild(HeapNode<E> newChild) {
			if (child == null) {                   //Case 1:has no children
				child = newChild;
				newChild.sibL = this; 
			} else if (child.isRightmostChild()) { //Case 2: has 1 child
				newChild.setLeft(child);
			} else {                               //Case 3: has at least 2 children
				child.sibR.setLeft(newChild);      //   add as a second child in chain
				newChild.setLeft(child);
			}
		}

/**
 * Removes this heap node from its
 * current sibling chain
**/

	void removeFromChain() {
		if (isLeftmostChild())   
			sibL.child = sibR;   //preserve Child
		else                   
			sibL.sibR = sibR;
		if (!isRightmostChild())  //preserve Parent
			sibR.sibL = sibL;
	}


		public String toString() {
			return  "" + element;
		}
		
		public int degree() {
			HeapNode ptr = this.child;
			int num = 0;
			while (ptr != null) {
				num++;
				ptr = ptr.sibR;
			}
			return num;
		}
	}

	public PairingHeap(){
		this(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp a user-provided comparator
**/

	public PairingHeap(Comparator<? super E> comp) {
		super(comp);
		root = null;         //satisfy Parent
		FORE.setNext(AFT);   //satisfy IterationList
		FORE.markDeleted();  //satisfy InUse
		AFT.markDeleted();
	}

/**
 * Reinitializes all
 * instance variables so this leftist heap is empty.
**/

	void reinitialize() {
		size = 0;
		root = null;
		FORE.setNext(AFT);
	}

/**
 * @param value the desired element
 * @return a new heap node holding the given element.
**/

	HeapNode<E> newHeapNode(E value) {
		return new HeapNode<E>(value);
	}

/**
 * @return a highest priority element
 * @throws NoSuchElementException this pairing heap is empty
**/

	public E max() {
		if (isEmpty())
			throw new NoSuchElementException();
		return root.element;
	}


	HeapNode<E> find(E element){
		HeapNode<E> ptr = FORE.next;  //first element in iteration list
		while (ptr != AFT){             
			if (equivalent(ptr.element,element))
				return ptr;
			ptr = ptr.next;
		}
		return null;
	}

/**
 * @param target the target element
 * @return an equivalent element
 * @throws NoSuchElementException there is no equivalent element in this
 * collection.
**/

	public E getEquivalentElement(E target) {
		HeapNode<E> node = find(target);
		if (node != null)
			return node.element;
		else
			throw new NoSuchElementException();
	}

/**
 * @param rootA the root of one pairing heap to merge
 * @param rootB the root of the other pairing heap to merge
 * <BR> 
 * REQUIRES: 
 *  <code>T(rootA)</code> and <code>T(rootB)</code> are valid
 * pairing heaps
 * @return a reference to the root of the merged pairing heap
**/

	HeapNode<E> merge(HeapNode<E> rootA, HeapNode<E> rootB) {
		if (comp.compare(rootA.element, rootB.element) > 0) {
			rootA.addChild(rootB);
			return rootA;
		} else {
			rootB.addChild(rootA);
			return rootB;
		}
	}

/**
 * @param x a reference to the
 * root of the pairing heap T(x) that is to be merged with T(r)
 * <BR> 
 * REQUIRES: 
 *  x is not null
**/

	void mergeWithRoot(HeapNode<E> x) {
		if (root == null)        //special case for an empty collection
			root = x;
		else
			root = merge(root, x);
	}

/**
 * @param element the new element to insert into this collection
 * @return a
 * reference to the heap node holding the new element.
**/

	HeapNode<E> insert(E element) {
		HeapNode<E> x = newHeapNode(element);
		mergeWithRoot(x);
		AFT.prev.setNext(x);     //preserve IterationList by
		x.setNext(AFT);          //adding new node just before AFT
		size++;                  //preserve Size
		return x;
	}	

/**
 * Adds it to the heap
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
		HeapNode<E> node = insert(element);
		Tracker t = new Tracker(node);
		return (PriorityQueueLocator<E>) t;
	}

/**
 * @param x a reference to the node whose
 * children are to be moved into the queue
 * <BR> 
 * REQUIRES: 
 *  x is not null
 * @return a queue of heap nodes
 * corresponding to the children of the given node
**/

	Queue<HeapNode<E>> moveChildrenToQueue(HeapNode<E> x){
		HeapNode<E> ptr = x.child;
		x.child = null;                      //detach children from node
		while (ptr != null) {                   //traverse through sibling chain
			HeapNode<E> right = ptr.sibR;      //remember the right sibling
			ptr.makeRoot();                          //make a root
			detached.enqueue(ptr);                   //put in queue
			ptr = right;                             //move to next sibling
		}
		return detached;
	}

/**
 * @return a reference to the root of the pairing heap that is the union of
 * all pairing heaps in the queue of detached heaps
**/

	HeapNode<E> mergeQueue() {
		while (detached.getSize() > 1)
			detached.enqueue(merge(detached.dequeue(), detached.dequeue()));
		return detached.dequeue();
	}

/**
 * @param x a reference to the pairing
 * heap node
 * @param element the new element to replace <code>x.element</code>
 * <BR> 
 * REQUIRES: 
 * <code>x</code> is not null, and
 * that <code>element</code> &le; <code>x.element</code>
**/

	void decreasePriority(HeapNode<E> x, E element) {
		x.element = element;
		if (x.child != null) {  //if node has a child
			moveChildrenToQueue(x);
			HeapNode<E> melded = mergeQueue();
			root = merge(root, melded);
			root.makeRoot();   //preserve Parent for the root
		}	
	}

/**
 * @param x a reference to the heap node
 * @param element the new element to replace <code>x.element</code>
 * <BR> 
 * REQUIRES: 
 *  <code>x</code> is not null,
 * <code>element</code> &ge; <code>x.element</code>
**/

	void increasePriority(HeapNode<E> x, E element){
		x.element = element;
		if (x != root){  //if node is not the root
			x.removeFromChain();
			x.sibL = x.sibR = null;
			root = merge(root,x);
			root.makeRoot();  //preserve Parent for the root
		}
	}

/**
 * @param x a reference a node holding the element to be replaced
 * @param element the replacement element
 * <BR> 
 * REQUIRES: 
 *  <code>x</code> is not null
**/

		protected void update(HeapNode<E> x, E element){
			if (comp.compare(element, x.element) > 0)
				increasePriority(x,element);
			else if (comp.compare(element, x.element) < 0)
				decreasePriority(x,element);
			else
				x.element = element;
		}

/**
 * Removes the node from the heap.
 * @param x a reference to the heap node to remove
 * <BR> 
 * REQUIRES: 
 *  the given node is not null,
 * and that it is not already deleted
 * @return the
 * element held in the removed node
**/

	E remove(HeapNode<E> x) { 
		if (x.child == null) {       //when x has no children
			x.removeFromChain();     //just remove from its sibling list
		}
		else {                       //x has at least one child
			moveChildrenToQueue(x);
			HeapNode<E> melded = mergeQueue();
			if (x == root)
				root = melded;
			else {
				x.removeFromChain();
				root = merge(root, melded);
			}
		}
		x.markDeleted();             //preserve Removed
		x.prev.setNext(x.next);      //preserve IterationList
		size--;                         //preserve Size
		return x.element;
	}

/**
 * Removes an equivalent element from the collection
 * @param element the target
 * @return true  if and only if an equivalent element was found, and hence removed
**/

	public boolean remove(E element) {
		HeapNode<E> node = find(element);
		if (node != null){
			remove(node);
			return true;
		}
		return false;
	}

/**
 * @return and removes the highest priority
 * element from the collection
 * @throws NoSuchElementException the collection is empty.
**/

	public E extractMax() {
		if (isEmpty())
			throw new NoSuchElementException();
		return remove(root);
	}

/**
 * Creates a new tracker that is at FORE.
**/

	public PriorityQueueLocator<E> iterator() {
		return new Tracker(FORE);
	}

/**
 * @param element the target
 * @return a tracker positioned at the given element.
 * @throws NoSuchElementException there is no equivalent element
 * in this collection.
**/

	public PriorityQueueLocator<E> getLocator(E element) {
		HeapNode node = find(element);
		if (node == null)
			throw new NoSuchElementException();
		else
			return new Tracker(find(element));
	}



	public String showStructure() {
		String out = printChildren("", root);
		return "root is " + root + "\n" + out;
		
	}
	
	private String printChildren(String indent, HeapNode start) {
		HeapNode ptr = start.child;
		String childIndent, result = "";
		result +=(indent+"Children of "+start+"\n");
		childIndent = indent+="\t";
		while (ptr != null) {
			result += (indent+"Child: "+ ptr);
			indent +="\t";
			result += ("\n"+indent+"Left: "+ptr.sibL+"\n"+indent+"Right: "+ptr.sibR+"\n");
			if (ptr.child != null)
				result += (printChildren(indent,ptr));
			ptr = ptr.sibR;
			indent = childIndent;
		}
		return result;
	}

	protected class Tracker extends AbstractCollection<E>.AbstractLocator<E>
				implements PriorityQueueLocator<E> {

		HeapNode<E> node;

/**
 * @param node a reference to
 * the node to track
**/

		Tracker(HeapNode<E> node) {
			this.node = node;
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
			if (node.isDeleted())
				throw new NoSuchElementException();
			return node.element;
		}

/**
 * @param ptr a pointer to a list item
 * @return a pointer to the first reachable list item
 * (including <code>ptr</code> itself if it is reachable)
 * obtained by following the <code>next</code> pointer of any
 * unreachable list item
**/

		private HeapNode<E> skipRemovedElements(HeapNode<E> ptr) {
			if (!ptr.isDeleted() || ptr == AFT) 
				return ptr;
			ptr.next = skipRemovedElements(ptr.next);
			return ptr.next;
		}

/**
 * Moves the tracker to the next element in the iteration order (or {\texttt AFT} if
 * the last element is currently tracked).
 * @return true  if and only if after the update, the tracker is at
 * an element of the collection.
 * @throws AtBoundaryException the tracker is at AFT since there is
 * no place to advance.
**/

		public boolean advance() {
			checkValidity();
			if (node == AFT)
				throw new AtBoundaryException("Already after end.");
			if (node.isDeleted())		//if tracked element has been deleted
				node = skipRemovedElements(node); //update tracker to successor
			else
				node = node.next;	 
			return node != AFT;			
		}

/**
 * Moves the tracker to the previous element in the iteration order (or FORE if the
 * first element is currently tracked).
 * @return true  if and only if after the update, the tracker is at an element of the collection.
 * @throws AtBoundaryException the tracker is at FORE since there is
 * no place to retreat.
**/

		public boolean retreat() {
			if (node == FORE)
				throw new AtBoundaryException("Already before start.");
			if (node.isDeleted())	         //if tracked element has been deleted
				node = skipRemovedElements(node);  //update tracker to successor
			node = node.prev;
			return node != FORE;
		}

/**
 * @return true  if there is some element after the current tracker position.
**/

		public boolean hasNext() {
			return ((node.next != AFT) &&
					(skipRemovedElements(node) != AFT));
		}

/**
 * Removes the tracked element and updates the tracker to be
 * at the element in the iteration order that preceded the one removed.
 * @throws NoSuchElementException the tracker is at FORE or AFT
**/

		public void remove() {
			if (node.isDeleted())
				throw new NoSuchElementException();
			PairingHeap.this.remove(node);
		}

/**
 * Replaces the tracked element by
 * <code>element</code>
 * <BR> 
 * REQUIRES: 
 * <code>element</code> is different than the element
 * at the current tracker position
**/

		public void update(E element) {
			if (node.isDeleted())
				throw new NoSuchElementException();
			PairingHeap.this.update(node,element);
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
			PairingHeap.this.increasePriority(node,element);
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
			PairingHeap.this.decreasePriority(node,element);
		}


	}
}
