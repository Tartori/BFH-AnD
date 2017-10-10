package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.tagged.TaggedCollection;
import goldman.collection.tagged.ordered.TaggedOrderedCollection;

public abstract class MappingTest extends TaggedCollectionTest {
	
	@Test
	public void testOne() {
		TaggedCollection<Integer,String> t = createCollection();
		t.put(2,"b");
		assertEquals(delimLeft() + "2 --> b" + delimRight(),t.toString());
	}
	
	@Test
	public void testSize() {
		TaggedCollection<Integer,String> t = createCollection();
		assertEquals(0,t.getSize());
		t.put(2,"b");
		assertEquals(delimLeft() + "2 --> b" + delimRight(),t.toString());
		assertEquals(1,t.getSize());
		t.put(3,"c");
		assertEquals(2,t.getSize());
		assertEquals(true, t.contains(2));
		assertEquals(true, t.contains(3));
		if (t instanceof TaggedOrderedCollection)
			assertEquals(delimLeft() + "2 --> b, 3 --> c" + delimRight(),t.toString());
		t.remove(3);
		assertEquals(true, t.contains(2));
		assertEquals(false, t.contains(3));
		assertEquals(delimLeft() + "2 --> b" + delimRight(),t.toString());
		assertEquals(1,t.getSize());
		t.put(1,"a");
		assertEquals(true, t.contains(2));
		assertEquals(true, t.contains(1));
		assertEquals(2,t.getSize());
		if (t instanceof TaggedOrderedCollection)
			assertEquals(delimLeft() + "a --> a, b -- b" + delimRight(),t.toString());
		t.clear();
		assertEquals(0,t.getSize());
		assertEquals(delimLeft() + delimRight(),t.toString());
	}
	
	@Test
	public void testContains() {
		TaggedCollection<Integer,String> t = createCollection();
		assertEquals(false,t.contains(2));
		t.put(2,"b");
		assertEquals(delimLeft() + "2 --> b" + delimRight(),t.toString());
		assertEquals(true,t.contains(2));
		assertEquals(false,t.contains(3));
		
		t.put(3,"c");
		assertEquals(true,t.contains(3));
		if (t instanceof TaggedOrderedCollection)
			assertEquals(delimLeft() + "2 --> b, 3 --> c" + delimRight(),t.toString());
		
		t.remove(3);
		assertEquals(delimLeft() + "2 --> b" + delimRight(),t.toString());
		assertEquals(true,t.contains(2));
		assertEquals(false,t.contains(3));
		
		t.put(1,"a");
		if (t instanceof TaggedOrderedCollection)
			assertEquals(delimLeft() + "1 --> a, 2 --> b" + delimRight(),t.toString());
		assertEquals(true,t.contains(1));
		assertEquals(true,t.contains(2));
		assertEquals(false,t.contains(3));

		t.clear();
		assertEquals(delimLeft() + "" + delimRight(),t.toString());
		//System.out.println("...after toString");
		assertEquals(false,t.contains(1));
		//System.out.println("...after contains");
		assertEquals(false,t.contains(2));
		assertEquals(false,t.contains(3));
	}
}
