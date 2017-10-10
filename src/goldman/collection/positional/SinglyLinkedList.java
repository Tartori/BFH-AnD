// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import goldman.Objects;
import goldman.ReverseComparator;
import goldman.collection.AbstractCollection;
import goldman.collection.AtBoundaryException;
import goldman.collection.Bucketizer;
import goldman.collection.Digitizer;
import goldman.collection.Locator;
import goldman.collection.Tracked;
import goldman.collection.Visitor;
import goldman.collection.ordered.RedBlackTree;
import goldman.collection.priority.BinaryHeap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
/**
 *  The simplest of the list-based positional collections, SinglyLinkedList
 * maintains a linked list where each list node only references the
 * next element in the list.  Our implementation also maintains a reference to the last item in
 * the list.  It is a tracked implementation.
 * When using a locator to iterate through the collection, in constant time a new
 * element can be added or the next element in the collection can be removed.
 * The primary drawback of using a singly linked list is that it takes O(p) time
 * to locate the element at position p for 0 &le; p &le; n-2.
 * Another weakness of SinglyLinkedList is that
 * it takes O(p) time to find the element that precedes the element at position p.  Hence, the
 * <code>retreat</code> method cannot be efficiently implemented.
 * Moreover, in order to efficiently remove a tracked
 * element from position p of the collection, it takes O(p) time.
**/

public class SinglyLinkedList<E> extends AbstractPositionalCollection<E> implements Tracked<E> {

	protected ListItem<E> head; // sentinel head ListItem
	private ListItem<E> last; // reference to last item


	static final Object REMOVED = new Object(); // singleton for a removed item

/**
 * The <code>ListItem</code> interface must be supported by any
 * class defining objects to be used in a singly linked list or a
 * doubly linked list.
**/

	public static class ListItem<E> {

		Object data;
		ListItem<E> next;

/**
 * @param data the element held in the list item
**/

		ListItem(Object data) {this.data = data;}


		public String toString(){return "" + data;}
/**
 * @param next the value to
 * update the <code>next</code> reference
**/

		protected void setNext(ListItem<E> next) {this.next = next;}

/**
 * Moves the list item after
 * this one so that it immediately follows the destination
 * @param destination a reference to a list item
**/

		protected boolean moveNextAfter(ListItem<E> destination) {
			if (destination != next && destination != this) {
				ListItem<E> temp = next;
				this.setNext(temp.next);
				temp.setNext(destination.next);
				destination.setNext(temp);
				return true;
			}
			return false;
		}


	}
/**
 * Creates an empty collection
**/

	public SinglyLinkedList() {
		initialize();
	}

/**
 * @param value the element to insert
 * @return a new list item with the given value
**/

	ListItem<E> newListItem(E value) {
		return new ListItem<E>(value);
	}

/**
 * Updates this collection to be empty
**/

	void initialize(){
		head = newListItem(null);
		head.setNext(getTail());
		last = head;
		size = 0;
	}	


    ListItem<E> getTail() {
    		return null;
    }

/**
 * @return a reference to the
 * list item for the last element in the collection.
**/

    ListItem<E> getLast() {
    	return last;
    }

/**
 * @param ptr a reference to a list item
 * <BR> 
 * REQUIRES: 
 *  <code>ptr</code> references
 * a reachable list item.
 * @return a reference to the previous list item
**/

	ListItem<E> getPtrForPrevElement(ListItem<E> ptr){
		ListItem<E> loc = head;
		while (loc != getTail() && loc.next != ptr)
			loc = loc.next;
		return loc;	
	}

/**
 * @param p a valid user position or -1
 * @return a reference to the element in the collection at
 * position <code>p</code> or the head sentinel if <code>p = -1</code>
**/

	protected ListItem<E> getPtr(int p){
		if (p == size - 1)  //special case to  efficiently retrieve the last item
			return getLast();
		ListItem<E> current = head;
		for (int i = 0; i <= p; i++)
			current = current.next;
		return current;
	}

/**
 * @param p a valid user position
 * @return the object at the position <code>p</code>
 * @throws PositionOutOfBoundsException the p is not a valid position
**/

	@SuppressWarnings("unchecked")

	public E get(int p) {
		if (p < 0 || p >= size)
			throw new PositionOutOfBoundsException(p);
		return (E) getPtr(p).data;
	}

/**
 * @param value the target
 * @return a reference to the first reachable list item
 * that immediately precedes one holding an element equivalent to <code>value</code>.
 * The method returns null, if there is no equivalent element.
**/

	@SuppressWarnings("unchecked")

	protected ListItem<E> getPtrForPrevElement(E value) {
		ListItem<E> loc = head;
		while (loc.next != getTail()) {
			if (equivalent(value, (E) loc.next.data))
				return loc;
			loc = loc.next;
		}
		return null;
	}

/**
 * @param value the target
 * @return true  if and only if an element equivalent to <code>value</code> exists in the collection
**/

	public boolean contains(E value) {
		return getPtrForPrevElement(value) != null;
	}

/**
 * @param value the target
 * @return the position in the collection for the first occurrence
 * (if any) of an element equivalent to <code>value</code>.
 * @throws NoSuchElementException no element in the collection is equivalent
 * to <code>value</code>
**/

	@SuppressWarnings("unchecked")

	public int positionOf(E value) {
		ListItem<E> ptr; // reference to ListItem currently at in traversal
		int pos; //position in the collection of ptr.data
		for (pos = 0, ptr = head.next; ptr != getTail(); pos++, ptr = ptr.next)
			if (equivalent(value, (E) ptr.data))
				return pos;
		throw new NoSuchElementException();
	}

/**
 * @param last the list element that is last in the collection.
**/

    protected void setLast(ListItem<E> last) {
    	this.last = last;
    }

/**
 * @param p a valid user position
 * @param value the element to put at position <code>p</code>
 * @return the prior element at position <code>p</code>
 * @throws PositionOutOfBoundsException <code>p</code> is not
 * a valid position
**/

	@SuppressWarnings("unchecked")

	public E set(int p, E value) {
		if (p < 0 || p >= size)
			throw new PositionOutOfBoundsException(p);
		ListItem<E> ptr = getPtr(p);
		E oldData = (E) ptr.data;
		ptr.data = value;
		return oldData;
	}

/**
 * @param aPrev a reference to list item before the one
 * to be swapped
 * @param bPrev a reference to the list item before the other one
 * to be swapped
**/

	protected void swapAfter(ListItem<E> aPrev, ListItem<E> bPrev) {
		if (aPrev != bPrev) {
			ListItem<E> aPtr = aPrev.next;  //one list item to be swapped
			ListItem<E> aNext = aPtr.next;  //   and the list item that follows it
			ListItem<E> bPtr = bPrev.next;  //other element to be swapped
			ListItem<E> bNext = bPtr.next;  //   and the list item that follows it
			if (bPtr == aPrev)   //special case if ``a'' immediately precedes ``b''
				aPtr.setNext(bPtr);
			else {
				aPtr.setNext(bNext);
				aPrev.setNext(bPtr);
			}
			if (aPtr == bPrev)  //special case if ``b'' immediately precedes ``a''
				bPtr.setNext(aPtr);
			else {
				bPtr.setNext(aNext);
				bPrev.setNext(aPtr);
			}
			if (last == aPtr)  //preserve last
				last = bPtr;
			else if (last == bPtr)
				last = aPtr;
			version.increment();   //invalidate all active trackers as iterators
		}
	}

/**
 * Swaps the elements held in positions <code>a</code> and <code>b</code>
 * @param a a valid position
 * @param b a valid position
 * @throws PositionOutOfBoundsException either <code>a</code> or <code>b</code>
 * is not a valid position
**/

	public void swap(int a, int b) {
		if (a < 0 || a >= size || b < 0 || b >= size)
			throw new PositionOutOfBoundsException();
		ListItem<E> aPrev = getPtr(a-1);		
		ListItem<E> bPrev = getPtr(b-1);
		swapAfter(aPrev,bPrev);
	}

/**
 * Inserts a list item holding the new element after
 * the list item  referenced by <code>ptr</code>
 * @param ptr a reference to a list
 * item in the collection (possibly the sentinel head)
 * @param value the element to insert
 * @return a tracker to the new element
**/

	protected PositionalCollectionLocator<E> insertAfter(ListItem<E> ptr, E value) {
		ListItem<E> newItem = newListItem(value);
		newItem.setNext(ptr.next);
		ptr.setNext(newItem);
		if (last == ptr)  //preserve Last 
			last = newItem;
		size++;           //preserve Size
		return new Tracker(newItem);
	}

/**
 * Inserts <code>value</code> at position <code>p</code>
 * and increments the position number for the elements that were at
 * positions <code>p</code> to <code>size-1</code>.
 * @param p the position where the element is to be placed
 * @param value the element to insert
 * @throws PositionOutofBoundsException <code>p</code> is neither <code>size</code>
 * nor a valid position
**/

	public void add(int p, E value) {
		if (p < 0 || p > size)
			throw new PositionOutOfBoundsException(p);
		insertAfter(getPtr(p-1),value);
	}

/**
 * Inserts it at the end of
 * the positional collection.
 * @param value the new element
**/

	public void add(E value) {
		insertAfter(getLast(), value);		
	}

/**
 * Inserts <code>value</code> into the collection
 * at an arbitrary position.
 * @param value the new element
 * @return a tracker that tracks the new element
**/

	public Locator<E> addTracked(E value) {
		return insertAfter(getLast(),value);
	}

/**
 * @param ptr a reference to the list item (possibly the sentinel head item) preceding
 * the one to remove
 * <BR> 
 * REQUIRES: 
 *  <code>ptr</code> references <code>head</code> or a reachable
 * list item.
 * @return the removed element
 * @throws NoSuchElementException <code>ptr</code> references the last list item
**/

	@SuppressWarnings("unchecked")

	protected E removeNext(ListItem<E> ptr) {
		if (ptr.next == getTail())
			throw new NoSuchElementException();
		if (last == ptr.next)     //preserve Last
			last = ptr;
		ListItem<E> x = ptr.next;
		ptr.setNext(x.next);      //remove the list item after ptr
		size--;                   //preserve Size
		E result = (E) x.data;
		x.data = REMOVED;         //preserve Removed
		return result;
	}

/**
 * Requires 0 &le; <code>fromPos</code> &le; <code>toPos</code> &lt; <code>size</code>.
 * It removes the elements at positions
 * <code>fromPos</code>, ..., <code>toPos</code>, inclusive,
 * from the collection and
 * decrements the positions of the
 * elements at positions <code>toPos+1</code> to <code>size-1</code> by
 * <code>toPos-fromPos+1</code> (the number of elements
 * being removed)
 * @param fromPos a valid position
 * @param toPos a valid position
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
		ListItem<E> beforeFromPos = getPtr(fromPos-1);
		ListItem<E> ptr = beforeFromPos.next;
		for (int i = fromPos; i<= toPos; i++, ptr = ptr.next) 
			ptr.data = REMOVED;   //mark items as removed
		beforeFromPos.setNext(ptr);      //remove them
		size = size - (toPos - fromPos + 1);  //preserve Size
		if (last.data == REMOVED)             //preserve Last
			last = beforeFromPos;
	}

/**
 * Removes the element at position <code>p</code> and decrements
 * the positions of
 * u_{p+1}, ..., u_{<code>size</code>-1} by one.
 * @param p a valid position
 * @return the removed element
 * @throws PositionOutOfBoundsException <code>p</code> is not a valid
 * position
**/

	public E remove(int p) {
		if (p < 0 || p >= size)
			throw new PositionOutOfBoundsException(p);
		ListItem<E> ptr = getPtr(p-1);
		return removeNext(ptr);
	}

/**
 * Removes the element at position 0 and decrements the position
 * for the elements that were
 * at positions <code>1</code> to <code>size-1</code>.
 * @return the removed element
 * @throws NoSuchElementException the collection is empty
**/

	public E removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException("collection is empty");
		return removeNext(head);
	}

/**
 * Removes the element at position <code>size-1</code>
 * @return the removed element
 * @throws NoSuchElementException the collection is empty
**/

	public E removeLast() {
		if (isEmpty())
			throw new NoSuchElementException("collection is empty");
		return remove(size-1);	
	}

/**
 * Removes the first element in the collection equivalent to
 * <code>value</code>
 * @param value the element to remove
 * @return true  if and only if an element is removed.
**/

	public boolean remove(E value) {
		ListItem<E> ptr = getPtrForPrevElement(value);
		if (ptr == null)
			return false;
		removeNext(ptr);
		return true;
	}

/**
 * Removes all elements from the collection.
**/

	public void clear() {
		ListItem<E> tail = getTail();
		for (ListItem<E> ptr = head.next; ptr != tail; ptr = ptr.next)
			ptr.data = REMOVED;
		size = 0;
		head.setNext(tail);
		last = head;
	}	

/**
 * Creates a new marker that is at FORE.
**/

	public PositionalCollectionLocator<E> iterator() {
		return new Tracker(head);
	}

/**
 * Creates a new marker that is at AFT.
**/

	public PositionalCollectionLocator<E> iteratorAtEnd() {
		return new Tracker(getTail());
	}

/**
 * Returns a new marker that is at the given position.
 * @param pos the user position of an element
 * @throws NoSuchElementException the given position is not a valid user position.
**/

	public PositionalCollectionLocator<E> iteratorAt(int pos){
		ListItem<E> ptr = getPtr(pos);
		if (ptr == null)
			throw new NoSuchElementException();
		return new Tracker(ptr);
	}

/**
 * @param element the element to track
 * @return a tracker that has been initialized
 * at the given element.
 * @throws NoSuchElementException the given element is not in the collection.
**/

	public PositionalCollectionLocator<E> getLocator(E element){
		ListItem<E> ptr = getPtrForPrevElement(element);
		if (ptr == null)
			throw new NoSuchElementException();
		return new Tracker(ptr.next);
	}

/**
 * @param x the item to be placed at the end of the list
**/

	protected void addItemLast(ListItem<E> x) {
		x.setNext(getTail());  //preserves EndsAtTail
		last.setNext(x);       //places x after last item
		last = x;            //preserves Last
	}


	public void insertionsort() {
		insertionsort(Objects.DEFAULT_COMPARATOR);
	}


	@SuppressWarnings("unchecked")
/**
 * @param comp the comparator
 * to use to order the elements
**/

	public void insertionsort(Comparator<? super E> comp) {	
		if (getSize() > 1) {			
			ListItem<E> loc = head.next;
			while (loc.next != getTail()) {  //loc.next is placed into position
				ListItem<E> ptr = head;      //find ptr after which loc.next should be placed
				while (ptr != loc && comp.compare((E) ptr.next.data, (E) loc.next.data) <= 0)
					ptr = ptr.next;   //if ptr.next.data <= loc.next.data, need to move forward
				if (ptr != loc) {     //loc.next needs to be moved after ptr
					if (loc.next == getLast())    //preserves Last
						setLast(loc);          //after moving loc.next, loc is last
					loc.moveNextAfter(ptr);	 //move loc.next into place
				}
				else                 //loc.next is already in place
					loc = loc.next;  // continue to the next list item to process 
			}
			version.increment();  //invalidate active trackers for iteration
		}
	}

/**
 * Uses the default comparator to order the elements
**/

	public void mergesort() {
		mergesort(Objects.DEFAULT_COMPARATOR);
	}

/**
 * Sorts the list using
 * mergesort
 * @param comp the comparator
 * to use to order the elements
**/

	public void mergesort(Comparator<? super E> comp) {
		if (getSize() > 1) {
			head.setNext( mergesortImpl(comp, getSize(), head.next));		
			version.increment();		//invalidate active trackers for iteration
		}
	}

/**
 * @param comp the comparator
 * to use to order the elements
 * @param n the number of elements in this portion
 * of this list
 * @param ptr the pointer to the first item in the list
 * @return a reference
 * to the first list item in the sorted list
**/

	protected ListItem<E> mergesortImpl(Comparator<? super E> comp, 
												int n, ListItem<E> ptr) {
		ListItem<E> ptrLeft = ptr;          //ptr is reference to first element on left half
		int numLeft = n/2;                  //number of elements in the left half
		for (int i = 0; i < numLeft-1; i++) //move to middle of the list
			ptr = ptr.next;                 //ptr stops at last element on left half
		ListItem<E> ptrRight = ptr.next;    //ptrRight references first element on right
		ptr.next = getTail();   //break into two list 
		if (numLeft > 1)  //recursively sort left half
			ptrLeft = mergesortImpl(comp, numLeft, ptrLeft);
		if (n - numLeft > 1)  //recursively sort right half
			ptrRight = mergesortImpl(comp, n - numLeft, ptrRight);
		return merge(comp,ptrLeft,ptrRight); //merge the two sorted lists into one
	}

/**
 * Merges the two lists and
 * @param comp the comparator
 * to use to order the elements
 * @param ptr1 the pointer to the first item in the first list
 * to merge
 * @param ptr2 the pointer to the first item in the second list to merge
 * @return a reference
 * to the first item in the merged list, which will be one of the two list items passed
 * in as parameters.
**/

	protected ListItem<E> merge(Comparator<? super E> comp, 
							ListItem<E> ptr1, ListItem<E> ptr2) {
		ListItem<E> retVal = ptr1;  //reference head of merged list
		if (comp.compare((E) ptr1.data, (E) ptr2.data) <= 0) { //when smallest item in first list
			ptr1 = ptr1.next;                      //move to second item (if any) from first list
		} else {                                              //when smallest item in second list
			retVal = ptr2;                        //front of second list will be head of merged lists
			ptr2 = ptr2.next;                     //move to second item (if any) from second list
		}
		ListItem<E> ptr = retVal;                //ptr references last item placed in merged list
		while (ptr1 != getTail() && ptr2 != getTail()) {
			if (comp.compare((E) ptr1.data, (E) ptr2.data) <= 0) {
				if (ptr.next != ptr1)           //when ptr at item from second list		
					ptr.setNext(ptr1);          //adjust next pointer
				ptr1 = ptr1.next;               //move to next item from first list
			} else {                            //next element from second list is next smallest
				if (ptr.next != ptr2)           //when ptr at item from first list
					ptr.setNext(ptr2);          //adjust next pointer
				ptr2 = ptr2.next;               //move to next element from second list
			}
			ptr = ptr.next;                     //move forward in merged list
		}
		if (ptr2 != getTail())                 //if items remain in second list
			ptr.setNext(ptr2);                 //append to end of partially merged list
		else {								   //still items in first list 
			ptr.setNext(ptr1);                 //append to end of partially merged list
			while (ptr.next != getTail())      //must also move ptr to last item
				ptr = ptr.next;                //of first list and hence of merged list
			setLast(ptr);					   //reset last (currently end of second list)
		}	
		return retVal;
	}

/**
 * @param comp the comparator to use for the elements
 * @return a comparator defined over list items, that wraps the provided
 * comparator
**/

	private Comparator getSorter(final Comparator<? super E> comp){
		return new Comparator<ListItem>() {
			public int compare(ListItem a, ListItem b) {
				return comp.compare((E) a.data, (E) b.data);
			}
		};
	}

/**
 * Update
 * the list to be empty without changing the value of
 * <code>size</code>
**/

	protected void resetList(){
		head.setNext(getTail());  //preserves EndsAtTail
		last = head;              //preserves Last
	}

/**
 * Uses the default comparator to order the elements
**/

	public void heapsort() {
		heapsort(Objects.DEFAULT_COMPARATOR);
	}	

/**
 * @param comp the comparator
 * to use to order the elements
**/

	public void heapsort(Comparator<? super E> comp) {
		heapsortImpl(getSorter(comp));
	}

/**
 * Is the implementation of heap sort
 * @param sorter the comparator defined
 * over list items to use
**/

	@SuppressWarnings("unchecked") 

	public void heapsortImpl(Comparator sorter) {
		BinaryHeap<Object> heap = new BinaryHeap<Object>(getSize(),
							new ReverseComparator(sorter));
		ListItem<E> ptr = head.next;  //place all list items into the heap
		while (ptr != getTail()) {
			heap.add(ptr);
			ptr = ptr.next;
		}
		resetList();                  //rebuild the list
		while (!heap.isEmpty())
			addItemLast((ListItem<E>) heap.extractMax());
		version.increment();         //invalidate active trackers for iteration
	}	

/**
 * Uses the default comparator to order the elements
**/

	public void treesort() {
		treesort(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp the comparator
 * to use to order the elements
**/

	public void treesort(Comparator<? super E> comp) {
		treesortImpl(getSorter(comp));
	}

/**
 * Is the implementation of tree sort
 * @param sorter the comparator to use
**/

	protected void treesortImpl(Comparator sorter) {
		RedBlackTree<ListItem<E>> tree = new RedBlackTree<ListItem<E>>(sorter);
		ListItem<E> ptr = head.next; //insert all list items into a tree
		while (ptr != getTail()) {
			tree.add(ptr);
			ptr = ptr.next;
		}
		resetList();                 //rebuild list using a visitor
		ptr = head;
		tree.accept(new Visitor<Object>() {                                   
			public void visit(Object o) throws Exception {
				addItemLast((ListItem<E>) o);
			}
		});
	}

/**
 * Quicksort is only applied to a subcollection with a size greater
 * than the constant <code>CUTOFF</code>.
**/

	final protected int CUTOFF = 7;


	class Divider {
		ListItem<E> beforeEndOfLeft;  //reference to item preceding last in left half
		ListItem<E> beforeEnd; //reference to item preceding last in the subcollection
		int numLeft; //number of elements on the left half
	}


	public void quicksort() {
		quicksort(Objects.DEFAULT_COMPARATOR);
	}

/**
 * Sorts
 * the array in place
 * @param comp the comparator
 * to use to order the elements in the positional collection
**/

	public void quicksort(Comparator<? super E> comp) {
		if (getSize() > CUTOFF) {
			Divider mid = new Divider();  //just create one divider to reuse
			quicksortImpl(head, getPtrForPrevElement(getLast()), mid, getSize(), comp);
		}
		insertionsort();
	}

/**
 * Sorts the subcollection from
 * <code>beforeStart</code> +1 to <code>end</code>+1 (inclusive).
 * @param beforeStart a reference to the list item just before the first one in
 * the subcollection to sort
 * @param beforeEnd a reference to the list item just before the last one in
 * the subcollection to sort
 * @param divider which holds information needed to divide the subcollection after
 * partitioning
 * @param size the number of elements in that subcollection
 * @param comp the comparator that defines the desired order of the elements
**/

		void quicksortImpl(ListItem<E> beforeStart, ListItem<E> beforeEnd, Divider divider, 
				int size, Comparator<? super E> comp) {
			if (size > CUTOFF) {  //insertion sort will be used to finish the sort
				partition(beforeStart, beforeEnd, divider, comp);
				int nLeft = divider.numLeft;
				beforeEnd = divider.beforeEnd;
				ListItem<E> beforeEndOfLeft = divider.beforeEndOfLeft;
				ListItem<E> mid = beforeStart.next; //value when nLeft = 0
				if (divider.numLeft > 1)  //left side has >1 element
					mid = beforeEndOfLeft.next.next; //value when nLeft > 0
				quicksortImpl(beforeStart, beforeEndOfLeft, divider, nLeft, comp);
				quicksortImpl(mid, beforeEnd, divider, size - nLeft - 1, comp);
			}
		}	

/**
 * Partitions the subcollection from
 * <code>loc</code> +1 to <code>beforeEnd</code>+1 (inclusive)
 * @param loc a reference to the list item that references the start of
 * the subcollection to partition
 * @param beforeEnd a reference to the second to last list item in the subcollection to partition
**/

		protected void partition(ListItem<E> loc, ListItem<E> beforeEnd, Divider divider,
				Comparator<? super E> comp) {
			E pivot = (E) beforeEnd.next.data;	    // partition around end
			divider.numLeft = 0;					//count number put on left half
			ListItem<E> ptr = loc;
			while (ptr != beforeEnd) {
				if (comp.compare((E) ptr.next.data, pivot) <= 0) {  // <= pivot
					if (ptr == loc) {	
						divider.beforeEnd = ptr;
						ptr = ptr.next;		       //move to the next element
					} else {
						if (ptr.next == beforeEnd) //moving beforeEnd, so now
							beforeEnd = ptr;        //ptr's value is the new beforeEnd
						if (loc == divider.beforeEnd & ptr.moveNextAfter(loc))
							divider.beforeEnd = loc.next;
					}
					divider.numLeft++;             //put one more on left half
					divider.beforeEndOfLeft = loc; //always just before loc
					loc = loc.next;			       //advance loc
				}
				else {
					divider.beforeEnd = ptr;
					ptr = ptr.next;		//move to the next element
				}
			}
			if (ptr == divider.beforeEnd)
				divider.beforeEnd = beforeEnd.next;
			ListItem<E> newLast = beforeEnd;
			if (getLast() == beforeEnd.next & beforeEnd.moveNextAfter(loc))
				setLast(newLast);
		}

/**
 * @param x a
 * reference to the list item to insert to the front of the list
**/

	protected void placeListItemFirst(ListItem<E> x) {
		x.setNext(head.next);
		head.setNext(x);
		if (getLast() == head)  //preserve Last
			setLast(x);	
	}		

/**
 * @param digitizer the digitizer to use
 * in radix sort
**/

	public void radixsort(Digitizer<? super E> digitizer) {
		int numDigits = 0;   //maximum number of digits in any element
		int b = digitizer.getBase();
		ListItem<E>[] headPtr = (ListItem<E>[]) new ListItem[b];
		for (E x : this) {   //determine maximum number of digits
			int digits = digitizer.numDigits(x);
			if (digits > numDigits)  //update numDigits
				numDigits = digits;
		}
		for (int d = 0; d < numDigits; d++) {//sort according to each digit d
			Arrays.fill(headPtr,null);       
			while (head.next != getTail()) {  
				int v = digitizer.getDigit((E) head.next.data, d);
				ListItem<E> temp = head.next.next;
				head.next.next = headPtr[v];
				headPtr[v] = head.next;
				head.next = temp;
			}
			for (int i = b-1; i >= 0; i--) {  //rebuild the sorted list
				while (headPtr[i] != null) {
					ListItem<E> temp = headPtr[i].next;
					placeListItemFirst(headPtr[i]);
					headPtr[i] = temp;
				}
			}
		}
	}

/**
 * @param bucketizer the bucketizer
 * to use
**/

	public void bucketsort(Bucketizer<? super E> bucketizer) {
		int b = bucketizer.getNumBuckets();
		ListItem<E>[] headPtr = (ListItem<E>[]) new ListItem[b];
		Arrays.fill(headPtr,null);
		while (head.next != getTail()) {
			int index = bucketizer.getBucket((E) head.next.data);
			ListItem<E> temp = head.next.next;
			head.next.next = headPtr[index];
			headPtr[index] = head.next;
			head.next = temp;
		}
		for (int i = b-1; i >= 0; i--) { //put back into list
			while (headPtr[i] != null) {
				ListItem<E> temp = headPtr[i].next;
				placeListItemFirst(headPtr[i]);
				headPtr[i] = temp;
			}
		}
		insertionsort(); //sort within buckets
	}

/**
 * @param r the rank of the desired element in the sorted order
 * of the elements
 * @return the element at rank <code>r</code> when using the default comparator
 * @throws PositionOutOfBoundsException <code>r</code> is not a valid position
**/

	public E repositionElementByRank(int r) {
		return repositionElementByRank(r, Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param r the rank of the desired element in the sorted order of the elements
 * @param comp the comparator to use for determining the order
 * @return the element at rank <code>r</code> when using the given comparator
 * @throws NoSuchElementException the collection is empty
 * or <code>r</code> is not a valid position
**/

	public E repositionElementByRank(int r, Comparator<? super E> comp) {
		if (isEmpty() || r < 0 || r >= getSize())
			throw new NoSuchElementException();
		Divider mid = new Divider();  //just create one divider to reuse
		E result = repositionElementByRankImpl(head, 
				getPtrForPrevElement(getLast()), mid, getSize(), r, comp);
		return result;
	}

/**
 * @param beforeStart the list item just before the start of the partition
 * @param beforeEnd the list item just before the last element of the partition
 * @param div the Divider instance to be used in partition
 * @param n the size of the partition
 * @param r the rank of the desired element in the sorted order
 * @param comp the comparator used to order the elements
 * <BR> 
 * REQUIRES: 
 * 0 &le; i &le; n-1
 * @return the element at rank <code>i</code>
**/

	E repositionElementByRankImpl(ListItem<E> beforeStart,	ListItem<E> beforeEnd, 
					Divider div, int n, int r, Comparator<? super E> comp) {
		if (n == 1)
			return (E) beforeStart.next.data;  //correct when n=1
		partition(beforeStart, beforeEnd, div, comp); // repositions endOfLeftHalf
		int nLeft = div.numLeft;
		ListItem<E> beforeEndOfLeft = div.beforeEndOfLeft;
		beforeEnd = div.beforeEnd;
		ListItem<E> mid = beforeStart.next; //value when nLeft = 0
		if (nLeft > 0)  //left side has >=1 element
			mid = beforeEndOfLeft.next.next; //value when nLeft > 0
		if (r == nLeft) {
			return (E) mid.data;
		} else if (r < nLeft) {    //recurse on left half
			return repositionElementByRankImpl(beforeStart, beforeEndOfLeft,
					 										 div, nLeft, r, comp);
		} else {                  //recurse on right half
			return repositionElementByRankImpl(mid, beforeEnd, 
					                         div, n = nLeft - 1, r-nLeft-1, comp);  
		}
	}	


	protected class Tracker extends AbstractCollection<E>.AbstractLocator<E>
		implements PositionalCollectionLocator<E>{

		ListItem<E> ptr;     //pointer to tracked element

/**
 * @param startLoc a reference to the list item holding the element to track.
**/

		protected Tracker(ListItem<E> startLoc){
			ptr = startLoc;
		}

/**
 * @return true  if and only if
 * the tracked element is currently in the collection
**/

		public boolean inCollection(){
			return ptr != head && ptr != getTail() && ptr.data != REMOVED;
		}

/**
 * @return the tracked element
 * @throws NoSuchElementException the tracker is not at an element in
 * the collection.
**/

		@SuppressWarnings("unchecked")

		public E get() {
			if (!inCollection())
				throw new NoSuchElementException();
			return (E) ptr.data;
		}

/**
 * @param value the element to put at the current tracker location.
 * @return the element that had been stored at the tracker location
 * @throws NoSuchElementException tracker is not at an element in
 * the collection.
**/

		@SuppressWarnings("unchecked")

		public E set(E value) {
			if (!inCollection())
				throw new NoSuchElementException();
			Object oldData = ptr.data;
			ptr.data = value;
			return (E) oldData;
		}

/**
 * This method takes constant time for an element not in
 * the collection.  For an element in the collection, this method takes time
 * proportional to the position of the tracked element, which in the worst-case
 * is linear time.
 * @return the position of the tracker within the collection.
 * @throws NoSuchElementException the tracker is not at an element
 * in the collection.
**/

		public int getCurrentPosition() {
			if (!inCollection())
				throw new NoSuchElementException();
			int i = 0;
			for (ListItem<E> loc = head.next; loc != ptr; loc = loc.next)
				i++;
			return i;
		}

/**
 * @param ptr a pointer to a list item
 * @return a pointer to the first reachable list item,
 * possibly <code>ptr</code> itself if it is reachable.
**/

		protected ListItem<E> skipRemovedElements(ListItem<E> ptr) {
			if (ptr == getTail() || ptr.data != REMOVED) 
				return ptr;
			ptr.next = skipRemovedElements(ptr.next);
			return ptr.next;
		}

/**
 * Moves the tracker to the next location
 * @return true  if and only if the tracker moves to a
 * valid position.
 * @throws AtBoundaryException the tracker is already at
 * AFT since
 * there is no place to advance.
**/

		public boolean advance() throws ConcurrentModificationException {
			if (ptr == getTail())
				throw new AtBoundaryException("Already after end.");
			checkValidity();
			if (ptr.data == REMOVED)
				ptr = skipRemovedElements(ptr);
			else
				ptr = ptr.next;
			return ptr != getTail();
		}

/**
 * Updates the tracker to the previous element
 * in the collection
 * @return true if and only if the tracker is at an element in the collection
 * after retreating.
 * @throws AtBoundaryException the tracker is
 * at FORE.
**/

		public boolean retreat() throws ConcurrentModificationException {
			if (ptr == head)
				throw new AtBoundaryException("Already before start.");
			checkValidity();
			if (ptr != getTail() && ptr.data == REMOVED)
				ptr = skipRemovedElements(ptr);
			ptr = getPtrForPrevElement(ptr);
			return (ptr != head);
		}

/**
 * @return true  if there is some element after the current tracker position.
**/

		public boolean hasNext() throws ConcurrentModificationException {
			checkValidity();
			return ptr != getTail() && ptr.next != getTail() && 
						skipRemovedElements(ptr) != getTail();
		}

/**
 * Adds the new element after the tracked
 * element
 * @param value the element to add
 * @return a tracker to the new element
**/

	public PositionalCollectionLocator<E> addAfter(E value)
	 					throws ConcurrentModificationException {
			if (ptr.data == REMOVED)
				throw new NoSuchElementException();
			return insertAfter(ptr, value);
		}

/**
 * Removes the tracked element from the collection.  Since there
 * are no previous pointers, this method takes O(p) time, where p is
 * the position of the tracked element.
 * @throws NoSuchElementException the tracked element has already been
 * removed.
**/

		public void remove() throws ConcurrentModificationException {
			if (!inCollection())
				throw new NoSuchElementException();
			removeNext(getPtrForPrevElement(ptr));
		}

/**
 * @return the element
 * after the tracked element
 * @throws NoSuchElementException the tracked element is not in the collection, or
 * if current element is at position
 * <code>size</code>-1.
**/

		@SuppressWarnings("unchecked")

		public E getNextElement() throws ConcurrentModificationException {
			if (ptr.data == REMOVED || !hasNext())
				throw new NoSuchElementException();
			return (E) ptr.next.data;
		}

/**
 * Removes the element that
 * follows the tracked element
 * @throws NoSuchElementException the tracked element is not in the collection, or
 * if current element is at position
 * <code>size</code>-1.
**/

		public void removeNextElement() throws ConcurrentModificationException {
			if (ptr.data == REMOVED || !hasNext())
				throw new NoSuchElementException();			
			removeNext(ptr);
		}


	}
}
