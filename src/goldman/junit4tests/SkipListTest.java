package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;

import goldman.collection.Collection;
import goldman.collection.ordered.SkipList;

public class SkipListTest extends OrderedCollectionTest {
	
	public Collection<Comparable> createCollection() {
		return new SkipList<Comparable>();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
}
