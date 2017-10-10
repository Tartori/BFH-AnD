// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.bucket;
import java.util.Iterator;
import java.util.NoSuchElementException;

import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.Tracked;
import goldman.collection.Visitor;
import goldman.collection.tagged.TaggedCollection;
import goldman.collection.tagged.TaggedElement;
/**
 * The tagged bucket wrapper is implemented as a wrapper for any
 * tagged collection, where each tag is placed in the wrapped collection
 * only once, and the associated element for each tag is a bucket object of
 * any collection type.
 * An important distinction is that the size of a wrapped tagged collection
 * only depends upon the number of distinct tags.  Thus, for the time complexity
 * of all methods delegated to the wrapped tagged collection, n is the
 * number of distinct tags.  However, from the view of the application,
 * the size of a tagged bucket collection is the number of elements, not
 * the number of tags.
**/

public class TaggedBucketCollectionWrapper<T,E> implements TaggedBucketCollection<T,E> {

	protected TaggedCollection<T, Collection<E>> tc;
	protected BucketFactory<E> factory;
	final Locator<E> emptyLocator;  

/**
 * @param tc the (empty)
 * tagged collection from T to buckets that is to be wrapped
 * @param factory a bucket factory that is used to create a bucket for each new tag
**/

	public TaggedBucketCollectionWrapper(
			TaggedCollection<T, Collection<E>> tc, BucketFactory<E> factory){
		if (!tc.isEmpty())
			throw new IllegalArgumentException("The given tagged collection must be empty.");
		this.tc = (TaggedCollection<T, Collection<E>>) tc;
		this.factory = factory;
		emptyLocator = factory.createBucket().iterator();
	}


/**
 * @param tc the
 * tagged collection from T to buckets that is to be wrapped
 * @param bucketType a class of the appropriate collection type for the buckets
 * <BR> 
 * REQUIRES: 
 * the provided class <code>bucketType</code> implement
 * the <code>Collection</code> interface
**/

	public TaggedBucketCollectionWrapper(
			TaggedCollection<T, Collection<E>> tc, final Class bucketType){
		this(tc, new BucketFactory<E>() {
			public Collection<E> createBucket() {
					try {
						return (Collection<E>) bucketType.newInstance();
					} catch (Exception e) {
						throw new RuntimeException("bucket creation failed");
					}
			}
		});
		if (!Collection.class.isAssignableFrom(bucketType))
			throw new IllegalArgumentException("bucket type does not implement Collection");
	}

/**
 * @return the underlying tagged collection used by this wrapper
**/

	public TaggedCollection<T, Collection<E>> getTaggedCollection() {
		return tc;
	}

/**
 * @return the number of tags held in the mapping
**/

	public int getNumTags(){
		return tc.getSize();
	}

/**
 * @return true  if and only if there are
 * no elements in the collection
**/

	public boolean isEmpty() {
		for (TaggedElement<T, Collection<E>> te : tc)
			if (te.getElement().getSize() > 0)
				return false;
		return true;
	}

/**
 * @return the number of elements, among all buckets,
 * held in this collection.
**/

	public int getSize() {
		int size = 0;
		for (TaggedElement<T, Collection<E>> te : tc)
			size += te.getElement().getSize();
		return size;
	}

/**
 * @return the capacity (in terms of the number of tags).
**/

	public int getCapacity() {
		return tc.getCapacity();
	}

/**
 * @param tag the target tag
 * @return true  if and only if there is a bucket associated with the given
 * tag.
**/

	public boolean contains(T tag) {
		boolean exists = tc.contains(tag);
		if (exists && tc.get(tag).isEmpty()) {
			tc.remove(tag);
			exists = false;
		}
		return exists;
	}

/**
 * @param tag the target tag
 * @return the bucket holding the elements associated with the given
 * tag.
 * @throws NoSuchElementException <code>tag</code> is not in use
**/

	public Collection<E> get(T tag) {
		Collection<E> bucket = tc.get(tag);
		if (bucket.isEmpty()) {
			tc.remove(tag);
			throw new NoSuchElementException();
		}
		return bucket;
	}

/**
 * @param tag the target tag
 * @return a locator positioned to just before the first element
 * in the bucket associated with the given tag.  For convenience
 * when the <code>tag</code>
 * is not in use, an iterator for an empty bucket is returned.
**/

	public Locator<E> getElements(T tag) {
		if (!contains(tag))
			return emptyLocator;
		else
			return tc.get(tag).iterator();
			
	}

/**
 * @return a string representation for the
 * bucket mapping, in keeping with the <code>toString</code> methods of the wrapped
 * tagged collection and the bucket type
**/

	public String toString() {
		return tc.toString();
	}

/**
 * Traverses the entire collection on behalf of a visitor.
 * @param v a visitor
 * @throws VisitAbortedException the traversal is aborted due to an
 * exception raised by the visitor,
 * in which case the cause held by the <code>VisitAbortedException</code> is the
 * exception thrown by the visitor.
**/

	public void accept(Visitor<TaggedElement<T, Collection<E>>> v) {
		tc.accept(v);
	}

/**
 * @param tag the target tag
 * @return the number of elements associated with <code>tag</code> (which by definition
 * is 0 when the tag is not in use)
**/

	public int bucketSize(T tag){
		if (!tc.contains(tag))
			return 0;
		return tc.get(tag).getSize();
	}


	public void ensureCapacity(int capacity) {tc.ensureCapacity(capacity);}


	public void trimToSize() {tc.trimToSize();}

/**
 * Adds <code>data</code> to the bucket for the given <code>tag</code>.
 * If no bucket exists for the given <code>tag</code>, a bucket is
 * created using the provided factory.
 * @param tag the tag for the new element
 * @param data the associated data
**/

	public void put(T tag, E data) {
		if (!tc.contains(tag))
			tc.put(tag, factory.createBucket());
		tc.get(tag).add(data);
	}

/**
 * Associates <code>data</code> with the given
 * <code>tag</code> by adding it to the bucket (which is
 * created if it does not exist) for <code>tag</code>.
 * @param tag the tag for the new element
 * @param data the associated data
 * @return a tracker in the bucket for the newly
 * added element
 * @throws UnsupportedOperationException the bucket type does not implement the
 * <code>Tracked</code> interface
**/

	public Locator<E> putTracked(T tag, E data) {
		if (!tc.contains(tag))
			tc.put(tag, factory.createBucket());
		Collection<E> coll = tc.get(tag);
		if (coll instanceof Tracked)
			return ((Tracked<E>) tc.get(tag)).addTracked(data);
		else
			throw new UnsupportedOperationException(
					"the bucket implementation is not tracked");
	}

/**
 * @param tc the tagged collection to be added to this
 * collection
**/

	public void putAll(TaggedCollection<T, E> tc) {
		for (TaggedElement<T,E> e : tc)
			put(e.getTag(), e.getElement());
	}

/**
 * Removes the first element in
 * the bucket for that tag, according to the iteration order
 * @param tag the target tag
 * @return the element removed.
 * @throws NoSuchElementException there are no elements associated with the given <code>tag</code>
**/

	public E remove(T tag) {
		Collection<E> bucket = tc.get(tag);
		if (bucket.getSize() <= 1)
			tc.remove(tag);
		if (bucket.getSize() > 0) {
			Iterator<E> it = bucket.iterator();
			E result = it.next();
			it.remove();
			return result;
		} else
			throw new NoSuchElementException();
	}

/**
 * Removes the specified element from
 * the bucket associated with the given tag (if it exists in that
 * bucket).
 * @param tag the target tag
 * @param element the
 * associated element
 * to remove
 * @return true  if and only if the element was found
 * in the specified bucket and removed
**/

	public boolean remove(T tag, E element) {
		if (tc.contains(tag)) {
			Collection<E> bucket = tc.get(tag);
			if (bucket.contains(element)) {
				bucket.remove(element);
				if (bucket.isEmpty())
					tc.remove(tag);
				return true;
			}
		}
		return false;
	}

/**
 * Removes all elements from this tagged bucket
 * collection that are associated with the given tag.
 * @param tag the target tag
 * @return the bucket that had been associated with the tag
 * @throws NoSuchElementException there is
 * no bucket for the given tag.
**/

	public Collection<E> removeBucket(T tag) {
		return tc.remove(tag);
	} 

/**
 * Removes all elements from this collection.
**/

	public void clear() {
		tc.clear();
	}

/**
 * Creates a new iterator that is at FORE.
**/

	public Iterator<TaggedElement<T, Collection<E>>> iterator() {
		return tc.iterator();
	}


}
