// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import goldman.collection.positional.CircularArray;
import goldman.collection.positional.PositionalCollection;
import goldman.collection.tagged.set.OpenAddressingMapping;

	class TreeData<V,E> {
		E edgeFromParent;
		double cost; 

		TreeData(E e, double cost) {
			edgeFromParent = e; //edge from parent in in-tree from source
			this.cost = cost;   //distance of in-tree path from source
		}


		public String toString() {
			return "( " + edgeFromParent + ", " + cost + ")";
		}

	}

/**
 * The InTree class provides an implementation of an in-tree that stores
 * a parent edge associated with each vertex of the graph (possibly null).
**/

	public class InTree<V, E extends Edge<V>> 
		extends OpenAddressingMapping<V, TreeData<V,E>> {

		V source;
		V cycleStart = null;

/**
 * @param source the source vertex that is the root of the
 * in-tree
**/

		InTree(V source) {
			this.source = source;
		}

/**
 * @param v the vertex to add to the tree
 * @param edgeFromParent the
 * edge to place in the path that defines the parent of v
 * @param cost the cost associated with v with respect to the specified parent
 * @return the tree data instance that is associated
 * with v
**/

		TreeData<V,E> add(V v, E edgeFromParent, double cost) {
			TreeData<V,E> data = new TreeData<V,E>(edgeFromParent, cost);
			put(v, data);  //create an association between v and data
			return data; 
		}

/**
 * @return the source vertex for this in-tree
**/

		public V getSource() {
			return source;
		}

/**
 * @param dest the destination vertex
 * @return true  if and only if there
 * is a path in this tree from the source to <code>dest</code> in this graph.
**/

	public boolean isReachableFromSource(V dest) {
		if (source == dest)  //any vertex is reachable from itself
			return true;
		return contains(dest) && get(dest).edgeFromParent != null;
	}

/**
 * @param dest the destination vertex
 * @return the cost of the path
 * represented in the in-tree from the source to <code>dest</code>.  By definition, an
 * unreachable vertex has infinite cost.  When
 * <code>dest</code> is not reachable from the source, infinity is returned.
**/

	public double getPathDistanceFromSource(V dest) {
		if (source == dest)
			return 0;
		else if (!contains(dest))
			return AbstractGraph.INF;
		else	
			return get(dest).cost;
	}	

/**
 * @param dest the destination vertex
 * @return a positional collection
 * containing the
 * edges along the path from the root to <code>dest</code>, in order.
 * If <code>dest</code> is not reachable from s, null is returned.
**/

	public PositionalCollection<E> getPathFromSource(V dest) {
		if (!isReachableFromSource(dest))  //no path from the source to dest
			return null;
		CircularArray<E> path = new CircularArray<E>(getSize());
		V curVertex = dest;
		while (curVertex != source) {
			E e = get(curVertex).edgeFromParent;
			path.addFirst(e);
			curVertex = AbstractGraph.neighbor(curVertex,e);
		}
		return path;
	}

/**
 * @return true  if and only if there is a negative weight cycle reachable
 * from s
**/

		public boolean hasNegativeWeightCycleReachableFromSource() {
			return cycleStart != null;
		}

/**
 * @param start a
 * vertex that is guaranteed to be part of negative weight cycle in the graph
 * <BR> 
 * REQUIRES: 
 *  there is a cycle in the
 * in-tree that includes <code>start</code>
 * @return a positional
 * collection containing the edges, in order, in a negative weight cycle in the graph, where the cycle
 * includes <code>start</code>.
**/

	PositionalCollection<E> buildCycleStartingAt(V start) {
		CircularArray<E> directedCycle = new CircularArray<E>(getSize());
		V u = get(start).edgeFromParent.source();
		while (u != start) {  //follow parent until start is reached
			E e = get(u).edgeFromParent;
			directedCycle.addFirst(e);
			u = e.source();
		}
		directedCycle.addLast(get(start).edgeFromParent);
		return directedCycle;
	}

/**
 * @return a positional
 * collection containing the edges of a cycle in the graph reachable from the source vertex,
 * or null if there is no cycle in the graph reachable from the source.
**/

	public PositionalCollection<E> getCycleReachableFromSource() {
		if (cycleStart == null)
			return null;
		else
			return buildCycleStartingAt(cycleStart);
	}


}
