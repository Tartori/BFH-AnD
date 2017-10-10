// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered.digitized;

import goldman.collection.Digitizer;
import goldman.collection.ordered.digitized.PatriciaTrie;
import goldman.collection.tagged.TaggedElement;
/**
 * A tagged version of a Patricia trie.
**/

public class TaggedPatriciaTrie<T,E>
							extends TaggedDigitizedOrderedCollectionWrapper<T,E> {
	
	public TaggedPatriciaTrie(Digitizer<? super T> digitizer) throws Exception {
		super(new PatriciaTrie<TaggedElement<T,E>>
						(new TaggedElementDigitizer<T>(digitizer)));
	}
}

