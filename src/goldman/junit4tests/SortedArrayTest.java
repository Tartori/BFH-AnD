package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;

import goldman.collection.Collection;
import goldman.collection.ordered.SortedArray;

public class SortedArrayTest extends OrderedCollectionTest {
	public Collection<Comparable> createCollection() {
		return new SortedArray<Comparable>();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
}
