// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;

import java.util.Comparator;
import goldman.Objects;
/**
 * This array-based data structure allows element 0 of the positional
 * collection to be in any slot of the underlying array, with the range of underlying
 * indices wrapping around as needed, and also performs automatic resizing.
 * It is best if the size of
 * the collection is not known a priori and if some insertions and/or
 * deletions tend to occur near the front portion of the collection.
 * As with DynamicArray, the application program can provide an initial size in the
 * constructor and use <code>ensureCapacity</code> and <code>trimToSize</code>
 * to optimize performance.  This is an untracked implementation.
**/

public class DynamicCircularArray<E> extends CircularArray<E> implements PositionalCollection<E> {

	int minCapacity; // capacity from last ensureCapacity (or init capacity)
	int lowWaterMark; // min value for size (unless smaller than minCapacity)
	int highWaterMark; // max value for size

	public DynamicCircularArray(int capacity, Comparator<? super E> equivalenceTester) {
		super(capacity, equivalenceTester);
		minCapacity = capacity;
	}
	
	public DynamicCircularArray(){
		this(DEFAULT_CAPACITY);
	}
	
	public DynamicCircularArray(int capacity){
		this(capacity, Objects.DEFAULT_EQUIVALENCE_TESTER);	
	}
		
    protected void resizeArray(int desiredCapacity){
		super.resizeArray(desiredCapacity);
		lowWaterMark = (int) Math.ceil(a.length/4);
		highWaterMark = a.length;
    }
    
    public void ensureCapacity(int capacity) {
    		if (a.length < capacity){
    			minCapacity = capacity;
    			resizeArray(capacity);
    		}
	}

	protected PositionalCollectionLocator<E> addImpl(int p, Object value) {
		if (size == highWaterMark)
			resizeArray(2*size);
		return super.addImpl(p,value);
	}

	public void removeRange(int fromPos, int toPos) {
		super.removeRange(fromPos,toPos);
		if (size == lowWaterMark)
			resizeArray(Math.max(2*size,minCapacity));
	}
}
