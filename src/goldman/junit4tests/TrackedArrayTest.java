package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.collection.*;
import goldman.collection.positional.*;

public class TrackedArrayTest extends PositionalCollectionTest {

	public Collection<Comparable> createCollection() {
		return new TrackedArray<Comparable>();
	}
	
	public Collection<Comparable> createCollection(int size) {
		return new TrackedArray<Comparable>(size);
	}

	protected PositionalCollection<IndexedNumber> createRadixCollection() {
		return new TrackedArray<IndexedNumber>();
	}
	
	protected PositionalCollection<IndexedNumber> createRadixCollection(int capacity) {
		return new TrackedArray<IndexedNumber>(capacity);
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
}
