package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.spatial.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public abstract class SpatialCollectionTest  {

	abstract SpatialCollection<MyPoint> createCollection();
	
	// Subclasses may override this method to create visualizations during the tests.
	void visualize(SpatialCollection<MyPoint> t) { }

	@Test
	public void testSize() {
		SpatialCollection<MyPoint> t = createCollection();
		assertEquals(0, t.getSize());
		t.checkRep();
		t.add(new MyPoint(0,1));
		t.checkRep();
		assertEquals(1, t.getSize());
		t.add(new MyPoint(1,2));
		t.checkRep();
		assertEquals(2, t.getSize());
	}

	@Test
	public void testContains() {
		SpatialCollection<MyPoint> t = createCollection();
		assertEquals(false, t.contains(new MyPoint(2,2)));
		assertEquals(false, t.contains(null));
		t.add(new MyPoint(0,1));
		assertEquals(true, t.contains(new MyPoint(0,1)));
		assertEquals(false, t.contains(new MyPoint(0,0)));
		t.add(new MyPoint(1,2));
		assertEquals(true, t.contains(new MyPoint(1,2)));	
		assertEquals(false, t.contains(new MyPoint(1,1)));
		t.add(new MyPoint(1,2));
		assertEquals(true, t.contains(new MyPoint(1,2)));	
		assertEquals(false, t.contains(new MyPoint(1,1)));
	}

	@Test
	public void testIterator() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(0,1));
		t.add(new MyPoint(1,2));
		t.add(new MyPoint(1,2));
		Iterator<MyPoint> it = t.iterator();
		assertEquals(true, it.hasNext());
		assertEquals(new MyPoint(0,1), it.next());
		assertEquals(true, it.hasNext());
		assertEquals(new MyPoint(1,2), it.next());
		assertEquals(true, it.hasNext());
		assertEquals(new MyPoint(1,2), it.next());
		assertEquals(false, it.hasNext());
	}

	@Test
	public void testConcurrentMod() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(0,1));
		t.add(new MyPoint(1,2));
		t.add(new MyPoint(1,2));
		Iterator<MyPoint> it = t.iterator();
		assertEquals(true, it.hasNext());
		assertEquals(new MyPoint(0,1), it.next());
		assertEquals(true, it.hasNext());
		assertEquals(new MyPoint(1,2), it.next());
		t.remove(new MyPoint(0,4));
		assertEquals(true, it.hasNext());
		t.remove(new MyPoint(0,1));
		try {
			assertEquals(new MyPoint(1,2), it.next());
			fail();
		} catch (ConcurrentModificationException cme) {
			// expected
		}
	}
	
	@Test
	public void testRemove1() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(0,1));
		t.add(new MyPoint(1,2));
		t.add(new MyPoint(2,-1));
		t.add(new MyPoint(-1,4));
		assertEquals(true, t.contains(new MyPoint(1,2)));
		t.remove(new MyPoint(2,-1));
		t.remove(new MyPoint(-1,4));
		assertEquals(false, t.contains(new MyPoint(-1,4)));
	}

	@Test
	public void testRemove2() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(7,1));
		t.add(new MyPoint(9,6));
		t.add(new MyPoint(9,3));
		t.remove(new MyPoint(7,1));
		t.checkRep();
	}
	
	@Test
	public void testRemove3() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(70,90));
		t.add(new MyPoint(20,10));
		t.add(new MyPoint(20,70));
		t.add(new MyPoint(10,50));
		visualize(t);
		t.checkRep();
		t.remove(new MyPoint(20,10));
		visualize(t);
		t.checkRep();	
	}
	
	@Test
	public void testRemove4() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(70,80));
		t.add(new MyPoint(60,30));
		t.add(new MyPoint(40,20));
		visualize(t);
		t.remove(new MyPoint(70,80));
		visualize(t);
		assertEquals(true, t.contains(new MyPoint(40,20)));
	}

	@Test
	public void testRemove5() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(60,60));
		t.add(new MyPoint(60,30));
		t.add(new MyPoint(40,0));
		visualize(t);
		t.remove(new MyPoint(60,60));
		visualize(t);
		assertEquals(true, t.contains(new MyPoint(40,0)));
	}

	@Test
	public void testRemove6() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(8,2));
		t.add(new MyPoint(9,3));
		t.add(new MyPoint(9,3));
		t.remove(new MyPoint(8,2));
		t.remove(new MyPoint(9,3));
		assertEquals(true, t.contains(new MyPoint(9,3)));

	}

	@Test
	public void testRemove7() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(10,80));
		t.add(new MyPoint(40,10));
		t.add(new MyPoint(20,40));
		t.add(new MyPoint(60,20));
		t.add(new MyPoint(70,50));
		visualize(t);
		t.remove(new MyPoint(10,80));
		visualize(t);
		t.checkRep();
		assertEquals(true, t.contains(new MyPoint(70,50)));
	}

	@Test
	public void testRemove8() {	
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(3,3));
		t.add(new MyPoint(1,0));
		t.add(new MyPoint(0,3));
		t.add(new MyPoint(8,3));
		t.add(new MyPoint(1,4));
		visualize(t);
		t.remove(new MyPoint(3,3));
		visualize(t);
		t.remove(new MyPoint(0,3));
		visualize(t);
		t.remove(new MyPoint(1,4));
	}

	@Test
	public void testRemove9() {	
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(40,80));
		t.add(new MyPoint(90,70));
		t.add(new MyPoint(70,80));
		t.add(new MyPoint(80,0));
		t.add(new MyPoint(70,10));
		visualize(t);
		t.remove(new MyPoint(40,80));
		visualize(t);
		assertEquals(true, t.contains(new MyPoint(70,10)));
	}

	@Test
	public void testRemove10() {	
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(0,7));
		t.add(new MyPoint(9,4));
		t.add(new MyPoint(5,2));
		t.add(new MyPoint(2,5));
		t.add(new MyPoint(1,3));
		t.remove(new MyPoint(0,7));
		assertEquals(true, t.contains(new MyPoint(1,3)));
	}

	@Test
	public void testRemove11() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(80,60));
		t.add(new MyPoint(10,80));
		t.add(new MyPoint(60,80));
		t.add(new MyPoint(30,70));
		assertEquals(true, t.contains(new MyPoint(30,70)));
		visualize(t);
		t.remove(new MyPoint(80,60));
		visualize(t);
		assertEquals(true, t.contains(new MyPoint(30,70)));
	}

	@Test
	public void testRemoveTracked() {	
		SpatialCollection<MyPoint> t = createCollection();
		if (!(t instanceof Tracked))  // NO TRACKERS YET!!!
			return;
		Locator<MyPoint> t1, t2, t3;
		t1 = ((Tracked<MyPoint>) t).addTracked(new MyPoint(3,3));
		t.add(new MyPoint(1,0));
		t2 = ((Tracked<MyPoint>) t).addTracked(new MyPoint(0,3));
		t.add(new MyPoint(8,3));
		t3 = ((Tracked<MyPoint>) t).addTracked(new MyPoint(1,4));
		visualize(t);
		t1.remove();
		visualize(t);
		t2.remove();
		visualize(t);
		t3.remove();
		assertEquals(false, t.contains(t1.get()));
		assertEquals(true, t.contains(new MyPoint(1,0)));
		assertEquals(false, t.contains(t2.get()));
		assertEquals(false, t.contains(t3.get()));
		assertEquals(true, t.contains(new MyPoint(8,3)));
	}

	@Test
	public void testLarge() {
		for (int j = 0; j < 10; j++) {
			int size = 100;
			ArrayList<MyPoint> points = new ArrayList<MyPoint>();
			for (int i = 0; i < size; i++)
				points.add(new MyPoint((int) (Math.random()*20), (int) (Math.random()*20)));
			Collections.shuffle(points);
			SpatialCollection<MyPoint> t = createCollection();
			for (MyPoint p : points) {
				t.add(p);
				t.checkRep();
			}
			visualize(t);
			Collections.shuffle(points);
			Iterator<MyPoint> it = points.iterator();
			while (it.hasNext()) {
				MyPoint p = it.next();
				t.remove(p);
				t.checkRep();
				it.remove();
				for (MyPoint q : points) {
					assertEquals(true, t.contains(q));
				}
			}
		}
	}

	@Test
	public void testLargeUnique() {
		int size = 30;
		ArrayList<MyPoint> points = new ArrayList<MyPoint>();
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				points.add(new MyPoint(i*50,j*50));
		//Collections.shuffle(points);
		SpatialCollection<MyPoint> t = createCollection();
		for (MyPoint p : points) {
			t.add(p);
			t.checkRep();
		}
		visualize(t);
		//Collections.shuffle(points);
		Iterator<MyPoint> it = points.iterator();
		while (it.hasNext()) {
			MyPoint p = it.next();
			t.remove(p);
			visualize(t);
			t.checkRep();
			it.remove();
			for (MyPoint q : points) {
				assertEquals(true, t.contains(q));
			}
			assertEquals(false, t.contains(p));
		}
		visualize(t);
	}

	@Test
	public void testMinMax() {
		SpatialCollection<MyPoint> t = createCollection();
		MyPoint p57, p42, p16, p83, p29, p94, p78, q16, p40, p03, p88, p49, p61, p82, p77; 
		t.add(p57 = new MyPoint(5,7));
		t.add(p42 = new MyPoint(4,2));
		t.add(p16 = new MyPoint(1,6));
		t.add(p83 = new MyPoint(8,3));
		t.add(p29 = new MyPoint(2,9));
		t.add(p94 = new MyPoint(9,4));
		t.add(p78 = new MyPoint(7,8));
		t.add(q16 = new MyPoint(1,6));
		t.add(p40 = new MyPoint(4,0));
		t.add(p03 = new MyPoint(0,3));
		t.add(p88 = new MyPoint(8,8));
		t.add(p49 = new MyPoint(4,9));
		t.add(p61 = new MyPoint(6,1));
		t.add(p49 = new MyPoint(4,9));
		t.add(p82 = new MyPoint(8,2));
		t.add(p77 = new MyPoint(7,7));
		assertEquals(p03, t.min(0));
		assertEquals(p40, t.min(1));
		assertEquals(p94, t.max(0));
		assertEquals(9, t.max(1).getY(), 0.1);
		t.checkRep();
		visualize(t);
		t.remove(new MyPoint(1,6));
		t.checkRep();
		visualize(t);
		t.remove(new MyPoint(5,7));
		t.checkRep();
		visualize(t);	
		t.remove(new MyPoint(1,6));
		t.checkRep();
		visualize(t);
		t.remove(new MyPoint(9,4));
		t.checkRep();
		visualize(t);
		t.remove(new MyPoint(8,2));
		t.checkRep();
		visualize(t);
		t.remove(new MyPoint(6,1));
		t.checkRep();
		visualize(t);
		t.remove(new MyPoint(4,2));
		t.checkRep();
		visualize(t);
	}

	

	@Test
	public void testBox() {
		SpatialCollection<MyPoint> t = createCollection();
		MyPoint p57, p42, p16, p83, p29, p94, p78, q16, p40, p03, p88, p49, p61, p82, p77; 
		t.add(p57 = new MyPoint(5,7));
		t.add(p42 = new MyPoint(4,2));
		t.add(p16 = new MyPoint(1,6));
		t.add(p83 = new MyPoint(8,3));
		t.add(p29 = new MyPoint(2,9));
		t.add(p94 = new MyPoint(9,4));
		t.add(p78 = new MyPoint(7,8));
		t.add(q16 = new MyPoint(1,6));
		t.add(p40 = new MyPoint(4,0));
		t.add(p03 = new MyPoint(0,3));
		t.add(p88 = new MyPoint(8,8));
		t.add(p49 = new MyPoint(4,9));
		t.add(p61 = new MyPoint(6,1));
		t.add(p49 = new MyPoint(4,9));
		t.add(p82 = new MyPoint(8,2));
		t.add(p77 = new MyPoint(7,7));
		Collection<MyPoint> coll = t.withinBounds(p16,p88);
		assertEquals(true, containsAll(coll, p57, p16, p78, q16, p88, p77));
		assertEquals(true, containsNone(coll, p42, p83, p29, p94, p40, p03, p49, p61,  p82));
	}

	boolean containsAll(Collection<MyPoint> coll, MyPoint...points) {
		for (MyPoint p : points)
			if (!coll.contains(p))
				return false;
		return true;
	}

	boolean containsNone(Collection<MyPoint> coll, MyPoint...points) {
		for (MyPoint p : points)
			if (coll.contains(p))
				return false;
		return true;
	}
	
	@Test
	public void createFigures() {
		SpatialCollection<MyPoint> t = createCollection();
		MyPoint p57, p42, p16, p83, p29, p94, p78, q16, p40, p03, p88, p49, p61, p82, p67; 
		t.add(p57 = new MyPoint(5,7));
		t.add(p42 = new MyPoint(4,2));
		t.add(p16 = new MyPoint(1,6));
		t.add(p83 = new MyPoint(8,3));
		t.add(p29 = new MyPoint(2,9));
		t.add(p94 = new MyPoint(9,4));
		t.add(p78 = new MyPoint(7,8));

		t.add(p40 = new MyPoint(4,0));
		t.add(p03 = new MyPoint(0,3));
		t.add(p88 = new MyPoint(8,8));
		t.add(p49 = new MyPoint(4,9));
		t.add(p61 = new MyPoint(6,1));
		t.add(p49 = new MyPoint(4,9));
		t.add(p82 = new MyPoint(8,2));
		t.add(p67 = new MyPoint(6,7));
		t.checkRep();
		visualize(t);
		t.add(q16 = new MyPoint(1,6));
		t.checkRep();
		visualize(t);	
		t.remove(new MyPoint(9,4));
		t.checkRep();
		visualize(t);
		t.remove(new MyPoint(5,7));
		t.checkRep();
		visualize(t);	
	}

	@Test
	public void createLargeFigure() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(250,250));
		t.add(new MyPoint(90,180));
		t.add(new MyPoint(300,40));
		t.add(new MyPoint(300,400));
		t.add(new MyPoint(350,450));
		t.add(new MyPoint(400,200));
		t.add(new MyPoint(50,450));		
		visualize(t);
		t.remove(new MyPoint(250,250));
		visualize(t);
	}
}

class MyPoint extends java.awt.Point implements goldman.collection.spatial.XYPoint {
	public MyPoint(int x, int y) {
		super(x,y);
	}
	public String toString() {
		return (int) x + "," + (int) y;
	}
}
