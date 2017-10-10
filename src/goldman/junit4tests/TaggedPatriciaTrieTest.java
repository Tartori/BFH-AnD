package goldman.junit4tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import goldman.collection.Collection;
import goldman.collection.Digitizer;
import goldman.collection.Locator;
import goldman.collection.positional.SinglyLinkedList;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.ordered.digitized.TaggedDigitizedOrderedCollection;
import goldman.collection.tagged.ordered.digitized.TaggedDigitizedOrderedCollectionWrapper;
import goldman.collection.tagged.ordered.digitized.TaggedPatriciaTrie;
public class TaggedPatriciaTrieTest extends TaggedTrieTest {
	
	public TaggedDigitizedOrderedCollection createCollection(int base) {
		Digitizer<String> digitizer = new Digitizer<String>() {
			public String formatDigit(String x, int place) {
				return x.substring(place,place+1);
			}
			public int getBase() {
				return 2;
			}
			public int getDigit(String x, int place) {
				return x.substring(place,place+1).equals("0") ? 0 : 1;
			}
			public boolean isPrefixFree() {
				return false;
			}
			public int numDigits(String x) {
				return x.length();
			}
			
		};
	    try {
			return new TaggedPatriciaTrie<String,Integer>(digitizer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLongAll() throws Exception {
		TaggedDigitizedOrderedCollectionWrapper<String,Integer> trie = 
			(TaggedDigitizedOrderedCollectionWrapper<String, Integer>) createCollection(1);
		assertEquals(0, trie.getSize());
		trie.put("01", 1);
		assertEquals(1, trie.getSize());
		assertEquals(delimLeft() + "01 --> 1" + delimRight(), trie.toString());
		assertEquals(true, trie.contains("01"));
		assertEquals(false, trie.contains("0101"));
		assertEquals(false, trie.contains("0"));
		assertEquals(false, trie.contains("0110"));
		try {
			trie.put("0101", 2);
			fail("should detect prefix-free violation");
		} catch (IllegalArgumentException iae) {
			//OK
		}
		assertEquals(1, trie.getSize());
		assertEquals(delimLeft() + "01 --> 1" + delimRight(), trie.toString());
		trie.put("111001", 3);
		assertEquals(2, trie.getSize());
		assertEquals(delimLeft() + "01 --> 1, 111001 --> 3" + delimRight(), trie.toString());
		trie.put("111010", 4);
		assertEquals(3, trie.getSize());
		assertEquals(delimLeft() + "01 --> 1, 111001 --> 3, 111010 --> 4" + delimRight(), trie.toString());
		trie.put("1111", 5);
		assertEquals(4, trie.getSize());
		assertEquals(delimLeft() + "01 --> 1, 111001 --> 3, 111010 --> 4, 1111 --> 5" + delimRight(), trie.toString());
		Locator<TaggedElement<String,Integer>> tracker = trie.putTracked("1010", 6);
		assertEquals(5, trie.getSize());
		assertEquals(delimLeft() + "01 --> 1, 1010 --> 6, 111001 --> 3, 111010 --> 4, 1111 --> 5" + delimRight(), trie.toString());
		
		tracker.retreat();
		assertEquals("01 --> 1",tracker.get().toString());
		tracker.advance();
		assertEquals("1010 --> 6",tracker.get().toString());
		tracker.advance();
		assertEquals("111001 --> 3",tracker.get().toString());
		tracker.retreat();
		assertEquals("1010 --> 6",tracker.get().toString());
	
		Collection<TaggedElement<String,Integer>> c =  new SinglyLinkedList<TaggedElement<String,Integer>>();
		
		trie.longestCommonPrefix("0",c);
		assertEquals("<01 --> 1>", c.toString());
		
		c.clear();
		trie.longestCommonPrefix("1110",c);		
		assertEquals("<111001 --> 3, 111010 --> 4>", c.toString());	
		
		c.clear();
		trie.longestCommonPrefix("1",c);	
		assertEquals("<1010 --> 6, 111001 --> 3, 111010 --> 4, 1111 --> 5>", c.toString());
		
		c.clear();
		trie.completions("0",c);
		assertEquals("<01 --> 1>", c.toString());
		
		c.clear();
		trie.completions("11111",c);
		assertEquals("<>", c.toString());	
		
		c.clear();
		trie.completions("1",c);
		assertEquals("<1010 --> 6, 111001 --> 3, 111010 --> 4, 1111 --> 5>", c.toString());
		
		
		trie.remove("111001");
		assertEquals(4, trie.getSize());
		assertEquals(false, trie.contains("111001"));
		assertEquals(true, trie.contains("1111"));
		assertEquals("1010", tracker.get().getTag());
		tracker.remove();
		assertEquals(3, trie.getSize());
		assertEquals(false, trie.contains("1010"));
		assertEquals("{01 --> 1, 111010 --> 4, 1111 --> 5}", trie.toString());
		assertEquals(true, trie.contains("01"));
		trie.remove("01");
		assertEquals(2, trie.getSize());
		assertEquals(false, trie.contains("01"));
		assertEquals(true, trie.contains("1111"));
		trie.remove("1111");
		assertEquals(1, trie.getSize());
		assertEquals(false, trie.contains("1111"));
		assertEquals("{111010 --> 4}", trie.toString());
		assertEquals(false, trie.isEmpty());
		assertEquals(true, trie.contains("111010"));
		trie.remove("111010");
		assertEquals(0, trie.getSize());
		assertEquals("{}", trie.toString());
		assertEquals(true, trie.isEmpty());
		assertEquals(false, trie.contains("111010"));
	}
}
