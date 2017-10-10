// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;

/**
 * The <code>DisconnectedGraphException</code> is thrown when a graph algorithm that
 * should only be applied on a connected graph (e.g., a spanning tree algorithm) is
 * applied to a graph that is composed of more than one connected component.
**/

public class DisconnectedGraphException extends Exception {

	public DisconnectedGraphException() {
		super();
	}
	public DisconnectedGraphException(String msg) {
		super(msg);
	}

}

