// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged;
import java.util.Iterator;

import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.Visitor;
/**
 * A tagged collection is a variation of an
 * algorithmically positioned collections (e.g., ordered collection, priority
 * queue, spatial collection)  that uses a tag associated with each element to
 * determine their proper placement within the data structure.
 * In other words, the application provides, for each element, an external tag that is to be associated
 * with that element in the collection.  Most tagged collections permit duplicate tags.
 * However, some tagged collections enforce the restriction that tags are unique.  Each tag in such a collection is
 * called a key, because it can be presented to the collection in order to find its associated element.
 * However, even when the tags are unique, multiple keys may map to the same element.
**/

public interface TaggedCollection<T,E> extends Iterable<TaggedElement<T,E>>{
/**
 * Traverses each tagged element of this collection,
 * in the iteration order, on behalf of the visitor.
**/

	 public void accept(Visitor<? super TaggedElement<T,E>> v);
/**
 * Removes all tagged elements from this collection.
**/

	public void clear();
/**
 * Returns true  if a tagged element with
 * an equivalent tag exists in this collection. Otherwise false  is returned.
**/

	public boolean contains(T tag);
/**
 * Returns an iterator over the
 * elements that has been
 * initialized to just before the first element in the iteration order.  The
 * iteration order of the elements is defined based on the iteration order of
 * the associated tags.
**/

	 public Iterator<E> elements();
/**
 * Increases the capacity
 * of this tagged collection, if necessary, to ensure that it can hold
 * at least <code>capacity</code> tagged elements.
**/

	public void ensureCapacity(int capacity);
/**
 * Returns an element with the
 * given tag.  This method requires that <code>tag</code> be in use.  It throws a
 * <code>NoSuchElementException</code> otherwise
**/

	public E get(T tag);
/**
 * Returns the current capacity of this tagged collection.
**/

	public int getCapacity();
/**
 * Returns a locator that has been initialized
 * to a tagged element with an equivalent tag.  Like the <code>iterator</code> method, this method enables
 * navigation, but from a specified starting point.  This method
 * throws a <code>NoSuchElementException</code> if there is no tagged element with an equivalent tag
 * in the collection.
**/

	public Locator<TaggedElement<T,E>> getLocator(T tag);
/**
 * Returns the number of tagged elements, size, in this collection.
**/

	public int getSize();
/**
 * Returns true  if this collection
 * contains no elements, and otherwise returns false.
**/

	boolean isEmpty();
/**
 * Returns an iterator that has been initialized to
 * FORE.  The returned iterator can be
 * used to navigate within the collection and to remove the element currently
 * associated with the iterator.
 * In general, no assumption is made about the iteration order, the order in
 * which the iterator advances through the collection.  However, some tagged collections
 * specify a particular iteration order based on the values of the tags.
**/

	public Iterator<TaggedElement<T,E>> iterator();
/**
 * This method
 * creates a new tagged element with the given values and
 * inserts this tagged element into this collection.
 * An <code>AtCapacityException</code>, an unchecked exception, is thrown if the
 * collection is already at capacity.
**/

	public void put(T tag, E element);
/**
 * Adds all
 * tagged elements in <code>tc</code> to this tagged collection.
**/

 	public void putAll(TaggedCollection<T,E> tc);
/**
 * Removes, from this tagged collection,
 * a tagged element with the given tag.
 * Returns the element that was removed, and throws a <code>NoSuchElementException</code>
 * when there is no element with an equivalent tag.
**/

	public E remove(T tag);
/**
 * Returns an iterator over the tags that has been
 * initialized to just before the first tag in the iteration order.
**/

	public Iterator<T> tags();
/**
 * Returns a string that describes each tagged element in
 * the collection, in the iteration order.
**/

	public String toString();
/**
 * Trims the capacity of this tagged
 * collection to be its current size. An application can use this
 * operation to minimize the space usage when future insertions are unlikely.
**/

	public void trimToSize();
/**
 * Returns a collection of all the elements
 * held in this tagged collection.  Adding (or removing) elements to (or from)
 * the returned collection does not change the tagged collection.
**/

	public Collection<E> values();
}
