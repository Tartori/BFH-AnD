package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.positional.*;

public class DynamicArrayTest extends ArrayTest {

	public Collection<Comparable> createCollection() {
		return new DynamicArray<Comparable>();
	}
	
	public Collection<Comparable> createCollection(int size) {
		return new DynamicArray<Comparable>(size);
	}

	protected AbstractPositionalCollection<IndexedNumber> createRadixCollection() {
		return new DynamicArray<IndexedNumber>();
	}
	
	protected AbstractPositionalCollection<IndexedNumber> createRadixCollection(int capacity) {
		return new DynamicArray<IndexedNumber>(capacity);
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
	


}
