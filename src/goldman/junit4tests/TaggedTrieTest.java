package goldman.junit4tests;

import static org.junit.Assert.*;

import org.junit.Test;

import goldman.collection.Collection;
import goldman.collection.StringDigitizer;
import goldman.collection.Locator;
import goldman.collection.PrefixFreeDigitizer;
import goldman.collection.positional.SinglyLinkedList;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.ordered.digitized.TaggedDigitizedOrderedCollection;
import goldman.collection.tagged.ordered.digitized.TaggedDigitizedOrderedCollectionWrapper;
import goldman.collection.tagged.ordered.digitized.TaggedTrie;
public class TaggedTrieTest {
	
	public TaggedDigitizedOrderedCollection createCollection(int base) throws Exception {
		PrefixFreeDigitizer<String> digitizer = new StringDigitizer(base);
	    return new TaggedTrie<String,Integer>(digitizer);
	}
	
	public String delimLeft() { return "{"; }
	public String delimRight() { return "}"; }
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLongAll() throws Exception {
		TaggedDigitizedOrderedCollectionWrapper<String,Integer> trie = 
			(TaggedDigitizedOrderedCollectionWrapper<String, Integer>) createCollection(4);
		assertEquals(trie.getSize(), 0);
		trie.put("ab", 1);
		assertEquals(trie.getSize(), 1);
		assertEquals(trie.toString(), delimLeft() + "ab --> 1" + delimRight());
		assertEquals(trie.contains("ab"), true);
		assertEquals(trie.contains("acb"), false);
		assertEquals(trie.contains("a"), false);
		assertEquals(trie.contains("abc"), false);
		trie.put("acb", 2);
		assertEquals(trie.getSize(), 2);
		assertEquals(trie.toString(), delimLeft() + "ab --> 1, acb --> 2" + delimRight());
		trie.put("bdac", 3);
		assertEquals(trie.getSize(), 3);
		assertEquals(trie.toString(), delimLeft() + "ab --> 1, acb --> 2, bdac --> 3" + delimRight());
		trie.put("bdab", 4);
		assertEquals(trie.getSize(), 4);
		assertEquals(trie.toString(), delimLeft() + "ab --> 1, acb --> 2, bdab --> 4, bdac --> 3" + delimRight());
		trie.put("bdb", 5);
		assertEquals(trie.getSize(), 5);
		assertEquals(trie.toString(), delimLeft() + "ab --> 1, acb --> 2, bdab --> 4, bdac --> 3, bdb --> 5" + delimRight());
		Locator<TaggedElement<String,Integer>> tracker = trie.putTracked("bac", 6);
		assertEquals(trie.getSize(), 6);
		assertEquals(trie.toString(), delimLeft() + "ab --> 1, acb --> 2, bac --> 6, bdab --> 4, bdac --> 3, bdb --> 5" + delimRight());
		trie.put("baca", 7);
		assertEquals(trie.getSize(), 7);
		assertEquals(trie.toString(), delimLeft() + "ab --> 1, acb --> 2, bac --> 6, baca --> 7, bdab --> 4, bdac --> 3, bdb --> 5" + delimRight());
		
		tracker.retreat();
		assertEquals(tracker.get().toString(),"acb --> 2");
		tracker.advance();
		assertEquals(tracker.get().toString(),"bac --> 6");
		tracker.advance();
		assertEquals(tracker.get().toString(),"baca --> 7");
		tracker.retreat();
		assertEquals(tracker.get().toString(),"bac --> 6");
	
		Collection<TaggedElement<String,Integer>> c =  new SinglyLinkedList<TaggedElement<String,Integer>>();
		
		trie.longestCommonPrefix("a",c);
		assertEquals(c.toString(), "<ab --> 1, acb --> 2>");
		
		c.clear();
		trie.longestCommonPrefix("bdada",c);		
		assertEquals(c.toString(), "<bdab --> 4, bdac --> 3>");	
		
		c.clear();
		trie.longestCommonPrefix("b",c);	
		assertEquals(c.toString(), "<bac --> 6, baca --> 7, bdab --> 4, bdac --> 3, bdb --> 5>");
		
		c.clear();
		trie.completions("a",c);
		assertEquals(c.toString(), "<ab --> 1, acb --> 2>");
		
		c.clear();
		trie.completions("bdada",c);
		assertEquals(c.toString(), "<>");	
		
		c.clear();
		trie.completions("b",c);
		assertEquals(c.toString(), "<bac --> 6, baca --> 7, bdab --> 4, bdac --> 3, bdb --> 5>");
		
		c.clear();
		trie.completions("c",c);
		assertEquals(c.toString(), "<>");
		trie.completions("d",c);
		assertEquals(c.toString(), "<>");
		
		trie.remove("bdac");
		assertEquals(trie.getSize(), 6);
		assertEquals(trie.contains("bdac"), false);
		assertEquals(trie.contains("bac"), true);
		tracker.remove();
		assertEquals(trie.getSize(), 5);
		assertEquals(trie.contains("ac"), false);
		assertEquals(trie.toString(), "{ab --> 1, acb --> 2, baca --> 7, bdab --> 4, bdb --> 5}");
		assertEquals(trie.contains("ab"), true);
		trie.remove("ab");
		assertEquals(trie.getSize(), 4);
		assertEquals(trie.contains("ab"), false);
		assertEquals(trie.contains("bdb"), true);
		trie.remove("bdb");
		assertEquals(trie.getSize(), 3);
		assertEquals(trie.contains("bdb"), false);
		assertEquals(trie.contains("baca"), true);
		trie.remove("baca");
		assertEquals(trie.getSize(), 2);
		assertEquals(trie.contains("baca"), false);
		assertEquals(trie.toString(), "{acb --> 2, bdab --> 4}");
		assertEquals(trie.contains("acb"), true);
		trie.remove("acb");
		assertEquals(trie.getSize(), 1);
		assertEquals(trie.contains("acb"), false);
		assertEquals(trie.toString(), "{bdab --> 4}");
		assertEquals(trie.isEmpty(), false);
		assertEquals(trie.contains("bdab"), true);
		trie.remove("bdab");
		assertEquals(trie.getSize(), 0);
		assertEquals(trie.toString(), "{}");
		assertEquals(trie.isEmpty(), true);
		assertEquals(trie.contains("bdab"), false);
	}
}
