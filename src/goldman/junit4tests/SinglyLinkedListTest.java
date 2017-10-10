package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.positional.*;

public class SinglyLinkedListTest extends PositionalCollectionTest {

	@Override
	public Collection<Comparable> createCollection() {
		return new SinglyLinkedList<Comparable>();
	}

	protected PositionalCollection<IndexedNumber> createRadixCollection() {
		return new SinglyLinkedList<IndexedNumber>();
	}

	protected PositionalCollection<IndexedNumber> createRadixCollection(int capacity) {
		return new SinglyLinkedList<IndexedNumber>();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
