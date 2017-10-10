package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.tagged.*;
import goldman.collection.tagged.ordered.*;
import goldman.collection.tagged.priority.TaggedPriorityQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;


public abstract class TaggedCollectionTest {

	protected TaggedCollectionTest() {}

	public abstract TaggedCollection<Integer,String> createCollection(int capacity);
	public abstract TaggedCollection<Integer,String> createCollection();
	public String delimLeft() { return "{"; }
	public String delimRight() { return "}"; }


	@Test
	public void testPairs() {
		final TaggedCollection<Integer,String> table = (TaggedCollection<Integer,String>) createCollection(8);
		String rec1 = new String("12345");
		String rec2 = new String("54321");	

		final Integer key1 = new Integer(5);
		Integer key2 = new Integer(12);
		Integer key3 = new Integer(10);
		Integer key4 = new Integer(8);
		Integer key5 = new Integer(10);

		table.put(key1, rec1);
		assertEquals(rec1, table.get(key1));

		table.put(key2, rec1);
		assertEquals(rec1, table.get(key2));

		if (!(table instanceof TaggedBTree || table instanceof TaggedBPlusTree)) {
			if (table instanceof TaggedOrderedCollection)
				table.remove(key2);

			table.put(key2, rec2);
			String x = table.get(key2);
			if (table instanceof TaggedPriorityQueue)
				assertEquals(true, x.equals(rec1) || x.equals(rec2));
			else
				assertEquals(rec2, x);

			table.put(key3, rec2);
			assertEquals(rec2, table.get(key3));

			table.remove(key1);
			assertEquals(false, table.contains(key1));
			try {
				table.get(key1);
				fail("expected NoSuchElementException");
			} catch(NoSuchElementException nsee) {
				//OK
			}

			if (table instanceof TaggedOrderedCollection)
				table.remove(key3);

			table.put(key3, null);
			if (table instanceof TaggedPriorityQueue)
				assertEquals(true, table.get(key3) == null || table.get(key3).equals(rec2));
			else
				assertEquals(null, table.get(key3));

			table.put(key4, null);
			assertEquals(null, table.get(key4));
			
			if (table instanceof TaggedOrderedCollection)
				table.remove(key3);
			table.put(key5, rec1); // duplicates key3
			
			if (table instanceof TaggedPriorityQueue)
				assertEquals(true, table.get(key5).equals(rec1) || table.get(key5).equals(rec2));
			else
				assertEquals(rec1, table.get(key5));
			
			if (table instanceof TaggedPriorityQueue)
				assertEquals(true, table.get(key3).equals(rec1) || table.get(key3).equals(rec2));
			else			
				assertEquals(rec1, table.get(key3));
		}
	}
	
	@Test
	public void testBoundaryEmpty() {
		final TaggedCollection<Integer,String> t = (TaggedCollection<Integer,String>) createCollection(8);
		assertEquals(0, t.getSize());
		Iterator<TaggedElement<Integer, String>> it = t.iterator();
		if (!(it instanceof Locator))
			return;
		final Locator<TaggedElement<Integer, String>> loc = (Locator<TaggedElement<Integer, String>>) it;
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
	public void testGet() {
		final TaggedCollection<Integer,String> t = (TaggedCollection<Integer,String>) createCollection(8);
		t.put(1,"a");
		t.put(3, "c");
		t.put(2, "b");
		assertEquals("a", t.get(1));
		assertEquals("b", t.get(2));
		assertEquals("c", t.get(3));
		assertEquals(false, t.contains(0));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testGetInvalidTag() {
		final TaggedCollection<Integer,String> t = (TaggedCollection<Integer,String>) createCollection(8);
		t.put(1,"a");
		t.put(3, "c");
		t.put(2, "b");
		assertEquals("a", t.get(1));
		assertEquals("b", t.get(2));
		assertEquals("c", t.get(3));
		t.get(0);
	}
	
	
	@Test
	public void testRemoveOne() {
		final TaggedCollection<Integer,String> t = (TaggedCollection<Integer,String>) createCollection(8);
		assertEquals(0, t.getSize());
		t.put(1,"a");	
		t.remove(1);
		assertEquals(false, t.contains(1));
	}
	
	@Test
	public void testBoundaryWithOne() {
		final TaggedCollection<Integer,String> t = (TaggedCollection<Integer,String>) createCollection(8);
		assertEquals(0, t.getSize());
		Iterator<TaggedElement<Integer, String>> it = t.iterator();
		if (!(it instanceof Locator))
			return;
		final Locator<TaggedElement<Integer, String>> loc = (Locator<TaggedElement<Integer, String>>) it;
		if (loc instanceof AbstractCollection.VisitingIterator)
			return; // not applicable
		t.put(1,"a");
		assertEquals(true, t.contains(1));
		assertEquals(false, t.contains(2));
		assertEquals(1, t.getSize());
		assertEquals(delimLeft()+"1 --> a"+delimRight(), t.toString());
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
		t.remove(1);
		assertEquals(false, t.contains(1));
	}
	
	@Test
	public void testLargeTaggedCollection() {
		Random r = new Random();
		final TaggedCollection<Integer,String> t = (TaggedCollection<Integer,String>) createCollection(8);
		ArrayList<Integer> values = new ArrayList<Integer>();
		ArrayList<Integer> removed = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) //should be 100
			values.add(i);
		Collections.shuffle(values);
		for (int i : values) {
			t.put(i, "" + ('a' + i));
		}
		while (!values.isEmpty()) {
			int index = r.nextInt(values.size());
			Integer x = values.get(index);
			values.remove(index);
			removed.add(x);
			t.remove(x);
			assertEquals(values.size(), t.getSize());
			hasAll(t, values);
			hasNone(t, removed);
		}
	}

	private void hasAll(TaggedCollection<Integer, String> t, ArrayList<Integer> values) {
		for (Integer i : values) {
			assertEquals(true, t.contains(i));
			assertEquals(""+ ('a'+i), t.get(i));
		}
	}
	
	private void hasNone(TaggedCollection<Integer, String> t, ArrayList<Integer> values) {
		for (Integer i : values) {
			assertEquals(false, t.contains(i));
		}
	}

}
