// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
/**
 * The <code>Edge</code> interface is used for an unweighted edge.
**/

public interface Edge<V> {
/**
 * Returns the destination (head) of this edge.
**/

	public V dest();
/**
 * Returns the source (tail) of this edge.
**/

	public V source();
}
