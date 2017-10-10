// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged;
import goldman.collection.Locator;
/**
 * Similar to the <code>Tagged</code> interface, the <code>TrackedTagged</code> interface
 * includes a method to insert a tagged element into a tagged collection that
 * returns a tracker for it.
**/

public interface TrackedTagged<T,E> extends TaggedCollection<T,E> {
/**
 * This method
 * creates a new tagged element for the given tag and element, and
 * inserts it into this collection.  A locator that tracks the new
 * tagged element is returned.
 * An <code>AtCapacityException</code>, an unchecked exception, is thrown if the
 * collection is already at capacity.
**/

	public Locator<TaggedElement<T,E>> putTracked(T tag, E element);
}
