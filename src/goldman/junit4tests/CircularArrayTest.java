package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.positional.*;

public class CircularArrayTest extends ArrayTest {

	public Collection<Comparable> createCollection() {
		return new CircularArray<Comparable>();
	}
	
	public Collection<Comparable> createCollection(int size) {
		return new CircularArray<Comparable>(size);
	}

	protected AbstractPositionalCollection<IndexedNumber> createRadixCollection() {
		return new CircularArray<IndexedNumber>();
	}
	
	protected AbstractPositionalCollection<IndexedNumber> createRadixCollection(int capacity) {
		return new CircularArray<IndexedNumber>(capacity);
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
