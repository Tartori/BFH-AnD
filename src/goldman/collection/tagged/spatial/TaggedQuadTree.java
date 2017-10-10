// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.
/**
 * @param comp the XY comparator that
 * defines the two orderings among the tags
**/

package goldman.collection.tagged.spatial;
import goldman.collection.spatial.DefaultXYComparator;
import goldman.collection.spatial.QuadTree;
import goldman.collection.spatial.XYComparator;
import goldman.collection.spatial.XYPoint;
import goldman.collection.tagged.TaggedElement;

/**
 * A tagged version of a quad tree.
**/

public class TaggedQuadTree<T extends XYPoint,E> extends TaggedSpatialCollectionWrapper<T,E> {
	
	public TaggedQuadTree(XYComparator<? super T> comp) {
		super(new QuadTree<TaggedElement<T,E>>
						(new TaggedElementXYComparator<T>(comp)));
	}


	public TaggedQuadTree() {
		this(new DefaultXYComparator());
	}
}

