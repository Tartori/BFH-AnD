// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.priority;
import java.util.Comparator;
import java.util.Iterator;

import goldman.collection.AbstractCollection;
import goldman.collection.Collection;
import goldman.collection.Tracked;
import goldman.collection.priority.PriorityQueueLocator;
import goldman.collection.priority.PriorityQueue;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedCollectionWrapper;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * The tagged priority queue wrapper can wrap any priority queue
 * implementation, where each element in the priority queue is
 * a tagged element.   It is required that the comparator depends only
 * on the tag, and the tag is immutable except through the use of the
 * <code>update</code> method.
**/

public abstract class TaggedPriorityQueueWrapper<T,E> 
	extends TaggedCollectionWrapper<T,E> implements TaggedPriorityQueue<T,E> {

	static class PQTaggedElement<T,E> extends TaggedElement<T,E> {
		PQTaggedElement(T t, E e) {
			super(t, e);
		}
		void setTag(T newTag) { 
			this.tag = newTag;
		}	
	}


	public TaggedPriorityQueueWrapper(Collection<TaggedElement<T,E>> pairs) {
		super(pairs);
	}

/**
 * @return a tagged element with a
 * highest priority tag
 * @throws NoSuchElementException this collection is empty.
**/

	public TaggedElement<T,E> max() {
		return ((PriorityQueue<TaggedElement<T,E>>) pairs).max();
	}

/**
 * @param tag the target tag
 * @return a priority queue locator that has been placed at a tagged element with
 * an equivalent tag
 * @throws NoSuchElementException there is no element
 * with an equivalent tag
**/

	public PriorityQueueLocator<TaggedElement<T,E>> getLocator(T tag) {
		TaggedElement<T,E> entry = pairs.getEquivalentElement(target.setTag(tag));
		return (PriorityQueueLocator<TaggedElement<T, E>>) pairs.getLocator(entry);
	}

/**
 * Creates a
 * new tagged element with the given values, and
 * inserts this tagged element into this collection.
 * @param tag the tag for the tagged element to add
 * @param data the associated data
 * @throws AtCapacityException the
 * collection is already at capacity.
**/

	public synchronized void put(T tag, E data) {
		pairs.add(new PQTaggedElement<T,E>(tag, data));        
	}

/**
 * Creates a
 * new tagged element with the given values, and
 * inserts this tagged element into this collection
 * @param tag the tag for the tagged element to add
 * @param data the associated data
 * @return a priority queue locator that tracks the new tagged
 * element
 * @throws AtCapacityException the
 * collection is already at capacity.
**/

	public synchronized PriorityQueueLocator<TaggedElement<T,E>> putTracked(T tag, 
																			E data) {
		return (PriorityQueueLocator<TaggedElement<T, E>>) 
			((Tracked<TaggedElement<T,E>>) pairs).addTracked(
								new PQTaggedElement<T,E>(tag, data));        
	}

/**
 * Removes a tagged element with a highest
 * priority tag
 * @return the removed tagged element
 * @throws NoSuchElementException this collection is empty.
**/

	public TaggedElement<T,E> extractMax() {
		return ((PriorityQueue<TaggedElement<T,E>>) pairs).extractMax();	
	}

/**
 * Replaces the tag of the tagged element at the locator position by <code>tag</code>,
 * and makes any required updates in the wrapped priority queue.
 * @param tag the new tag
 * @param loc a priority queue locator specifying the tagged element to update
**/

	public void updateTag(T tag, PriorityQueueLocator<TaggedElement<T,E>> loc) {
		PQTaggedElement<T,E> te = (PQTaggedElement<T, E>) loc.get();
		Comparator<? super T> comp = ((TaggedElementComparator<T>)
			 ((AbstractCollection) pairs).getComparator()).getTagComparator();
		int comparison = comp.compare(tag, te.getTag());
		te.setTag(tag);
		if (comparison > 0)
			loc.increasePriority(te);
		else if (comparison < 0)
			loc.decreasePriority(te);
	}

/**
 * Creates and returns new iterator initialized to FORE.
**/

	public Iterator<TaggedElement<T, E>> iterator() {
		return pairs.iterator();
	}


}
