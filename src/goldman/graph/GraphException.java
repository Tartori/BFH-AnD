// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;

/**
 * The <code>GraphException</code> is thrown when a graph algorithm is called on
 * the incorrect type of graph.  As just one example, if an algorithm that is only defined for
 * a directed graph is called an undirected graph then a <code>GraphException</code> is thrown.
**/

public class GraphException extends RuntimeException {

	public GraphException(String msg) {
		super(msg);
	}
}

