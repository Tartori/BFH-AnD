// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
/**
 * The <code>WeightedEdge</code> interface is used for a weighted edge.
**/

public interface WeightedEdge<V> extends Edge<V> {
	double weight();   //returns the weight of this edge
}

