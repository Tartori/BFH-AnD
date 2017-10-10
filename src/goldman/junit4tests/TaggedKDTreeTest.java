package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;

import goldman.collection.Collection;
import goldman.collection.spatial.XComparator;
import goldman.collection.spatial.YComparator;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.spatial.TaggedKDTree;
import goldman.collection.tagged.spatial.TaggedSpatialCollection;

public class TaggedKDTreeTest extends TaggedSpatialCollectionTest {
	
	public TaggedSpatialCollection<MyPoint,Integer> createCollection() {
		TaggedKDTree<MyPoint,Integer> t =
			new TaggedKDTree<MyPoint,Integer>(new XComparator(), new YComparator());
		return t;
	}
	
	@Override
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
		assertEquals("<5,7 --> 1, 1,6 --> 3, 1,6 --> 8, 7,8 --> 7, 7,7 --> 16, 8,8 --> 11>", coll.toString());
	}
}
