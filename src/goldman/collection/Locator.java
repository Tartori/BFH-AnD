// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
import java.util.Iterator;
/**
 * A locator provides the user with
 * a mechanism for remembering a location within a collection
 * without exposing the internal representation.  A locator can
 * be used to advance or retreat through a collection, or to
 * directly an element in the collection (in order to remove it,
 * for example) without incurring the cost of searching the collection
 * for that element.
**/

public interface Locator<E> extends Iterator<E>, Cloneable {
/**
 * Advances to the next element in the collection
 * (if there is one) and returns <code>true</code>.  If the locator is already
 * at the last element of the collection then <code>false</code> is returned
 * and the locator is moved to AFT.
 * If a call to <code>advance</code> is made when the locator is at AFT,
 * an <code>AtBoundaryException</code> is thrown.   Starting with the locator
 * at FORE and calling <code>advance</code> repeatedly until
 * false  is returned will reach each element in the collection
 * exactly once.
**/

	boolean advance();
/**
 * Returns the element associated with this locator.
 * When a tracker is used, the element associated with the locator might no longer
 * be in the collection.  If desired, the <code>inCollection</code> method can be
 * used to determine if a tracked element is currently in the collection.
 * If the locator is at FORE or AFT then
 * a <code>NoSuchElementException</code> is thrown.
**/

	E get();
/**
 * If
 * <code>ignore</code> is true then concurrent modification exceptions are disabled.
 * If <code>ignore</code> is false, it sets the state of the iterator so that (future)
 * concurrent modifications will be noticed and result in
 * concurrent modification exceptions.
**/

	void ignoreConcurrentModifications(boolean ignore);
/**
 * Any prior
 * concurrent modifications
 * are ignored, but if another critical mutator is executed then any later
 * access will result in a concurrent modification exception.
**/

 	void ignorePriorConcurrentModifications();
/**
 * Returns true  if and only if the locator is at an element of
 * the collection.
**/

	boolean inCollection();
/**
 * Retreats to the previous element in the collection
 * (if there is one) and returns <code>true</code>.  If the locator is already
 * at the first element of the collection then <code>false</code> is returned and
 * the locator is moved to FORE.
 * If a call to <code>retreat</code> is made when the Locator is at FORE,
 * an <code>AtBoundaryException</code> is thrown.  Starting with the locator
 * at AFT and calling <code>retreat</code> repeatedly until
 * false  is returned will reach each element in the underlying collection
 * exactly once, in the reverse order.
**/

	boolean retreat();
 	//public Locator clone();
}
