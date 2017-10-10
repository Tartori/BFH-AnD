// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.
/**
 * Creates a hash table with the specified capacity (plus one to handle null values)
 * @param capacity the desired capacity
**/

package goldman.collection.tagged.set;
import goldman.collection.set.DirectAddressing;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedCollectionWrapper;
/**
 * A tagged version of a directed addressing.
**/

public class DirectAddressingMapping<K,V> extends TaggedCollectionWrapper<K,V> 
														implements Mapping<K,V> {
	public DirectAddressingMapping(int capacity) {
		super(new DirectAddressing<TaggedElement<K,V>>(capacity));
	}
}

