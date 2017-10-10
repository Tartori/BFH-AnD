// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;

import goldman.Objects;

import java.util.Comparator;
/**
 * This array-based data structure provides space for a fixed number
 * of elements, which are stored in an underlying Java primitive array.
 * However unlike Array and CircularArray, it performs automatic resizing.
 * When the size of the collection
 * is unknown or changing, the support for automatic resizing is important.
 * However, the resizing introduces a small amount of additional computation whenever
 * an element is added or removed, and the resizing method has linear cost
 * (though the amortized cost is constant).  On average, the size of the array
 * is roughly 50\% larger than
 * that needed.   As with Array and CircularArray,
 * DynamicArray supports user-controlled resizing through <code>ensureCapacity</code>
 * and <code>trimToSize</code>.
 * When the size of the collection matches that of the user-specified capacity,
 * the performance of a dynamic array is almost identical to that of an array.
 * The advantage of using a dynamic array over an array is
 * that when the initial estimate for the size is wrong, then the data structure automatically
 * resizes, versus leaving it to the application program.
 * This is an untracked implementation.
**/

public class DynamicArray<E> extends Array<E> implements PositionalCollection<E> {

	int minCapacity;   //capacity from last ensureCapacity (or initial capacity)
	int lowWaterMark;  //size < lowWaterMark triggers shrinking
	int highWaterMark; //size > highWaterMark trigger growing

/**
 * Creates a dynamic array with the given capacity that uses
 * the provided equivalence tester
 * @param capacity the desired initial capacity for the underlying array
 * @param equivalenceTester a user-provided equivalence tester
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public DynamicArray(int capacity, Comparator<? super E> equivalenceTester) {
		super(capacity);
		minCapacity = capacity;
	}

/**
 * Creates an underlying
 * array <code>a</code> with a default initial capacity.
**/

	public DynamicArray(){
		this(DEFAULT_CAPACITY);
	}

/**
 * Creates an underlying
 * array <code>a</code> with the given capacity.
 * @param capacity the initial capacity for the underlying array <code>a</code>
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public DynamicArray(int capacity){
		this(capacity, Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * Changes the capacity of the underlying array
 * to <code>desiredCapacity</code> while maintaining the same positional collection.
 * @param desiredCapacity the desired capacity of the underlying array
 * @throws IllegalArgumentException executing it would
 * violate the property Capacity
**/

    protected void resizeArray(int desiredCapacity){
		super.resizeArray(desiredCapacity);
		lowWaterMark =  (int) Math.ceil(a.length/4);
		highWaterMark = a.length;
  
    }

/**
 * Increases the capacity of the underlying array, if
 * needed.
 * @param capacity the desired capacity
**/

    public void ensureCapacity(int capacity) {
  		minCapacity = capacity;
		if (a.length < capacity)
			super.ensureCapacity(capacity);
	}

/**
 * Inserts object <code>value</code> at position <code>p</code>
 * and increments the position number for the elements that were at
 * positions <code>p</code>, ..., <code>size</code>-1.
 * @param p a valid user position
 * @param value the object to insert
 * @return null for an untracked implementations
**/

	protected PositionalCollectionLocator<E> addImpl(int p, Object value) {
		if (size == highWaterMark)
			resizeArray(2*size);
		return super.addImpl(p,value);
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
		super.removeRange(fromPos,toPos);
		if (size <= lowWaterMark)
			resizeArray(Math.max(2*size,minCapacity));
	}


}
