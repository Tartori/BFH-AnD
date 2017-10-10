package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ConcurrentModificationException;

import goldman.collection.*;
import goldman.collection.positional.*;

public class ArrayTest extends PositionalCollectionTest {

	public Collection<Comparable> createCollection() {
		return new Array<Comparable>();
	}
	
	public Collection<Comparable> createCollection(int size) {
		return new Array<Comparable>(size);
	}

	protected AbstractPositionalCollection<IndexedNumber> createRadixCollection() {
		return new Array<IndexedNumber>();
	}
	
	protected AbstractPositionalCollection<IndexedNumber> createRadixCollection(int capacity) {
		return new Array<IndexedNumber>(capacity);
	}
	
	@Test
	public void testSortEmpty() {
		Array<Comparable> a = (Array<Comparable>) createCollection();
		assertEquals(false, a.iterator().hasNext());
		a.quicksort();
		assertEquals(false, a.iterator().hasNext());
	}
	
	@Test
	public void testForConcurrentModification() {
		Array<Comparable> t = (Array<Comparable>) createCollection(20);
		final Locator<Comparable> loc = t.iterator();
		for (int i = 0; i < 20; i++) {
			t.add(i);
		}
		t.remove(10);
		if (t instanceof Tracked)
			return;
		try {
			loc.advance();
			fail("expected ConcurrentModificationException");
		} catch (ConcurrentModificationException cme) {
			//OK
		}
	}
	
	@Test
	public void testForConcurrentModificationAfterSort() {
		Array<Comparable> t = (Array<Comparable>) createCollection(20);
		final Locator<Comparable> loc = t.iterator();
		for (int i = 0; i < 20; i++) {
			t.add(i);
		}
		t.quicksort();
		try {
			loc.advance();
			fail("expected ConcurrentModificationException");
		} catch (ConcurrentModificationException cme) {
			//OK
		}
	}

}
