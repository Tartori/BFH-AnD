// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import goldman.Objects;

import java.util.Arrays;
import java.util.Comparator;
/**
 * This array-based data structure allows element 0 of the positional
 * collection to be in any slot of the underlying array, with the range of underlying
 * indices wrapping around as needed.  Managing this introduces a small
 * amount of overhead, but enables
 * efficient insertion and deletion near either the front or end of the collection.
 * Like Array, there is no support for automatic resizing.  This is an untracked implementation.
**/

public class CircularArray<E> extends Array<E> {

	protected int start; 	//index where the collection starts

/**
 * Creates a circular array with the given capacity that uses
 * the provided equivalence tester
 * @param capacity the desired initial capacity for the underlying array
 * @param equivalenceTester a user-provided equivalence tester
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public CircularArray(int capacity, Comparator<? super E> equivalenceTester){
		super(capacity, equivalenceTester);
		start = 0;
	}  

/**
 * Creates a circular array with a default initial capacity that uses
 * the default equivalence tester.
**/

	public CircularArray(){
		this(DEFAULT_CAPACITY, Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * Creates a circular array with the given capacity that uses the default
 * equivalence tester
 * @param capacity the desired initial capacity
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public CircularArray(int capacity) {
		this(capacity, Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * @param p a valid user position
 * @return the corresponding index in the underlying array <code>a</code>
**/

	protected final int getIndex(int p){
		int loc = start + p;
		if (loc < a.length)
			return loc;
		else 
			return loc - a.length;
	}	

/**
 * @param index an index of <code>a</code> that is in use
 * <BR> 
 * REQUIRES: 
 *  <code>index</code>
 * is a valid position
 * @return the corresponding user position.
**/

	protected final int getPosition(int index){
		int pos = index - start;
		if (pos >= 0)
			return pos;
		else 
			return pos + a.length;
	}

/**
 * The next index is computed without regard to the contents of
 * that slot.  If the parameter is the index of the last element of the
 * collection, then <code>nextIndex</code> returns the index of the slot
 * immediately after the end of the collection (wrapping if required),
 * unless the <code>CircularArray</code> is at capacity, in which the index returned
 * is that of the first element in the collection.
 * @param index an underlying index of <code>a</code>
 * @return the underlying index for the next element in the collection
**/

	protected final int nextIndex(int index){	
		if (index < a.length - 1)
			return index+1;
		else
			return 0;
	}

/**
 * If the parameter is the index of the first element of the
 * collection, then <code>prevIndex</code> returns the index of the slot
 * immediately before the start of the collection (wrapping if required),
 * unless the <code>CircularArray</code> is at
 * capacity, in which
 * case the index returned is that of the last element in the collection.
 * @param index an underlying index
 * @return the underlying index for the previous element in the collection
**/

	protected final int prevIndex(int index){
		if (index > 0)
			return index-1;
		else 
			return a.length-1;
	}

/**
 * @param newArray the array to which the elements should be moved
 * @return the parameter value for convenience
**/

	@SuppressWarnings("unchecked")

	protected Object[] moveElementsTo(Object[] newArray) {
		if (a != null) {
			if (start+size <= a.length)  //a doesn't wrap
				System.arraycopy(a, start, newArray, 0, size);
			else{ // a wraps, so copy in two sections
				System.arraycopy(a, start, newArray, 0, a.length-start);
				System.arraycopy(a, 0, newArray, a.length-start, size-a.length+start);
			}
		}
		start = 0;
		return newArray;
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
 * 0 &lt; <code>num</code> &le; <code>a.length</code> - (<code>p2</code> - <code>p1</code> + 1).
 * The amount of the shift is at least 1 and is not so much that
 * the portion of the array being shifted would intersect with itself.
**/

	protected void shiftLeft(int p1, int p2, int num){
		int numToMove = p2 - p1 + 1;
		int i = getIndex(p1);  // underlying index for p1
		int j = getIndex(p2);  // underlying index for p2
		int cap = a.length; //capacity of a
		if (i <= j){// Case 1: a[i]...a[j] doesn't wrap
			if (i >= num)  // Case 1a: a[i]...a[j] moves left, won't wrap
				System.arraycopy(a, i, a, i-num, numToMove);
			else if (num >= j+1) //Case 1b: shifts past array boundary, won't wrap
				System.arraycopy(a, i, a, i-num+cap, numToMove);
			else { // Case 1c: i < num < j+1, a[i],...a[j] will now wrap
				System.arraycopy(a, i, a, i-num+cap, num-i);  //block 1
				System.arraycopy(a, num, a, 0, numToMove-(num-i));  //block2
			}
		} else { // Case 2: a[i]...a[j] does wrap
			System.arraycopy(a, i, a, i-num, cap-i); //block 1 (2a and 2b)
			if (num > j)  // Case 2a: all of front wraps to back
				System.arraycopy(a, 0, a, cap-num, j+1);  //block 2
			else { // Case 2b: portion of front wraps to back
				System.arraycopy(a, 0, a, cap-num, num);  //block2
				System.arraycopy(a, num, a, 0, j+1-num);  //block3
			}
		}
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
 * 0 &lt; <code>num</code> &le; <code>a.length</code> - (<code>p2</code> -<code>p1</code> + 1).
 * The amount to shift is at least 1, and
 * the portion of the array being shifted does intersect with itself
**/

	protected void shiftRight(int p1, int p2, int num){
		int numToMove = p2 - p1 + 1;
		int i = getIndex(p1);
		int j = getIndex(p2);
		int cap = a.length;
		if (i <= j){// a[i]...a[j] doesn't wrap
			if (num <= cap-j-1)    // a[i]...a[j] moves right, doesn't wrap
				System.arraycopy(a, i, a, i+num, numToMove);
			else if (num >= cap-i)  // moves right past array boundary
				System.arraycopy(a, i, a, i+num-cap, numToMove);
			else {  //n-j+1 < num < n-i, a[i]...a[j] will wrap
				System.arraycopy(a, cap-num, a, 0, numToMove-cap+num+i);
				System.arraycopy(a, i, a, num+i, cap-(num+i));
			}
		} else { // a[i]...a[j] does wrap
			System.arraycopy(a, 0, a, num, j+1); //shift a[0]..a[j] right num slots
			if (num >= cap-i)  // all of end wraps to front
				System.arraycopy(a, i, a, 0, num);
			else  {           // only portion of end wraps to front
				System.arraycopy(a, cap-num+1, a, 0, num);
				System.arraycopy(a, i, a, i+num, cap-i-num);
			}
		}
	}

/**
 * Moves elements in such a way to
 * increment the position number for the elements that were at
 * positions <code>p</code>, ..., <code>size</code>-1 to be at positions
 * <code>p+1</code>, ..., <code>size</code>.
 * @param p a valid user position where the gap is to be created
**/

	protected void createGap(int p) {
		if (p + 1 <= size - p){ // if # to move left <= # to move right
			if (p != 0)				      //and not adding to front
				shiftLeft(0, p-1, 1);     //move front portion left
			start = prevIndex(start);	  //back up start by one
		} 
		else if (p !=size)                //if not adding to back
			shiftRight(p,size-1,1);       //move end portion right
	}

/**
 * Sets positions <code>pos1</code>, ..., <code>pos2</code> to null.
 * @param pos1 a valid position
 * @param pos2 a valid position
 * <BR> 
 * REQUIRES: 
 * that
 * <code>pos1</code> &le; <code>pos2</code>.
**/

	protected void setToNull(int pos1, int pos2){
		int i = getIndex(pos1); //index for pos1
		int j = getIndex(pos2); //index for pos2
		if (i <= j) //circular array does not wrap
			Arrays.fill(a,i,j+1,null);
		else { //circular array wraps
			Arrays.fill(a,i,a.length,null); 
			Arrays.fill(a,0,j+1,null);
		}
	}

/**
 * Moves elements to decrement the position number for elements that were at
 * positions <code>toPos</code>+1, ..., <code>size</code>-1 to positions
 * <code>fromPos</code>, ...,
 * <code>size</code>-1 - (<code>toPos</code> - <code>fromPos</code> + 1).
 * Furthermore, the indices in the underlying array that are no longer in use
 * are set to null.
 * @param fromPos the first position in the gap to close
 * @param toPos the last position in the gap to close
**/

	protected void closeGap(int fromPos, int toPos){
		int sizeOfFrontPortion = fromPos;      //introduced for clarity
		int sizeOfBackPortion = size-toPos-1;  //also introduced for clarity
		int numRemoving = toPos - fromPos + 1; //number of elements removed
		if (sizeOfBackPortion <= sizeOfFrontPortion){ //shift back portion left
			if (toPos != size-1)
				shiftLeft(toPos+1,size-1,numRemoving);
			setToNull(size-numRemoving,size-1);
		} else {                                      //shift front portion right
			if (fromPos != 0)
				shiftRight(0,fromPos-1,numRemoving);
			setToNull(0,numRemoving-1);
			start = getIndex(numRemoving); //i.e., (start+numRemoving)%a.length
		}
	}


}
