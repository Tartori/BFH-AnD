// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
/**
 * The <code>Digitizer</code> interface
 * provides a mechanism for any
 * algorithm or data structure
 * to treat each element in a collection as a sequence of digits where each digit is mapped
 * to an integer from 0 to b-1 where b is the base of the digit.
**/

public interface Digitizer<T> {
/**
 * Returns the base.  Observe that a base
 * b digit takes on values from 0 to b-1.
**/

	public int getBase();
/**
 * Returns true  if and only if the digitizer guarantees that no element
 * is a prefix of another.
**/

	public boolean isPrefixFree();
/**
 * Returns the number of digits in the element x.
**/

	public int numDigits(T x);
/**
 * Returns the value of digit <code>place</code>
 * for element x.
**/

	public int getDigit(T x, int place);
	public String formatDigit(T x, int place);
}
