// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered.digitized;

import goldman.collection.Digitizer;
import goldman.collection.ordered.digitized.TernarySearchTrie;
import goldman.collection.tagged.TaggedElement;
/**
 * A tagged version of a ternary search trie.
**/

public class TaggedTernarySearchTrie<T,E> 
				extends TaggedDigitizedOrderedCollectionWrapper<T,E> {
	
	public TaggedTernarySearchTrie(Digitizer<? super T> digitizer) {
		super(new TernarySearchTrie<TaggedElement<T,E>>
						(new TaggedElementDigitizer<T>(digitizer)));
	}
}

