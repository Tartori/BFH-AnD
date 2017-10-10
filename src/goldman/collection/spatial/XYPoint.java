// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
/**
 * This interface is used in conjunction with the DefaultXYComparator to define
 * the default comparator used to
 * partition the space into four quadrants.
**/

public interface XYPoint {

/**
 * Returns the x-coordinate of the point.
**/

	public double getX();

/**
 * Returns the y-coordinate of the point.
**/

	public double getY();
}

