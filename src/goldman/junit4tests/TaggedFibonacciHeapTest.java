package goldman.junit4tests;

import static org.junit.Assert.*;
import org.junit.Test;

import goldman.collection.tagged.TaggedCollection;
import goldman.collection.tagged.priority.TaggedFibonacciHeap;

public class TaggedFibonacciHeapTest extends TaggedPriorityQueueTest {

	@Override
	public TaggedCollection<Integer, String> createCollection() {
		return new TaggedFibonacciHeap<Integer, String>();
	}
	
	@Test
	public void empty() {
		assertEquals(0, createCollection().getSize());
	}

}
