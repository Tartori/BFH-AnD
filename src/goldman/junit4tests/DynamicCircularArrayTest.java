package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.collection.*;
import goldman.collection.positional.*;

public class DynamicCircularArrayTest extends ArrayTest {

	public Collection<Comparable> createCollection() {
		return new DynamicCircularArray<Comparable>();
	}
	
	public Collection<Comparable> createCollection(int size) {
		return new DynamicCircularArray<Comparable>(size);
	}

	protected AbstractPositionalCollection<IndexedNumber> createRadixCollection() {
		return new DynamicCircularArray<IndexedNumber>();
	}
	
	protected AbstractPositionalCollection<IndexedNumber> createRadixCollection(int capacity) {
		return new DynamicCircularArray<IndexedNumber>(capacity);
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
