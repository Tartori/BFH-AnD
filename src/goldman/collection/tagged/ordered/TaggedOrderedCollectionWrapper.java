// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered;
import goldman.collection.Locator;
import goldman.collection.Tracked;
import goldman.collection.ordered.OrderedCollection;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedCollectionWrapper;
import goldman.collection.tagged.TrackedTagged;
/**
 * The tagged ordered collection wrapper can wrap any ordered collection
 * implementation, where each element in the collection is
 * a tagged element.   It is required that the comparator depends only
 * on the tag, and the tag is immutable.
**/

public abstract class TaggedOrderedCollectionWrapper<T,E> 
					extends TaggedCollectionWrapper<T,E>
					implements TaggedOrderedCollection<T,E>, TrackedTagged<T,E> {


	public TaggedOrderedCollectionWrapper(
								OrderedCollection<TaggedElement<T, E>> pairs) {
		super(pairs);
	}

/**
 * @return a tagged element with the smallest tag
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public TaggedElement<T,E> min() {
		return ((OrderedCollection<TaggedElement<T,E>>) pairs).min();
	}

/**
 * @return a tagged element with the largest tag
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public TaggedElement<T,E> max() {
		return ((OrderedCollection<TaggedElement<T,E>>) pairs).max();
	}

/**
 * This method does not require that <code>tag</code> is in use.
 * @param tag the tag for which to find the
 * predecessor
 * @return the largest tag used by some tagged element in the
 * collection that is less than <code>tag</code>.
 * @throws NoSuchElementException there is no tag used in the collection
 * less than <code>tag</code>
**/

	public T predecessor(T tag) {
		return ((OrderedCollection<TaggedElement<T,E>>) 
					pairs).predecessor(target.setTag(tag)).getTag();
	}

/**
 * This method does not require that <code>tag</code> is in use.
 * @param tag the tag for which to find the
 * successor
 * @return the smallest tag used by some tagged element in the
 * collection that is greater than <code>tag</code>.
 * @throws NoSuchElementException there is no tag used in the collection greater than <code>tag</code>
**/

	public T successor(T tag) {
		return ((OrderedCollection<TaggedElement<T,E>>) 
						pairs).successor(target.setTag(tag)).getTag();
	}

/**
 * Creates a
 * new tagged element with the given values and
 * inserts this tagged element into this collection
 * @param tag the tag for the tagged element to add
 * @param element the associated data
 * @return a locator that tracks the new tagged element
 * @throws AtCapacityException the
 * collection is already at capacity.
**/

	public synchronized Locator<TaggedElement<T,E>> putTracked(T tag, E element) {
		return ((Tracked<TaggedElement<T,E>>) pairs).addTracked(
										new TaggedElement<T,E>(tag, element));        
	}

/**
 * Creates and returns a new locator initialized to AFT.
**/

	public Locator<TaggedElement<T, E>> iteratorAtEnd() {
		return ((OrderedCollection<TaggedElement<T,E>>) pairs).iteratorAtEnd();
	}


}
