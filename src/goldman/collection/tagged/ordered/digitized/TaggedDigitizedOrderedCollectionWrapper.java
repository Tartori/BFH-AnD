// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered.digitized;
import goldman.collection.Collection;
import goldman.collection.ordered.digitized.DigitizedOrderedCollection;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TrackedTagged;
import goldman.collection.tagged.ordered.TaggedOrderedCollectionWrapper;
/**
 * The tagged ordered collection wrapper can wrap any digitized ordered collection
 * implementation, where each element in the collection is
 * a tagged element.   It is required that the comparator depends only
 * on the tag, and the tag is immutable.
**/

public abstract class TaggedDigitizedOrderedCollectionWrapper<T,E> extends TaggedOrderedCollectionWrapper<T,E>
	implements TaggedDigitizedOrderedCollection<T,E>, TrackedTagged<T,E> {

	public TaggedDigitizedOrderedCollectionWrapper
						(DigitizedOrderedCollection<TaggedElement<T, E>> pairs) {
		super(pairs);
	}

/**
 * @param prefix the desired prefix
 * @param tc the tagged collection to append all tagged elements
 * for which the tag has the given prefix
**/

	public void completions(T prefix, Collection<? super TaggedElement<T,E>> tc) {
		((DigitizedOrderedCollection<TaggedElement<T,E>>) 
										pairs).completions(target.setTag(prefix),tc);
	}

/**
 * @param tag the desired prefix
 * @param tc the tagged collection to append all tagged elements
 * in this collection where the tag has a longest common
 * prefix with <code>tag</code>.
**/

	public void longestCommonPrefix(T tag, Collection<? super TaggedElement<T,E>> tc) {
		((DigitizedOrderedCollection<TaggedElement<T,E>>) 
									pairs).longestCommonPrefix(target.setTag(tag),tc);
	}


}
