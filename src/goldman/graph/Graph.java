// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import java.util.Iterator;

import goldman.NonMutatingIterator;
import goldman.collection.positional.PositionalCollection;
import goldman.collection.set.Set;
/**
 * A graph represents general relationships between pairs of elements from
 * among a set of elements.  The elements are called vertices, and
 * the relationships between them are called edges.
 * The graph algorithms we present do not depend on a particular graph implementation.  Instead,
 * they are implemented entirely in terms of
 * the Graph ADT.  One advantage of this approach
 * is that it allows the presentation and implementation of the graph algorithms
 * to be expressed independent of the underlying graph representation.
 * The <code>Graph</code> interface that provides the methods
 * required to create and modify a graph, and methods to perform commonly needed
 * computation on the graph, such as computing a shortest path tree for a specified
 * source, determining if a graph is cyclic, or finding a
 * valid topological order for a precedence graph.
**/

public interface Graph<V,E extends Edge<V>> extends Iterable<V> {
/**
 * Adds the given edge to this
 * graph. If the graph does not allow multi-edges, this
 * method requires that there is not already an edge from the source
 * to the destination.
**/

	public void addEdge(E edge);
/**
 * Adds the given
 * vertex to this graph.  It returns true  if the vertex is
 * successfully added, and false  if the vertex already existed
 * in the graph.
**/

	public boolean addVertex(V vertex);
/**
 * Returns
 * true  if and only if this graph allows multi-edges.
**/

	public boolean allowsMultiEdges();
/**
 * Returns
 * true  if and only if there is an edge from <code>source</code>
 * to <code>dest</code> in this graph.
**/

	public boolean containsEdge(V source, V dest);
/**
 * Returns
 * true  if and only if the given vertex is in this graph.
**/

	public boolean containsVertex(V vertex);
/**
 * Returns
 * an iterator over the outgoing edges from the given vertex.
**/

	public Iterator<E> edgesFrom(V vertex);
/**
 * Returns
 * an iterator over the incoming edges for the given vertex.
 * This is an optional operation for directed graphs.
**/

	public Iterator<E> edgesTo(V vertex);
/**
 * Returns an
 * edge in the graph from <code>source</code>
 * to <code>dest</code>, or null if there is no such edge.
**/

	public E getEdge(V source, V dest); 
/**
 * Returns
 * true  if and only if this graph is a directed graph.
**/

	public boolean isDirected();
/**
 * Returns a
 * non-mutating iterator over the vertices in this graph.
**/

	NonMutatingIterator<V> iterator();
/**
 * Returns the number
 * of vertices in this graph.
**/

	public int numVertices();
/**
 * Removes the given
 * edge from this graph.  It returns true  when the edge is
 * successfully removed,
 * and false  if the edge did not exist in the graph.
**/

	public boolean removeEdge(E edge);
/**
 * Removes the given
 * vertex from this graph.   It returns true  when the vertex is
 * successfully removed, and false  if the vertex did not exist in the graph.
 * Removing a vertex implicitly removes all of its incident edges.
 * Because edges may be deleted lazily in some implementations on the basis of
 * whether or not the incident vertices are still in the graph, this method
 * requires that the removed vertex (or any equivalent one)
 * is never added back into the graph
**/

	public boolean removeVertex(V vertex);
/**
 * Returns
 * a set holding the set of vertices in each connected component of this graph.
 * This method is applicable only for undirected graphs.  It throws
 * a <code>GraphException</code> when
 * the graph is directed since the connected components are defined only for
 * an undirected graph.
**/

	public Set<Set<V>> getConnectedComponents();
/**
 * Returns
 * a positional collection containing the edges in some cycle in this
 * graph, in the order they appear in the cycle.  It throws a
 * <code>GraphException</code> if the
 * graph is acyclic.  The <code>hasCycle</code> method can be used to determine
 * if the graph has a cycle.
**/

	public PositionalCollection<E> getCycle();
/**
 * Returns
 * a set holding the set of vertices in each strongly connected component of this graph.
 * This method is applicable only for directed graphs. It throws a <code>GraphException</code> when
 * the graph is undirected since the strongly connected components are defined only for
 * a directed graph.
**/

	public Set<Set<V>> getStronglyConnectedComponents();
/**
 * Returns true  if and only
 * this graph has a cycle.
**/

	public boolean hasCycle();
/**
 * Returns the number of
 * connected components in this graph.  It throws a <code>GraphException</code> when
 * the graph is directed since the connected components are defined only for
 * an undirected graph.
**/

	public int numConnectedComponents();
/**
 * Returns the number of strongly
 * connected components in this graph.  It throws a <code>GraphException</code> when
 * the graph is undirected since the strongly connected components are defined only for
 * a directed graph.
**/

	public int numStronglyConnectedComponents();
/**
 * Returns
 * a positional collection that holds a permutation of the vertices of a directed graph
 * in a valid topological order.  It throws a <code>GraphException</code> if the graph is
 * undirected, or if there is a directed cycle.
**/

	public PositionalCollection<V> topologicalOrder();
/**
 * Uses
 * breadth-first search to compute and return a shortest path tree for the given source vertex.
**/

	public InTree<V,E> unweightedShortestPaths(V source);
}
