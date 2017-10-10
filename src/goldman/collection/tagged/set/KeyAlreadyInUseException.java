// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.set;

/**
 * The <code>KeyAlreadyInUseException</code> is thrown by a set data structure
 * when there is an attempt to insert a tagged element in which the key (tag) is
 * equivalent to the key of a tagged element held in the collection.
**/

public class KeyAlreadyInUseException extends RuntimeException {

	public KeyAlreadyInUseException() {
		super();
	}

}

