package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.spatial.TaggedSpatialCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public abstract class TaggedSpatialCollectionTest {
	
	class MyPoint extends java.awt.Point implements goldman.collection.spatial.XYPoint {
		public MyPoint(int x, int y) {
			super(x,y);
		}
		public String toString() {
			return (int) x + "," + (int) y;
		}
	}
	
	public abstract TaggedSpatialCollection<MyPoint,Integer> createCollection();

	@Test
	public void testSize() {
		TaggedSpatialCollection<MyPoint,Integer> t = createCollection();
		assertEquals(0, t.getSize());
		t.put(new MyPoint(0,1), 1);
		assertEquals(1, t.getSize());
		t.put(new MyPoint(1,2), 2);
		assertEquals(2, t.getSize());
	}

	@Test
	public void testContains() {
		TaggedSpatialCollection<MyPoint, Integer> t = createCollection();
		assertEquals(false, t.contains(new MyPoint(2,2)));
		assertEquals(false, t.contains(null));
		t.put(new MyPoint(0,1), 1);
		assertEquals(true, t.contains(new MyPoint(0,1)));
		assertEquals(false, t.contains(new MyPoint(0,0)));
		t.put(new MyPoint(1,2), 2);
		assertEquals(true, t.contains(new MyPoint(1,2)));	
		assertEquals(false, t.contains(new MyPoint(1,1)));
		Integer ans = t.get(new MyPoint(1,2));
//		assertEquals(2,ans);
		t.put(new MyPoint(1,2),3);
		assertEquals(true, t.contains(new MyPoint(1,2)));	
		assertEquals(false, t.contains(new MyPoint(1,1)));
	}

	@Test
	public void testIterator() {
		TaggedSpatialCollection<MyPoint, Integer> t = createCollection();
		t.put(new MyPoint(0,1),1);
		t.put(new MyPoint(1,2),2);
		t.put(new MyPoint(1,2),3);
		Iterator<TaggedElement<MyPoint,Integer>> it = t.iterator();
		assertEquals(true, it.hasNext());
		assertEquals("0,1 --> 1", it.next().toString());
		assertEquals(true, it.hasNext());
		assertEquals("1,2 --> 2", it.next().toString());
		assertEquals(true, it.hasNext());
		assertEquals("1,2 --> 3", it.next().toString());
		assertEquals(false, it.hasNext());
	}

	@Test
	public void testRemove7() {
		TaggedSpatialCollection<MyPoint,Integer> t = createCollection();
		t.put(new MyPoint(10,80),1);
		t.put(new MyPoint(40,10),2);
		t.put(new MyPoint(20,40),3);
		t.put(new MyPoint(60,20),4);
		t.put(new MyPoint(70,50),5);
		assertEquals(true, t.contains(new MyPoint(70,50)));
	}

	@Test
	public void testLarge() {
		for (int j = 0; j < 10; j++) {
			int size = 100;
			ArrayList<MyPoint> points = new ArrayList<MyPoint>();
			for (int i = 0; i < size; i++)
				points.add(new MyPoint((int) (Math.random()*20), (int) (Math.random()*20)));
			Collections.shuffle(points);
			TaggedSpatialCollection<MyPoint,Integer> t = createCollection();
			int i=1;
			for (MyPoint p : points) {
				t.put(p, i++);
			}
			Collections.shuffle(points);
			Iterator<MyPoint> it = points.iterator();
			while (it.hasNext()) {
				it.next();
				it.remove();
				for (MyPoint q : points) {
					assertEquals(true, t.contains(q));
				}
			}
		}
	}
	
	@Test
	public void testMinMax() {
		TaggedSpatialCollection<MyPoint,Integer> t = createCollection();
		MyPoint p57, p42, p16, p83, p29, p94, p78, q16, p40, p03, p88, p49, p61, p82, p77; 
		t.put(p57 = new MyPoint(5,7),1);
		t.put(p42 = new MyPoint(4,2),2);
		t.put(p16 = new MyPoint(1,6),3);
		t.put(p83 = new MyPoint(8,3),4);
		t.put(p29 = new MyPoint(2,9),5);
		t.put(p94 = new MyPoint(9,4),6);
		t.put(p78 = new MyPoint(7,8),7);
		t.put(q16 = new MyPoint(1,6),8);
		t.put(p40 = new MyPoint(4,0),9);
		t.put(p03 = new MyPoint(0,3),10);
		t.put(p88 = new MyPoint(8,8),11);
		t.put(p49 = new MyPoint(4,9),12);
		t.put(p61 = new MyPoint(6,1),13);
		t.put(p49 = new MyPoint(4,9),14);
		t.put(p82 = new MyPoint(8,2),15);
		t.put(p77 = new MyPoint(7,7),16);
		assertEquals("0,3 --> 10", t.min(0).toString());
		assertEquals("4,0 --> 9", t.min(1).toString());
		assertEquals("9,4 --> 6", t.max(0).toString());
	}
	
	@Test
	public void testBox() {
		TaggedSpatialCollection<MyPoint,Integer> t = createCollection();
		MyPoint p57, p42, p16, p83, p29, p94, p78, q16, p40, p03, p88, p49, p61, p82, p77; 
		t.put(p57 = new MyPoint(5,7),1);
		t.put(p42 = new MyPoint(4,2),2);
		t.put(p16 = new MyPoint(1,6),3);
		t.put(p83 = new MyPoint(8,3),4);
		t.put(p29 = new MyPoint(2,9),5);
		t.put(p94 = new MyPoint(9,4),6);
		t.put(p78 = new MyPoint(7,8),7);
		t.put(q16 = new MyPoint(1,6),8);
		t.put(p40 = new MyPoint(4,0),9);
		t.put(p03 = new MyPoint(0,3),10);
		t.put(p88 = new MyPoint(8,8),11);
		t.put(p49 = new MyPoint(4,9),12);
		t.put(p61 = new MyPoint(6,1),13);
		t.put(p49 = new MyPoint(4,9),14);
		t.put(p82 = new MyPoint(8,2),15);
		t.put(p77 = new MyPoint(7,7),16);
		Collection<TaggedElement<MyPoint,Integer>> coll = t.withinBounds(p16,p88);
		assertEquals("<5,7 --> 1, 1,6 --> 3, 1,6 --> 8, 7,7 --> 16, 7,8 --> 7, 8,8 --> 11>", coll.toString());
	}
}
