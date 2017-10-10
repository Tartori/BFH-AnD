// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;

/**
 * An <code>AtBoundaryException</code> is thrown when <code>advance</code>
 * is called from a locator at <code>AFT</code> or <code>retreat</code> is called from
 * a locator at <code>FORE</code>.
**/

public class AtBoundaryException extends RuntimeException {

	public AtBoundaryException() {
		super();
	}

	public AtBoundaryException(String s) {
		super(s);
	}

}

