package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.collection.*;
import goldman.collection.positional.*;

public class DoublyLinkedListTest extends PositionalCollectionTest {

	@Override
	public Collection<Comparable> createCollection() {
		return new DoublyLinkedList<Comparable>();
	}

	protected PositionalCollection<IndexedNumber> createRadixCollection() {
		return new DoublyLinkedList<IndexedNumber>();
	}

	protected PositionalCollection<IndexedNumber> createRadixCollection(int capacity) {
		return new DoublyLinkedList<IndexedNumber>();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
