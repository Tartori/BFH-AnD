// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered.digitized;
import goldman.collection.Collection;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.ordered.TaggedOrderedCollection;
/**
 * The TaggedDigitizedOrderedCollection ADT
 * is a tagged variation of the DigitizedOrderedCollection ADT.
 * While the digitizer provided for an ordered digitized collection might
 * use a single field within the element (that can be viewed
 * as a tag), in a tagged digitized ordered collection there is an explicit association created
 * from the tag to the associated data element.  Also, the
 * digitizer is required to be based upon only the tags.  The tags held
 * within a tagged digitized ordered collection need not be unique.
**/

public interface TaggedDigitizedOrderedCollection<T,E> extends TaggedOrderedCollection<T,E> {
/**
 * Adds
 * to the provided tagged collection, <code>tc</code>, all tagged elements for which the tag has the given prefix.
 * (We consider a tag to be a prefix of itself.)
**/

	public void completions(T prefix, Collection<? super TaggedElement<T,E>> tc);
/**
 * Adds
 * to 
 * the provided tagged collection, <code>tc</code>, all tagged elements in this collection whose tag has a longest common
 * prefix with <code>tag</code>.
**/

	public void longestCommonPrefix(T tag, Collection<? super TaggedElement<T,E>> tc);
	//public Locator<TaggedElement<T,E>> putTracked(T tag, E element);
}
