// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
/**
 * The WeightedAdjacencyList class provides an implementation for
 * a weighted adjacency list.
**/

public class WeightedAdjacencyList<V,E extends WeightedEdge<V>> extends AbstractWeightedGraph<V,E> {

	public WeightedAdjacencyList(boolean directed, boolean multigraph,
														boolean storeIncomingEdges) {
		super(new AdjacencyListRepresentation<V,E>(directed, storeIncomingEdges),
																		multigraph);
	}
	
	public WeightedAdjacencyList() {
		this(true, false, true);
	}


}
