// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import goldman.NonMutatingIterator;
import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.positional.DoublyLinkedList;
import goldman.collection.set.OpenAddressing;
import goldman.collection.set.Set;
import goldman.collection.tagged.bucket.TaggedBucketCollection;
import goldman.collection.tagged.bucket.TaggedBucketCollectionWrapper;
import goldman.collection.tagged.set.OpenAddressingMapping;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * The AdjacencyListRepresentation class implements the adjacency
 * list representation of a graph.
**/

public class AdjacencyListRepresentation<V,E extends Edge<V>> implements GraphRepresentation<V,E> {

	Set<V> vertices = new OpenAddressing<V>();  //set of vertices in graph
	TaggedBucketCollection<V,E> outEdges;  //maps each vertex to a bucket of outgoing edges
	TaggedBucketCollection<V,E> inEdges;   //maps each vertex to a bucket of incoming edges
	boolean isDirected = true;             //by default, the graph is directed
	

/**
 * Observe that no parameter is needed to indicate whether the graph allows multi-edges,
 * since the underlying representation of an adjacency list is not affected by this choice.
 * The differences in the implementation for a graph and a multigraph are handled by the GraphRepresentation
 * class
 * @param isDirected which is true when the graph is a directed graph
 * @param storeIncomingEdges which is true  when the incoming edges should be maintained
**/

	public AdjacencyListRepresentation(boolean isDirected, boolean storeIncomingEdges) {
		this.isDirected = isDirected;
		outEdges = new TaggedBucketCollectionWrapper<V, E>
				(new OpenAddressingMapping<V, Collection<E>>(), DoublyLinkedList.class);
		if (isDirected && storeIncomingEdges)
			inEdges = new TaggedBucketCollectionWrapper<V, E>
				(new OpenAddressingMapping<V, Collection<E>>(), DoublyLinkedList.class);
	}

/**
 * @return true  if and only if this graph representation is
 * being used to support a directed graph.
**/

	public boolean isDirected() {
		return isDirected;
	}

/**
 * @return the number of vertices in
 * this graph.
**/

	public int numVertices() {
		return vertices.getSize();
	}

/**
 * @return true  if and only if
 * the given vertex is in this graph.
**/

	public boolean containsVertex(V vertex) {
		return vertices.contains(vertex);
	}

/**
 * @param source the source vertex
 * @param dest the destination vertex
 * @return true  if and only if there is
 * an edge in the graph from the source vertex to the destination vertex.
**/

	public boolean containsEdge(V source, V dest) {
		if (outEdges.contains(source)) {
			Iterator<E> it = edgesFrom(source);
			while (it.hasNext()) {
				E nextEdge = it.next();
				if ((nextEdge.dest() == dest && nextEdge.source() == source) ||
					(!isDirected && nextEdge.dest() == source && nextEdge.source() == dest))
					return true;
			}
		}
		return false;
	}

/**
 * @param source the source vertex
 * @param dest the destination vertex
 * @return an edge in the graph from the
 * source vertex to the destination vertex, or null if no such edge exists.
**/

	public E getEdge(V source, V dest) {
		if (outEdges.contains(source)) {
			Iterator<E> it = edgesFrom(source);
			while (it.hasNext()) {
				E nextEdge = it.next();
				if ((nextEdge.dest() == dest && nextEdge.source() == source) ||
					(!isDirected && nextEdge.dest() == source && nextEdge.source() == dest))
					return nextEdge;
			}
		}
		return null;
	}

/**
 * @return an iterator over the vertices of the graph
**/

	public NonMutatingIterator<V> iterator() {
		return new NonMutatingIterator<V>(vertices.iterator());
	}

/**
 * It adds the given vertex unless it is already in the graph
 * @param vertex the vertex to add to this graph
 * @return true  if and only if the vertex is added to the graph
**/

	public boolean addVertex(V vertex) {
		if (!vertices.contains(vertex)) {
			vertices.add(vertex);       //add vertex to set of vertices
			return true;
		}
		return false;                   //the vertex was already in the graph
	}

/**
 * @param vertex the vertex to be treated as the source vertex
 * @param edge the edge to add
**/

	void addEdge(V vertex, E edge) {
		outEdges.put(vertex, edge);
		if (inEdges != null && edge.source() != edge.dest())
			inEdges.put(edge.dest(), edge);
	}

/**
 * @param edge the edge to
 * add to this graph
**/

	public void addEdge(E edge) {
		addEdge(edge.source(), edge);
		if (!isDirected)
			addEdge(edge.dest(), edge);
	}

/**
 * @param vertex the vertex to remove
 * @return true  if the vertex was in the graph, and false  otherwise.
**/

	public boolean removeVertex(V vertex) {
		if (vertices.contains(vertex)) {
			vertices.remove(vertex);
			if (outEdges.contains(vertex))
				outEdges.removeBucket(vertex);
			if (inEdges != null && inEdges.contains(vertex))
				inEdges.removeBucket(vertex);
			return true;
		}
		return false;
	}

/**
 * @param vertex the vertex that is to be treated as the
 * source vertex of <code>edge</code>
 * @param edge the edge to remove
 * @return true   when the edge is
 * successfully removed, and false, if the edge did not exist.
**/

	boolean removeEdge(V vertex, E edge) {
		if (outEdges.remove(vertex,edge)) {            
			if (inEdges != null)
				inEdges.remove(edge.dest(), edge);
			return true;
			}
		return false;
	}

/**
 * @param edge the edge to be removed from
 * this graph
 * @return true   when the edge is
 * successfully removed, and false, if the edge did not exist.
**/

	public boolean removeEdge(E edge) {
		if (removeEdge(edge.source(), edge)) {  //returns true if edge was in list
			if (!isDirected && edge.source() != edge.dest())
				return removeEdge(edge.dest(), edge);
			return containsVertex(edge.dest()); //check that dest is in the graph
		}
		return false;
	}	

/**
 * @param source the source vertex
 * @return an iterator over the outgoing edges from the given vertex
**/

	public Iterator<E> edgesFrom(V source) {
		return new EdgeIterator(outEdges.getElements(source));
	}

/**
 * @param dest the destination vertex
 * @return an iterator over the incoming edges from the given vertex
 * @throws UnsupportedOperationException the graph is directed and the constructor
 * parameter specified not to maintain the inedges
**/

	public Iterator<E> edgesTo(V dest) {
		if (!isDirected)
			return edgesFrom(dest);
		if (inEdges == null)
			throw new UnsupportedOperationException("incoming edges are not stored");
		return new EdgeIterator(inEdges.getElements(dest));
	}


	class EdgeIterator implements Iterator<E> {

		Locator<E> wrapped;  //the wrapped locator
		E lastEdgeReturned;   //the edge most recently returned by this iterator

/**
 * @param edges a locator over a set of edges
**/

		EdgeIterator(Locator<E> edges) {
			this.wrapped = edges;
		}

/**
 * Moves to the next edge, and deletes
 * any edges that were implicitly removed when the destination vertex (or the source
 * vertex in an undirected graph)
 * was removed.
**/

		public boolean hasNext() {
			while (wrapped.hasNext()) {  //more edges in the list
				wrapped.advance();          //move to the next edge
				E edge = wrapped.get();
				if (!vertices.contains(edge.dest())            //if destination vertex no longer exists
						|| !vertices.contains(edge.source()))  //or source vertex no longer exists
					wrapped.remove();                        //remove the edge
				else {
					wrapped.retreat();                    //move back so next advance behaves properly
					return true;
				}
			}
			return false;
		}

/**
 * Moves the locator to the next edge in the list, which is
 * in the graph
 * @throws NoSuchElementException there is no next edge in the iteration
**/

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return lastEdgeReturned = wrapped.next();
		}

/**
 * @throws NoSuchElementException <code>next</code>
 * has not been called since the most recent call to <code>remove</code>
**/

		public void remove() {
			if (lastEdgeReturned == null)
				throw new NoSuchElementException();
			removeEdge(lastEdgeReturned);
			lastEdgeReturned = null;
		}
	}


}
