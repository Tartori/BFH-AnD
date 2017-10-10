// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.bucket;
import java.util.Iterator;
import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.Visitor;
import goldman.collection.tagged.TaggedElement;
/**
 * In a tagged bucket collection, the elements are bucketed into sets based on
 * their tags.  The semantics of <code>put</code> is modified to put a new bucket into the
 * collection when
 * the given tag is not yet in use.   The new bucket is created by a bucket factory, which gives
 * the application the flexibility to specify the collection type of the bucket.
 * This can be done either by providing a bucket factory to the constructor, or by providing
 * the constructor with a class to be instantiated when a new bucket is required.
 * When the given tag is already in use, then the element is simply added
 * to the existing bucket.  Likewise, when an element is removed form a bucket of size
 * 1, then the tag is also removed.
 * In a tagged collection, the cost associated with locating a desired element generally
 * depends on the number of tagged elements in the collection, whereas the cost associated
 * with locating a desired element generally depends on the number of unique tags.
**/

public interface TaggedBucketCollection<T,E> extends Iterable<TaggedElement<T, Collection<E>>>{
/**
 * Traverses each element of this collection,
 * in the iteration order, on behalf of the visitor.  The visitor sees a tagged element for each tag and its associated bucket.
**/

	 public void accept(Visitor<TaggedElement<T, Collection<E>>> v);
/**
 * Removes all tagged elements from this tagged bucket collection.
**/

	public void clear();
/**
 * Returns true  if an
 * equivalent tag exists in this tagged bucket collection. Otherwise, false  is returned.
**/

	public boolean contains(T tag);
/**
 * Increases the capacity
 * of this tagged collection, if necessary, to ensure that it can hold
 * at least <code>capacity</code> distinct tags.
**/

	public void ensureCapacity(int capacity);
/**
 * Returns a collection of all elements with the given
 * tag.  This method requires that tag be in use and will throw a
 * <code>NoSuchElementException</code> otherwise.
**/

	public Collection<E> get(T tag);  //collection of elements with the given tag
/**
 * Returns a locator positioned
 * at FORE within the bucket
 * associated with the given tag, or an iterator over an empty bucket
 * if the tag is not in use.
**/

	public Locator<E> getElements(T tag); 
/**
 * Returns the current capacity (in terms of the number
 * of tags) of this tagged collection.
**/

	public int getCapacity();
/**
 * Returns the number of elements, size, in this collection.
**/

	public int getSize();
/**
 * Returns the number of distinct tags used in this collection.
**/

	public int getNumTags();
/**
 * Returns the number of elements with the given tag.
**/

	public int bucketSize(T tag);
/**
 * Returns true  if this collection
 * contains no elements, and otherwise returns false.
**/

	boolean isEmpty();
/**
 * Returns an iterator initialized at FORE that may be
 * used to iterate over the tags and their associated buckets.
**/

	public Iterator<TaggedElement<T, Collection<E>>> iterator();
/**
 * This method
 * creates a new tagged element with the given values and
 * inserts this tagged element into this collection.   If there is already an
 * element with an equivalent tag, then <code>element</code> is added to the bucket.
 * Otherwise, a new bucket is created for <code>tag</code>, and <code>element</code> is
 * placed in that bucket.
 * An <code>AtCapacityException</code>, an unchecked exception, is thrown if the
 * collection is already at capacity.
**/

	public void put(T tag, E element);
/**
 * Removes from this tagged collection
 * an arbitrary element with the given tag.  It
 * returns the element that was removed, and
 * \tagthrows{NoSuchElementException}{there is
 * no element with an equivalent tag.}
**/

	public E remove(T tag);
/**
 * Removes from this tagged
 * collection an element equivalent to the given element with the given tag.
 * It returns true  if an equivalent element with the given tag was found, and
 * otherwise returns false.
**/

	public boolean remove(T tag, E element);
/**
 * Removes and returns the
 * bucket associated with the given tag.
**/

	public Collection<E> removeBucket(T tag);
/**
 * Returns a string that describes each tagged element in
 * the collection, as produced by the <code>toString</code> method for that element, in the iteration order.
**/

	public String toString();
/**
 * Trims the capacity of this tagged
 * bucket
 * collection to be its current size. An application can use this
 * operation to minimize the space usage.  For elastic implementations, this
 * method does nothing.
**/

	public void trimToSize();
}
