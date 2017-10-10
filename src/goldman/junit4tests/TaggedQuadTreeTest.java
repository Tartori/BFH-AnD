package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;

import goldman.collection.tagged.spatial.TaggedQuadTree;
import goldman.collection.tagged.spatial.TaggedSpatialCollection;

public class TaggedQuadTreeTest extends TaggedSpatialCollectionTest {
	
	public TaggedSpatialCollection<MyPoint,Integer> createCollection() {
		TaggedQuadTree<MyPoint,Integer> t = new TaggedQuadTree<MyPoint,Integer>();
		return t;
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createCollection().getSize());
	}
	
}
