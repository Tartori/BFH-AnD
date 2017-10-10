package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.spatial.*;
import java.util.NoSuchElementException;

public class KDTreeTest extends SpatialCollectionTest {

	@SuppressWarnings("unchecked")
	KDTree<MyPoint> createCollection() {
		return new KDTree<MyPoint>(new XComparator(), new YComparator());
	}

	@Test
	public void testMinimum() {
		KDTree<MyPoint> t = createCollection();
		try {
			assertEquals(0,t.min(0));
			fail();
		} catch (NoSuchElementException nsee) {
			// expected
		}
		t.add(new MyPoint(1,2));
		try {
			assertEquals(0,t.min(2));
			fail();
		} catch (IllegalArgumentException nsee) {
			// expected
		}
		assertEquals(new MyPoint(1,2), t.min(0));
		assertEquals(new MyPoint(1,2), t.min(1));
		t.add(new MyPoint(0,1));
		assertEquals(new MyPoint(0,1), t.min(0));
		assertEquals(new MyPoint(0,1), t.min(1));
		t.add(new MyPoint(2,-1));
		assertEquals(new MyPoint(0,1), t.min(0));
		assertEquals(new MyPoint(2,-1), t.min(1));
		t.add(new MyPoint(-1,4));
		assertEquals(new MyPoint(-1,4), t.min(0));
		assertEquals(new MyPoint(2,-1), t.min(1));
	}
	
	@Test
	public void testMaximum() {
		KDTree<MyPoint> t = createCollection();
		try {
			assertEquals(0,t.max(0));
			fail();
		} catch (NoSuchElementException nsee) {
			// expected
		}
		t.add(new MyPoint(1,2));
		try {
			assertEquals(0,t.max(2));
			fail();
		} catch (IllegalArgumentException nsee) {
			// expected
		}
		assertEquals(new MyPoint(1,2), t.max(0));
		assertEquals(new MyPoint(1,2), t.max(1));
		t.add(new MyPoint(0,1));
		assertEquals(new MyPoint(1,2), t.max(0));
		assertEquals(new MyPoint(1,2), t.max(1));
		t.add(new MyPoint(2,-1));
		assertEquals(new MyPoint(2,-1), t.max(0));
		assertEquals(new MyPoint(1,2), t.max(1));
		t.add(new MyPoint(-1,4));
		assertEquals(new MyPoint(2,-1), t.max(0));
		assertEquals(new MyPoint(-1,4), t.max(1));
	}
	
	boolean visualize = false;
	void visualize(SpatialCollection<MyPoint> t) {
		if (visualize) {
			System.out.println(t);
			//new KDTreeVisualizer((KDTree<MyPoint>) t);
		}
	}
	
	public static void main(String[] args) {
		KDTreeTest tester = new KDTreeTest();
		tester.visualize = true;
		tester.createFigures();
	}
}
