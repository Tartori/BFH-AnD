package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.tagged.ordered.*;

public abstract class TaggedOrderedCollectionTest extends TaggedCollectionTest {
	
	public abstract TaggedOrderedCollection<Integer,String> createCollection();
	
	@Test
	public void testSize() {
		TaggedOrderedCollection<Integer,String> c = createCollection();
		c.put(2,"b");
		c.put(1,"a");
		c.put(3,"c");
		assertEquals(3, c.getSize());
		if (! (c instanceof TaggedBTree || c instanceof TaggedBPlusTree)) {
			c.remove(2);
			assertEquals(2, c.getSize());
			c.remove(3);
			assertEquals(1, c.getSize());
			c.remove(1);
			assertEquals(0, c.getSize());
		}
		c.put(4,"d");
		if (! (c instanceof TaggedBTree || c instanceof TaggedBPlusTree))
			assertEquals(1,c.getSize());
		else
			assertEquals(4,c.getSize());
		c.clear();
		assertEquals(0, c.getSize());
	}
	
	@Test
	public void testToSring() {
		TaggedOrderedCollection<Integer,String> c = createCollection();
		c.put(2,"b");
		c.put(1,"a");
		c.put(3,"c");
		assertEquals("{1 --> a, 2 --> b, 3 --> c}", c.toString());
		if (! (c instanceof TaggedBTree || c instanceof TaggedBPlusTree)) {
			c.remove(2);
			assertEquals("{1 --> a, 3 --> c}", c.toString());
			c.remove(3);
			assertEquals("{1 --> a}", c.toString());
			c.remove(1);
			assertEquals("{}", c.toString());
		}
		c.put(4,"d");
		if (! (c instanceof TaggedBTree || c instanceof TaggedBPlusTree))
			assertEquals("{4 --> d}", c.toString());
		else
			assertEquals("{1 --> a, 2 --> b, 3 --> c, 4 --> d}", c.toString());
		
		c.clear();
		assertEquals("{}", c.toString());
		assertEquals(0, c.getSize());
	}


}
