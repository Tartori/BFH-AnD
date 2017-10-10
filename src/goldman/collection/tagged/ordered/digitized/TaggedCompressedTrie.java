// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered.digitized;

import goldman.collection.Digitizer;
import goldman.collection.ordered.digitized.CompressedTrie;
import goldman.collection.tagged.TaggedElement;
/**
 * A tagged version of a compressed trie.
**/

public class TaggedCompressedTrie<T,E>
					extends TaggedDigitizedOrderedCollectionWrapper<T,E> {
	
	public TaggedCompressedTrie(Digitizer<? super T> digitizer) {
		super(new CompressedTrie<TaggedElement<T,E>>
						(new TaggedElementDigitizer<T>(digitizer)));
	}
}

