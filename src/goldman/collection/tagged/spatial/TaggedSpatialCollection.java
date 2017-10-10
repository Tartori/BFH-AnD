// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.spatial;
import goldman.collection.Collection;
import goldman.collection.tagged.TaggedCollection;
import goldman.collection.tagged.TaggedElement;
/**
 * In this section we present the TaggedSpatialCollection ADT, which
 * is a tagged variation of the SpatialCollection ADT.
**/

public interface TaggedSpatialCollection<T,E> extends TaggedCollection<T,E> {
/**
 * Returns a tagged element for the least tag
 * in the collection (according to the comparator) along the given dimension. It throws a
 * <code>NoSuchElementException</code> when the collection is empty.
**/

	public TaggedElement<T,E> min(int dimension);
/**
 * Returns a tagged element for the greatest tag
 * in the collection (according to the comparator) in the given dimension.  It throws a
 * <code>NoSuchElementException</code> when the collection is empty.
**/

	public TaggedElement<T,E> max(int dimension);
/**
 * Returns a 
 * collection of the tagged elements for which the tag
 * fall within (or on) the boundary of the multidimensional box defined by
 * the two given corners, <code>minCorner</code> and <code>maxCorner</code>.
 * The method requires that the coordinates of <code>minCorner</code>
 * are less than or equal to those of <code>maxCorner</code> along every dimension of the spatial collection.
**/

	public Collection<TaggedElement<T,E>> withinBounds(T minCorner, T maxCorner);
}
