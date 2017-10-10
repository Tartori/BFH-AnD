package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.spatial.*;
import java.util.Iterator;

import goldman.collection.AbstractCollection.VisitingIterator;

public class QuadTreeTest extends SpatialCollectionTest {

	public SpatialCollection<MyPoint> createCollection() {
		QuadTree<MyPoint> t = new QuadTree<MyPoint>();
		t.checkRep();
		return t;
	}
	
	@Test
	public void testCanceledIterator() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(0,1));
		t.add(new MyPoint(1,2));
		t.add(new MyPoint(1,2));
		Iterator<MyPoint> it = t.iterator();
		assertEquals(true, it.hasNext());
		assertEquals(new MyPoint(0,1), it.next());
		assertEquals(true, it.hasNext());
		assertEquals(new MyPoint(1,2), it.next());
		((VisitingIterator) it).cancel();
		assertEquals(false, it.hasNext());
	}
	
	@Test
	public void testExample() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(100,150));
		t.add(new MyPoint(190,180));
		t.add(new MyPoint(50,60));
		t.add(new MyPoint(200,70));
		t.add(new MyPoint(160,40));
		t.add(new MyPoint(30,150));
		visualize(t);
	}
	
	@Test
	public void deleteExample() {
		SpatialCollection<MyPoint> t = createCollection();
		t.add(new MyPoint(100,150));
		t.add(new MyPoint(190,180));
		t.add(new MyPoint(50,60));
		t.add(new MyPoint(200,70));
		t.add(new MyPoint(160,40));
		t.add(new MyPoint(30,160));
		visualize(t);
		t.remove(new MyPoint(100,150));
		visualize(t);
	}

	boolean visualize = false;
	void visualize(SpatialCollection<MyPoint> t) {
		if (visualize) {
			System.out.println(t);
			//new QuadTreeVisualizer((QuadTree<MyPoint>) t);
		}
	}
	
	public static void main(String[] args) {
		QuadTreeTest tester = new QuadTreeTest();
		tester.visualize = true;
		tester.deleteExample();
	}

}
