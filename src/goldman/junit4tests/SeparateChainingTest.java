package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.positional.Array;
import goldman.collection.set.*;

public class SeparateChainingTest extends SetTest {

	public Collection<Comparable> createCollection() {
		return new SeparateChaining<Comparable>();
	}
	
	public Collection<Comparable> createCollection(int size) {
		return new SeparateChaining<Comparable>(size);
	}
	
	@Test
	public void testRemoveFromIterator2() {
		Set<Comparable> t = (SeparateChaining<Comparable>) createCollection();
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
		Locator<Comparable> aLoc = a.iterator();
		Locator<Comparable> tLoc = t.iterator();
		aLoc.advance();
		tLoc.advance();
		aLoc.advance();
		tLoc.advance();
		assertEquals(aLoc.get(), tLoc.get());
		aLoc.remove();
		tLoc.remove();
		assertEquals(aLoc.get(), tLoc.get());
		aLoc.advance();
		tLoc.advance();
		assertEquals(aLoc.get(),tLoc.get());
		while (aLoc.retreat()) {
			tLoc.retreat();
			assertEquals(aLoc.get(), tLoc.get());
		}
	}

}

