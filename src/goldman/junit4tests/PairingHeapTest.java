package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.collection.*;
import goldman.collection.priority.*;

public class PairingHeapTest extends PriorityQueueTest {

	public Collection<Comparable> createCollection() {
		return new PairingHeap<Comparable>();
	}
	
	public Collection<Comparable> createTrackedCollection() {
		return new PairingHeap<Comparable>();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
