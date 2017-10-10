package goldman.junit4tests;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import goldman.collection.*;
import goldman.collection.priority.*;

public abstract class PriorityQueueTest extends CollectionTest {

	protected PriorityQueueTest() {}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSmall() {
		PriorityQueue<Comparable> t = (PriorityQueue<Comparable>) createCollection();
		t.add("d");
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("c");
		t.add("g");
		assertEquals(true,t.contains("d"));
		assertEquals(true,t.contains("b"));
		assertEquals(true,t.contains("a"));
		assertEquals(true,t.contains("c"));
		assertEquals(true,t.contains("g"));
		assertEquals(false,t.contains("h"));
		assertEquals("g",t.max());	
		assertEquals("g",t.extractMax());
		assertEquals("d",t.max());	
		assertEquals("d",t.extractMax());
		assertEquals("c",t.max());
		assertEquals("c",t.extractMax());
		assertEquals("c",t.max());
		assertEquals(true,t.contains("c"));
		assertEquals(false,t.contains("d"));
		assertEquals(true,t.contains("b"));
		assertEquals(true,t.contains("a"));
		assertEquals(false,t.contains("g"));
		assertEquals(false,t.contains("h"));
		assertEquals("c",t.extractMax());
		assertEquals("b",t.max());
		assertEquals(false,t.contains("c"));
		assertEquals(false,t.contains("d"));
		assertEquals(true,t.contains("b"));
		assertEquals(true,t.contains("a"));
		assertEquals(false,t.contains("g"));
		assertEquals(false,t.contains("h"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMore() {
		PriorityQueue<Comparable> t = (PriorityQueue<Comparable>) createTrackedCollection();
		PriorityQueueLocator<Comparable>[] locs = (PriorityQueueLocator<Comparable>[]) new PriorityQueueLocator[10];
	 	locs[0] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(75); //add
		assertEquals(new Integer(75), t.max());
		locs[1] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(55); //add
		locs[2] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(65); //add
		locs[3] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(85); //add
		assertEquals(new Integer(85), t.max());
		locs[4] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(95); //add
		assertEquals(new Integer(95), t.max());
		locs[5] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(90); //add
		locs[6] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(80); //add
		locs[7] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(70); //add
		locs[8] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(50); //add
		locs[9] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(60); //add
		locs[0].update((Integer) (locs[0].get())-50); //update
		locs[1].update((Integer) (locs[1].get())-50); //update
		locs[2].update((Integer) (locs[2].get())-50); //update
		locs[3].update((Integer) (locs[3].get())-50); //update
		locs[4].update((Integer) (locs[4].get())-50); //update
		assertEquals(new Integer(90), t.max());
		locs[5].update((Integer) (locs[5].get())-50); //update
		assertEquals(new Integer(80), t.max());
		locs[6].update((Integer) (locs[6].get())-50); //update
		assertEquals(new Integer(70), t.max());
		locs[7].update((Integer) (locs[7].get())-50); //update
		assertEquals(new Integer(60), t.max());
		locs[8].update((Integer) (locs[8].get())-50); //update
		assertEquals(new Integer(60), t.max());
		locs[9].update((Integer) (locs[9].get())-50); //update
		assertEquals(new Integer(45), t.max());
		assertEquals(new Integer(45), t.extractMax());
		assertEquals(new Integer(40), t.extractMax());
		assertEquals(new Integer(35), t.extractMax());
		assertEquals(new Integer(30), t.extractMax());
		assertEquals(new Integer(25), t.extractMax());
		assertEquals(new Integer(20), t.extractMax());
		assertEquals(new Integer(15), t.extractMax());
		assertEquals(new Integer(10), t.extractMax());
		assertEquals(new Integer(5), t.extractMax());
		assertEquals(new Integer(0), t.extractMax());		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMedium() {
		PriorityQueue<Comparable> t = (PriorityQueue<Comparable>) createTrackedCollection();
		PriorityQueueLocator<Comparable>[] locs = (PriorityQueueLocator<Comparable>[]) new PriorityQueueLocator[10];
		locs[0] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(60); //add
		locs[1] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(50); //add
		locs[2] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(60); //add
		locs[3] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(50); //add
		locs[4] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(55); //add
		locs[5] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(55); //add
		locs[0].update((Integer) (locs[0].get())-50); //update
		locs[1].update((Integer) (locs[1].get())-50); //update
		locs[2].update((Integer) (locs[2].get())-50); //update
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTwoSmall() {
		PriorityQueue<Comparable> t = (PriorityQueue<Comparable>) createTrackedCollection();
		PriorityQueueLocator<Comparable>[] locs = (PriorityQueueLocator<Comparable>[]) new PriorityQueueLocator[10];
		for (int i = 0; i < 10; i++)
			locs[i] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(50);
		locs[0].update(0);
		locs[1].update(0);
		locs[2].update(0);
		locs[3].update(0);
		locs[4].update(1);
		locs[5].update(1);
		locs[6].update(1);
		locs[7].update(1);
		locs[8].update(1);
		locs[9].update(0);

		assertEquals(1,t.max());
		assertEquals(1,t.extractMax());
		assertEquals(1,t.extractMax());
		assertEquals(1,t.extractMax());
		assertEquals(1,t.extractMax());
		assertEquals(1,t.extractMax());
		assertEquals(0,t.extractMax());
		assertEquals(0,t.extractMax());
		assertEquals(0,t.extractMax());
		assertEquals(0,t.extractMax());
		assertEquals(0,t.extractMax());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTracking(){
		PriorityQueue<Comparable> t = (PriorityQueue<Comparable>) createTrackedCollection();
		if (!(t instanceof Tracked)) return;
		PriorityQueueLocator<Comparable>[] locs = (PriorityQueueLocator<Comparable>[]) new PriorityQueueLocator[100];
		for (int i = 0; i < 50; i++){
			Integer item = new Integer(i);
			locs[i] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(item);
		}
		for (int i = 50; i < 100; i++){
			Integer item = new Integer(i);
			locs[i] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(item);
		}
		
		for (int i = 0; i < 100; i++)
			assertEquals(new Integer(i),locs[i].get());
	
		locs[0].update(new Integer(200));
		locs[99].update(new Integer(-10));
		assertEquals(new Integer(200), locs[0].get());
		for (int i = 1; i < 99; i++)
			assertEquals(new Integer(i),locs[i].get());
		assertEquals(new Integer(-10), locs[99].get());
		assertEquals(new Integer(200), t.max());
		assertEquals(new Integer(200), t.extractMax());
		assertEquals(new Integer(98), t.max());
		for (int i = 98; i >= 1; i--){
			Integer ans = new Integer(i);
			assertEquals(ans,t.max());
			assertEquals(true,t.contains(ans));
			assertEquals(ans,t.extractMax());
			assertEquals(false,t.contains(ans));
			assertEquals(i,t.getSize());
		}
		assertEquals(false,t.isEmpty());
		assertEquals(new Integer(-10), t.max());
		assertEquals(new Integer(-10), t.extractMax());
		assertEquals(true,t.isEmpty());
	}
	
	static final int size = 20;
	static final int reps = 10;
	
	@Test
	public void testRepeatedly() {
		for (int i = 1; i< 50; i++){
			testLargeThree();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void doTestLarge() {
		PriorityQueue<Comparable> t = (PriorityQueue<Comparable>) createTrackedCollection();
		PriorityQueueLocator<Comparable>[] locs = (PriorityQueueLocator<Comparable>[]) new PriorityQueueLocator[200];
		ArrayList<Comparable> al = new ArrayList<Comparable>();
		for (int i = 0; i < size; i++)
			for (int j = 0; j < reps; j++)
				al.add(5*i+50);
		java.util.Collections.shuffle(al);
		for (int i = 0; i < size*reps; i++) {
			locs[i] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(al.get(i));
		}
		
		for (int i =0; i < size*reps; i++) {
			locs[i].update((Integer) (locs[i].get()) - 50);
		}
		for (int i = size-1; i >= 0; i--){
			for (int j = 0; j < reps; j++) {
				assertEquals(5*i, t.extractMax());
			}
		}
	}
	
	@Test
	public void testTwoPQs() {
		PriorityQueue<Comparable> t1 = (PriorityQueue<Comparable>) createTrackedCollection();
		PriorityQueue<Comparable> t2 = (PriorityQueue<Comparable>) createTrackedCollection();
		t1.add("a");
		t2.add("A");
		t2.add("C");
		t1.add("c");
		t1.add("b");
		t2.add("B");
		assertEquals(true, t1.contains("a"));
		assertEquals(false, t2.contains("a"));
		assertEquals(true, t1.contains("b"));
		assertEquals(false, t2.contains("b"));
		assertEquals(true, t1.contains("c"));
		assertEquals(false, t2.contains("c"));
		assertEquals(true, t2.contains("A"));
		assertEquals(false, t1.contains("A"));
		assertEquals(true, t2.contains("B"));
		assertEquals(false, t1.contains("B"));
		assertEquals(true, t2.contains("C"));
		assertEquals(false, t1.contains("C"));
		assertEquals("c", t1.extractMax());
		assertEquals(false, t1.contains("c"));
		assertEquals("C", t2.extractMax());
		assertEquals(false, t2.contains("C"));	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLargeTwo() {
		PriorityQueue<Comparable> t = (PriorityQueue<Comparable>) createTrackedCollection();
		PriorityQueueLocator<Comparable>[] locs = (PriorityQueueLocator<Comparable>[]) new PriorityQueueLocator[200];
		ArrayList<Comparable> al = new ArrayList<Comparable>();
		for (int i = 0; i < size; i++)
			for (int j = 0; j < reps; j++)
				al.add(i);
		java.util.Collections.shuffle(al);
		for (int i = 0; i < size*reps; i++) 
			locs[i] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(50);

		for (int i = 0; i < size*reps; i++) {
			locs[i].update(al.get(i));

		}
		for (int i = size-1; i >= 0; i--){
			for (int j = 0; j < reps; j++) {
				assertEquals(i, t.extractMax());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLargeThree() {
		PriorityQueue<Comparable> t = (PriorityQueue<Comparable>) createTrackedCollection();
		PriorityQueueLocator<Comparable>[] locs = (PriorityQueueLocator<Comparable>[]) new PriorityQueueLocator[200];
		ArrayList<Comparable> al = new ArrayList<Comparable>();
		for (int i = 0; i < size; i++)
			for (int j = 0; j < reps; j++)
				al.add(i);
		java.util.Collections.shuffle(al);
		for (int i = 0; i < size*reps; i++) 
			locs[i] = (PriorityQueueLocator<Comparable>) ((Tracked) t).addTracked(-1);
		for (int i = 0; i < size*reps; i++) {
			locs[i].update(al.get(i));
		}
		for (int i = size-1; i >= 0; i--){
			for (int j = 0; j < reps; j++) {
				assertEquals(i, t.extractMax());
			}
		}
	}
	
}
