// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged;
import java.util.Iterator;

import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.Visitor;
import goldman.collection.positional.Array;
/**
 * Rather than implement each of the tagged collections from scratch, we define
 * a tagged collection wrapper that can be used to transform any untagged collection
 * into a tagged collection.  Each element in the collection is
 * a tagged element.   For a tagged collection to have
 * the specified behavior, it is required that the equivalence tester
 * over a tagged element be defined according to the tag.
**/

public class TaggedCollectionWrapper<T,E> implements TaggedCollection<T,E> {

	protected Collection<TaggedElement<T,E>> pairs;
	protected MutableTaggedElement<T,E> target = new MutableTaggedElement<T,E>();

/**
 * @param pairs the collection
 * to wrap
 * @throws IllegalArgumentException the provided collection
 * is not empty
**/

	public TaggedCollectionWrapper(Collection<TaggedElement<T,E>> pairs) {
		if (!pairs.isEmpty())
			throw new IllegalArgumentException("provided collection must be initially empty");
		this.pairs = pairs;
	}


	public int getCapacity() {return pairs.getCapacity();}


	public int getSize() {return pairs.getSize();}


	public boolean isEmpty() {return pairs.isEmpty();}

/**
 * @param tag the target tag
 * @return true  if and only if there is some tagged element in this
 * collection with an equivalent tag to the target
**/

	public synchronized boolean contains(T tag) {
		return pairs.contains(target.setTag(tag));
	}

/**
 * @param tag the target tag
 * @return a tagged element with an equivalent tag
 * @throws NoSuchElementException there is no element
 * with an equivalent tag
**/

	public synchronized E get(T tag) {
		return pairs.getEquivalentElement(target.setTag(tag)).getElement();
	}

/**
 * @param tag the target tag
 * @return a locator that has been placed at a tagged element with
 * an equivalent tag
 * @throws NoSuchElementException there is no element
 * with an equivalent tag
**/

	public synchronized Locator<TaggedElement<T,E>> getLocator(T tag) {
		return pairs.getLocator(pairs.getEquivalentElement(target.setTag(tag)));
	}

/**
 * This implementation creates a positional collection
 * (specifically, an array), and the elements are inserted in the iteration order
 * @return a collection of all elements
 * in this tagged collection.
**/

	public Collection<E> values() {
		Collection<E> coll = new Array<E>(getSize());
		for (TaggedElement<T,E> te : this)
			coll.add(te.element);
		return coll;
	}

/**
 * @return a string that describes each tagged element in
 * the collection
**/

	public String toString() {
		return pairs.toString();
	}

/**
 * Traverses the entire collection on behalf of a visitor.
 * @param v a visitor
 * @throws VisitAbortedException the traversal is aborted due to an
 * exception raised by the visitor,
 * in which case the cause held by the VisitAbortedException is the
 * exception thrown by the visitor.
**/

	public void accept(Visitor<? super TaggedElement<T,E>> v) {
		pairs.accept(v);
	}

/**
 * Increases the capacity of the collection, if necessary,
 * to ensure that it can hold at least <code>capacity</code> elements.
 * @param capacity the desired capacity for the collection
**/

	public void ensureCapacity(int capacity) {
		pairs.ensureCapacity(capacity);
	}

/**
 * Trims the capacity of
 * this collection to be its current size.  An application can use this
 * operation to minimize space usage.
**/

	public void trimToSize() {pairs.trimToSize();}

/**
 * Creates a
 * new tagged element with the given values and
 * inserts this tagged element into this collection.
 * @param tag the tag for the tagged element to add
 * @param data the associated data
 * @throws AtCapacityException the
 * collection is already at capacity.
**/

	public synchronized void put(T tag, E data) {
		pairs.add(new TaggedElement<T,E>(tag, data));        
	}

/**
 * @param tc the tagged collection to be added to this
 * collection
**/

	public synchronized void putAll(TaggedCollection<T,E> tc) {
		pairs.addAll((Collection<? extends TaggedElement<T, E>>) tc);
	}

/**
 * Removes some tagged element in this collection with an tag equivalent
 * to the given tag
 * @param tag the target tag
 * @return the removed element
 * @throws NoSuchElementException there is no element
 * with an equivalent tag
**/

	public synchronized E remove(T tag) {
		Locator<TaggedElement<T,E>> loc = pairs.getLocator(target.setTag(tag));
		Object x = loc.get();
		E elem = loc.get().getElement();
		loc.remove();
		return elem;
	}	

/**
 * Removes all elements from this collection.
**/

	public void clear() {
		pairs.clear();
	}

/**
 * Creates a new iterator at FORE.
**/

	public Iterator<TaggedElement<T,E>> iterator() {
		return pairs.iterator();
	}
	
	public Iterator<T> tags() {
		return new Iterator<T>() {
			Iterator<TaggedElement<T,E>> wrapped = iterator();
			public boolean hasNext() { return wrapped.hasNext(); }
			public T next() { return wrapped.next().tag; }
			public void remove() { wrapped.remove(); }
		};
	}
	
	public Iterator<E> elements() {
		return new Iterator<E>() {
			Iterator<TaggedElement<T,E>> wrapped = iterator();
			public boolean hasNext() { return wrapped.hasNext(); }
			public E next() { return wrapped.next().element; }
			public void remove() { wrapped.remove(); }
		};
	}


}
