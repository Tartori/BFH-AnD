// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.


package sorting.list;

import java.util.Collection;

/**
 * The <code>Tracked</code> interface adds a single method to
 * the <code>Collection</code> interface that allows the user to obtain a tracker
 * when an element is inserted.
**/

public interface Tracked<E> extends Collection<E> {
/**
 * Inserts <code>o</code> into
 * the collection in an arbitrary location and returns
 * a tracker to the inserted element.
 * An <code>AtCapacityException</code> (an unchecked exception) is thrown if the
 * collection is already at capacity.
**/

	Locator<E> addTracked(E o);
}
