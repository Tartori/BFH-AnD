// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import goldman.collection.AtCapacityException;
import goldman.collection.Locator;
/**
 * More specialized than a buffer, a stack is natural for applications that insert
 * and remove elements only at one end of the buffer.
 * That is, a stack implements a last-in-first-out (LIFO) line.
 * A stack is a more specialized abstraction
 * than a buffer that maintains a last-in,
 * first-out (LIFO) line.  A stack is logically viewed as a vertical
 * line in which elements are inserted and removed at the "top"
 * of the stack.
**/

public class Stack<E> {

	private PositionalCollection<E> stack;  // stores the stack elements
	private int bound;                      //maximum number of elements

/**
 * Creates a stack satisfying the given requirements
 * @param capacity the initial capacity for the stack
 * @param bounded a boolean that indicates if the stack should be bounded
 * @param tracked a boolean that indicates if a tracked implementation is needed
**/

	public Stack(int capacity, boolean bounded, boolean tracked){
		bound = bounded? capacity : java.lang.Integer.MAX_VALUE;
		if (tracked)	// use singly linked list if tracked
			stack = new SinglyLinkedList<E>();
		else  { // use an array-based approach if not tracked
			if (bounded)  // use an array if bounded and untracked
				stack = new Array<E>(capacity);
			else	 // use a dynamic array if unbounded and untracked
				stack = new DynamicArray<E>(capacity);
		}
	}

/**
 * Creates an bounded, untracked stack
 * with a default initial capacity
**/

	public Stack(){
		this(Buffer.DEFAULT_CAPACITY, true, false);
	}

/**
 * Creates an unbounded, untracked stack
 * with the given capacity
 * @param capacity the initial capacity for the stack
**/

	public Stack(int capacity){
		this(capacity, false, false);
	}

/**
 * Creates an untracked stack satisfying the given requirements
 * @param capacity the initial capacity for the stack
 * @param bounded which indicates if the stack should be bounded
**/

	public Stack(int capacity, boolean bounded){
		this(capacity, bounded, false);
	}

/**
 * Returns true  if and only if the given value is contained within the stack.
**/

	public boolean contains(E value) {return stack.contains(value);}

/**
 * Removes all elements from the stack.
**/

	public void clear() {stack.clear();}

/**
 * Returns the number of elements in the stack.
**/

	public int getSize() {return stack.getSize();}

/**
 * Returns true  if and only if there are no elements in the stack.
**/

	public boolean isEmpty() {return stack.isEmpty();}

/**
 * Returns an iterator initialized at FORE.
**/

	public Locator<E> iterator() {return stack.iterator();}

/**
 * Returns a string representation of the elements in the stack.
**/

	public String toString() {return stack.toString();}
/**
 * Returns the object on the top of the
 * stack.  The stack is not changed
 * Returns the object on the top of the stack.  The stack is not changed.
**/

	public E peek() {
		if (stack instanceof Array)
			return stack.get(getSize()-1);
		else
			return stack.get(0);
	}

/**
 * Inserts the given element at the top of the stack.
 * @param element the new element to insert
 * @throws AtCapacityException a bounded stack is full
**/

	public void push(E element) {
		if (stack.getSize() == bound)  //always false when unbounded
			throw new AtCapacityException();
		if (stack instanceof Array)
			stack.addLast(element);
		else
			stack.addFirst(element);
	}

/**
 * Removes the item at the top of the stack
 * @return the removed element
 * @throws NoSuchElementException the stack is empty
**/

	public E pop() {
		if (stack instanceof Array)
			return stack.removeLast();
		else
			return stack.removeFirst();
	}


}
