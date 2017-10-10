// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import goldman.collection.Locator;
/**
 * The <code>PositionalCollectionLocator</code> interface
 * extends the <code>Locator</code> interface to add methods that are
 * specific to a positional collection.
**/

public interface PositionalCollectionLocator<E> extends Locator<E> {
/**
 * Inserts <code>value</code>
 * immediately after the object referenced by this locator and returns a
 * fresh locator at the position of insertion.
**/

	public Locator<E> addAfter(E value);
/**
 * Returns the position within the collection for this
 * locator.
**/

	public int getCurrentPosition();
}
