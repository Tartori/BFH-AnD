// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import goldman.collection.Locator;
/**
 * A queue is a more specialized abstraction
 * than a buffer that
 * maintains a first-in, first-out (FIFO) line.
 * Elements can only be inserted at the back of the line and removed from
 * the front of the line.
**/

public class Queue<E> {

	Buffer<E> buffer;


	public Queue(){this(Buffer.DEFAULT_CAPACITY,false,false);}


	public Queue(int capacity){this(capacity,false,false);}


	public Queue(int capacity, boolean bounded){this(capacity,bounded,false);}


	public Queue(int capacity, boolean bounded, boolean tracked){
		buffer = new Buffer<E>(capacity,bounded,tracked);
	}

/**
 * Removes all elements from the queue.
**/

	public void clear() {buffer.clear();}	

/**
 * Returns true  if and only if the given value is contained within the queue.
**/

	public boolean contains(E value) {return buffer.contains(value);}

/**
 * Returns the number of elements in the queue.
**/

	public int getSize() {return buffer.getSize();}

/**
 * Returns true  if and only if there are no elements in the queue.
**/

	public boolean isEmpty() {return buffer.isEmpty();}

/**
 * Returns a string representation of the elements in the queue.
**/

	public String toString() {return buffer.toString();}

/**
 * Returns an iterator initialized at FORE.
**/

	public Locator<E> iterator() {return buffer.iterator();}

/**
 * Returns the first object in the queue.  The queue is not changed.
**/

	public E peek() {
		return buffer.getFirst();
	}

/**
 * Inserts <code>element</code> at the end of the queue
 * @param element the new element to insert
 * @throws AtCapacityException a bounded queue is full
**/

	public void enqueue(E element) {
		buffer.addLast(element);
	}

/**
 * Removes the element from the front of the queue.
 * @return the object that was removed
 * @throws NoSuchElementException the queue is empty
**/

	public E dequeue() {
		return buffer.removeFirst();
	}


}
