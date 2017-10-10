package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.priority.*;

public class BinaryHeapTest extends PriorityQueueTest {

	public Collection<Comparable> createCollection() {
		return new BinaryHeap<Comparable>();
	}
	
	public Collection<Comparable> createTrackedCollection() {
		return new BinaryHeap<Comparable>(true);
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
}
