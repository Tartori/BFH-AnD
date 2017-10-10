// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.
/**
 * @param digitzer the digitizer to be
 * used to define the digits for any tag, and that implicitly defines the ordering among
 * the tags
**/

package goldman.collection.tagged.ordered.digitized;

import goldman.collection.Digitizer;
import goldman.collection.ordered.digitized.Trie;
import goldman.collection.tagged.TaggedElement;
/**
 * A tagged version of a trie.
**/

public class TaggedTrie<T,E> extends TaggedDigitizedOrderedCollectionWrapper<T,E> {
	
	public TaggedTrie(Digitizer<? super T> digitizer) {
		super(new Trie<TaggedElement<T,E>>
						(new TaggedElementDigitizer<T>(digitizer)));
	}
}

