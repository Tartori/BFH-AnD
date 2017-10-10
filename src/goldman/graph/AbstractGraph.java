// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import goldman.NonMutatingIterator;
import goldman.collection.positional.CircularArray;
import goldman.collection.positional.PositionalCollection;
import goldman.collection.positional.Queue;
import goldman.collection.set.OpenAddressing;
import goldman.collection.set.Set;
import goldman.collection.tagged.set.Mapping;
import goldman.collection.tagged.set.OpenAddressingMapping;
import java.util.Iterator;
/**
 * The AbstractGraph class embodies algorithms that perform
 * computations on a graph in terms of the <code>Graph</code>
 * interface.   This allows subclasses that use any graph
 * representation to benefit from these algorithms.
**/

public abstract class AbstractGraph<V, E extends Edge<V>> implements
	Graph<V,E>  {

	GraphRepresentation<V,E> graph;    //the graph
	boolean multigraph;                //true iff its a multigraph


	Set<Set<V>> components;


	Mapping<V, DFSData<E>> dfsForest;       //data associated with each vertex by dfs
	PositionalCollection<V> topOrder;       //a valid topological order for an acyclic graph
	PositionalCollection<E> directedCycle;  //a directed cycle in a cyclic graph


	Set<Set<V>> scc;

/**
 * It
 * creates an abstract graph with the given specification.
 * @param graph the graph representation
 * @param multigraph true  if the graph allows multi-edges
**/

	public AbstractGraph(GraphRepresentation<V,E> graph, boolean multigraph){
		this.graph = graph;
		this.multigraph = multigraph;
	}

/**
 * @return true  if and only if the graph is directed
**/

	public boolean isDirected() {
		return graph.isDirected();
	}

/**
 * @return true  if and only if the graph
 * is a multigraph.
**/

	public boolean allowsMultiEdges() {
		return multigraph;
	}

/**
 * @return the number of vertices in this graph
**/

	public int numVertices() {
		return graph.numVertices();
	}

/**
 * @param v the target vertex
 * @return true  if and only if v is in this graph.
**/

	public boolean containsVertex(V v) {
		return graph.containsVertex(v);
	}

/**
 * @param source the source vertex
 * @param dest the destination vertex
 * @return true  if and
 * only if there is an edge in the graph from the source vertex to
 * the destination vertex
**/

	public boolean containsEdge(V source, V dest) {
		return graph.containsEdge(source, dest);
	}

/**
 * @param source the source vertex
 * @param dest the destination vertex
 * @return an edge in the graph from the
 * source vertex to the destination vertex, or null if no such edge exists.
**/

	public E getEdge(V source, V dest) {
		return graph.getEdge(source, dest);
	}

/**
 * @param v a vertex
 * @param e one of its incident edges
 * <BR> 
 * REQUIRES: 
 *  <code>v</code> is one of the two endpoints of <code>e</code>.
 * @return the
 * other endpoint of edge <code>e</code>.
**/

	static<V,E extends Edge<V>> V neighbor(V v, E e) {
		if (e.source() == v)
			return e.dest();
		else
			return e.source();
	}

/**
 * Preserves the property Updated by
 * resetting to null all variables that are invalidated
 * when the graph is modified.
**/

	void graphUpdated() {
		dfsForest = null;      //set by depth-first search
		topOrder = null;       //set by depth-first search
		directedCycle = null;  //set by depth-first search
		components = null;     //set by depth-first search
		scc = null;            //set by the strongly connected components algorithm
	}

/**
 * @param v the vertex to add to this graph
 * @return true  if v is added to this graph, or false  if v is already in
 * this graph.
**/

	public boolean addVertex(V v) {
		if (!graph.addVertex(v))
			return false;     //vertex is already in the graph
		graphUpdated();       //preserve Updated
		return true;
	}

/**
 * @param v the vertex to remove
 * <BR> 
 * REQUIRES: 
 * the removed vertex (or any equivalent vertex)
 * is never added back into the graph
 * because edges are deleted lazily in some implementations
 * on the basis of whether or not the incident vertices are still in the graph
 * @return true  if v is removed, or false  if v is not in the graph,
 * and so cannot be removed.
**/

	public boolean removeVertex(V v) {
		if (!graph.removeVertex(v))
			return false;     //vertex is not in graph to remove
		graphUpdated();       //preserve Updated
		return true;
	}

/**
 * @param e the edge to add to this graph
 * <BR> 
 * REQUIRES: 
 *  multi-edges are added only to multigraphs
**/

	public void addEdge(E e) {
		assert allowsMultiEdges() || !graph.containsEdge(e.source(), e.dest());
		graphUpdated();
		graph.addEdge(e);
	}	

/**
 * @param e the edge to remove
 * @return true  if e is removed, or false  if e
 * is not in the graph.
**/

	public boolean removeEdge(E e) {
		graphUpdated();
		return graph.removeEdge(e);
	}	

/**
 * @return a set of all edges in this graph
**/

	Set<E> edgeSet() {
		Set<E> set = new OpenAddressing<E>();
		for (V v: graph) {
			Iterator<E> edges = edgesFrom(v);
			while (edges.hasNext())
				set.add(edges.next());
		}
		return set;
	}

/**
 * @return a non-mutating
 * iterator defined over all vertices that is
 * initialized to be just before the first vertex in the set
**/

	public NonMutatingIterator<V> iterator() {
		return graph.iterator();
	}

/**
 * @param v the desired vertex
 * @return an iterator defined over all outedges of v that is
 * initially positioned just before the first one.  If the graph is an undirected
 * graph, then the iteration order will include all edges incident to v.
**/

	public Iterator<E> edgesFrom(V v) {
		return graph.edgesFrom(v);
	}

/**
 * @param v the desired vertex
 * @return a non-mutating iterator defined over all inedges of v that is
 * initialized just before the first one.  If the graph is an undirected
 * graph then the iterator will include all edges incident to v.
 * @throws UnsupportedOperationException the graph representation does not
 * support this capability
**/

	public Iterator<E> edgesTo(V v) {
		if (!isDirected())
			return edgesFrom(v);
		return graph.edgesTo(v);
	}

/**
 * Representation for infinity.
**/

	public static final double INF = java.lang.Double.POSITIVE_INFINITY;	

/**
 * @param s the source vertex
 * @return an in-tree that is a shortest path tree for source vertex s.
**/

	public InTree<V, E> unweightedShortestPaths(V s) {  //uses bfs
		InTree<V, E> shortestPathTree = new InTree<V, E>(s);
		shortestPathTree.add(s, null, 0);
		Queue<V> q = new Queue<V>();  //create an empty queue of vertices
		q.enqueue(s);            //place source in the queue
		while (!q.isEmpty()) {        //while some vertex is still in the queue
			V u = q.dequeue();        //u is next vertex in the queue
			TreeData<V,E> uData = shortestPathTree.get(u);
			Iterator<E> outEdges = edgesFrom(u);     
			while (outEdges.hasNext()) {
				E e = outEdges.next();         //e is edge from u to v
				V v = neighbor(u,e);           //v is reachable from u by edge e
				if (!shortestPathTree.contains(v)) {   //v is discovered
					shortestPathTree.add(v, e, uData.cost + 1);
					q.enqueue(v);                      //put v at the end of the queue
				}
			}
		}
		return shortestPathTree;
	}


	static class DFSData<E> {
		E edgeFromParent;  //edge to parent in the DFS forest
		boolean finished;  //true when the vertex has been removed from the stack

/**
 * @param e an edge used to initialize
 * the value of <code>edgeFromParent</code>
**/

		public DFSData(E e) {
			finished = false;
			edgeFromParent = e;
		}
	}

/**
 * @param v the vertex to
 * be associated with this DFSData instance
 * @param e the
 * edge that leads to v in the shortest path tree (i.e., the parent edge
 * for v)
**/

	DFSData<E> newDFSData(V v, E e) {
		DFSData<E> data = new DFSData<E>(e);
		dfsForest.put(v, data);   
		return data;
	}

/**
 * Calls <code>dfs</code>
 * if the graph has been updated since last time <code>dfs</code> was run
**/

	void dfsUpdateAsNeeded() {
		if (topOrder == null) {
			dfsForest = new OpenAddressingMapping<V,DFSData<E>>(numVertices());
			topOrder = new CircularArray<V>(numVertices());
			components = new OpenAddressing<Set<V>>();
			dfs();
		}
	}

/**
 * @param u a possible
 * "starting" point for a directed cycle in this graph
 * @param e = (u,v) an outgoing edge of
 * u in the potential cycle
 * <BR> 
 * REQUIRES: 
 *  u is at the top of the dfs stack.
**/

	void checkForCycle(V u, E e) {
		V v = e.dest();
		DFSData<E> vData = dfsForest.get(v);  
		if (!vData.finished) {  //this implies there is a cycle
			directedCycle = new CircularArray<E>(numVertices());
			while (u != v) {
				E nextEdge = dfsForest.get(u).edgeFromParent;
				directedCycle.addFirst(nextEdge);
				u = neighbor(u,nextEdge);
			}
			directedCycle.addLast(e);
		}
	}

/**
 * This method
 * visits all vertices reachable from u that are not yet in <code>dfsForest</code>.
 * @param u the vertex to visit next
 * @param uData the DFSData instance associated with u
 * @param component an empty set in which to place the vertices of the connected component.
 * <BR> 
 * REQUIRES: 
 *  <code>component</code> is null when the graph is undirected
**/

	void dfsVisit(V u, DFSData<E> uData, Set<V> component) {
		if (component != null)             //if an undirected graph
			component.add(u);                 //place newly discovered vertices into component
		Iterator<E> outEdges = edgesFrom(u);  //iterate over all outedges of u
		while (outEdges.hasNext()) {
			E e = outEdges.next();
			if (e != uData.edgeFromParent) {     //don't use same edge in both directions
				V v = neighbor(u,e);             //v is reachable from u
				if (!dfsForest.contains(v))      //if v is not yet in dfsForest
					dfsVisit(v, newDFSData(v, e), component); //put v in dfsForest and visit
				else if (directedCycle == null)         //check for a directed cycle if none found yet
					checkForCycle(u, e);
			}
		}
		uData.finished = true;              //finished exploring outedges from u
		topOrder.addFirst(u);               //add u to front of the topological order
	}

/**
 * Iterates over all vertices, visiting each one that is not yet
 * in the depth-first search forest
**/

	void dfs() {
		Set<V> component = null;
		for (V u: this) {  //iterate through all vertices in the graph
			if (!dfsForest.contains(u)) {                     //if u is not yet in forest
				if (!isDirected())								 //if the graph is undirected
					component = new OpenAddressing<V>();           //found a new component
				dfsVisit(u, newDFSData(u, null), component);       //place u in forest and visit
			}
			if (!isDirected())								     //if the graph is undirected
				components.add(component);                         //add new component
		}
		dfsForest = null;  //free space
	}

/**
 * @return true  if and only if this
 * graph has a directed cycle
**/

	public boolean hasCycle() {
		dfsUpdateAsNeeded();
		return directedCycle != null;
	}

/**
 * The <code>hasCycle</code> method can be used to determine if the graph has
 * a directed cycle.
 * @return a positional collection holding
 * a directed cyclic in the graph
 * @throws GraphException the graph is acyclic
**/

	public PositionalCollection<E> getCycle() {
		dfsUpdateAsNeeded();
		if (directedCycle != null)
			return directedCycle;
		else
			throw new GraphException("no directed cycle exists");
	}

/**
 * @return the number of connected
 * components in this graph
 * @throws GraphException the graph is directed,
 * since the connected components are defined only for undirected graphs
**/

	public int numConnectedComponents() {
		if (isDirected())
			throw new GraphException("applies only to undirected graphs");
		dfsUpdateAsNeeded();
		return components.getSize();
	}

/**
 * @return the connected
 * components in this graph
 * @throws GraphException the graph is directed,
 * since the connected components are only defined for undirected graphs
**/

	public Set<Set<V>> getConnectedComponents() {
		if (isDirected())
			throw new GraphException("applies only to undirected graphs");
		dfsUpdateAsNeeded(); 
		return components;
	}

/**
 * The <code>hasCycle</code> method can be called first to find out if
 * the graph has a cycle.
 * @return a positional collection holding
 * a valid topological order in the graph
 * @throws GraphException the graph is cyclic
**/

	public PositionalCollection<V> topologicalOrder(){
		dfsUpdateAsNeeded();
		if (directedCycle == null)
			return topOrder;
		else
			throw new GraphException("a directed cycle exists");
	}

/**
 * Calls <code>stronglyConnectedComponents</code> to update
 * <code>scc</code> if the graph has been updated since last
 * time <code>stronglyConnectedComponents</code>
 * was run.
**/

	void sccUpdateAsNeeded() {
		if (scc == null ) {
			scc = new OpenAddressing<Set<V>>();
			stronglyConnectedComponents();
		}		
	}

/**
 * This
 * method is just like <code>dfsVisit</code> except that it does not modify <code>topOrder</code>,
 * and it adds each vertex visited in this directed graph into <code>component</code>.
 * @param u the vertex from
 * which to start
 * @param uData the data associated with u
 * @param component a set that is to hold all elements in this strongly connected component
 * <BR> 
 * REQUIRES: 
 *  this is a directed graph
**/

	void dfsVisitForSCC(V u, DFSData<E> uData, Set<V> component) {
		component.add(u);                   //place all newly discovered vertices into this component
		Iterator<E> outEdges = edgesFrom(u);  
		while (outEdges.hasNext()) {
			E e = outEdges.next();
			if (!dfsForest.contains(e.dest()))     
				dfsVisitForSCC(e.dest(), newDFSData(e.dest(), e), component);
		}
		uData.finished = true;              //finished exploring inedges to u
	}

/**
 * Computes the strongly
 * connected components of the graph storing the result in <code>scc</code>
**/

	void stronglyConnectedComponents() {
		dfsUpdateAsNeeded();    //if needed, run depth-first search on the original graph
		dfsForest = new OpenAddressingMapping<V,DFSData<E>>(numVertices());  //reset
		Set<V> component = null;
		for (int p = numVertices()-1; p >= 0; p--) {   //in finishing order from first dfs
			V u = topOrder.get(p);
			if (!dfsForest.contains(u))  {             //visit u if not yet in dfsForest
				component = new OpenAddressing<V>();
				dfsVisitForSCC(u, newDFSData(u, null), component);
			}
			scc.add(component);
		}
		dfsForest = null;
	}

/**
 * @return a set of sets of vertices, with one set corresponding
 * to each strongly connected component of this graph
 * @throws GraphException the graph is undirected,
 * since the strongly connected components are defined only for directed graphs
**/

	public Set<Set<V>> getStronglyConnectedComponents() {
		if (!isDirected())
			throw new GraphException("applies only to directed graphs");
		sccUpdateAsNeeded();
		return scc;
	}

/**
 * @return the number
 * of strongly connected components in this graph.
 * @throws GraphException the graph is undirected,
 * since the strongly connected components are defined only for directed graphs
**/

	public int numStronglyConnectedComponents() {
		if (!isDirected())
			throw new GraphException("applies only to directed graphs");
		sccUpdateAsNeeded();
		return scc.getSize();
	}


	public String toString() {
		String result = "";
		for (V v: this){
			result += v + ":";
			Iterator<E> it2 = edgesFrom(v);
			while (it2.hasNext())
				result += " " + it2.next();
			result += "\n";
		}
		return result;
	}
}
