// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;

/**
 * A <code>PositionOutOfBoundsException</code> is thrown when there is an
 * attempt to access a position that does not exists (i.e., &lt; 0 or
 * &ge; <code>size</code>).
**/

public class PositionOutOfBoundsException extends RuntimeException {

	public PositionOutOfBoundsException() {
		super();
	}

	public PositionOutOfBoundsException(int position) {
		super("Position = " + position);
	}
}


