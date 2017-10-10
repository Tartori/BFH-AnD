// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import goldman.collection.AtCapacityException;
/**
 * For many applications, elements need
 * only be added or removed from the front or back end of the collection.
 * Data structures designed for such
 * settings can gain efficiency by limiting access to the ends.
 * The buffer is the most general ADT for this
 * category.   A buffer allows the user to insert or
 * remove an element from either end of the collection.
**/

public class Buffer<E> {

	static final int DEFAULT_CAPACITY = 8; //default capacity
	private PositionalCollection<E> buffer;  //stores the buffer elements
	private int bound; //max number of elements to be held in the buffer


/**
 * Creates a buffer satisfying the specification of the
 * given parameters
 * @param capacity the initial capacity for the underlying positional collection
 * @param bounded a boolean which is true if and only if the buffer should be bounded
 * at the given capacity
 * @param tracked a boolean which is true if and only if the buffer should support a tracker
**/

	public Buffer(int capacity, boolean bounded, boolean tracked){
		if (bounded)
			bound = capacity;
		else
			bound = java.lang.Integer.MAX_VALUE;
		if (tracked)
			buffer = new DoublyLinkedList<E>();
		else if (bounded)
			buffer = new CircularArray<E>(capacity);
		else
			buffer = new DynamicCircularArray<E>(capacity);
	}

/**
 * Creates an unbounded, untracked buffer
 * with a default initial capacity
**/

	public Buffer(){
		this(DEFAULT_CAPACITY,false,false);
	}

/**
 * Creates an unbounded, untracked buffer
 * with the given initial capacity
 * @param capacity the initial capacity for the underlying positional collection
**/

	public Buffer(int capacity){
		this(capacity,false,false);
	}

/**
 * Creates an untracked buffer with the specified parameters
 * @param capacity the initial capacity for the underlying positional collection
 * @param bounded a boolean which is true if and only if the buffer should be bounded
**/

	public Buffer(int capacity, boolean bounded){
		this(capacity,bounded,false);
	}

/**
 * @return true  if and only if no elements are stored in
 * the collection
**/

	public boolean isEmpty() {
		return buffer.isEmpty();		
	}

/**
 * @return the number of elements stored in the
 * buffer
**/

	public int getSize() {
		return buffer.getSize();
	}


	public String toString() {
		return buffer.toString();
	}

/**
 * @param value the target
 * @return true  if and only if an equivalent value exists in the buffer
**/

	public boolean contains(E value) {
		return buffer.contains(value);
	}

/**
 * @return the first element
 * in the buffer, leaving the buffer unchanged
**/

	public E getFirst() {
		return buffer.get(0);
	}

/**
 * @return the last
 * element in the buffer, leaving the buffer unchanged
**/

	public E getLast() {
		return buffer.get(buffer.getSize()-1);
	}

/**
 * @param element the element that is to be inserted at the
 * front of the buffer.
 * @throws AtCapacityException a bounded buffer is full
**/

	public void addFirst(E element) {
		if (buffer.getSize() == bound)  //always false for unbounded buffer
			throw new AtCapacityException();
		buffer.addFirst(element);
	}

/**
 * @param element the element that is to be inserted at the end
 * of the buffer.
 * @throws AtCapacityException a bounded buffer is full
**/

	public void addLast(E element) {
		if (buffer.getSize() == bound)
			throw new AtCapacityException();
		buffer.addLast(element);
	}

/**
 * Removes the first element in the buffer,
 * @return the removed element
 * @throws NoSuchElementException the buffer is empty
**/

	public E removeFirst() {
		return buffer.removeFirst();
	}

/**
 * Removes the last element in the buffer
 * @return the element that was removed.
 * @throws NoSuchElementException the buffer is empty
**/

	public E removeLast() {
		return buffer.removeLast();
	}

/**
 * Removes all elements from the buffer.
**/

	public void clear() {
		buffer.clear();
	}

/**
 * Creates a new locator that starts just before the first item in the buffer.
**/

	public PositionalCollectionLocator<E> iterator() {
		return buffer.iterator();
	}


}
