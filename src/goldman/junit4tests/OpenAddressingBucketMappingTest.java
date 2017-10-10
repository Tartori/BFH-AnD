package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.collection.Collection;
import goldman.collection.positional.DoublyLinkedList;
import goldman.collection.tagged.bucket.*;
import goldman.collection.tagged.set.*;

public class OpenAddressingBucketMappingTest extends BucketMappingTest {

	TaggedBucketCollection<String, Integer> createBucketMapping() {
		return new TaggedBucketCollectionWrapper<String,Integer>
			(new OpenAddressingMapping<String,Collection<Integer>>(), DoublyLinkedList.class);
	}

	@Test
	public void initializationTest() {
		assertEquals(0, createBucketMapping().getSize());
	}
}
