// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
import java.util.Comparator;

/**
 * The <code>Collection</code> interface contains the operations that must
 * be supported by all data structures that maintain a collection of elements.
 * Checks, for the purposes of checking correctness,
 * that the correctness properties are preserved.
**/

public interface Collection<E> extends Iterable<E>{
	public void checkRep();
/**
 * Traverses each element of this collection,
 * in the iteration order, on behalf of the visitor.
**/

	 public void accept(Visitor<? super E> v);
/**
 * Inserts <code>value</code> into
 * the collection in an arbitrary location.  If a tracker is to be returned
 * then the method <code>addTracked</code> which is part of the
 * <code>Tracked</code> interface should be called instead.
 * An <code>AtCapacityException</code> (an unchecked exception) is thrown when a bounded
 * collection is already at capacity.
**/

	public void add(E value);
/**
 * Adds all elements in <code>c</code>
 * to this collection.
**/

 	public void addAll(Collection<? extends E> c);
/**
 * Removes all elements from this collection.
**/

	public void clear();
/**
 * Returns true  if an element
 * equivalent to target exists in this collection.  Otherwise false  is returned.
**/

	public boolean contains(E target);
/**
 * Increases the capacity
 * of this collection, if necessary, to ensure that it can hold
 * at least <code>capacity</code> elements.  For elastic implementations, this
 * method does nothing.
**/

	public void ensureCapacity(int capacity);
/**
 * Returns the current capacity of this collection.
 * For elastic implementations
 * <code>Integer.MAX_VALUE</code> is returned.
**/

	public int getCapacity();
/**
 * Returns
 * the comparator for elements in this collection.
**/

	public Comparator<? super E> getComparator();
/**
 * Returns an element in
 * the collection that is equivalent to <code>target</code>.  It
 * throws a <code>NoSuchElementException</code> when there is no equivalent
 * element.  The <code>contains</code> method should be used to
 * determine if an element is in the collection.
**/

	public E getEquivalentElement(E target);
/**
 * Returns a locator that has been initialized
 * to an element equivalent to <code>target</code>.  Like the <code>iterator</code> method, this method enables
 * navigation, but from a specified starting point.  This method
 * throws a <code>NoSuchElementException</code> if there is no equivalent element
 * in the collection.
**/

	public Locator<E> getLocator(E target);
/**
 * Returns the number of elements, size, in this collection.
**/

	public int getSize();
/**
 * Returns true if this collection
 * contains no elements, and otherwise returns false.
**/

	public boolean isEmpty();
/**
 * Returns a locator that has been initialized to
 * FORE.  The locator returned can be
 * used to navigate within the collection and to remove the element currently
 * associated with the locator.
 * In general, no assumption is made about the iteration order, the order in
 * which the iterator advances through the collection.  However, some collection
 * ADTs specify a particular iteration order.
**/

	public Locator<E> iterator();
/**
 * Removes from this collection
 * an arbitrary element equivalent to <code>target</code>, if such an element exists
 * in the collection.   It
 * returns <code>true</code> if an element was removed, and <code>false</code> otherwise.
**/

	public boolean remove(E target);
/**
 * Removes from this collection all
 * elements for which there is no equivalent element in <code>c</code>. Thus, the elements that
 * remain are those in the intersection of c and this collection.
**/

 	public void retainAll(Collection<E> c);
/**
 * Returns a Java primitive array of length n
 * that holds the elements in this collection in the iteration order.
**/

	public Object[] toArray();
/**
 * Fills a Java array with
 * the elements in this collection in the iteration order, and returns the array
 * that was filled.
 * If the array provided as a parameter is not large enough to hold all the elements
 * of the collection,
 * a new array of the same type is created with length n
 * and is returned instead of the provided one.
**/

	public E[] toArray(E[] a);
/**
 * Returns a string that includes each element in
 * this collection
 * (as produced by the <code>toString</code> method for that element), in the iteration order.
**/

	public String toString();
/**
 * Trims the capacity of
 * an oversized collection to exactly hold its current elements.  An application can use this
 * operation to minimize the space usage.  For elastic implementations, this
 * method does nothing.
**/

	public void trimToSize();
}
