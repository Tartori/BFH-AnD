// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
import java.util.Comparator;
import java.awt.Point;
/**
 * The following YComparator uses the y coordinates to compare points.
**/

public class YComparator implements Comparator<Point> {

/**
 * @return a negative value if  p1.y &lt; p2.y, zero if p1.y = p2.y, and a positive value if p1.y &gt; p2.y
**/

	public int compare(Point p1, Point p2) {
		return p1.y - p2.y;
	}
}

