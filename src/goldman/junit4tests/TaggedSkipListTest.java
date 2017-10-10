package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.tagged.ordered.*;

public class TaggedSkipListTest extends TaggedOrderedCollectionTest {

	
	public TaggedOrderedCollection<Integer, String> createCollection(int initialCapacity) {
		return createCollection();
	}
	
	public TaggedOrderedCollection<Integer, String> createCollection() {
		return new TaggedSkipList<Integer,String>();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
