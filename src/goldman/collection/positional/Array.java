// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import goldman.Objects;
import goldman.collection.AbstractCollection;
import goldman.collection.AtBoundaryException;
import goldman.collection.AtCapacityException;
import goldman.collection.Bucketizer;
import goldman.collection.Digitizer;
import goldman.collection.Visitor;
import goldman.collection.ordered.RedBlackTree;
import goldman.collection.priority.BinaryHeap;
import goldman.collection.priority.PriorityQueue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import static java.lang.Math.*;
/**
 * The simplest of the positional collections, Array provides space for
 * a fixed number of elements, which are stored in an underlying Java primitive array.
 * Methods are provided for changing the capacity of the collection, but there is no
 * support for automatic resizing. If the approximate size of the collection is known
 * a priori then this is a very good choice for minimizing space usage.  However,
 * it can only efficiently support insertions and deletions near the end of the collection.
 * This is an untracked implementation.
**/

public class Array<E> extends AbstractPositionalCollection<E> 
	implements PositionalCollection<E> {

	Object[] a;	// the underlying array

/**
 * Creates an array with the given capacity that uses
 * the provided equivalence tester
 * @param capacity the desired initial capacity for the underlying array
 * @param equivalenceTester a user-provided equivalence tester
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	@SuppressWarnings("unchecked")

	public Array(int capacity, Comparator<? super E> equivalenceTester){
		super(equivalenceTester);
		resizeArray(capacity);
		size = 0;
	}

/**
 * Creates an array with a default initial capacity that uses
 * the default equivalence tester.
**/

	public Array(){
		this(DEFAULT_CAPACITY, Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * Creates an array with the given capacity that uses the default
 * equivalence tester
 * @param capacity the desired initial capacity for the underlying array
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public Array(int capacity) {
		this(capacity, Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * @return the current capacity of the
 * collection
**/

	public int getCapacity(){
		return a.length;
	}

/**
 * @param p a valid user position
 * <BR> 
 * REQUIRES: 
 *  p is a valid position
 * @return the element at user position <code>p</code>
**/

	@SuppressWarnings("unchecked")

	protected final E read(int p){
		return (E) a[getIndex(p)];
	}

/**
 * @param p the desired user position
 * @return the element at the position <code>p</code>
 * @throws PositionOutOfBoundsException p is not a valid position
**/

	public E get(int p) {
		if (p < 0 || p >= size)
			throw new PositionOutOfBoundsException(p);
		return read(p);
	}

/**
 * @param p a valid user position
 * <BR> 
 * REQUIRES: 
 * <code>p</code> to be a valid position
 * @return the corresponding index in the underlying array <code>a</code>
**/

	int getIndex(int p){
		return p;
	}

/**
 * @param index an underlying array index that is in use
 * <BR> 
 * REQUIRES: 
 * index is in use
 * @return the corresponding user position.
**/

	int getPosition(int index){
		return index;
	}

/**
 * Note that this method requires that <code>index</code> is
 * between 0 and n-2, inclusive.  It must
 * be overridden by any data structure in which the underlying index and user
 * position may differ.
 * @param index an underlying index of <code>a</code>
 * @return the underlying index for the next element in the collection
**/

	int nextIndex(int index){
		return index+1;
	}

/**
 * Note that this method requires that <code>index</code> is
 * between 1 and n-1, inclusive. It must be
 * overridden by any data structure in which the underlying index and user
 * position may differ.
 * @param index an underlying index
 * @return the underlying index for the previous element in the collection
**/

	int prevIndex(int index){
		return index-1;
	}

/**
 * @param value the element to be located
 * @return the first position <code>p</code> in the collection
 * such that <code>value</code> is equivalent to the element at position
 * <code>p</code>, or <code>NOT_FOUND</code>
 * if there is no equivalent element in the collection.
**/

	@SuppressWarnings("unchecked")

	protected int findPosition(E value){
		for (int pos=0; pos<size; pos++){
			if (equivalent(value,read(pos)))
				return pos;
		}
		return NOT_FOUND; 
	}

/**
 * @param value the element to be located
 * @return true  if and only if an equivalent element exists in the collection
**/

	public boolean contains(E value) {
		return (findPosition(value) != NOT_FOUND);
	}

/**
 * @param value the element to be located
 * @return the position in the collection for the first occurrence
 * (if any) of an element equivalent to <code>value</code>.
 * @throws NoSuchElementException no element in the collection is equivalent
 * to <code>value</code>
**/

	public int positionOf(E value) {
		int loc = findPosition(value);
		if (loc == NOT_FOUND)
			throw new NoSuchElementException();
		return loc;
	}

/**
 * Changes the capacity of the underlying array
 * to <code>desiredCapacity</code> while maintaining the same positional collection.
 * @param desiredCapacity the desired capacity of the underlying array
 * @throws IllegalArgumentException executing it would
 * make the array capacity too small to hold the current collection
**/

	@SuppressWarnings("unchecked")

	void resizeArray(int desiredCapacity){
		if (desiredCapacity < size)
			throw new IllegalArgumentException("desiredCapacity < size");
		a = moveElementsTo(new Object[desiredCapacity]);
	}

/**
 * @param newArray the array to which the elements should be moved
 * @return the parameter value for convenience
**/

	protected Object[] moveElementsTo(Object[] newArray) {
		if (a != null)
			System.arraycopy(a,0,newArray,0,size);
		return newArray;
	}

/**
 * Increases the capacity of the underlying array if
 * needed
 * @param capacity the desired capacity
**/

	public void ensureCapacity(int capacity) {
		resizeArray(capacity);
	}

/**
 * Reduces the capacity of the array to the current
 * number of elements in the collection
**/

	public void trimToSize() {
		if (size != a.length)
			resizeArray(size);	
	}

/**
 * @param p a user position to update
 * @param value the element to put at position <code>p</code>
 * @return the prior element at position <code>p</code>
 * @throws PositionOutOfBoundsException <code>p</code> is not
 * a valid position
**/

	@SuppressWarnings("unchecked")

	public E set(int p, E value) {
		if (p < 0 || p >= size)
			throw new PositionOutOfBoundsException(p);
		int index = getIndex(p);
		E oldValue = (E) a[index];
		a[index] = value;
		return oldValue;
	}

/**
 * Swaps the values held in positions <code>pos1</code> and <code>pos2</code>
 * @param pos1 a valid position
 * @param pos2 a valid position
 * @throws PositionOutOfBoundsException either <code>pos1</code> or <code>pos2</code>
 * is not a valid position
**/

	@SuppressWarnings("unchecked")

	public void swap(int pos1, int pos2){
		if (pos1 < 0 || pos1 >= size)
			throw new PositionOutOfBoundsException(pos1);
		if (pos2 < 0 || pos2 >= size)
			throw new PositionOutOfBoundsException(pos2);
		swapImpl(pos1,pos2);
		version.increment();   //invalidate all markers for iteration
	}

/**
 * Swaps the elements held
 * in positions <code>pos1</code> and <code>pos2</code>
 * @param pos1 a valid position
 * @param pos2 a valid position
 * <BR> 
 * REQUIRES: 
 * that
 * <code>pos1</code> and <code>pos2</code> are both valid positions.
**/

	void swapImpl(int pos1, int pos2) {
		if (pos1 == pos2) return;
		int index1 = getIndex(pos1);
		int index2 = getIndex(pos2);
		Object temp = a[index1];
		a[index1] = a[index2];
		a[index2] = temp;
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
		a[getIndex(toPos)] = a[getIndex(fromPos)];
	}

/**
 * @param p a valid position
 * @param value the object to place at position <code>p</code>
 * <BR> 
 * REQUIRES: 
 *  <code>p</code> is a legal position
**/

	void put(int p, Object value) {
		a[getIndex(p)] = value;
	}

/**
 * Moves elements at
 * positions <code>p</code>, ..., <code>size</code>-1 to positions
 * <code>p+1</code>, ..., <code>size</code>.
 * @param p a valid user position where the gap is to be created
**/

	protected void createGap(int p){
		System.arraycopy(a,p,a,p+1,size-p);
		version.increment();   //invalidate all active markers for iteration
	}

/**
 * Inserts object <code>value</code> at position <code>p</code>
 * and increments the position number for the elements that were at
 * positions <code>p</code>, ..., <code>size</code>-1.
 * @param p a valid user position
 * @param value the object to insert
 * @return null for an untracked implementations
**/

		PositionalCollectionLocator<E> addImpl(int p, Object value) {
			if (size == a.length)
				throw new AtCapacityException(a.length);
			if (p != size)
				createGap(p);  //invalidates active locators for iteration
			a[getIndex(p)] = value;
			size++;
			return null;
		}

/**
 * Inserts <code>value</code> at position <code>p</code>
 * and increments the position number for the elements that were at
 * positions <code>p</code>, ..., <code>size</code>-1.
 * @param p a valid user position
 * @param value the new element
 * @throws PositionOutOfBoundsException <code>p</code> is neither <code>size</code>
 * nor a valid position
 * @throws AtCapacityException the underlying array is already at capacity
**/

		public final void add(int p, E value) {
			if (p < 0 || p > size)
				throw new PositionOutOfBoundsException(p);
			addImpl(p, value);
		}

/**
 * Inserts it at the end of the collection.
 * @param value the new element
 * @throws AtCapacityException the underlying array is already at capacity
**/

	public void add(E value) {
		add(size,value);
	}

/**
 * Moves the elements that were at
 * positions <code>toPos</code>+1, ..., <code>size</code>-1 to positions
 * <code>fromPos</code>, ...,
 * <code>size</code>-1 - (<code>toPos</code> - <code>fromPos</code> + 1).  We use
 * <code>Arrays.fill</code> to set all slots in the underlying array that are no
 * longer in use to null. 
 * @param fromPos the first position in the gap to close
 * @param toPos the last position in the gap to close
**/

	protected void closeGap(int fromPos, int toPos){
		int numElementsToMove = size - toPos - 1;
		if (numElementsToMove != 0)
			System.arraycopy(a,toPos+1,a,fromPos,numElementsToMove);
		Arrays.fill(a,fromPos+numElementsToMove,size,null);
	}

/**
 * Requires 0 &le; <code>fromPos</code> &le; <code>toPos</code> &lt; <code>size</code>.
 * It removes the elements at positions
 * <code>fromPos</code>, ..., <code>toPos</code>, inclusive,
 * from the collection and
 * decrements the positions of the
 * elements at positions <code>toPos+1</code> to <code>size-1</code> by
 * <code>toPos-fromPos+1</code> (the number of elements
 * being removed).
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
		closeGap(fromPos,toPos);  
		size = size - (toPos - fromPos + 1);
		if (toPos != size - 1)     //removed elements end at last element
			version.increment();   //invalidate locators for iteration
	}

/**
 * Removes the element at position <code>p</code> and shifts elements
 * u_{p+1}, ..., u_{<code>size</code>-1} left by one position.
 * @param p a valid position
 * @return the removed element
 * @throws PositionOutOfBoundsException <code>p</code> is not a valid
 * position
**/

	public E remove(int p) {
		if (p < 0 || p >= size)
			throw new PositionOutOfBoundsException(p);
		E removedElement = get(p);
		removeRange(p,p);
		return removedElement;
	}

/**
 * Removes the element at position 0 and decrements the position
 * for the elements that were
 * at positions <code>1</code> to <code>size-1</code>.
 * @return the element that was removed
 * @throws NoSuchElementException the collection is empty
**/

	public final E removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException("collection is empty");
		return remove(0);
	}

/**
 * Removes the element at position <code>size</code>-1
 * @return the element that was removed
 * @throws NoSuchElementException the collection is empty
**/

	public final E removeLast() {
		if (isEmpty())
			throw new NoSuchElementException("collection is empty");
		return remove(size-1);
	}

/**
 * Removes the first element in the collection equivalent to
 * <code>value</code>
 * @param value the element to be removed
 * @return true  if and only if an element is removed.
**/

	public boolean remove(E value) {
		int position = findPosition(value);
		if (position == NOT_FOUND)
			return false;
		removeRange(position,position);
		return true;
	}

/**
 * Removes all elements from the collection.
**/

	public void clear(){
		if (!isEmpty()) {
			Arrays.fill(a, 0, size, null);
			size = 0;
			version.increment();
		}
	}

/**
 * Traverses the entire collection on behalf of v.
 * @param v a visitor to apply for each element in the collection
 * @throws Exception the visitor throws an exception.
**/

	public void traverseForVisitor(Visitor<? super E> v) throws Exception {
		for (int p = 0; p < size; p++)
			v.visit((E) get(p));
	}

/**
 * Creates a new marker that starts at FORE.
**/

	public PositionalCollectionLocator<E> iterator() {
		return new Marker(FORE);
	}

/**
 * Creates a new marker that starts at AFT.
**/

	public PositionalCollectionLocator<E> iteratorAtEnd() {
		return new Marker(size);
	}

/**
 * Returns a new marker that is at the given position.
 * @param pos the user position of an element
 * @throws NoSuchElementException the given position is not a valid user position.
**/

	public PositionalCollectionLocator<E> iteratorAt(int pos) {
		if (pos < 0 || pos >= size)
			throw new NoSuchElementException();
		return new Marker(getIndex(pos));
	}

/**
 * @param value the target
 * @return a marker initialized to the position of the first
 * element in the collection equivalent to <code>value</code>
 * @throws NoSuchElementException <code>value</code> does not occur in the collection
**/

	public PositionalCollectionLocator<E> getLocator(E value) {
		int position = findPosition(value);
		if (position == NOT_FOUND)
			throw new NoSuchElementException();
		return new Marker(position);
	}


	public void insertionsort() {
		insertionsort(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp the comparator
 * to use to order the elements
**/

	public void insertionsort(Comparator<? super E> comp) {
		insertionsortImpl(comp);
	}

/**
 * Is our implementation of insertion sort
 * @param sorter the comparator to use
**/

	@SuppressWarnings("unchecked")

	void insertionsortImpl(Comparator sorter) {	
		if (getSize() > 1) {
			for (int j = 1; j < getSize(); j++) {   //pos 0...j-1 are sorted
				Object value = read(j);             //holds object being put in place
				int i = j - 1;					//start at element at position just before it
				while (i >= 0 && sorter.compare(read(i),value) > 0) {   //while element i larger
					move(i,i+1);                //move it forward
					i--;                        //move to the left
				}
				put(i+1, value);                //put object into place
			}
			version.increment();                //invalidate active markers for iteration
		}
	}

/**
 * Uses the default comparator to order the elements
**/

	public void mergesort() {
		mergesort(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp the comparator that defines the
 * ordering of the elements
**/

	public void mergesort(Comparator<? super E> comp) {
		mergesortImpl(comp);
	}

/**
 * Starts the recursive merge sort method
 * @param sorter the comparator to use
**/

	void mergesortImpl(Comparator<? super E> sorter) {
		if (getSize() > 1) {
			Object toSort[] = new Object[getSize()];       //array created to sort
			for (int i = 0; i < getSize(); i++)            //put elements into toSort
				toSort[i] = read(i);                          //so pos i in index i
			mergesortImpl(toSort,a,0,getSize()-1,sorter);  //sort toSort, using a as aux. array
			for (int i = 0;  i < getSize(); i++)           //put elements back into a
				put(i, toSort[i]);        
			version.increment();	                 //invalidate active markers for iteration
		}
	}

/**
 * Sorts the subarray from <code>left</code> to <code>right</code>
 * using the given comparator.
 * @param data the array to sort
 * @param aux an auxiliary array to use for the merge
 * @param left the starting index of the subarray to sort
 * @param right the ending index of the subarray to sort
 * @param sorter the comparator to use to compare elements
**/

	void mergesortImpl(Object[] data, Object[] aux, int left, int right, 
													Comparator<? super E> sorter) {
		if (left < right) {
			int mid = (left + right) / 2; // find the middle
			int i = left;  // index in data of current loc in left half 
			int j = mid+1; // index in data of current loc in right half
			int k = left;  // index of current location in aux
			mergesortImpl(data, aux, left, mid, sorter);     // recursively sort the left half
			mergesortImpl(data, aux, mid + 1,right, sorter); // recursively sort the right half
			while (i <= mid && j <= right) {         //merge two sorted halves
				if (sorter.compare((E) data[i], (E) data[j]) < 0)    //if next element on left smaller
					aux[k++] = data[i++];                  //   move into aux
				else                                    //otherwise next element on right smaller
					aux[k++] = data[j++];                  //   move it into aux
			}
			System.arraycopy(data,i,aux,k,mid-i+1);      //copy rest of left half to aux
			System.arraycopy(aux,left,data,left,j-left); //copy used portion of aux back into data
		}
	}

/**
 * Sorts this collection with
 * heap sort using the default comparator
**/

	public void heapsort() {
		heapsort(Objects.DEFAULT_COMPARATOR);
	}	

/**
 * Sorts this collection with heap sort using the provided comparator
 * @param comp a comparator
**/

	public void heapsort(Comparator<? super E> comp) {
		heapsortImpl(comp);
	}


	@SuppressWarnings("unchecked") 
/**
 * It inserts all the elements in the positional
 * collection on which this method is called into <code>pq</code>.
 * @param pq the
 * priority queue used
 * by heap sort
**/

	void buildPriorityQueue(PriorityQueue<Object> pq) {
		pq.addAll(this);
	}

/**
 * Is the implementation of heap sort
 * @param sorter the comparator to use
**/

	void heapsortImpl(Comparator sorter) {
		PriorityQueue<Object> heap = new BinaryHeap<Object>(getSize(), sorter);
		buildPriorityQueue(heap);  //put all elements in heap
		for (int i = getSize()-1; i >= 0; i--)   //extractMax putting elements back into this
			put(i, heap.extractMax());        //positional collection starting at position 0
		version.increment();                 //invalidate active markers for iteration
	}

/**
 * Sorts this collection using the tree sort algorithm and the default comparator
**/

	public void treesort() {
		treesort(Objects.DEFAULT_COMPARATOR);
	}

/**
 * Sorts this collection using the tree sort algorithm and the provided comparator
 * @param comp a comparator
**/

	public void treesort(Comparator<? super E> comp) {
		treesortImpl(comp);
	}

/**
 * Is the implementation of tree sort
 * @param sorter the comparator to use
**/

	void treesortImpl(Comparator<? super E> sorter) {
		RedBlackTree<E> tree = new RedBlackTree<E>(sorter);
		for (int i = 0; i < getSize(); i++)  //add all elements to tree
			tree.add(read(i));
		tree.accept(new Visitor<Object>() {     //use visitor to traverse
			int pos = 0;                          //position for next element  
			public void visit(Object o) throws Exception {
				put(pos++, o);
			}
		});
		version.increment();                 //invalidate active markers for iteration
	}

/**
 * Sorts this collection with quicksort using the default comparator
**/

	public void quicksort() {
		quicksort(Objects.DEFAULT_COMPARATOR);
	}

/**
 * Sorts this collection with quicksort using the provided comparator
 * @param comp a comparator
**/

	public void quicksort(Comparator<? super E> comp) {
		if (getSize() > 1)
			quicksortImpl(0, getSize()-1, comp);
		version.increment();   //invalidate all markers for iteration
	}

/**
 * Sorts the subarray from positions
 * <code>left</code> to <code>right</code>, inclusive
 * @param left the leftmost position within the subarray to be sorted
 * @param right the rightmost position with the subarray
 * @param sorter the comparator to use when comparing the elements
**/

	void quicksortImpl(int left, int right, Comparator<? super E> sorter){
		if (left < right) {								//done when left >= right
			swap(getMedianOfThree(left, right, sorter), right); 
			int mid = partition(left, right, sorter);   // partition around right element
			quicksortImpl(left, mid-1, sorter); 		// recursively sort portion before pivot
			quicksortImpl(mid+1, right, sorter);		// recursively sort portion after pivot
		}
	}

/**
 * @param left the leftmost position within the subarray
 * @param right the rightmost position with the subarray
 * @param sorter the comparator to use when comparing the elements
 * @return the position in the array of the median of the left, right,
 * and middle element
**/

	int getMedianOfThree(int left, int right, Comparator<? super E> sorter) {
		if (right - left + 1 >= 3) {     //only perform if at least 3 elements in subarray
			int mid = (left + right)/2;  //position of middle element of subarray
			E leftObject = read(left);    //object at left
			E midObject = read(mid);      //object at mid
			E rightObject = read(right);  //object at right
			if (sorter.compare(leftObject, midObject) <= 0) {
				if (sorter.compare(midObject, rightObject) <= 0)
					return mid;
				else if (sorter.compare(rightObject, leftObject) <= 0)
					return left;
			} else if (sorter.compare(midObject,rightObject) > 0) {
					return mid;
			}
		}
		return right;             
	}

/**
 * Modifies the array so that
 * all elements in the subarray left of the returned position are
 * strictly less than the
 * pivot element, and all elements in the subarray right of the returned position are greater
 * than or equal to the pivot element
 * @param left the leftmost index of the subarray to partition
 * @param right the rightmost index of the subarray to partition
 * @param sorter the comparator to use to order the elements
 * @return the final position of the pivot element, which is
 * initially in position <code>right</code>
**/

	int partition(int left, int right, Comparator<? super E> sorter){
		E pivot = read(right);   //pivot around the right element
		int i = left;		//positions left...i-1 hold elements < pivot
		int j = right;      //positions j...right-1 hold elements >= pivot
		while (i < j){          
			while (i < j && sorter.compare(read(i), pivot) < 0)
				i++;
			while (j > i && sorter.compare(read(j), pivot) >= 0)
				j--;
			if (i < j)  
				swapImpl(i,j);  
		}
		swapImpl(i,right);
		return i;
	}

/**
 * And sorts the collection with radix sort.
 * @param digitizer the digitizer
 * to use
**/

	public void radixsort(Digitizer<? super E> digitizer) {
		radixsortImpl(digitizer);
	}

/**
 * Is the complete implementation of radix sort
 * @param digitizer the digitizer to use
**/

	protected void radixsortImpl(Digitizer<? super E> digitizer) {
		Object[] from = new Object[getSize()]; //elements start in from
		Object[] to = new Object[getSize()];   //placed in sorted order into to
		int b = digitizer.getBase();           //base for digitizer
		int count[] = new int[b];              //counter for each possible value
		int numDigits = 0;  //maximum number of digits in any element
		for (int i = 0; i < getSize(); i++) {
			from[i] = a[getPosition(i)]; //move position i into index i of from
			numDigits = max(numDigits,digitizer.numDigits((E) from[i]));
		}
		for (int d = 0; d < numDigits; d++) {      //digit to use in current pass
			Arrays.fill(count,0);                  //reset all counts to 0
			for (Object x : from)                  //count # elements with 
				count[digitizer.getDigit((E) x, d)]++;     //each value for digit d
			for (int i = 1; i < b; i++)            //update to cumulative count
				count[i] += count[i-1];
			for (int i = getSize()-1; i >= 0; i--) //put elements in array to
				to[--count[digitizer.getDigit((E) from[i], d)]] = from[i];
			Object[] temp = from;  from = to;  to = temp;  //swap the "from" and "to" arrays
		}
		for (int i = 0; i < getSize(); i++)  //put sorted elements back into the collection
			put(getPosition(i), from[i]);    
		version.increment();                 //invalidate locators for iteration
	}

/**
 * @param bucketizer the bucketizer
 * to use
**/

	public void bucketsort(Bucketizer<? super E> bucketizer) {
		bucketsortImpl(bucketizer);
	}

/**
 * And sorts the array using bucket sort
 * @param bucketizer the bucketizer to use
**/

	protected void bucketsortImpl(Bucketizer<? super E> bucketizer) {
		int numBuckets = bucketizer.getNumBuckets();  //number of buckets
		int[] count = new int[getSize()];            //# elements in each bucket
		Object[] temp = new Object[getSize()];       //auxiliary array
		for (int i = 0; i < getSize(); i++)          //Step 1
			count[bucketizer.getBucket(read(i))]++;		
		for (int i = 1; i < numBuckets; i++)         //Step 2
			count[i] += count[i-1];
		for (int i = getSize() - 1; i >= 0; i--) {   //Step 3
			Object x = read(i);                //go backwards so stable
			temp[--count[bucketizer.getBucket((E) x)]] =  x;
		}
		for (int i = 0; i < size; i++)               //Step 4
			put(i, temp[i]);
		insertionsort();		                     //Step 5
	}

/**
 * @param r the rank of the desired element in the sorted collection
 * @return the element at rank <code>r</code> when using the default comparator
 * @throws PositionOutOfBoundsException <code>r</code> is not a valid position
**/

	public E repositionElementByRank(int r) {
		return (E) repositionElementByRank(r, Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param r the rank of the desired element in the sorted array
 * @param comp the comparator to use
 * @return the element at rank <code>r</code> when using the given comparator
 * @throws noSuchElementException the collection is empty
 * or <code>r</code> is not a valid position
**/

	public E repositionElementByRank(int r, Comparator<? super E> comp) {
		if (isEmpty() || r < 0 || r >= getSize())
			throw new NoSuchElementException();
		version.increment();   //invalidate all markers for iteration
		return (E) repositionElementByRankImpl(0, getSize()-1, r, comp);
	}

/**
 * @param left the leftmost position within the subarray
 * @param right the rightmost position with the subarray
 * @param r the rank of the desired element
 * @param comp the comparator to use when comparing the elements
 * <BR> 
 * REQUIRES: 
 * <code>left</code> &le; r &le; <code>right</code>
**/

	E repositionElementByRankImpl(int left, int right, int r, Comparator<? super E> comp) {
		swap(getMedianOfThree(left, right, comp), right);
		int mid = partition(left, right, comp);  //invalidates markers if a swap occurs
		if (r == mid)      //rank r element is in its place
			return get(r);
		else if (r < mid)  //rank r in part left of mid
			return repositionElementByRankImpl(left, mid-1, r, comp);
		else               //rank r in part right of mid
			return repositionElementByRankImpl(mid+1, right, r, comp);
	}


	public class BasicMarker extends AbstractCollection<E>.AbstractLocator<E> {

		int pos;	//position for the marker

/**
 * @param p the position to store within the marker.
 * @throws IllegalArgumentException the property MarkerLoc would be violated
**/

		public BasicMarker(int p) {
			if (p < FORE || p > size)
				throw new IllegalArgumentException();
			this.pos = p;
			updateVersion(); //initialize modification count
		}

/**
 * @return true  if and only if the locator is at an element of the collection
**/

		public boolean inCollection() {
			return (pos > FORE && pos < size);
		}

/**
 * @return the element stored at the current locator position.
 * @throws NoSuchElementException the locator is
 * at FORE or AFT
**/

		@SuppressWarnings("unchecked")

		public E get() {
			if (!inCollection())
				throw new NoSuchElementException();
			return (E) a[getIndex(pos)];
		}

/**
 * Moves the locator forward by one position
 * @return true  if and only if after the update the locator is
 * still at a position in the collection.
 * @throws AtBoundaryException the locator is at AFT since there is
 * no place to advance.
**/

		public boolean advance() throws ConcurrentModificationException {
			checkValidity();      //check if marker has been invalidated
			if (pos >= size)      //by MarkerLoc, marker is at AFT
				throw new AtBoundaryException("Already after end.");
			pos++;                //move to the next position
			return (pos != size); //true iff marker now at AFT	
		}

/**
 * Moves the locator to the previous position
 * @return true  if and only if after the update the locator is still at a
 * valid position.
 * The user program can use this return value to
 * recognize when the locater has reached FORE.
 * @throws AtBoundaryException the locator is at FORE since then there is
 * no place to retreat.
**/

		public boolean retreat() throws ConcurrentModificationException {
			checkValidity();     //check if marker has been invalidated
			if (pos == FORE)     //by MarkerLoc, marker is at FORE
				throw new AtBoundaryException("Already before front.");
			if (pos > size)      //if marker at AFT
				pos = size;         //make sure its value is size
			pos--;               //move to the previous position
			return (pos != FORE);
		}	

/**
 * @return true  if there is some element after the current locator position.
**/

		public boolean hasNext() throws ConcurrentModificationException {
			checkValidity();      //check if marker has been invalidated
			return (pos < size-1);
		}

/**
 * Removes the element at the locator and updates the locator to be
 * at the element in the collection preceding the one deleted.
 * @throws NoSuchElementException the locator is at FORE or AFT
**/

		public void remove()  throws ConcurrentModificationException {
			if (!inCollection())
				throw new NoSuchElementException();
			removeRange(pos,pos);  //invalidates all active locators
			updateVersion();       //ensures that this locator is still valid
			retreat();
		}


	}

	public class Marker extends BasicMarker implements PositionalCollectionLocator<E> {

		public Marker(int p) {
			super(p);
		}

/**
 * @return the user position of the marker.
**/

		public int getCurrentPosition() throws ConcurrentModificationException {
			if (!inCollection())
				throw new NoSuchElementException();
			return pos;
		}

/**
 * Adds <code>value</code> to the collection after the marker location
 * @param value the element to be added
 * @return null
 * @throws RuntimeException the marker is at AFT
**/

		public PositionalCollectionLocator<E> addAfter(E value)
		 					throws ConcurrentModificationException {
			checkValidity();                //check if locator is valid
			if (pos >= size)                //marker is at AFT
				throw new RuntimeException("can't add past the end");
			PositionalCollectionLocator<E> result;
			result = addImpl(pos+1,value);  //invalidates all active locators
			updateVersion();                //ensures that this locator is still valid
			return result;
		}


	}
}
