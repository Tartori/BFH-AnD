// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.
/**
 * Create a hash table with the specified capacity (plus one to handle null values), and
 * with the given comparator and hasher
 * @param capacity the desired capacity
 * @param load the target load
 * @param equivalenceTester the comparator that defines equivalence of objects
 * @param hasher the user-supplied hash code computation
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0 or <code>load</code> &ge; 1.0
**/

package goldman.collection.tagged.set;
import java.util.Comparator;
import goldman.collection.AbstractCollection;
import goldman.Objects;
import goldman.collection.set.OpenAddressing;
import goldman.collection.tagged.TaggedCollectionWrapper;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a open addressing.
**/

public class OpenAddressingMapping<K,V> extends TaggedCollectionWrapper<K,V> 
										implements Mapping<K,V> {


	public OpenAddressingMapping(int capacity, double load, Comparator<? super K> equiv) {
		super(new OpenAddressing<TaggedElement<K,V>>(capacity, load, 
				new TaggedElementComparator<K>(equiv)));
	}
	
	public OpenAddressingMapping(int capacity) {
		this(capacity, OpenAddressing.DEFAULT_LOAD, 
								Objects.DEFAULT_EQUIVALENCE_TESTER);
	}


	public OpenAddressingMapping() {
		this(AbstractCollection.DEFAULT_CAPACITY,  OpenAddressing.DEFAULT_LOAD, 
				Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

	public OpenAddressingMapping(Comparator<? super K> equivalenceTester) {
		this(AbstractCollection.DEFAULT_CAPACITY,  
					OpenAddressing.DEFAULT_LOAD, equivalenceTester);
	}

}

