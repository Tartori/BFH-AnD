// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.set;
import goldman.collection.tagged.TaggedCollection;
/**
 * The Mapping ADT,
 * is a tagged variation of the Set ADT.
 * While the equivalence tester provided for a set might
 * use a single field within the element (that can be viewed
 * as a tag), in a mapping there is an explicit association created
 * from the tag to the associated data element.  Also, the
 * equivalence of two elements is defined based on the equivalence
 * between the tags.   Likewise the <code>hashCode</code> method for
 * a tagged element is based solely on the tag.
 * Just as a Set requires that its elements be unique,
 * a Mapping requires that its tags be unique, so we refer to its tags as
 * keys.
**/

public interface Mapping<K,E> extends TaggedCollection<K,E> {}
/**
 * Returns the most recent value
 * inserted in the tagged collection with the given key.
 * This method
 * creates a new tagged element with the given key and element.
 * If a tagged element already exists with the given key, then the
 * tagged element is replaced.
 * An <code>AtCapacityException</code> (an unchecked exception) is thrown when the
 * collection is at capacity and the key was not already in use.
**/
