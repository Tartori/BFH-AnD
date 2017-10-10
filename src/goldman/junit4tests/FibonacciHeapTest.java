package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.collection.*;
import goldman.collection.priority.*;

public class FibonacciHeapTest extends PriorityQueueTest {

	public Collection<Comparable> createCollection() {
		return new FibonacciHeap<Comparable>();
	}
	
	public Collection<Comparable> createTrackedCollection() {
		return new FibonacciHeap<Comparable>();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
