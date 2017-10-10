// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.set;
import java.util.Comparator;
import goldman.Objects;
import goldman.collection.set.SeparateChaining;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
import goldman.collection.tagged.TaggedCollectionWrapper;
/**
 * A tagged version of a separate chaining.
**/

public class SeparateChainingMapping<K,V> extends TaggedCollectionWrapper<K,V>
												implements Mapping<K,V> {
	public SeparateChainingMapping(int capacity, double load,
			Comparator<? super K> equivalenceTester) {
		super(new SeparateChaining<TaggedElement<K,V>>(capacity, load, 
				new TaggedElementComparator<K>(equivalenceTester)));
	}


	public SeparateChainingMapping() {
		this(SeparateChaining.DEFAULT_CAPACITY,  SeparateChaining.DEFAULT_LOAD, 
				Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

	public SeparateChainingMapping(Comparator<? super K> equivalenceTester) {
		this(SeparateChaining.DEFAULT_CAPACITY,  SeparateChaining.DEFAULT_LOAD, equivalenceTester);
	}

	public SeparateChainingMapping(int capacity) {
		this(capacity,  SeparateChaining.DEFAULT_LOAD, Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

	public SeparateChainingMapping(int capacity, Comparator<? super K> equivalenceTester) {
		this(capacity,  SeparateChaining.DEFAULT_LOAD, equivalenceTester);
	}

	public SeparateChainingMapping(int capacity, double load) {
		this(capacity,  load, Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

}

