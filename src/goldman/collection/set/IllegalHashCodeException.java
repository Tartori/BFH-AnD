// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.set;
/**
 * The <code>IllegalHashCodeException</code> is thrown when an attempt is made
 * to use a hash code that is not between 0 and m-1 where m is the size of
 * the hash table.
**/

public class IllegalHashCodeException extends RuntimeException {

	public IllegalHashCodeException() {
		super();
	}

	public IllegalHashCodeException(int hashCode) {
		super("illegal hash code: " + hashCode);
	}


}
