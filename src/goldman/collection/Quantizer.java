// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
/**
 * The <code>Quantizer</code> interface allows an application program to provide
 * a mechanism to convert an element <code>x</code> in the collection to a
 * double.  (While an integer could have been used instead of a double, using a
 * double provides the maximum flexibility.)
**/

public interface Quantizer<T> {

/**
 * Converts x to a double.
**/

	public double getDouble(T x);  //converts x to a double
}

