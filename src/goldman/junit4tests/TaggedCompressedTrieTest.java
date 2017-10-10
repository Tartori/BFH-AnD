package goldman.junit4tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import goldman.collection.StringDigitizer;
import goldman.collection.PrefixFreeDigitizer;
import goldman.collection.tagged.ordered.digitized.TaggedCompressedTrie;
import goldman.collection.tagged.ordered.digitized.TaggedDigitizedOrderedCollection;

public class TaggedCompressedTrieTest extends TaggedTrieTest {
		
	public TaggedDigitizedOrderedCollection createCollection(int base) {
		PrefixFreeDigitizer<String> digitizer = new StringDigitizer(base);
	    return new TaggedCompressedTrie<String,Integer>(digitizer);
	}
	
	@Test
	public void empty() {
		assertEquals(0, createCollection(2).getSize());
	}
}
