package goldman.junit4tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import goldman.collection.tagged.TaggedCollection;
import goldman.collection.tagged.priority.TaggedPairingHeap;

public class TaggedPairingHeapTest extends TaggedPriorityQueueTest {

	@Override
	public TaggedCollection<Integer, String> createCollection() {
		return new TaggedPairingHeap<Integer, String>();
	}
	
	@Test
	public void empty() {
		assertEquals(0, createCollection().getSize());
	}

}
