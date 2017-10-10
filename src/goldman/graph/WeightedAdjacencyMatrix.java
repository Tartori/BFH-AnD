// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
/**
 * The WeightedAdjacencyMatrix class provides an implementation for
 * a weighted adjacency matrix.
**/

public class WeightedAdjacencyMatrix<V,E extends WeightedEdge<V>> extends AbstractWeightedGraph<V,E> {

	public WeightedAdjacencyMatrix(boolean directed, boolean multigraph) {
		super(new AdjacencyMatrixRepresentation<V,E>(directed), multigraph);
	}
	
	public WeightedAdjacencyMatrix() {
		this(true, false);
	}


}
