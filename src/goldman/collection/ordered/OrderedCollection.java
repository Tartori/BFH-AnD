// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import goldman.collection.Collection;
import goldman.collection.Locator;
/**
 * An ordered collection is an untagged algorithmically
 * positioned collection of comparable elements that
 * may contain duplicates.  That is, order is defined by a comparator
 * and the collection may
 * contain multiple "equal" elements and multiple occurrences
 * of the same element.  The primary methods are to
 * add an element, determine if an element is in the collection,
 * to remove an element, and to find the previous or next element
 * in the ordering defined by the comparator.
 * The iteration order must follow the ordering defined by
 * the comparator.  Data structures
 * for an ordered collection typically provide logarithmic time
 * implementations for these methods.  However, in some cases
 * the data structures support constant time implementations, or
 * require linear time implementations.
 * An ordered collection supports methods that concern order
 * such as <code>min</code>, <code>max</code>, <code>predecessor</code>, and
 * <code>successor</code> and methods that allow the
 * user application to efficiently perform queries such as a range search in which one can iterate
 * through all elements in the collection between a specified range of values.
**/

public interface OrderedCollection<E> extends Collection<E> {
/**
 * Returns the r<sup>th</sup> element in the
 * sorted order, where r=0 is the minimum element. It throws an
 * <code>IllegalArgumentException</code> when r &lt; 0 or r &ge; n.
**/

	public E get(int r);
/**
 * Returns an element in
 * the collection that is equivalent to <code>target</code> according to the comparator
 * associated with this collection.
 * It throws a <code>NoSuchElementException</code> when
 * there is no equivalent element.
**/

	public E getEquivalentElement(E target);
/**
 * Returns a
 * locator that has been initialized
 * to AFT.
**/

	public Locator<E> iteratorAtEnd();
/**
 * Returns a least element
 * in the collection (according to the comparator).  More
 * specifically, the first element in the iteration order
 * is returned.
 * This method throws a
 * <code>NoSuchElementException</code> when the collection is empty.
**/

	public E min();
/**
 * Returns a greatest element
 * in the collection (according to the comparator).  More
 * specifically, the last element in the iteration order
 * is returned.
 * This method throws a
 * <code>NoSuchElementException</code> when the collection is empty.
**/

	public E max();
/**
 * Returns a greatest
 * element in the ordered collection that is less than <code>x</code>.
 * If <code>x</code> is in the collection,
 * the element before the first occurrence of <code>x</code>
 * in the iteration order is returned.
 * Note that <code>x</code> need not be an element
 * of the collection for <code>predecessor</code> to return a value.  It throws
 * a <code>NoSuchElementException</code> when there is no element in the collection
 * smaller than <code>x</code>.  (The element returned would be the last element in the collection
 * returned by the method call <code>headSet(x)</code> in Java's <code>SortedSet</code> interface.)
**/

	public E predecessor(E x);
/**
 * Returns a least
 * element in the ordered collection that is greater than <code>x</code>.
 * If <code>x</code> is in the collection, the element after the last occurrence of
 * <code>x</code> in the iteration order is returned.
 * Note that <code>x</code> need not be an element
 * of the collection for <code>successor</code> to return a value.  It throws
 * a <code>NoSuchElementException</code> when there is no element in the collection
 * larger than <code>x</code>.  (The element returned would be the first element in the collection
 * returned by the method call <code>tailSet(x)</code> in Java's <code>SortedSet</code> interface.
 * If an application needs to iterate through <code>tailSet(x)</code>, the
 * <code>getLocator</code> method in conjunction with <code>advance</code> could
 * be used.
**/

	public E successor(E x);
}
