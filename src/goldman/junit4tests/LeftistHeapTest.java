package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.priority.*;

public class LeftistHeapTest extends PriorityQueueTest {

	public Collection<Comparable> createCollection() {
		return new LeftistHeap<Comparable>();
	}
	
	public Collection<Comparable> createTrackedCollection() {
		return new LeftistHeap<Comparable>();
	}

	@Test
	public void testMerge() {
		LeftistHeap<Comparable> t1 = (LeftistHeap<Comparable>) createTrackedCollection();
		LeftistHeap<Comparable> t2 = (LeftistHeap<Comparable>) createTrackedCollection();
		LeftistHeap<Comparable> t3 = (LeftistHeap<Comparable>) createTrackedCollection();
		LeftistHeap<Comparable> t4 = (LeftistHeap<Comparable>) createTrackedCollection();
		LeftistHeap<Comparable> t5 = (LeftistHeap<Comparable>) createTrackedCollection();
		LeftistHeap<Comparable> t6 = (LeftistHeap<Comparable>) createTrackedCollection();
		PriorityQueueLocator<Comparable> loc;
		t1.add("a");
		t2.add("b");
		t2.add("g");
		t1.add("f");
		t1.add("c");
		loc = t2.addTracked("d");
		t3.add("x");
		t3.add("y");
		t4.add("p");
		t5.add("l");
		t5.add("j");
		t6.add("q");
		
		assertEquals(true, t1.contains("a"));
		assertEquals(false, t2.contains("a"));
		assertEquals(true, t1.contains("f"));
		assertEquals(false, t2.contains("f"));
		assertEquals(true, t1.contains("c"));
		assertEquals(false, t2.contains("c"));
		assertEquals(true, t2.contains("b"));
		assertEquals(false, t1.contains("b"));
		assertEquals(true, t2.contains("g"));
		assertEquals(false, t1.contains("g"));
		assertEquals(true, t2.contains("d"));
		assertEquals(false, t1.contains("d"));
		assertEquals(3,t1.getSize());
		assertEquals(3,t2.getSize());
		LeftistHeap<Comparable> t = new LeftistHeap<Comparable>(t1, t2); 
		assertEquals(6, t.getSize());
		assertEquals(true, t.contains("a"));
		assertEquals(true, t.contains("b"));
		assertEquals(true, t.contains("c"));
		assertEquals(true, t.contains("f"));
		assertEquals(true, t.contains("g"));
		assertEquals(true, t.contains("d"));
		assertEquals(false, t.contains("e"));
		assertEquals("d", loc.get());
		loc.update("k");
		assertEquals(true, t.contains("k"));
		assertEquals(false, t.contains("d"));
		
		LeftistHeap<Comparable> t10 = new LeftistHeap<Comparable>(t3, t4);   
		LeftistHeap<Comparable> t11 = new LeftistHeap<Comparable>(t10, t5);   
		LeftistHeap<Comparable> t12 = new LeftistHeap<Comparable>(t11, t);
		loc.update("s");
		assertEquals(false, t12.contains("k"));
		assertEquals(true, t12.contains("s"));
		
		t1.add("3");
		t1.add("5");
		t1.add("2");
		assertEquals(3,t1.getSize());
		LeftistHeap<Comparable> t13 = new LeftistHeap<Comparable>(t1, t6);  
		
		LeftistHeap<Comparable> t14 = new LeftistHeap<Comparable>(t12,t13);  
		assertEquals(15, t14.getSize());
		assertEquals(0,t1.getSize());
		assertEquals("y", t14.extractMax());
		assertEquals("x", t14.extractMax());
		assertEquals("s", t14.extractMax());
		assertEquals("q", t14.extractMax());
		assertEquals("p", t14.extractMax());
		assertEquals("l", t14.extractMax());
		assertEquals("j", t14.extractMax());
		assertEquals("g", t14.extractMax());
		assertEquals("f", t14.extractMax());
		assertEquals("c", t14.extractMax());
		assertEquals("b", t14.extractMax());
		assertEquals("a", t14.extractMax());
		assertEquals("5", t14.extractMax());
		assertEquals("3", t14.extractMax());
		assertEquals("2", t14.extractMax());
		try {
			t14.extractMax();
			fail("expected NoSuchElementException");
		} catch (java.util.NoSuchElementException nsee){}
		assertEquals(0, t14.getSize());
	}

	// The following inherited tests are not applicable to LeftistHeap:
	
	@Override
	@Test
	public void testBoundaryEmptyAft() { }

	@Override
	@Test
	public void testBoundaryEmptyFore() { }

	@Override
	@Test
	public void testBoundaryWithOneAft() { }

	@Override
	@Test
	public void testBoundaryWithOneFore() { }
	
}
