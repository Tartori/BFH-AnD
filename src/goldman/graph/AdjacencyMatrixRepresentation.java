// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import goldman.NonMutatingIterator;
import goldman.collection.positional.DoublyLinkedList;
import goldman.collection.positional.Queue;
import goldman.collection.tagged.set.Mapping;
import goldman.collection.tagged.set.OpenAddressingMapping;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * The AdjacencyMatrixRepresentation class implements the adjacency
 * matrix representation of a graph.
**/

public class AdjacencyMatrixRepresentation<V,E extends Edge<V>> implements GraphRepresentation<V,E> {

	public static final int DEFAULT_INITIAL_CAPACITY = 8;
	Object[][] edges; // indexed by sourceID,destinationID
	boolean isDirected;  // true if this represents a directed graph


	Mapping<V, Integer> ids = new OpenAddressingMapping<V, Integer>();


	int nextFreeID = 0; 


	static class EdgeList<E> extends DoublyLinkedList<E> {
		EdgeList(E x) {
			add(x);
		}
	}


		Queue<Integer> idPool = new Queue<Integer>();

/**
 * @return the id for a newly added vertex
**/

		int nextID() {
			if (!idPool.isEmpty())
				return idPool.dequeue();
			else
				return nextFreeID++;
		}


	public AdjacencyMatrixRepresentation(boolean isDirected) {
		this.isDirected = isDirected;
		edges = new Object[DEFAULT_INITIAL_CAPACITY]
		                  [DEFAULT_INITIAL_CAPACITY];
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
		return ids.getSize();
	}

/**
 * @return true  if and only if
 * the given vertex is in this graph.
**/

	public boolean containsVertex(V vertex) {
		return ids.contains(vertex);
	}

/**
 * @param source the source vertex
 * @param dest the destination vertex
 * @return true  if and only if there is
 * an edge in the graph from the source vertex to the destination vertex.
**/

	public boolean containsEdge(V source, V dest) {
		try {
			return edges[ids.get(source)][ids.get(dest)] != null; 
		} catch (NoSuchElementException nsee) {
			return false;
		} catch (ArrayIndexOutOfBoundsException nsee) {
			return false;
		}
	}

/**
 * @param source the source vertex
 * @param dest the destination vertex
 * @return an edge in the
 * graph from the source vertex to the destination vertex, or null if there is no such edge.
 * If the graph is undirected, the edge e returned may
 * have <code>e.source</code> = <code>dest</code> and <code>e.dest</code> = <code>source</code>.
**/

	public E getEdge(V source, V dest) {
		try {
			Object entry = edges[ids.get(source)][ids.get(dest)];
			if (entry instanceof EdgeList)
				return ((EdgeList<E>) entry).get(0);
			else
				return (E) entry;
		} catch (NoSuchElementException nsee) {
			return null;
		} catch (ArrayIndexOutOfBoundsException nsee) {
			return null;
		}
	}	

/**
 * @return a non-mutating iterator over the vertices of the graph
**/

	public NonMutatingIterator<V> iterator() {
		return new NonMutatingIterator<V>(ids.tags());
	}

/**
 * @param size the desired number of rows
 * and columns for the matrix
 * <BR> 
 * REQUIRES: 
 *  all vertex ids for vertices
 * in the graph are at most <code>size</code> - 1
**/

	void resize(int size) {
		Object[][] replacement = new Object[size][size];
		for (int i = 0; i < edges.length; i++)
			System.arraycopy(edges[i], 0, replacement[i], 0, edges[i].length);
		edges = replacement;
	}

/**
 * It adds the given vertex unless it is already in the graph
 * @param vertex the vertex to add to this graph
 * @return true  if and only if the vertex is added to the graph
**/

	public boolean addVertex(V vertex) {
		if (!ids.contains(vertex)) {
			ids.put(vertex, nextID());  //creating mapping from vertex to id
			return true;                //the vertex was created and added to the graph
		}
		return false;                   //the vertex was already in the graph
	}

/**
 * @param sourceID the id associated
 * with the source vertex
 * @param destID the id associated with the
 * destination vertex
 * @param edge the edge to add
 * <BR> 
 * REQUIRES: 
 * the ids provided for the source and destination vertices
 * are based upon the source and destination vertices of the provided edge
**/

	protected void addEdge(int sourceID, int destID, E edge) {
		if (Math.max(sourceID,destID) >= edges.length) 
			resize(edges.length*2);
		if (edges[sourceID][destID] == null)   //first edge from source to dest
			edges[sourceID][destID] = edge;
		else {                 //use edge list when multiple edges between sourceID and destID
			if (!(edges[sourceID][destID] instanceof EdgeList))
				edges[sourceID][destID] = new EdgeList<E>((E) edges[sourceID][destID]);
			((EdgeList<E>) edges[sourceID][destID]).add(edge);
		}
	}

/**
 * @param edge the edge to add to
 * this graph
 * <BR> 
 * REQUIRES: 
 *  if the graph
 * is not a multigraph, there must not already be an edge between the two vertices.
**/

	public void addEdge(E edge) {
		addVertex(edge.source());                                 //add source vertex if needed 
		int sourceID = ids.get(edge.source());                    //and get its id
		addVertex(edge.dest());                                   //add destination vertex if needed
		int destID = ids.get(edge.dest());                        //and get its id
		addEdge(sourceID, destID, edge);                          //add the edge
		if (!isDirected && sourceID != destID)       //if an undirected graph and not a self loop
			addEdge(destID, sourceID, edge);		              //add edge in other direction
	}	

/**
 * Removing a vertex implicitly
 * removes all of its incident edges.
 * @param vertex the
 * vertex to be removed
 * @return true  if and only if the vertex
 * existed and was removed.
**/

	public boolean removeVertex(V vertex) {
		if (ids.contains(vertex)) {
			int id = ids.get(vertex);    //get id of the vertex
			for (int i = 0; i < edges.length; i++) {  // remove all incident edges
				edges[i][id] = null;
				edges[id][i] = null;
			}
			ids.remove(vertex);       //removing mapping entry from this vertex to its id
			idPool.enqueue(id);       //reclaim id for later use
			return true;
		}
		return false;
	}

/**
 * @param sourceID the id assigned to the source vertex
 * @param destID the id assigned to the destination vertex
 * <BR> 
 * REQUIRES: 
 *  <code>edge.source</code> has id <code>sourceId</code> and
 * <code>edge.dest</code> has id <code>destID</code>
 * @return true  when the edge is
 * successfully removed, and false, if the edge did not exist.
**/

	boolean removeEdge(int sourceID, int destID, E edge) {
		Object entry = edges[sourceID][destID];
		if (entry == null)   //no such edge to remove
			return false;
		if (entry == edge) {   //single edge from sourceID to destID
			edges[sourceID][destID] = null;
			return true;
		} else if (entry instanceof EdgeList) {      //list of >= 1 edge from sourceID to destID
			((EdgeList) entry).remove(edge);  //remove an edge from the list
			if (((EdgeList) entry).isEmpty()) //preserve MatrixEntry
				edges[sourceID][destID] = null;
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
		if (!ids.contains(edge.source()) || !ids.contains(edge.dest()))
				return false;
		int sourceID = ids.get(edge.source());
		int destID = ids.get(edge.dest());
		if (removeEdge(sourceID, destID, edge)) {  //returns true if edge was in graph
			if (!isDirected && destID != sourceID)
				removeEdge(destID, sourceID, edge);
			return true;
		}
		return false;
	}	

/**
 * @param source the source vertex
 * @return an iterator over the outgoing edges from <code>source</code>
 * @throws IllegalArgumentException <code>source</code> is not in the graph
**/

	public Iterator<E> edgesFrom(V source) {
		if (!ids.contains(source))
			throw new IllegalArgumentException(source + " is not in the graph.");
		return (Iterator<E>) new IncidentEdgeIterator(ids.get(source), true);
	}

/**
 * @param dest the destination vertex
 * @return an iterator over the incoming edges to <code>dest</code>
 * @throws IllegalArgumentException <code>dest</code> is not in the graph
**/

	public Iterator<E> edgesTo(V dest) {
		if (!ids.contains(dest))
			throw new IllegalArgumentException(dest + " is not in the graph.");
		return (Iterator<E>) new IncidentEdgeIterator(ids.get(dest), false);
	}


	class IncidentEdgeIterator implements Iterator<E> {

		int row, col;    //current matrix entry (row,col)
		Iterator subIterator;  //iterator within list at (row,col)
		int rowInc;      //0 for outedges, +1 for inedges
		int colInc;      //0 for inedges, +1 for outedges
		E lastEdgeReturned;  // the most recent return value from next

/**
 * @param vertexID the id for the desired vertex
 * @param outgoing which is true  when the desired action is
 * to iterate over the outgoing edges from the given vertex, and false  when
 * the desired action is to iterator over the incoming edges from the
 * given vertex
**/

		public IncidentEdgeIterator(int vertexID, boolean outgoing) {
			if (outgoing) {
				row = vertexID;          //iterate over row vertexID
				col = -1;                //begin just "left" of first entry
				rowInc = 0; colInc = 1;  //add one to col to move forward
			} else {
				col = vertexID;          //iterate over column vertexID
				row = -1;                //begin just "above" the first entry
				rowInc = 1; colInc = 0;  //add one to the row to move down
			}
		}

/**
 * @return true  if and only if there is another edge
 * in the row, or column, being considered.
**/

		public boolean hasNext() {
			if (subIterator != null) {     //Case 1: when iterating through a list in edges[row][col]
				if (subIterator.hasNext())    //Case 1a: more in current edge list
					return true;
				else                          //Case 1b: if no more entries, prevent examining again
					subIterator = null;
			}
			while (row+rowInc < edges.length 
					&& col+colInc < edges.length) {  //Case 2: find next non-null entry
				if (edges[row+rowInc][col+colInc] == null) {     //when next matrix entry is null
					row += rowInc;  col += colInc;               //move forward 
				} else {                            //Case 2a: found a non-null entry that could be a list or an edge 
					if (edges[row+rowInc][col+colInc] instanceof EdgeList) {
						row += rowInc;  col += colInc;		                   //move to entry with edge list				
						subIterator = ((Iterable) edges[row][col]).iterator(); //init list iterator
					} 
					return true;	 
				}
			} 
			return false;  //Case 2b: all remaining entries in desired row/col are null
		}

/**
 * @return a reference to the next edge in the iteration order
 * @throws NoSuchElementException there is no next edge
**/

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			if (subIterator != null)
				return lastEdgeReturned = (E) subIterator.next();  //move forward in sub-iterator
			else {
				row += rowInc;  col += colInc;	//move to the next matrix entry			
				return lastEdgeReturned = (E) edges[row][col];
			}
		}

/**
 * @throws NoSuchElementException <code>next</code> has not been called since the most recent call to remove
**/

		public void remove() {
			if (lastEdgeReturned == null)
				throw new NoSuchElementException();
			removeEdge(lastEdgeReturned);
			lastEdgeReturned = null;
		}
	}


}
