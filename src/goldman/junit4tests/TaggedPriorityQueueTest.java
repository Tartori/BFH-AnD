package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.collection.priority.PriorityQueueLocator;
import goldman.collection.tagged.*;
import goldman.collection.tagged.priority.TaggedPriorityQueue;

import java.util.ArrayList;


public abstract class TaggedPriorityQueueTest extends TaggedCollectionTest {

	
	public TaggedCollection<Integer, String> createCollection(int capacity) {
		return createCollection();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSmall() {
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		t.put(4,"d");
		t.put(2,"b");
		t.put(1,"a");
		t.put(3,"c");
		t.put(3,"c");
		t.put(7,"g");
		assertEquals(true,t.contains(4));
		assertEquals(true,t.contains(2));
		assertEquals(true,t.contains(1));
		assertEquals(true,t.contains(3));
		assertEquals(true,t.contains(7));
		assertEquals(false,t.contains(8));
		assertEquals("g",t.max().getElement());	
		assertEquals("g",t.extractMax().getElement());
		assertEquals("d",t.max().getElement());	
		assertEquals("d",t.extractMax().getElement());
		assertEquals("c",t.max().getElement());
		assertEquals("c",t.extractMax().getElement());
		assertEquals("c",t.max().getElement());
		assertEquals(true,t.contains(3));
		assertEquals(false,t.contains(4));
		assertEquals(true,t.contains(2));
		assertEquals(true,t.contains(1));
		assertEquals(false,t.contains(7));
		assertEquals(false,t.contains(8));
		assertEquals("c",t.extractMax().getElement());
		assertEquals("b",t.max().getElement());
		assertEquals(false,t.contains(3));
		assertEquals(false,t.contains(4));
		assertEquals(true,t.contains(2));
		assertEquals(true,t.contains(1));
		assertEquals(false,t.contains(7));
		assertEquals(false,t.contains(8));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMore() {
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		PriorityQueueLocator<TaggedElement<Integer,String>>[] locs = (PriorityQueueLocator<TaggedElement<Integer,String>>[]) new PriorityQueueLocator[10];
	 	locs[0] = t.putTracked(75, "sf"); //add
		assertEquals(new Integer(75), t.max().getTag());
		locs[1] = t.putTracked(55, "ff"); //add
		locs[2] = t.putTracked(65, "sxf"); //add
		locs[3] = t.putTracked(85, "ef"); //add
		assertEquals(new Integer(85), t.max().getTag());
		locs[4] = t.putTracked(95, "95"); //add
		assertEquals(new Integer(95), t.max().getTag());
		locs[5] = t.putTracked(90, "nt"); //add
		locs[6] = t.putTracked(80, "et"); //add
		locs[7] = t.putTracked(70, "st"); //add
		locs[8] = t.putTracked(50, "ft"); //add
		locs[9] = t.putTracked(60, "sxt"); //add
		for (int i = 0; i < 5; i++)
			t.updateTag(locs[i].get().getTag()-50, locs[i]);
		assertEquals(new Integer(90), t.max().getTag());
		t.updateTag(locs[5].get().getTag()-50, locs[5]);//update
		assertEquals(new Integer(80), t.max().getTag());
		t.updateTag(locs[6].get().getTag()-50, locs[6]);//update
		assertEquals(new Integer(70), t.max().getTag());
		t.updateTag(locs[7].get().getTag()-50, locs[7]);//update
		assertEquals(new Integer(60), t.max().getTag());
		t.updateTag(locs[8].get().getTag()-50, locs[8]);//update
		assertEquals(new Integer(60), t.max().getTag());
		t.updateTag(locs[9].get().getTag()-50, locs[9]);//update
		assertEquals(new Integer(45), t.max().getTag());
		assertEquals(new Integer(45), t.extractMax().getTag());
		assertEquals(new Integer(40), t.extractMax().getTag());
		assertEquals(new Integer(35), t.extractMax().getTag());
		assertEquals(new Integer(30), t.extractMax().getTag());
		assertEquals(new Integer(25), t.extractMax().getTag());
		assertEquals(new Integer(20), t.extractMax().getTag());
		assertEquals(new Integer(15), t.extractMax().getTag());
		assertEquals(new Integer(10), t.extractMax().getTag());
		assertEquals(new Integer(5), t.extractMax().getTag());
		assertEquals(new Integer(0), t.extractMax().getTag());		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMedium() {
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		PriorityQueueLocator<TaggedElement<Integer,String>>[] locs = (PriorityQueueLocator<TaggedElement<Integer,String>>[]) new PriorityQueueLocator[10];
		locs[0] = t.putTracked(60, "sxt"); //add
		locs[1] = t.putTracked(50, "fft"); //add
		locs[2] = t.putTracked(60, "st"); //add
		locs[3] = t.putTracked(50, "ft"); //add
		locs[4] = t.putTracked(55, "ftfv"); //add
		locs[5] = t.putTracked(55, "ftf"); //add
		for (int i = 0; i <= 5; i++)
			t.updateTag(locs[i].get().getTag()-50, locs[i]);//update
		assertEquals(locs[0].get().getTag(), 10);
		assertEquals(locs[1].get().getTag(), 0);
		assertEquals(locs[2].get().getTag(), 10);
		assertEquals(locs[3].get().getTag(), 0);
		assertEquals(locs[4].get().getTag(), 5);
		assertEquals(locs[5].get().getTag(), 5);	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTwoSmall() {
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		PriorityQueueLocator<TaggedElement<Integer,String>>[] locs = (PriorityQueueLocator<TaggedElement<Integer,String>>[]) new PriorityQueueLocator[10];
		for (int i = 0; i < 10; i++)
			locs[i] = t.putTracked(50, "x"+i);
		t.updateTag(0, locs[0]);
		t.updateTag(0, locs[1]);
		t.updateTag(0, locs[2]);
		t.updateTag(0, locs[3]);
		t.updateTag(1, locs[4]);
		t.updateTag(1, locs[5]);
		t.updateTag(1, locs[6]);
		t.updateTag(1, locs[7]);
		t.updateTag(1, locs[8]);
		t.updateTag(0, locs[9]);

		assertEquals(1,t.max().getTag());
		assertEquals(1,t.extractMax().getTag());
		assertEquals(1,t.extractMax().getTag());
		assertEquals(1,t.extractMax().getTag());
		assertEquals(1,t.extractMax().getTag());
		assertEquals(1,t.extractMax().getTag());
		assertEquals(0,t.extractMax().getTag());
		assertEquals(0,t.extractMax().getTag());
		assertEquals(0,t.extractMax().getTag());
		assertEquals(0,t.extractMax().getTag());
		assertEquals(0,t.extractMax().getTag());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTracking(){
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		PriorityQueueLocator<TaggedElement<Integer,String>>[] locs = (PriorityQueueLocator<TaggedElement<Integer,String>>[]) new PriorityQueueLocator[100];
		for (int i = 0; i < 100; i++){
			locs[i] = t.putTracked(i, "item"+i);
		}
		
		for (int i = 0; i < 100; i++)
			assertEquals(i ,locs[i].get().getTag());
	
		t.updateTag(200, locs[0]);
		t.updateTag(-10, locs[99]);
		assertEquals(new Integer(200), locs[0].get().getTag());
		for (int i = 1; i < 99; i++)
			assertEquals(new Integer(i),locs[i].get().getTag());
		assertEquals(new Integer(-10), locs[99].get().getTag());
		assertEquals(new Integer(200), t.max().getTag());
		assertEquals(new Integer(200), t.extractMax().getTag());
		assertEquals(new Integer(98), t.max().getTag());
		for (int i = 98; i >= 1; i--){
			Integer ans = new Integer(i);
			assertEquals(ans,t.max().getTag());
			assertEquals(true,t.contains(ans));
			assertEquals(ans,t.extractMax().getTag());
			assertEquals(false,t.contains(ans));
			assertEquals(i,t.getSize());
		}
		assertEquals(false,t.isEmpty());
		assertEquals(new Integer(-10), t.max().getTag());
		assertEquals(new Integer(-10), t.extractMax().getTag());
		assertEquals(true,t.isEmpty());
	}
	
	static final int size = 3;
	static final int reps = 1;
	
	@Test
	public void testRepeatedly() {
		for (int i = 1; i< 50; i++){
			doTestLarge();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void doTestLarge() {
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		PriorityQueueLocator<TaggedElement<Integer,String>>[] locs = (PriorityQueueLocator<TaggedElement<Integer,String>>[]) new PriorityQueueLocator[200];
		ArrayList<Integer> al = new ArrayList<Integer>();
		for (int i = 0; i < size; i++)
			for (int j = 0; j < reps; j++)
				al.add(5*i+50);
		java.util.Collections.shuffle(al);
		for (int i = 0; i < size*reps; i++) {
			locs[i] = t.putTracked(al.get(i), "item"+al.get(i));
		}
		
		for (int i =0; i < size*reps; i++) {
			t.updateTag(locs[i].get().getTag() - 50 , locs[i]);
		}
		for (int i = size-1; i >= 0; i--){
			for (int j = 0; j < reps; j++) {
				assertEquals(5*i, t.extractMax().getTag());
			}
		}
	}
	
	@Test
	public void testTwoPQs() {
		TaggedPriorityQueue<Integer,String> t1= (TaggedPriorityQueue<Integer,String>) createCollection();
		TaggedPriorityQueue<Integer,String> t2 = (TaggedPriorityQueue<Integer,String>) createCollection();
		t1.put(1,"a");
		t2.put(10,"A");
		t2.put(30,"C");
		t1.put(3,"c");
		t1.put(2,"b");
		t2.put(20,"B");
		assertEquals(true, t1.contains(1));
		assertEquals(false, t2.contains(1));
		assertEquals(true, t1.contains(2));
		assertEquals(false, t2.contains(2));
		assertEquals(true, t1.contains(3));
		assertEquals(false, t2.contains(3));
		assertEquals(true, t2.contains(10));
		assertEquals(false, t1.contains(10));
		assertEquals(true, t2.contains(20));
		assertEquals(false, t1.contains(20));
		assertEquals(true, t2.contains(30));
		assertEquals(false, t1.contains(30));
		assertEquals("c", t1.extractMax().getElement());
		assertEquals(false, t1.contains(3));
		assertEquals("C", t2.extractMax().getElement());
		assertEquals(false, t2.contains(30));	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLargeTwo() {
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		PriorityQueueLocator<TaggedElement<Integer,String>>[] locs = (PriorityQueueLocator<TaggedElement<Integer,String>>[]) new PriorityQueueLocator[200];
		ArrayList<Integer> al = new ArrayList<Integer>();
		for (int i = 0; i < size; i++)
			for (int j = 0; j < reps; j++)
				al.add(i);
		java.util.Collections.shuffle(al);
		for (int i = 0; i < size*reps; i++) 
			locs[i] = t.putTracked(50, "ft");

		for (int i = 0; i < size*reps; i++) {
			t.updateTag(al.get(i), locs[i]);

		}
		for (int i = size-1; i >= 0; i--){
			for (int j = 0; j < reps; j++) {
				assertEquals(i, t.extractMax().getTag());
			}
		}
		assertEquals(0, t.getSize());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLargeThree() {
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		PriorityQueueLocator<TaggedElement<Integer,String>>[] locs = (PriorityQueueLocator<TaggedElement<Integer,String>>[]) new PriorityQueueLocator[200];
		ArrayList<Integer> al = new ArrayList<Integer>();
		for (int i = 0; i < size; i++)
			for (int j = 0; j < reps; j++)
				al.add(i);
		java.util.Collections.shuffle(al);
		for (int i = 0; i < size*reps; i++) 
			locs[i] = t.putTracked(-1,"neg1");
		for (int i = 0; i < size*reps; i++) {
			t.updateTag(al.get(i), locs[i]);
		}
		for (int i = size-1; i >= 0; i--){
			for (int j = 0; j < reps; j++) {
				assertEquals(i, t.extractMax().getTag());
			}
		}
	}
	
	@Test
	public void repeat() {
		for (int i = 0; i < 100; i++) {
			testLargeThree();
		}
	}
	
	@Test
	public void testSmallExample() {
		TaggedPriorityQueue<Integer,String> t = (TaggedPriorityQueue<Integer,String>) createCollection();
		PriorityQueueLocator<TaggedElement<Integer,String>>[] locs = (PriorityQueueLocator<TaggedElement<Integer,String>>[]) new PriorityQueueLocator[3];
		for (int i = 0; i < 3; i++) 
			locs[i] = t.putTracked(-1,"neg1");
		t.updateTag(2, locs[0]);
		t.updateTag(1, locs[1]);
		t.updateTag(0, locs[2]);
		for (int i = 2; i >= 0; i--)  {
			int extracted = t.extractMax().getTag();
			assertEquals(i, extracted);
		}
	}
}

