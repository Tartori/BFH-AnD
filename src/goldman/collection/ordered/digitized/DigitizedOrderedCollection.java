// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
import goldman.collection.Collection;
import goldman.collection.ordered.OrderedCollection;
/**
 * A digitized ordered collection is an untagged algorithmically
 * positioned collection whose elements can each be viewed
 * as a sequence of digits (e.g., bit string, character string).
 * The <code>DigitizedOrderedCollection</code> interface extends
 * the <code>OrderedCollection</code> interface by adding methods to find all
 * extensions of a given prefix, and also to find all elements in
 * the collection that have the longest prefix match with a given element.
**/

public interface DigitizedOrderedCollection<E> extends OrderedCollection<E> {
/**
 * Appends
 * all elements in this collection that have the given prefix to the given collection c.
 * We consider an element to be a prefix of itself.
**/

	public void completions(E prefix, Collection<? super E> c);
/**
 * Appends
 * to the provided collection c all elements in this collection that have a longest common
 * prefix with <code>element</code>.
**/

	public void longestCommonPrefix(E element, Collection<? super E> c);
}
