// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import java.util.Iterator;

import goldman.collection.positional.PositionalCollection;
import goldman.collection.tagged.set.OpenAddressingMapping;
/**
 * The ShortestPathMatrix class is used
 * to store the result from an all-pairs
 * shortest path algorithm.
**/

public class ShortestPathMatrix<V, E extends WeightedEdge<V>> 
	extends OpenAddressingMapping<V, InTree<V, E>> {

	V cycleStart = null;

/**
 * @param g the weighted graph on which
 * the shortest path matrix is defined
**/

	ShortestPathMatrix(WeightedGraph<V,E> g) {
		for (V u: g) {  //initialization
			InTree<V, E> uTree = new InTree<V, E>(u);
			put(u, uTree);
			for (V v: g)  //start with
				if (u == v)    //(u,v) entry of 0 for u = v
					uTree.add(v, null, 0);
				else           //and (u,v) entry of infinity u != v
					uTree.add(v, null, AbstractGraph.INF);
			Iterator<E> outEdges = g.edgesFrom(u);
			while (outEdges.hasNext()) {     //consider all edges from u
				E e = outEdges.next();          //next edge e
				V v = e.dest();                 //is to vertex v
				TreeData<V,E> uvData = uTree.get(v);
				//if e is cheaper (u,v) edge, then replace edge stored in uvData by e
				if (uvData.cost == AbstractGraph.INF || 
						e.weight() < uvData.edgeFromParent.weight()) {
					uvData.edgeFromParent = e;
					uvData.cost = e.weight();
				}
			}
		}
	}

/**
 * @return true  if and only
 * if the graph has a negative weight cycle.
**/

	public boolean hasNegativeWeightCycle() {
		return cycleStart != null;
	}

/**
 * @return a positional collection
 * that holds the edges in a negative weight cycle, or null if there is
 * no negative weight cycle
**/

	public PositionalCollection<E> getNegativeWeightCycle() {
		if (hasNegativeWeightCycle())
			return get(cycleStart).buildCycleStartingAt(cycleStart);
		return null;	
	}

/**
 * @param source the desired
 * source vertex
 * @return a shortest path tree for the given source vertex
**/

	public InTree<V,E> getInTreeForSource(V source) {
		return get(source);
	}

/**
 * @param source the source vertex
 * @param dest the destination vertex
 * @return a positional collection
 * that holds the edges, in order, in a shortest path from <code>source</code>
 * to <code>dest</code>. If there is no path from <code>source</code> to <code>dest</code> then null\
 * is returned.
**/

	public PositionalCollection<E> getPath(V source, V dest) {
		return get(source).getPathFromSource(dest);
	}

/**
 * @param source the source vertex
 * @param dest the destination vertex
 * @return the length of the shortest
 * path from <code>source</code> to <code>dest</code>, or <code>INF</code> (infinity) if there is no path
 * from <code>source</code> to <code>dest</code>
**/

	public double getPathDistance(V source, V dest){
		return get(source).get(dest).cost;
	}	


}
