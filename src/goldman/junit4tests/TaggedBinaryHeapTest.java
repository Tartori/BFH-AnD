package goldman.junit4tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import goldman.collection.tagged.TaggedCollection;
import goldman.collection.tagged.priority.TaggedBinaryHeap;

public class TaggedBinaryHeapTest extends TaggedPriorityQueueTest {

	@Override
	public TaggedCollection<Integer, String> createCollection() {
		return new TaggedBinaryHeap<Integer, String>(8, true);
	}
	
	@Test
	public void empty() {
		assertEquals(0, createCollection().getSize());
	}

}
