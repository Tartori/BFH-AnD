// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
/**
 * The AdjacencyMatrix class provides an implementation for the adjacency
 * matrix representation of a graph.  This class
 * just contains two constructors that wrap the AdjacencyMatrixRepresentation class.
**/

public class AdjacencyMatrix<V,E extends Edge<V>> extends AbstractGraph<V,E> {

	public AdjacencyMatrix(boolean directed, boolean allowsMultiEdges) {
		super(new AdjacencyMatrixRepresentation<V,E>(directed), allowsMultiEdges);
	}


	public AdjacencyMatrix() {
		this(true, true);
	}


}
