// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;

/**
 * An <code>AtCapacityException</code> is thrown when there is an attempt
 * to insert a new element into a non-elastic collection that is already at its maximum
 * capacity.
**/

public class AtCapacityException extends RuntimeException {

	public AtCapacityException() {
		super();
	}

	public AtCapacityException(int capacity) {
		super("Capacity is " + capacity);
	}

}

