// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.priority;
import java.util.Iterator;

import goldman.collection.priority.PriorityQueueLocator;
import goldman.collection.tagged.TaggedCollection;
import goldman.collection.tagged.TaggedElement;
/**
 * The TaggedPriorityQueue ADT is a tagged version of the
 * priority queue ADT.  While the comparator used within a priority queue might
 * use a single field of the element (that can be viewed
 * as a tag), in a tagged priority queue there is an explicit association created
 * from the tag to the associated data element.  Also, the
 * comparator is required to be based upon only the tags.   The tags held
 * within a tagged priority queue need not be unique.
**/

public interface TaggedPriorityQueue<T,E> extends TaggedCollection<T,E> {
/**
 * Removes and returns a tagged
 * element with a highest priority tag.  It throws a
 * <code>NoSuchElementException</code> when this collection is empty.
**/

	public TaggedElement<T,E> extractMax();
/**
 * Returns a
 * priority queue locator that has been initialized at a tagged element with the given tag.
 * It throws a <code>NoSuchElementException</code>
 * if there is no element in this collection with an equivalent tag.
**/

	public PriorityQueueLocator<TaggedElement<T,E>> getLocator(T tag);
/**
 * Returns an
 * iterator that has been initialized to FORE.
**/

	public Iterator<TaggedElement<T,E>> iterator(); 
/**
 * Returns a tagged element with a
 * highest priority tag.  It throws a
 * <code>NoSuchElementException</code> when this collection is empty.
**/

	public TaggedElement<T,E> max();
/**
 * This method
 * creates a new tagged element using the given tag and element, and
 * inserts it into this collection.  A priority queue locator that tracks the new tagged element is returned.
 * An <code>AtCapacityException</code>, an unchecked exception, is thrown if the
 * collection is already at capacity.
**/

	public PriorityQueueLocator<TaggedElement<T,E>> putTracked(T tag, E element);
/**
 * This method
 * replaces the tag of the tagged element at the given
 * locator position by <code>tag</code>, and makes any required updates
 * to the underlying data structure.
**/

	public void updateTag(T tag, PriorityQueueLocator<TaggedElement<T,E>> loc);
}
