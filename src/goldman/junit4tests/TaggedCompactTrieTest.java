package goldman.junit4tests;

import static org.junit.Assert.*;

import org.junit.Test;

import goldman.collection.StringDigitizer;
import goldman.collection.PrefixFreeDigitizer;
import goldman.collection.tagged.ordered.digitized.TaggedCompactTrie;
import goldman.collection.tagged.ordered.digitized.TaggedDigitizedOrderedCollection;
public class TaggedCompactTrieTest extends TaggedTrieTest {
	
	public TaggedDigitizedOrderedCollection createCollection(int base) {
		PrefixFreeDigitizer<String> digitizer = new StringDigitizer(base);
	    return new TaggedCompactTrie<String,Integer>(digitizer);
	}
	
	@Test
	public void empty() {
		assertEquals(0, createCollection(2).getSize());
	}
}
