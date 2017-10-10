// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import goldman.Objects;
import goldman.collection.AbstractCollection;
import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.positional.Array;
import goldman.collection.positional.DynamicArray;

import java.util.Comparator;
import java.util.NoSuchElementException;
/**
 * The sorted array provides very efficient use of space and
 * the fastest search time independent of the access pattern.
 * Also, the sorted array provides constant time access
 * to the element at a given rank.   However,
 * this data structure has a very significant drawback: adding or removing
 * an element requires shifting all elements that follow it.  Thus, this
 * data structure is a good choice only when the elements added or removed are
 * near the maximum, or when there are few mutations after inserting all elements.
**/

public class SortedArray<E> extends AbstractCollection<E> implements 
	OrderedCollection<E> {

	Array<E> a;


	public SortedArray() { 
		this(Objects.DEFAULT_COMPARATOR);  //internally uses a dynamic array
	}

/**
 * @param comp the comparator to use to
 * order the elements
**/

	public SortedArray(Comparator<? super E> comp){
		super(comp);
		a = new DynamicArray<E>();  //internally use a dynamic array
	}

/**
 * This constructor should not
 * be used if the array should be automatically resized.
 * @param capacity the capacity for
 * the array
**/

	public SortedArray(int capacity){
		this(Objects.DEFAULT_COMPARATOR, capacity);
	}

/**
 * This constructor should not
 * be used if the array should be automatically resized.
 * @param comp the user-provided
 * comparator
 * @param capacity the capacity for
 * the array
**/

	public SortedArray(Comparator<? super E> comp, int capacity){
		super(comp);
		a = new Array<E>(capacity);  //internally use an array
	}	


	public boolean isEmpty(){
		return a.isEmpty();
	}


	public int getSize(){
		return a.getSize();
	}


	public int getCapacity(){
		return a.getCapacity();
	}


	public void ensureCapacity(int desiredCapacity){
		a.ensureCapacity(desiredCapacity);
	}


	public void trimToSize(){
		a.trimToSize();
	}

/**
 * @param left the beginning index of the portion of the array to search
 * @param right the ending index of the portion of the array to search
 * @param target the value to search for
 * <BR> 
 * REQUIRES: 
 * that
 * 0 &le; <code>left</code> &le; <code>right</code> &lt; <code>size</code> (referred to
 * as condition P_INDEX),
 * and that a[<code>left</code>-1] &lt; <code>target</code> &lt; a[<code>right</code>+1]
 * (referred to as condition P_VALUE).
 * @return the
 * position where the target was found, or otherwise its insert position
**/

	int binarySearch(int left, int right, E target) {
			int mid = (left + right)/2; //index midway between left and right
			int comparison = comp.compare(target, a.get(mid));	
			if (right == left) //case 1a and 1b
				return (comparison <= 0) ? left : right+1;	
			else if (comparison == 0 || (comparison < 0 && left == mid)) //case 2a and 2b
				return mid;
			else if (comparison < 0) //case 3
				return binarySearch(left, mid-1, target);
			else  //case 4
				return binarySearch(mid+1, right, target);
	}

/**
 * @param element the target
 * @return the position where an equivalent element occurs,
 * or otherwise the insert position for <code>element</code>
**/

	int find(E element){
		if (isEmpty())
			return 0;		
		else
			return binarySearch(0,a.getSize()-1,element);
	}

/**
 * @param target the element being tested for membership in the collection
 * @return true  if and only if an equivalent element exists in the collection
**/

	public boolean contains(E target) {
		int pos = find(target);
		return (pos <= a.getSize()-1 && comp.compare(a.get(pos), target) == 0);
	}

/**
 * @param r the desired rank
 * @return the r<sup>th</sup> element in the
 * sorted order, where r=0 is the minimum element
 * @throws IllegalArgumentException r &lt; 0 or r &ge; n
**/

	public E get(int r){
		if (r < 0 || r >= getSize())
			throw new IllegalArgumentException(""+r);
		return a.get(r);
	}

/**
 * @param element the target
 * @return an equivalent element from the collection
 * @throws NoSuchElementException there is no equivalent element in this
 * collection.
**/

	public E getEquivalentElement(E element) {
		int pos = find(element);
		if (pos <= a.getSize()-1 && comp.compare(a.get(pos), element) == 0)
			return a.get(pos);
		else
			throw new NoSuchElementException();
	}

/**
 * @return a smallest element
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public E min() {
		return a.get(0);
	}

/**
 * @return a largest element
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public E max() {
		return a.get(a.getSize()-1);
	}

/**
 * @param left the beginning index of the portion of the array to search
 * @param right the ending index of the portion of the array to search
 * @param target the value to search for
 * <BR> 
 * REQUIRES: 
 * that
 * 0 &le; <code>left</code> &le; <code>right</code> &lt; <code>size</code>
 * and that a[<code>left</code>-1] &lt; <code>target</code> &lt; a[<code>right</code>+1].
 * @return the insert position for the element that would place it before
 * any equivalent elements
**/

	protected int findFirstInsertPosition(int left, int right, E target) {
		int mid = (left + right)/2; //index midway between left and right
		int comparison = comp.compare(target, a.get(mid));
		if (right == left)
			return (comparison <= 0) ? left : right+1;
		if (comparison <= 0) {
			if (mid == left || (comparison == 0 && comp.compare(a.get(mid-1),target) != 0))
				return mid;
			else
				return findFirstInsertPosition(left, mid-1, target);
		} else
			return findFirstInsertPosition(mid+1, right, target);
	}

/**
 * This method does not require that <code>target</code>
 * be in the collection.
 * @param target the element for which to find
 * the predecessor
 * @return the largest
 * element in the ordered collection that is less than <code>target</code>.
 * @throws NoSuchElementException no element in the collection
 * is smaller than <code>target</code>
**/

	public E predecessor(E target) {
		if (isEmpty())
			throw new NoSuchElementException();
		int p = findFirstInsertPosition(0,a.getSize()-1,target);
		if (p == 0)
			throw new NoSuchElementException();
		else
			return a.get(p-1);
	}

/**
 * @param left the beginning index of the portion of the array to search
 * @param right the ending index of the portion of the array to search
 * @param target the value to search for
 * <BR> 
 * REQUIRES: 
 * that
 * 0 &le; <code>left</code> &le; <code>right</code> &lt; <code>size</code>
 * and that a[<code>left</code>-1] &lt; <code>target</code> &lt; a[<code>right</code>+1].
 * @return the
 * last position where the element occurs, or otherwise the insert position
**/

	protected int findLastInsertPosition(int left, int right, E target) {
		int mid = (left + right)/2; //index midway between left and right
		int comparison = comp.compare(a.get(mid),target);
		if (comparison == 0 &&
				(mid == right || comp.compare(a.get(mid+1),target) != 0))  //Case 1
			return mid+1;  
		if (right == left)   // Case 2
			return (comparison > 0 ? left : right+1);
		if (comparison > 0)    // Case 3
			return (mid == left ? left : findLastInsertPosition(left, mid-1, target));
		else 			      // Case 4
		    return findLastInsertPosition(mid+1, right, target);
	}

/**
 * This method does not require that <code>target</code>
 * be in the collection.
 * @param target the element for which to find
 * the successor
 * @return the smallest
 * element in the ordered collection that is greater than <code>target</code>
 * @throws NoSuchElementException no element in the collection
 * is greater than <code>target</code>
**/

	public E successor(E target) {
		if (isEmpty())
			throw new NoSuchElementException();
		int p = findLastInsertPosition(0,a.getSize()-1,target);
		if (p == a.getSize())
			throw new NoSuchElementException();
		else 
			return a.get(p);
	}

/**
 * Inserts it into the collection.
 * @param element the new element
**/

	public void add(E element) {
		if (a.isEmpty() || comp.compare(element,a.get(a.getSize()-1)) >= 0)  // new element is max
			a.addLast(element);
		else
			a.add(find(element),element);
	}

/**
 * Adds all elements in <code>c</code> to the collection.
 * @param c the collection to be added
**/

	public void addAll(Collection<? extends E> c) {
		a.addAll(c);
		a.quicksort();
	}

/**
 * Removes
 * an arbitrary element from the collection
 * equivalent to <code>element</code>, if such an element exists in the collection.
 * @param element the element to remove
 * @return <code>true</code> if an element was removed, and <code>false</code> otherwise.
**/

	public boolean remove(E element) {
		int pos = find(element);
		if (pos == getSize() || comp.compare(element, a.get(pos)) != 0)
			return false;
		a.remove(pos);
		return true;
	}

/**
 * Updates the current collection to contain only
 * elements that are also in <code>c</code>
 * @param c a collection
**/

	public void retainAll(Collection<E> c) {
		if (isEmpty()) 
			return;
		if (c instanceof OrderedCollection 
				&& c.getComparator().equals(getComparator())) { //special case for efficiency
			int newSize = 0;   //number of elements retained in this collection
			int p = 0;  //position of next element to consider in this collection
			for (E element: c) {  //c is sorted
				while (p < getSize() && comp.compare(element, get(p)) > 0)
					p++;
				while (p < getSize() && comp.compare(element,get(p++)) == 0) 
					a.set(newSize++, element);   //if tracked instead use a swap
			}
			a.removeRange(newSize, getSize()-1);
		}
		else
			a.retainAll(c);
	}

/**
 * @param pos the desired position
 * @param value the value to be added at position <code>pos</code>
 * @throws PositionOutOfBoundsException <code>pos</code> is
 * not a valid position in <code>a</code>.
 * @throws IllegalArgumentException performing
 * this operation would violate the property Ordered
**/

	protected void set(int pos, E value){
		if ((pos > 0) && comp.compare(value, a.get(pos-1)) < 0)
			throw new IllegalArgumentException("value " + value +
					"is smaller than the element at position " + (pos-1));
		if ((pos < a.getSize()-1) && comp.compare(value,a.get(pos+1)) > 0)
			throw new IllegalArgumentException("value " + value +
					" is larger than the element at position " + (pos+1));
		a.set(pos,value);
	}

/**
 * @param pos the desired position
 * @param element the element to be added at position <code>pos</code>
 * @throws PositionOutOfBoundsException <code>pos</code> is
 * not a valid position for adding to <code>a</code>.
 * @throws IllegalArgumentException performing
 * this operation would violate the property Ordered
**/

	protected void add(int pos, E element){
		if ((pos > 0) && comp.compare(element, a.get(pos-1)) < 0)
			throw new IllegalArgumentException("value " + element +
					"is smaller than the element at position " + (pos-1));
		if ((pos < a.getSize()-1) && comp.compare(element,a.get(pos)) > 0)
			throw new IllegalArgumentException("value " + element +
					" is larger than the element currently at position " + pos);
		a.add(pos,element);
	}

/**
 * @param pos the position of the
 * element to removed
 * @return the removed element
 * @throws PositionOutOfBoundsException <code>p</code> is
 * not a valid position in <code>a</code>.
**/

	protected E remove(int pos){
		return a.remove(pos);
	}


	public Locator<E> iterator() {
		return a.new BasicMarker(Array.FORE);
	}


	public Locator<E> iteratorAtEnd() {
		return a.new BasicMarker(a.getSize());
	}


	public Locator<E> getLocator(E element) {
		int pos = find(element);
		if (pos >= a.getSize() || comp.compare(a.get(pos), element) != 0)
			throw new NoSuchElementException();
		else
			return a.new BasicMarker(pos);
	}


}
