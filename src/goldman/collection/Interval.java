// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
/**
 * The <code>Interval</code> interface provides a way for an application program to provide
 * a minimum and maximum element when it is known.
**/

public interface Interval<T> {

/**
 * Returns a minimum element in the range.
**/

	public T getMin(); //returns min element in the range of a collection

/**
 * Returns a maximum element in the range.
**/

	public T getMax(); //returns max element in the range of a collection
}

