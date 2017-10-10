package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;

import goldman.collection.Collection;
import goldman.collection.ordered.RedBlackTree;

public class RedBlackTreeTest extends BinarySearchTreeTest {
	
	public Collection<Comparable> createCollection() {
		return new RedBlackTree<Comparable>();
	}
	
	protected void checkRep(Collection c) {
		((RedBlackTree) c).checkRep();
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
	

}
