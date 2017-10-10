package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.collection.Collection;
import goldman.collection.ordered.SplayTree;

public class SplayTreeTest extends OrderedCollectionTest {
		
	public Collection<Comparable> createCollection() {
		SplayTree<Comparable> t = new SplayTree<Comparable>();
		return t;
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}

}
