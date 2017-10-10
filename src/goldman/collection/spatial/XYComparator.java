// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
import java.util.Comparator;
/**
 * The <code>XYComparator</code> interface is used by the quad tree implementation to determine
 * which quadrant a point belongs.
**/

public interface XYComparator<T> extends Comparator<T> {

/**
 * Returns the x-coordinate of the given item.
**/

	public double getX(T item);

/**
 * Returns the y-coordinate of the given item.
**/

	public double getY(T item);

/**
 * Compares a and b according to their x-coordinates.
**/

	public double compareX(T a, T b);

/**
 * Compares a and b according to their y-coordinates.
**/

	public double compareY(T a, T b);

/**
 * Returns the quadrant for the given item with respect to the given origin where
 * 0 indicates lower left, 1 indicates lower right, 2 indicates upper right,
 * 3 indicates upper left, and 4 indicates the item is at the origin.
**/

	public int quadrant(T origin, T item);  //0, 1, 2, 3, or 4 (match)
}                                          

