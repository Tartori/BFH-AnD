// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;

/**
 * The <code>NegativeWeightEdgeException</code> is thrown when a graph algorithm that
 * should only be applied on a weighted graph without any negative weight edges
 * (e.g., Dijkstra's single-source shortest path algorithm) whenever
 * a negative weight edge that is encountered.
**/

public class NegativeWeightEdgeException extends Exception {

	public NegativeWeightEdgeException() {
		super();
	}
	public NegativeWeightEdgeException(String msg) {
		super(msg);
	}

}

