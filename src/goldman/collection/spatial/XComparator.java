// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
import java.util.Comparator;
import java.awt.Point;
/**
 * Each dimension in a spatial collection requires its own comparator.
 * For example, the following XComparator uses x coordinates to compare points.
**/

public class XComparator implements Comparator<Point> {

/**
 * @return a negative value if  p1.x &lt; p2.x, zero if p1.x = p2.x, and a positive value if p1.x &gt; p2.x
**/

	public int compare(Point p1, Point p2) {
		return p1.x - p2.x;
	}
}

