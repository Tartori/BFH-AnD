// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered;
import goldman.collection.Locator;
import goldman.collection.tagged.TaggedCollection;
import goldman.collection.tagged.TaggedElement;
/**
 * A tagged variation of the OrderedCollection ADT
**/

public interface TaggedOrderedCollection<T,E> extends TaggedCollection<T,E> {
/**
 * Returns a tagged element for the least tag
 * in the collection (according to the comparator).  More
 * specifically, the first tagged element in the iteration order
 * is returned.
 * This method throws a
 * <code>NoSuchElementException</code> when the collection is empty.
**/

	public TaggedElement<T,E> min();
/**
 * Returns a tagged element for the greatest tag
 * in the collection (according to the comparator).  More
 * specifically, the last tagged element in the iteration order
 * is returned.
 * This method throws a
 * <code>NoSuchElementException</code> when the collection is empty.
**/

	public TaggedElement<T,E> max();
/**
 * Returns
 * the largest tag used by some tagged element in the
 * collection that is less than <code>tag</code>.  It does
 * not require that <code>tag</code> is in use.  It throws
 * a <code>NoSuchElementException</code> when there is no
 * tag in the collection less than <code>tag</code>.
**/

	public T predecessor(T tag);
/**
 * Returns smallest tag
 * used by some tagged element in the
 * collection that is greater than <code>tag</code>.  It
 * does not require that <code>tag</code> is in use.  It throws
 * <code>NoSuchElementException</code> when there is no tag used in the collection
 * greater than <code>tag</code>.
**/

	public T successor(T tag);
/**
 * Returns a
 * locator that has been initialized
 * to AFT.  As with the <code>iterator</code> method, this method enables
 * navigation.
**/

		public Locator<TaggedElement<T,E>> iteratorAtEnd();
}
