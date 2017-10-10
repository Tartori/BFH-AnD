// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
/**
 * The DefaultXYComparator class provides an implementation of
 * the <code>XYComparator</code> interface that simply uses the x and y coordinates.
**/

public class DefaultXYComparator<E extends XYPoint> implements XYComparator<E> {

	public double compareX(E a, E b) {return a.getX() - b.getX();}
	public double compareY(E a, E b) {return a.getY() - b.getY();}

	public double getX(E item) { return item.getX();}
	public double getY(E item) { return item.getY();}

	public int compare(E a, E b) { // uses X comparisons by default
		double d = compareX(a,b);
		if (d < 0)
			return -1;
		else if (d == 0)
			return 0;
		else
			return 1;
	}

/**
 * @param origin the element that defines
 * the origin
 * @param target the target element
 * @return the
 * quadrant (with respect to <code>origin</code>) that contains the target.
**/

	public int quadrant(E origin, E target) {
		if (getX(origin) == getX(target) && getY(origin) == getY(target))
			return 4;
		boolean toLeft = compareX(target, origin) <= 0;
		boolean below = compareY(target, origin) <= 0;
		if (toLeft)
			return below ? 0 : 3;
		else
			return below ? 1 : 2;
	}
}

