// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import goldman.collection.AbstractCollection;
import goldman.collection.AtBoundaryException;
import goldman.collection.Bucketizer;
import goldman.collection.Digitizer;
import goldman.collection.Tracked;
import goldman.collection.priority.PriorityQueue;
/**
 * This array-based data structure can wrap any of the other
 * array-based data structures to create a tracked implementation of the wrapped
 * data structure.   In the provided
 * implementation, TrackedArray wraps DynamicCircularArray.
 * However, the provided implementation could easily be changed to use
 * any of the other array-based data structures.
 * Since the maintenance
 * of the trackers requires some additional structure as compared to that needed for
 * a marker, a tracked array should be used only if it is
 * important to be able to track elements.
 * Observe that the tracker does not reduce the cost of shifting array elements when an
 * element is added or removed, even through a tracker.  The tracker would be useful
 * when the application needs to determine the position of a tracked element in
 * constant time.
**/

public class TrackedArray<E> extends DynamicCircularArray<E> implements
	PositionalCollection<E>, Tracked<E> {

		static final Node FORE_NODE = new Node(-1, null); 
		static final Node AFT_NODE = new Node(-1, null);


	static class Node<E> {
		int index;	    //satisfies, a[node.index] = node for underlying array a
		Node<E> redirect;  //head pointer for redirect chain, or null if in the collection
		E data;      //the element held in this node

/**
 * @param index the index in the underlying array
 * @param element the element to hold in the node
**/

		Node(int index, E element) {
			this.index = index;
			data = element;
			redirect = null;
		}
	}

/**
 * Creates a tracked array with the given capacity that uses
 * the provided equivalence tester
 * @param capacity the desired initial capacity for the underlying array
 * @param equivalenceTester a user-provided equivalence tester
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public TrackedArray(int capacity, Comparator<? super E> equivalenceTester) {
		super(capacity, equivalenceTester);
	}

/**
 * Creates an underlying
 * array <code>a</code> with a default initial capacity.
**/

	public TrackedArray(){
		this(DEFAULT_CAPACITY);
	}

/**
 * @param capacity the initial capacity for the underlying array <code>a</code>
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public TrackedArray(int capacity){
		super(capacity);
	} 

/**
 * @param p a valid position
 * <BR> 
 * REQUIRES: 
 *  <code>p</code> is a valid position
 * @return the element held at user position <code>p</code>
**/

	@SuppressWarnings("unchecked")

	protected E readElement(int p) {
		return ((Node<E>) read(p)).data;
	}

/**
 * @param p a valid position
 * @param element the element to write at position <code>p</code>
 * <BR> 
 * REQUIRES: 
 *  <code>p</code> is a valid position.
 * @return the element that was previously at position <code>p</code>
**/

	@SuppressWarnings("unchecked")

	protected E writeElement(int p, E element) {
		Node<E> t = (Node<E>) read(p);
		E oldElement = t.data;
		t.data = element;
		return oldElement;
	}

/**
 * @param p a valid user position
 * @return the element at the position <code>p</code>
 * @throws PositionOutOfBoundsException the position is not valid
**/

	public E get(int p) {
		if (p < 0 || p > size - 1)
			throw new PositionOutOfBoundsException(p);
		return readElement(p);
	}

/**
 * @param element the target
 * @return the first position <code>p</code> in the collection holding
 * an equivalent element, or <code>NOT_FOUND</code> if there is no equivalent
 * element in the collection
**/

	protected int findPosition(E element) {
		for (int p = 0; p < size; p++)
			if (equivalent(element,readElement(p)))
				return p;
		return NOT_FOUND;
	}
	

/**
 * @param i the starting index that needs updating
 * @param num the number of nodes to update starting at <code>i</code>,
 * possibly wrapping
**/

	protected void updateNodes(int i, int num) {
		for (int m = 0; m < num; m++){
			((Node) a[i]).index = i;
			i = nextIndex(i);
		}
	}

/**
 * Shifts the elements held in <code>p1, ..., p2</code> (possibly wrapped) left
 * <code>num</code> slots
 * @param p1 the starting position to shift
 * @param p2 the ending position for the shift
 * @param num the number of slots to shift
 * <BR> 
 * REQUIRES: 
 *  <code>p1</code> &le; <code>p2</code>, and
 * 0 &lt; <code>num</code> &le; <code>a.length</code> - (<code>p2</code> - <code>p1</code> + 1)
 * (the amount of the shift is at least 1, and is not so much that
 * the portion of the array being shifted would intersect with itself).
**/

	protected void shiftLeft(int p1, int p2, int num) {
		super.shiftLeft(p1, p2, num);	// perform the shift
		int i = getIndex(p1) - num;
		if (i < 0)
			i += a.length;
		updateNodes(i, p2-p1+1);
	}

/**
 * Shifts the elements held in <code>p1, ..., p2</code> (possibly wrapped) right
 * <code>num</code> slots.
 * @param p1 a valid position
 * @param p2 a valid position
 * @param num the number of positions to shift
 * <BR> 
 * REQUIRES: 
 * that
 * <code>p1</code> &le; <code>p2</code>, and
 * 0 &lt; <code>num</code> &le; <code>a.length</code> - (<code>p2</code> -<code>p1</code> + 1)
 * (the amount to shift is at least 1, and
 * the portion of the array being shifted does intersect with itself)
**/

	protected void shiftRight(int p1, int p2, int num) {
		super.shiftRight(p1, p2, num); // perform the shift
		updateNodes(getIndex(p1+num),p2-p1+1);
	}

/**
 * Changes the size of the underlying array
 * to <code>desiredCapacity</code> while maintaining the same positional collection.
 * @param desiredCapacity the size to make the underlying array
 * @throws IllegalArgumentException executing it would
 * make the array capacity too small to hold the current collection
**/

	protected void resizeArray(int desiredCapacity) {
		super.resizeArray(desiredCapacity);
		updateNodes(0,size);
	}

/**
 * @param p a valid user position to be updated
 * @param element the element to put at position <code>p</code>
 * @return the prior element at position <code>p</code>
 * @throws PositionOutOfBoundsException <code>p</code> is not
 * a valid position
**/

	public E set(int p, E element) {
		if (p < 0 || p > size - 1)
			throw new PositionOutOfBoundsException(p);
		return writeElement(p, element);
	}

/**
 * Swaps the elements held in positions <code>pos1</code> and <code>pos2</code>
 * @param pos1 a valid position
 * @param pos2 a valid position
**/

	void swapImpl(int pos1, int pos2) {
		super.swapImpl(pos1,pos2);  //swaps and invalidates locators for iteration
		int index1 = getIndex(pos1);
		int index2 = getIndex(pos2);
		((Node) a[index1]).index = index1;    //preserve SelfReference
		((Node) a[index2]).index = index2;	  //preserve SelfReference
	}

/**
 * @param fromPos the current position of the
 * element to move
 * @param toPos the position where the element is to be moved.
 * <BR> 
 * REQUIRES: 
 *  <code>fromPos</code> and <code>toPos</code> are both legal positions
**/

	void move(int fromPos, int toPos) {
		int toIndex = getIndex(toPos);
		a[toIndex] = a[getIndex(fromPos)];    //move the node
		((Node) a[toIndex]).index = toIndex;  //preserve SelfReference
		version.increment();   //invalidate all trackers for iteration
	}

/**
 * @param p a valid position
 * @param x the node to place at position <code>p</code>
 * <BR> 
 * REQUIRES: 
 *  <code>p</code> is a legal position
**/

	void put(int p, Object x) {
		int i = getIndex(p);
		a[i] = x;                  //put x at given position
		((Node) a[i]).index = i;   //preserve SelfReference
	}

/**
 * Inserts <code>element</code> at position <code>p</code>,
 * incrementing the position for the elements that were at
 * positions <code>p</code> to <code>size-1</code>
 * @param p a valid position
 * @param element the element to insert
 * @return a tracker for the new element.
**/

	public PositionalCollectionLocator<E> addImpl(int p, Object element) {
		Node newNode = new Node(-1,element);  //temporarily put -1 for the index
		int count = version.getCount();
		super.addImpl(p, newNode);
		newNode.index = getIndex(p);          //preserve SelfReference
		version.restoreCount(count);          //don't need to invalidate locators
		return new Tracker(newNode);
	}

/**
 * Inserts <code>value</code> it at the end of the collection.
 * @param value the new element
**/

	public PositionalCollectionLocator<E> addTracked(E value) {
		return addImpl(size, value); //adds to end
	}

/**
 * Inserts the new element at position <code>p</code>
 * and increments the position number for the elements that were at
 * positions <code>p</code>, ..., <code>size</code>-1.
 * @param p a valid user position
 * @param value the new element
 * @throws PositionOutOfBoundsException <code>p</code> is neither <code>size</code>
 * nor a valid position
**/

	public PositionalCollectionLocator<E> addTracked(int p, E value) {
		return addImpl(p, value);
	}

/**
 * Removes <code>a[i]...a[j]</code> from the collection moving
 * the elements at positions <code>j+1</code> to <code>size-1</code> to the left
 * by <code>j-1+1</code> positions.
 * @param fromPos the first position to be removed
 * @param toPos the last position to be removed
 * @throws PositionOutOfBoundsException either of the arguments
 * is not a valid position
 * @throws IllegalArgumentException <code>fromPos</code> is greater
 * than <code>toPos</code>
**/

	public void removeRange(int fromPos, int toPos) {
		if (fromPos < 0 || toPos >= size)
			throw new PositionOutOfBoundsException();
		if (fromPos > toPos)
			throw new IllegalArgumentException();
		Node predecessor = FORE_NODE;  //predecessor for all removed elements
		if (fromPos > 0)
			predecessor = (Node) a[getIndex(fromPos - 1)];		
		for (int p = fromPos; p <= toPos; p++)   //preserve RedirectChain
			((Node) a[getIndex(p)]).redirect = predecessor;
		int count = version.getCount();
		super.removeRange(fromPos,toPos);
		version.restoreCount(count);  //do not need to invalidate locators
	}

/**
 * Removes all elements from the collection.
**/

	public void clear() {
		if (!isEmpty())
			removeRange(0,getSize()-1);
	}

/**
 * @return a new
 * tracker that is initialized to  be logically just
 * before the first element in the collection.
**/

	public PositionalCollectionLocator<E> iterator() {
		return new Tracker(FORE_NODE);
	}	

/**
 * @return a new tracker that is initialized to
 * be logically just after the last element in the collection
**/

	public PositionalCollectionLocator<E> iteratorAtEnd() {
		return new Tracker(AFT_NODE);
	}	

/**
 * Returns a new tracker that is at the given position.
 * @param pos the user position of an element
 * @throws NoSuchElementException the given position is not a valid user position.
**/

	public PositionalCollectionLocator<E> iteratorAt(int pos) {
		if (pos < 0 || pos >= size)
			throw new NoSuchElementException();
		return new Tracker((Node) read(pos));
	}

/**
 * @param value the target
 * @return a tracker initialized to the position of the first
 * element in the collection equivalent to <code>value</code>
 * @throws NoSuchElementException <code>value</code> does not occur in the collection
**/

		public PositionalCollectionLocator<E> getLocator(E value) {
			int position = findPosition(value);
			if (position == NOT_FOUND)
				throw new NoSuchElementException();
			return new Tracker((Node) read(position));
		}

/**
 * @param comp the comparator to use for the elements
 * @return a comparator defined over nodes for use by the
 * sorting algorithms
**/

	private Comparator getSorter(final Comparator<? super E> comp){
		return new Comparator<Node<E>>() {	  //anonymous comparator over nodes
			public int compare(Node<E> a, Node<E> b) {
				return comp.compare(a.data, b.data); 
			}
		};
	}

/**
 * @param pq the
 * priority queue instance to which all elements are to be placed
**/

	protected  void buildPriorityQueue(PriorityQueue<Object> pq) {
		for (int i = 0; i < getSize(); i++)
			pq.add(a[getIndex(i)]);
	}


	public void insertionsort(Comparator <? super E> comp) {
		insertionsortImpl(getSorter(comp));
	}


	public void mergesort(Comparator <? super E> comp) {
		mergesortImpl(getSorter(comp));
	}


	public void heapsort(Comparator <? super E> comp) {
		heapsortImpl(getSorter(comp));
	}


	public void treesort(Comparator <? super E> comp) {
		treesortImpl(getSorter(comp));
	}


	public void quicksort(Comparator <? super E> comp) {
		super.quicksort(getSorter(comp));
	}


	public E repositionElementByRank(int i, Comparator <? super E> comp) {
		return (E) super.repositionElementByRank(i, getSorter(comp));
	}

/**
 * @param digitizer the digitizer to use for the elements
 * @return a digitizer defined over nodes for use by
 * radix sort
**/

	private Digitizer getNodeDigitizer(final Digitizer<? super E> digitizer) {
		return new Digitizer<Node<E>>() {  //anonymous node digitizer class
			public int getDigit(Node<E> x, int place) {
				return digitizer.getDigit((x.data),place);
			}
			public int getBase() {
				return digitizer.getBase();
			}
			public boolean isPrefixFree(){
				return digitizer.isPrefixFree();
			}
			public int numDigits(Node<E> x) {
				return digitizer.numDigits(x.data);
			}


			public String formatDigit(Node<E> x, int place) {
				return digitizer.formatDigit(x.data, place);
			}

		};
	}


	public void radixsort(Digitizer <? super E> digitizer) {
		radixsortImpl(getNodeDigitizer(digitizer));
	}

/**
 * @param bucketizer the bucketizer to use for the elements
 * @return a bucketizer defined over nodes for use by
 * bucket sort.
**/

	private  Bucketizer getNodeBucketizer(final Bucketizer<? super E> bucketizer) {
		return new Bucketizer<Node<E>>() {
			public int getBucket(Node<E> x) {
				return bucketizer.getBucket(x.data);
			}
			public int getNumBuckets() {
				return bucketizer.getNumBuckets();
			}
			public int compare(Node<E> a, Node<E> b) {
				return comp.compare(a.data, b.data);
			}
		};
	}


	public void bucketsort(Bucketizer <? super E> bucketizer) {
		bucketsortImpl(getNodeBucketizer(bucketizer));
	}


	protected class Tracker extends AbstractCollection<E>.AbstractLocator<E> 
		implements PositionalCollectionLocator<E> {

		protected Node<E> trackedNode;

/**
 * @param toTrack the node to track.
 * @throws IllegalArgumentException the property TracksElement would be violated
**/

		protected Tracker(Node<E> toTrack) {
			if (toTrack == null)
				throw new IllegalArgumentException();
			this.trackedNode = toTrack;
		}

/**
 * @return true  if and only if
 * the tracked element is currently in the collection
**/

		public boolean inCollection(){
			if (trackedNode == FORE_NODE || trackedNode == AFT_NODE)
				return false;
			return trackedNode.redirect == null;
		}

/**
 * @return the tracked element
 * @throws NoSuchElementException the tracker is not at an element
 * in the collection.
**/

		@SuppressWarnings("unchecked")

		public E get() {
			if (!inCollection())
				throw new NoSuchElementException();
			return trackedNode.data;
		}

/**
 * @param element the element to store at the current tracker location.
 * @return the element that had been stored at the tracker location
 * @throws NoSuchElementException the tracker is not at an element in
 * the collection
**/

		@SuppressWarnings("unchecked")

		public E set(E element) {
			if (!inCollection())
				throw new NoSuchElementException();
			E oldElement = trackedNode.data;
			trackedNode.data = element;
			return oldElement;
		}

/**
 * @return the position of the tracker.
 * @throws NoSuchElementException the tracker is not at an
 * element in the collection
**/

		public int getCurrentPosition() {
			if (!inCollection())
				throw new NoSuchElementException();
			return getPosition(trackedNode.index);
		}

/**
 * @param ptr reference to a node
 * @return the first element still in the
 * collection that is reached when following the <code>redirect</code>
 * pointers.
**/

		private Node<E> skipRemovedElements(Node<E> ptr) {
			if (ptr.redirect == null) 
				return ptr;
			if (ptr.redirect.redirect != null) //for efficiency
				ptr.redirect = skipRemovedElements(ptr.redirect);
			return ptr.redirect;
		}

/**
 * <BR> 
 * REQUIRES: 
 *  that <code>trackedNode</code> is not at <code>AFT_NODE</code>
 * @return a reference
 * to the next node in the collection
**/

		private Node<E> nextCollectionElement() {
			if (trackedNode.redirect != null)  //tracked node not in collection
				trackedNode = skipRemovedElements(trackedNode);		
			if ((trackedNode == FORE_NODE && size==0) || 
					trackedNode.index == getIndex(size-1))
				return AFT_NODE;
			if (trackedNode == FORE_NODE)
				return (Node<E>) a[getIndex(0)];
			else
				return (Node<E>) a[nextIndex(trackedNode.index)];
		}

/**
 * <BR> 
 * REQUIRES: 
 * <code>trackedNode</code> is not at <code>FORE_NODE</code>
 * @return a reference
 * to the previous node in the collection
**/

		Node<E> prevCollectionElement() {
			if ((trackedNode == AFT_NODE && size == 0) || 
				(trackedNode.redirect == null && trackedNode.index == getIndex(0)))
				return FORE_NODE;
			if (trackedNode == AFT_NODE)
				return (Node<E>) a[getIndex(size-1)];
			if (trackedNode.redirect == null)  
				return (Node<E>) a[prevIndex(trackedNode.index)];
			else
				return skipRemovedElements(trackedNode);
		}

/**
 * Moves the tracker to the next position
 * @return true  if and only if the tracker has not reached AFT
 * @throws AtBoundaryException the tracker is at AFT, since there is
 * no place to advance.
**/

		public boolean advance() throws ConcurrentModificationException  {
			if (trackedNode == AFT_NODE)
				throw new AtBoundaryException("Already at end.");
			checkValidity();   //throw ConcurrentModificationException if tracker invalidated
			trackedNode = nextCollectionElement();
			return trackedNode != AFT_NODE;
		}

/**
 * Moves the tracker to the previous position
 * @return true  if and only if the tracker has not reached FORE.
 * @throws AtBoundaryException the tracker is at FORE, since then there is
 * no place to retreat.
**/

		public boolean retreat() throws ConcurrentModificationException {
			if (trackedNode == FORE_NODE)
				throw new AtBoundaryException("Already before front.");
			checkValidity();  //throw ConcurrentModificationException if tracker invalidated
			trackedNode = prevCollectionElement();
			return trackedNode != FORE_NODE;
		}

/**
 * @return true  if there is some element after the current tracker position.
**/

		public boolean hasNext() throws ConcurrentModificationException {
			checkValidity();  //throw ConcurrentModificationException if tracker invalidated
			return (trackedNode != AFT_NODE && nextCollectionElement() != AFT_NODE);
		}

/**
 * Adds to the collection after the tracker location
 * If the tracker is between positions <code>p-1</code>, and <code>p</code> then
 * the new element is inserted at position <code>p</code>.
 * @param e the element to be added
 * @return a tracker for the new element
 * @throws AtBoundaryException the tracker is at AFT
**/

		public PositionalCollectionLocator<E> addAfter(E e) {
			int pos;  //position to add new element
			if (trackedNode.redirect ==  null)
				pos = getPosition(trackedNode.index)+1;
			else {
				Node t = prevCollectionElement();
				if (t == FORE_NODE)
					pos = 0;
				else 
					pos = getPosition(t.index) + 1;
			}
			return addImpl(pos,e);
		}

/**
 * Removes the element at the tracker and updates the tracker to be between
 * its current position and the one before it.
 * @throws NoSuchElementException the tracker is not at a valid position
 * in the collection
**/

		public void remove() {
			if (!inCollection())
				throw new NoSuchElementException();
			TrackedArray.this.remove(getPosition(trackedNode.index));
		}

/**
 * @param tracker the tracker to be checked for equality
 * @return true  if the given tracker refers to the same element in this collection
**/

		public boolean equals(Object tracker) {
			return ((tracker instanceof TrackedArray.Tracker) &&
					trackedNode == ((TrackedArray.Tracker) tracker).trackedNode);
		}
	}
}
