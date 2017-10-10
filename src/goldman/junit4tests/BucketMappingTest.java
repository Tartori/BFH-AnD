package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.tagged.*;
import goldman.collection.tagged.bucket.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public abstract class BucketMappingTest {
	
	abstract TaggedBucketCollection<String,Integer> createBucketMapping();
	
	@Test
	public void testSize() {
		TaggedBucketCollection<String,Integer> map = createBucketMapping();
		map.put("one",11);
		map.put("two",21);
		map.put("two",22);
		map.put("three",31);
		map.put("three",32);
		map.put("three",33);
		assertEquals(6, map.getSize());
		assertEquals(21, (int) map.remove("two"));
		assertEquals(5, map.getSize());
		assertEquals(true, map.contains("two"));
		assertEquals(22, (int) map.remove("two"));
		assertEquals(4, map.getSize());
		map.remove("three");
		assertEquals(3, map.getSize());
		map.removeBucket("three");
		assertEquals(1,map.getSize());
	}
	
	@Test
	public void testBoundaryEmpty() {
		final TaggedBucketCollection<String, Integer> t = (TaggedBucketCollection<String, Integer>) createBucketMapping();
		assertEquals(0, t.getSize());
		Iterator<TaggedElement<String, Collection<Integer>>> it = t.iterator();
		if (!(it instanceof Locator))
			return;
		final Locator<TaggedElement<String, Collection<Integer>>> loc = (Locator<TaggedElement<String, Collection<Integer>>>) it;
		if (loc instanceof AbstractCollection.VisitingIterator)
			return; // not applicable
		assertEquals(false, loc.advance()); // now at AFT
		try {
			loc.advance();
			fail("expected AtBoundaryException");
		} catch(AtBoundaryException abe) {
			//OK
		}
		assertEquals(false, loc.retreat()); // now at FORE
		try {
			loc.retreat();
			fail("expected AtBoundaryException");
		} catch(AtBoundaryException abe) {
			//OK
		}   	
	}
	
	@Test
	public void testBoundaryWithOne() {
		final TaggedBucketCollection<String, Integer> t = (TaggedBucketCollection<String, Integer>) createBucketMapping();
		assertEquals(0, t.getSize());
		Iterator<TaggedElement<String, Collection<Integer>>> it = t.iterator();
		if (!(it instanceof Locator))
			return;
		final Locator<TaggedElement<String, Collection<Integer>>> loc = (Locator<TaggedElement<String, Collection<Integer>>>) it;
		if (loc instanceof AbstractCollection.VisitingIterator)
			return; // not applicable
		t.put("a", 1);
		assertEquals(true, t.contains("a"));
		assertEquals(false, t.contains("b"));
		assertEquals(1, t.getSize());
		assertEquals("{a --> <1>}", t.toString());
		assertEquals(true, loc.advance()); // now at the element
		assertEquals(false, loc.advance()); // now at AFT
		try {
			loc.advance();
			fail("expected AtBoundaryException");
		} catch(AtBoundaryException abe) {
			//OK
		}
		assertEquals(true, loc.retreat()); // now at the element
		assertEquals(false, loc.retreat()); // now at FORE
		try {
			loc.retreat();
			fail("expected AtBoundaryException");
		} catch(AtBoundaryException abe) {
			//OK
		}
		t.remove("a");
		assertEquals(false, t.contains("a"));
	}
	
	@Test
	public void testLargeTaggedCollection() {
		Random r = new Random();
		final TaggedBucketCollection<String, Integer> t = (TaggedBucketCollection<String, Integer>) createBucketMapping();
		ArrayList<Integer> values = new ArrayList<Integer>();
		ArrayList<Integer> removed = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++)
			values.add(i);
		Collections.shuffle(values);
		for (int i : values)
			t.put("" + ('a' + i), i);
		while (!values.isEmpty()) {
			int index = r.nextInt(values.size());
			Integer x = values.get(index);
			values.remove(index);
			removed.add(x);
			t.remove("" + ('a' + x));
			assertEquals(values.size(), t.getSize());
			hasAll(t, values);
			hasNone(t, removed);
		}
	}


	private void hasAll(TaggedBucketCollection<String, Integer> t, ArrayList<Integer> values) {
		for (Integer i : values) {
			assertEquals(true, t.contains("" + ('a' + i)));
			assertEquals(true, t.get("" + ('a' + i)).contains(i));
		}
	}
	
	private void hasNone(TaggedBucketCollection<String, Integer> t, ArrayList<Integer> values) {
		for (Integer i : values) {
			assertEquals(false, t.contains("" + ('a' + i)));
		}
	}


}
