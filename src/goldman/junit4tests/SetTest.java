package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.positional.Array;
import goldman.collection.set.*;

public abstract class SetTest extends CollectionTest {

	@Test
	public void testOne() {
		Set<Comparable> t = (Set<Comparable>) createCollection();
		t.add("sally");
		t.add("ken");
		t.add("mark");
		t.add("julie");
		t.add("ben");
		assertEquals(false,t.contains("mom"));
		assertEquals(true,t.contains("ken"));
		assertEquals(true,t.contains("julie"));
		assertEquals(true,t.contains("sally"));
		assertEquals(true,t.contains("mark"));
		assertEquals(true,t.contains("ben"));
		
		t.remove("sally");
		assertEquals(false,t.contains("mom"));
		assertEquals(true,t.contains("ken"));
		assertEquals(true,t.contains("julie"));
		assertEquals(false,t.contains("sally"));
		assertEquals(true,t.contains("mark"));
		assertEquals(true,t.contains("ben"));
	}
	
	@Test
	public void testRetreat() {
		Set<Comparable> t = (Set<Comparable>) createCollection();
		t.add("sally");
		t.add("ken");
		t.add("mark");
		t.add("julie");
		t.add("ben");
		t.add("michael");
		t.add("joni");
		t.add("david");
		t.add("louis");
		t.add("sharon");
		t.ensureCapacity(10);
		Array<Comparable> a = new Array<Comparable>(10);
		a.addAll(t);
		Locator<Comparable> aLoc = a.iteratorAtEnd();
		Locator<Comparable> tLoc = t.iterator();
		while (tLoc.hasNext()) tLoc.advance();
		aLoc.retreat();
		assertEquals(aLoc.get(), tLoc.get());
		while (aLoc.retreat()) {
			tLoc.retreat();
			assertEquals(aLoc.get(), tLoc.get());
		}
		
		aLoc = a.getLocator("michael");
		tLoc = t.getLocator("michael");
		assertEquals(aLoc.get(), tLoc.get());
		while (aLoc.retreat()) {
			tLoc.retreat();
			assertEquals(aLoc.get(), tLoc.get());
		}
	}
	
	@Test
	public void testRetreat2() {
		Set<Comparable> t = (Set<Comparable>) createCollection();
		t.add("sally");
		t.add("ken");
		t.add("mark");
		t.add("julie");
		t.add("ben");
		assertEquals(false,t.contains("mom"));
		assertEquals(true,t.contains("ken"));
		assertEquals(true,t.contains("julie"));
		assertEquals(true,t.contains("sally"));
		assertEquals(true,t.contains("mark"));
		assertEquals(true,t.contains("ben"));
		t.ensureCapacity(8);
		Array<Comparable> a = new Array<Comparable>(8);
		a.addAll(t);
		Locator<Comparable> aLoc = a.iteratorAtEnd();
		Locator<Comparable> tLoc = t.iterator();
		while (tLoc.hasNext()) tLoc.advance();
		aLoc.retreat();
		assertEquals(aLoc.get(), tLoc.get());
		while (aLoc.retreat()) {
			tLoc.retreat();
			assertEquals(aLoc.get(), tLoc.get());
		}
		
		aLoc = a.getLocator("ben");
		tLoc = t.getLocator("ben");
		assertEquals(aLoc.get(), tLoc.get());
		while (aLoc.retreat()) {
			tLoc.retreat();
			assertEquals(aLoc.get(), tLoc.get());
		}
	}
	
	@Test
	public void testRemoveFromIterator() {
		Set<Comparable> t = (Set<Comparable>) createCollection();
		t.add("sally");
		t.add("ken");
		t.add("mark");
		t.add("julie");
		t.add("ben");
		t.add("michael");
		t.add("joni");
		t.add("david");
		t.add("louis");
		t.add("sharon");
		t.ensureCapacity(10);
		Array<Comparable> a = new Array<Comparable>(10);
		a.addAll(t);
		Locator<Comparable> aLoc = a.iteratorAtEnd();
		Locator<Comparable> tLoc = t.iterator();
		while (tLoc.hasNext()) tLoc.advance();
		aLoc.retreat();
		assertEquals(aLoc.get(), tLoc.get());
		for (int i=0; i < 3; i++) {
			aLoc.retreat();
			tLoc.retreat();
			assertEquals(aLoc.get(), tLoc.get());
		}
		for (int i=0; i < 3; i++) {
			aLoc.remove();
			tLoc.remove();
			assertEquals(aLoc.get(), tLoc.get());
		}
		aLoc.advance();
		tLoc.advance();
		assertEquals(aLoc.get(),tLoc.get());
		while (aLoc.retreat()) {
			tLoc.retreat();
			assertEquals(aLoc.get(), tLoc.get());
		}
	}
	
	// The following inherited test is not applicable.
	@Override
	@Test
	public void testMembershipWithIterator() {}
}

