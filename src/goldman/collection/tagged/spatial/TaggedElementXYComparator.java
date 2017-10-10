// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.spatial;
import goldman.collection.spatial.XYComparator;
import goldman.collection.spatial.XYPoint;
import goldman.collection.tagged.TaggedElement;
/**
 * The TaggedElementXYComparator used by the quad tree
 * is defined over tags that are two-dimensional points.
**/

public class TaggedElementXYComparator<T extends XYPoint>
								implements XYComparator<TaggedElement<T,?>> {
	
	XYComparator<? super T> comp;

	public TaggedElementXYComparator(XYComparator<? super T> comp) {
		this.comp = comp;
	}
	
	public double compareX(TaggedElement<T,?> a, TaggedElement<T,?> b) {
		return comp.compareX(a.getTag(), b.getTag());
	}

	public double compareY(TaggedElement<T,?> a, TaggedElement<T,?> b) {
		return comp.compareY(a.getTag(), b.getTag());
	}

	public double getX(TaggedElement<T,?> item) {
		return comp.getX(item.getTag());
	}

	public double getY(TaggedElement<T,?> item) {
		return comp.getY(item.getTag());
	}

	//uses x-coordinate by default
	public int compare(TaggedElement<T,?> a, TaggedElement<T,?> b) {
		return comp.compare(a.getTag(), b.getTag());
	}

	public int quadrant(TaggedElement<T,?> origin, TaggedElement<T,?> item) {
		return comp.quadrant(origin.getTag(), item.getTag());
	}
}

