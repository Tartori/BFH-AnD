// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.priority;
import goldman.collection.Collection;
/**
 * A priority queue is an untagged algorithmically
 * positioned collection of comparable elements in which there
 * can be equivalent elements.  The primary methods are to
 * add an element, find a maximum priority element, and
 * remove a maximum priority element.  In addition,
 * one can remove an element or change the priority of
 * an element through a locator.  However, there is no
 * efficient general search method.
**/

public interface PriorityQueue<E> extends Collection<E> {
/**
 * Removes and returns a highest priority element.
 * It throws a
 * <code>NoSuchElementException</code> when this collection is empty.
**/

	public E extractMax();
/**
 * Returns a
 * priority queue locator that has been set to the given element.
 * It throws a <code>NoSuchElementException</code>
 * if there is no equivalent element in this collection.
**/

	public PriorityQueueLocator<E> getLocator(E element);
/**
 * Returns a highest priority element.  It throws a
 * <code>NoSuchElementException</code> when this collection is empty.
**/

	public E max();

}
