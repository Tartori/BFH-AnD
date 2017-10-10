// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import goldman.collection.set.Set;
/**
 * The weighted graph algorithms we present do not depend on a
 * particular graph implementation.  Instead,
 * they are implemented entirely in terms of
 * the WeightedGraph ADT.  One advantage of this approach
 * is that it allows the presentation and implementation of the weighted graph algorithms
 * to be expressed independent of the underlying weighted graph representation.
 * The <code>WeightedGraph</code> interface that provides the methods
 * required to create and modify a weighted graph, and methods to perform commonly needed
 * computation on the graph.
**/

public interface WeightedGraph<V,E extends WeightedEdge<V>> extends Graph<V,E> {
/**
 * Uses the
 * Floyd-Warshall all-pairs shortest path algorithm to compute and return a shortest
 * path matrix.
**/

	public ShortestPathMatrix<V,E> allPairsShortestPaths();
/**
 * Uses
 * the Bellman-Ford shortest path algorithm to compute and return a shortest path tree
 * for the given source vertex.  This algorithm will yield the correct answer if there are
 * negative weight edges, provided that there is no negative weight cycle.
**/

	public InTree<V,E> generalShortestPathFromSource(V source);
/**
 * Uses Kruskal's minimum spanning tree to
 * return a set of edges that form a minimum spanning tree for this graph.
 * It throws a <code>DisconnectedGraphException</code> when the graph is not connected,
 * a <code>NegativeWeightEdgeException</code> when a negative weight edge
 * is encountered, and a <code>GraphException</code> when the graph is directed.
**/

	public Set<E> kruskalMST() throws NegativeWeightEdgeException, DisconnectedGraphException;
/**
 * Uses the
 * Edmonds-Karp implementation of the basic Ford-Fulkerson augmenting path
 * method to find a maximum flow (and a minimum cut) for the flow network
 * defined by the provided source and sink.
**/

	public AbstractWeightedGraph<V,E>.FlowGraph maximumFlow(V source, V sink);
/**
 * Uses Prim's minimum spanning tree to
 * return a set of edges that forms a minimum spanning tree for this graph.
 * It throws a <code>DisconnectedGraphException</code> when the graph is not connected,
 * a <code>NegativeWeightEdgeException</code> when a negative weight edge
 * is encountered, and an <code>IllegalArgumentException</code> when the graph is directed.
**/

	public Set<E> primMST() throws NegativeWeightEdgeException, DisconnectedGraphException;
/**
 * Uses
 * Dijkstra's shortest path algorithm to compute and return a shortest path tree
 * for the given source vertex.  It throws a <code>NegativeWeightEdgeException</code> when the
 * graph has a negative weight edge reachable from the source.
**/

	public InTree<V,E> weightedShortestPaths(V source)
		throws NegativeWeightEdgeException, DisconnectedGraphException;
}
