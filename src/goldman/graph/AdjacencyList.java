// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
/**
 * The AdjacencyList class provides an implementation for the adjacency
 * list representation of a graph.  This class
 * just contains two constructors that wrap the AdjacencyListRepresentation class.
**/

public class AdjacencyList<V,E extends Edge<V>> extends AbstractGraph<V,E> {

	public AdjacencyList(boolean directed, boolean containsMultiEdges, 
															boolean storeIncomingEdges) {
		super(new AdjacencyListRepresentation<V,E>(directed, storeIncomingEdges), 
																	containsMultiEdges);
	}


	public AdjacencyList() {
		this(true, true, true);
	}


}
