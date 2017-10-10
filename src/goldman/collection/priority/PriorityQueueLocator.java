// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.priority;
import goldman.collection.Locator;
/**
 * The <code>PriorityQueueLocator</code> interface
 * extends the <code>Locator</code> interface to add methods that are
 * specific to a priority queue.
**/

public interface PriorityQueueLocator<E> extends Locator<E> {
/**
 * Replaces the element associated
 * with this priority queue locator by the given lower priority element.
 * This method requires that the given parameter is less than e, or that
 * e is the parameter being passed and its value has been mutated to have a lower priority than
 * it had previously.  That is, it is acceptable practice to mutate the element to have a lower
 * priority and then immediately call <code>decreasePriority</code> to restore the properties of the priority queue.
**/

	public void decreasePriority(E element);
/**
 * Replaces the element associated
 * with this priority queue locator by the given higher priority element.
 * This method requires that the given parameter is greater than e, or that
 * e is the parameter being passed and its value has been mutated to have a greater priority than
 * it had previously.  That is, it is acceptable practice to mutate the element to have a greater
 * priority and then immediately call <code>increasePriority</code> to restore the properties of the priority queue.
**/

	public void increasePriority(E element);
/**
 * Replaces the
 * element associated with this priority queue locator by the given element.
 * This method requires that the parameter is not the same object as e.
 * This is necessary because the method compares
 * the old and the new values to decide whether the priority must be
 * decreased or increased.  Therefore, it would be an error to mutate the element referenced by
 * the locator and then call update.
**/

	public void update(E element);


}
