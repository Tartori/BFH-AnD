// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import java.util.Iterator;
import goldman.NonMutatingIterator;
/**
 * The <code>GraphRepresentation</code> interface
 * defines the methods that must be
 * supported by any graph representation,
 * such as the adjacency list and adjacency matrix representations.  This design
 * allows different types of graphs
 * (e.g., weighted and unweighted)
 * to independently choose their internal representation from among the classes that
 * implement <code>GraphRepresentation</code>.
**/

public interface GraphRepresentation<V,E extends Edge<V>> extends Iterable<V> {
/**
 * Adds the given edge to this graph.
**/

	public void addEdge(E edge);
/**
 * Adds the given vertex to this
 * graph unless it is already in the graph.  It returns
 * true  if and only if the vertex is added.
**/

	public boolean addVertex(V vertex);
/**
 * Returns true 
 * if and only if there is
 * an edge in the graph from the source vertex to the destination vertex.
**/

	public boolean containsEdge(V source, V dest);
/**
 * Returns true 
 * if and only if the give vertex is in the graph.
**/

	public boolean containsVertex(V vertex);
/**
 * Returns
 * an iterator over the outgoing edges from <code>source</code>.  Observe that
 * for an undirected graph, the iterator is over all edges incident to <code>source</code>.
**/

	public Iterator<E> edgesFrom(V source);
/**
 * Returns
 * an iterator over the incoming edges from <code>dest</code>.
 * This method throws an <code>UnsupportedOperationException</code> when the graph representations
 * does not support this capability.
**/

	public Iterator<E> edgesTo(V dest);
/**
 * Returns an edge in the
 * graph from <code>source</code> to <code>dest</code>,
 * or null if there is no such edge.
**/

	public E getEdge(V source, V dest);
/**
 * Returns true  if and only if this graph representation is
 * being used to support a directed graph.
**/

	public boolean isDirected();
/**
 * Returns
 * a non-mutating iterator over the vertices of the graph.
 * A non-mutating iterator is returned to ensure that the application
 * program can only remove a vertex from a graph using the <code>removeVertex</code>
 * method.
**/

	public NonMutatingIterator<V> iterator();
/**
 * Returns the number of vertices in
 * this graph.
**/

	public int numVertices();
/**
 * Returns
 * true   when the given edge is
 * successfully removed, and false, if the edge did not exist.
**/

	public boolean removeEdge(E edge);
/**
 * Returns
 * true  if and only if the given vertex
 * existed and was removed.
**/

	public boolean removeVertex(V vertex);
}
