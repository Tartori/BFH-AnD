package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.ordered.BPlusTree;

public class BPlusTreeTest extends BTreeTest {

	@Override
	public Collection<Comparable> createCollection() {
		return new BPlusTree<Comparable>();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
	
}
