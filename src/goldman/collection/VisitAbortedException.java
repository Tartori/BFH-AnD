// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;

/**
 * A <code>VisitAbortedException</code> wraps an exception that is thrown by
 * the <code>visit</code> method during an <code>accept</code> call.
**/

public class VisitAbortedException extends RuntimeException {

	public VisitAbortedException(Throwable cause) {
		super(cause);
	}

}

