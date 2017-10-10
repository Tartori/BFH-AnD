// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
import goldman.collection.Collection;
/**
 * A spatial collection organizes its elements by location
 * in a multidimensional space.
**/

public interface SpatialCollection<E> extends Collection<E> {
/**
 * Returns a greatest element
 * in the collection along the given dimension.
 * This method throws a
 * <code>NoSuchElementException</code> when the collection is empty.  It
 * throws an
 * <code>IllegalArgumentException</code> when the given dimension index is not
 * valid for this spatial collection.
**/

	public E max(int dimension);
/**
 * Returns a least element
 * in the collection along the given dimension.
 * This method throws a
 * <code>NoSuchElementException</code> when the collection is empty.  It throws an
 * <code>IllegalArgumentException</code> when the given dimension index is not
 * valid for this spatial collection.
**/

	public E min(int dimension);
/**
 * Returns a collection of
 * the elements that fall within (or on) the boundary of the multidimensional box defined by
 * the two given corners, <code>minCorner</code> and <code>maxCorner</code>.  That is, this method performs an
 * orthogonal range search.  It requires that the
 * coordinates of <code>minCorner</code>
 * are less than or equal to those of <code>maxCorner</code> along every dimension of the spatial collection.
**/

	public Collection<E> withinBounds(E minCorner, E maxCorner);
}
