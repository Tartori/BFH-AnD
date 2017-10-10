package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.Collection;
import goldman.collection.ordered.BinarySearchTree;

public class BinarySearchTreeTest extends OrderedCollectionTest {
		
	public Collection<Comparable> createCollection() {
		return new BinarySearchTree<Comparable>();
	}
	
	@Test
	public void testToArray() {
		BinarySearchTree<String> bst = new BinarySearchTree<String>();
		bst.add("foo");
		Object[] a = bst.toArray();
		assertEquals(1, a.length);
	}
	
	// Most tests are inherited

}
