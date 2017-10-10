// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.priority;
import java.util.Comparator;
import java.util.NoSuchElementException;
import static goldman.Objects.*;
import goldman.collection.AbstractCollection;
import goldman.collection.Collection;
import goldman.collection.Tracked;
import goldman.collection.positional.DynamicArray;
import goldman.collection.positional.PositionalCollectionLocator;
import goldman.collection.positional.TrackedArray;
import goldman.collection.positional.PositionalCollection;
/**
 * The binary heap is a very simple data structure
 * that has worst-case logarithmic
 * cost for <code>add</code>, <code>extractMax</code>, and <code>update</code> (through
 * a locator).  However, the lead constant within the asymptotic notation
 * is small, so all of the methods run quite quickly.  Also the space usage
 * is very efficient, especially if the required size is passed to the constructor.
 * The main drawback is that merging two binary heaps takes linear time.
 * Also, increasing
 * the priority of a tracked element takes logarithmic time.
**/

public class BinaryHeap<E> extends AbstractCollection<E> implements 
	PriorityQueue<E>, Tracked<E> {

	final int NOT_FOUND = -1;
	PositionalCollection<E> bh; 
	boolean tracked;

/**
 * It creates an empty priority queue
 * with the specified characteristics.
 * @param initialCapacity the desired initial
 * capacity
 * @param comp a user-provided
 * comparator that defines the ordering among the elements
 * @param tracked to indicate
 * whether or not tracking should be supported.
**/

	public BinaryHeap(int initialCapacity, Comparator<? super E> comp, boolean tracked){
		super(comp);
		this.tracked = tracked;
		if (tracked)
			bh = new TrackedArray<E>(initialCapacity, comp);
		else
			bh = new DynamicArray<E>(initialCapacity, comp);
	}

/**
 * Creates an empty priority queue
 * that uses the default comparator
**/

	public BinaryHeap(){
		this(DEFAULT_CAPACITY, DEFAULT_COMPARATOR, false);
	}

/**
 * Creates an untracked empty priority queue that orders the elements using
 * the provided comparator, and has a default initial capacity
 * @param comp a user-provided
 * comparator
**/

	public BinaryHeap(Comparator<? super E> comp) {
		this(DEFAULT_CAPACITY, comp, false);
	}

/**
 * Creates an empty untracked empty priority queue that uses
 * the default comparator, and the provided
 * initial capacity.
 * @param initialCapacity the desired initial
 * capacity
**/

	public BinaryHeap(int initialCapacity) {
		this(initialCapacity, DEFAULT_COMPARATOR, false);
	}

/**
 * Creates
 * an untracked empty priority queue that uses
 * the provided initial capacity and comparator.
 * @param initialCapacity the desired initial
 * capacity
 * @param comp a user-provided comparator
**/

	public BinaryHeap(int initialCapacity, Comparator<? super E> comp) {
		this(initialCapacity, comp, false);
	}

/**
 * Creates a tracked or untracked empty priority queue
 * depending on whether <code>tracked</code> is true  or false.  The priority queue is
 * created with the default comparator, and has a default initial capacity
 * @param tracked to indicate whether
 * tracking is desired
**/

	public BinaryHeap(boolean tracked) {
		this(DEFAULT_CAPACITY, DEFAULT_COMPARATOR, true);
	}


	public boolean supportsTracking() {
		return tracked;
	}  

	public int getSize() {return bh.getSize();}


	public boolean isEmpty() {return bh.isEmpty();}

/**
 * As long as i &ge; 1, the parent method returns
 * a non-negative integer.  A negative return value indicates
 * that i is 0, meaning that it is the root node and has no parent
 * @param i the index of a
 * node
 * @return the index of its parent
**/

	final int parent(int i) {return ((int) (i - 1) / 2);}

/**
 * A return value greater than <code>size</code>-1
 * indicates that no left child exists.
 * @param i the index of
 * the current node
 * @return the index of its left child
**/

	final int left(int i) {return 2 * i + 1;}

/**
 * A return value that is greater than <code>size</code>-1
 * indicates that no right child exists.
 * @param i the index of
 * the current node
 * @return the index of its right child
**/

	final int right(int i) {return 2 * i + 2;}

/**
 * @return a highest priority element
 * @throws NoSuchElementException this collection is empty
**/

	public E max() {
		if (isEmpty())
			throw new NoSuchElementException();
		return bh.get(0);
	}

/**
 * @param element the target
 * @return a reference to a heap node holding an equivalent element, or null\
 * if there is no equivalent element in this collection.
**/

	protected int find(E element){
		for (int i = 0; i < bh.getSize(); i++)
			if (equivalent(element,bh.get(i)))
				return i;
		return NOT_FOUND;
	}

/**
 * @param element the target
 * @return true  if and only if an equivalent element exists in this collection
**/

	public boolean contains(E element) {
		return bh.contains(element);
	}
	


	@SuppressWarnings("unchecked")
/**
 * @param element the target
 * @return an equivalent element from the collection
 * @throws NoSuchElementException there is no equivalent element in this
 * collection.
**/

	public E getEquivalentElement(E target) {
		int index = find(target);
		if (index == NOT_FOUND)
			throw new NoSuchElementException();
		else
			return (E) bh.get(index);
	}

/**
 * This method requires
 * that the only violation to the property HeapOrdered occurs between node i and
 * its parent.
 * @param i the index of
 * the node causing a possible violation with its parent
**/

	private void fixUpward(int i) {
		while ((i > 0) && comp.compare(bh.get(i),bh.get(parent(i))) > 0) { //parent(i) > i
			bh.swap(i, parent(i));
			i = parent(i);
		}
	}

/**
 * @param i the index of
 * the node causing a possible violation with its children
 * <BR> 
 * REQUIRES: 
 *  the descendants of
 * node i, if any, satisfy the the property HeapOrdered property.
**/

	private void fixDownward(int i) {
		int max = i;   //index of higher priority child
		while (true) {
			int left = left(i);
			int right = left + 1;
			if (right > bh.getSize()) //node i is a leaf, so has no children
				return;
			else if (right == bh.getSize()
					|| comp.compare(bh.get(left),bh.get(right)) > -1) //node i only has left child
				max = left;
			else
				max = right;
			if (comp.compare(bh.get(i),bh.get(max)) < 0) { //max has higher priority
				bh.swap(i, max);
				i = max;
			}
			else  //node i has a priority at least as high as its children
				return;
		}
	}

/**
 * @param p the position of the element to update
 * @param element the replacement element
 * <BR> 
 * REQUIRES: 
 *  0 &le; p &lt; <code>size</code>
**/

	protected void update(int p, E element){
		bh.set(p,element);    //update the element at position p
		if (comp.compare(element, bh.get(parent(p))) > 0) 
			fixUpward(p);
		else 
			fixDownward(p);
	}

/**
 * @param element the element to add
**/

	public void add(E element) {
		bh.addLast(element);       //adds element and updates size
		fixUpward(bh.getSize()-1); //new element at position size-1
	}

/**
 * @param element the element
 * to add
 * @return a tracker to the newly added element
 * @throws UnsupportedOperationException tracking is not being
 * supported, as specified in the constructor call
**/

	public PriorityQueueLocator<E> addTracked(E element) {
		if (!tracked)
			throw new UnsupportedOperationException();
		PositionalCollectionLocator<E> t = ((TrackedArray<E>) bh).addTracked(element);
		fixUpward(bh.getSize()-1); 
		return new BinaryHeapLocator(t); 
	}

/**
 * @param c the collection to be added to this collection
**/

	public void addAll(Collection<? extends E> c) {
		bh.addAll(c);
		for (int i = (bh.getSize()/2 - 1); i >= 0; i--)
			fixDownward(i);
	}

/**
 * @return the highest priority
 * element from the collection
 * @throws NoSuchElementException called on an empty collection.
**/

	public E extractMax() {
		bh.swap(0, bh.getSize()-1); //swap root with last element
		E data = bh.removeLast();  //remove max priority element
		fixDownward(0);           //restore HeapOrdered property
		return data;			  //return max priority element
	}

/**
 * @param p the position
 * of the element to remove
 * <BR> 
 * REQUIRES: 
 *  0 &le; p &lt; <code>size</code>
**/

	protected void remove(int p) {
		E removed = bh.get(p);
		int lastPos = getSize()-1;
		if (p == lastPos)
			bh.removeLast();
		else {
			bh.swap(lastPos, p);   //move element to remove to last position
			bh.removeLast();      //and remove it
			if (comp.compare(bh.get(p),removed) >= 0) //priority at pos p least as high
				fixUpward(p);
			else                                     //priority at pos p decreases
				fixDownward(p);
			version.increment(); //invalidate all locators
		}
	}

/**
 * Removes from the collection
 * an arbitrary element (if any) equivalent to the target.
 * @param target the element to remove
 * @return <code>true</code> if an element was removed, and <code>false</code> otherwise.
**/

	public boolean remove(E target) {
		int p = find(target); //find index of equivalent value
		if (p == NOT_FOUND)
			return false;
		else {
			remove(p);
			return true;
		}
	}

/**
 * Removes all elements from the collection
**/

	public void clear(){
		bh.clear();
	}

/**
 * Updates the current collection to contain only
 * elements that are also in <code>c</code>
 * @param c a collection
**/

	public void retainAll(Collection<E> c) {
		bh.retainAll(c);
		for (int i = (bh.getSize()/2 - 1); i >= 0; i--)
			fixDownward(i);
	}

/**
 * Creates a new locator that is at FORE.
**/

	public PriorityQueueLocator<E> iterator() {
		return new BinaryHeapLocator(bh.iterator());
	}

/**
 * This method runs in worst-case linear time.
 * @param element the target
 * @return a locator positioned at the given element.
 * @throws NoSuchElementException there is no equivalent element
 * in this collection.
**/

	public PriorityQueueLocator<E> getLocator(E element){
		return new BinaryHeapLocator(
				(PositionalCollectionLocator<E>) bh.getLocator(element));
	}


	public class BinaryHeapLocator extends AbstractCollection<E>.AbstractLocator<E>
		implements PriorityQueueLocator<E> {

		PositionalCollectionLocator<E> loc;   //wrapped locator

/**
 * @param loc a reference to the node to place the locator.
**/

		protected BinaryHeapLocator(PositionalCollectionLocator<E> loc){
			this.loc = loc;
		}

/**
 * @return true  if and only if the element at the locator
 * is in the collection.
**/

		public boolean inCollection() {
			return loc.inCollection();
		}

/**
 * @return the element at the locator position
 * @throws NoSuchElementException locator is not at an element in
 * the collection.
**/

		public E get() {
			return loc.get();
		}

/**
 * Moves the locator to the next element in the iteration order (or {\texttt AFT} if
 * it is currently at the last element).
 * @return true  if and only if after the update, the locator is at
 * an element of the collection.
 * @throws AtBoundaryException the locator is at AFT since there is
 * no place to advance.
**/

		public boolean advance() {
			return loc.advance();
		}

/**
 * Moves the locator to the previous element in the iteration order (or FORE if
 * it is currently at the first element).
 * @return true  if and only if after the update, the locator is at an element of the collection.
 * @throws AtBoundaryException the locator is at FORE since then there is
 * no place to retreat.
**/

		public boolean retreat() {
			return loc.retreat();
		}

/**
 * @return true if and only if advancing it would leave the locator at an element in
 * the collection
**/

		public boolean hasNext() {
			return loc.hasNext();
		}

/**
 * @param element the new element to replace the
 * one at the locator position
**/

		public void update(E element) {
			int p = loc.getCurrentPosition();
			BinaryHeap.this.update(p,element);
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
			int p = loc.getCurrentPosition();
			BinaryHeap.this.bh.set(p,element);   
			BinaryHeap.this.fixUpward(p);
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
			int p = loc.getCurrentPosition();
			BinaryHeap.this.bh.set(p,element);   
			BinaryHeap.this.fixDownward(p);
		}

/**
 * Removes the element at the locator position, and updates the locator
 * to be
 * at the element in the iteration order that preceded the one removed.
 * @throws NoSuchElementException called on a locator at FORE or AFT
**/

		public void remove() {
			int p = loc.getCurrentPosition();
			BinaryHeap.this.remove(p);
		}


	}
}
