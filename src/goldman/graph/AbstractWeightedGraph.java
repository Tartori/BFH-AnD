// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
import goldman.collection.positional.DynamicArray;
import goldman.collection.positional.Queue;
import goldman.collection.priority.PriorityQueueLocator;
import goldman.collection.set.OpenAddressing;
import goldman.collection.set.Set;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.priority.TaggedPairingHeap;
import goldman.collection.tagged.priority.TaggedPriorityQueue;
import goldman.collection.tagged.set.Mapping;
import goldman.collection.tagged.set.OpenAddressingMapping;
import goldman.partition.UnionFindNode;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * The AbstractWeightedGraph class embodies algorithms that perform
 * computations on a weighted graph in terms of the <code>WeightedGraph</code>
 * interface.   This allows subclasses that use any graph
 * representation to benefit from these algorithms.
**/

public abstract class AbstractWeightedGraph<V, E extends WeightedEdge<V>> extends
		AbstractGraph<V, E> implements WeightedGraph<V,E> {
/**
 * @param rep the graph representation to use
 * @param multigraph which indicates if the graph is a multigraph
**/

	public AbstractWeightedGraph(GraphRepresentation<V,E> rep, boolean multigraph) {
		super(rep, multigraph);
	}


	static abstract class GTBD<V,E> extends TreeData<V,E> {
		PriorityQueueLocator<TaggedElement<Double,V>> loc;
		
		 GTBD(E edge, double cost) {
			super(edge, cost);
		}	

/**
 * @param e the edge leading to
 * v from its parent
 * @param parentCost the cost
 * associated with the parent
 * @return the cost v would
 * have if e were the edge from its parent
**/

		abstract double getCost(E e, double parentCost);

/**
 * If replacing
 * the current edge from the parent for u to e increases the priority for v,
 * then this change is made.
 * @param e the edge being considered
 * @param parentCost the
 * cost associated with u, the new parent candidate
**/

		void consider(E e, double parentCost, TaggedPriorityQueue<Double,V> pq) {
			double newCost = getCost(e, parentCost);
			if (newCost < loc.get().getTag()) {
				edgeFromParent = e;
				cost = newCost;
				pq.updateTag(cost,loc);	
			}
		}
	}

/**
 * @param tree an (initially empty) in-tree to be constructed
 * @param seedCost the cost that should be associated with the first vertex
 * placed in the tree
 * @param comp the comparator to apply to the cost function so that the
 * highest priority vertex is the one that should be placed in the tree next
 * @throws NegativeWeightEdgeException the algorithm cannot guarantee a
 * correct result due to a negative weight edge.
**/

	public void greedyTreeBuilder(InTree<V, E> tree, double seedCost,
						Comparator<Double> comp) throws NegativeWeightEdgeException {
		TaggedPriorityQueue<Double,V> pq = 	new TaggedPairingHeap<Double,V>(comp);
		GTBD<V,E> vData = (GTBD<V, E>) tree.add(tree.source, null, seedCost);
		vData.loc = pq.putTracked(seedCost, tree.source);
		while (!pq.isEmpty()) {
			V u = pq.extractMax().getElement();
			GTBD<V,E> uData = (GTBD<V, E>) tree.get(u);
			Iterator<E> outEdges = edgesFrom(u);
			while (outEdges.hasNext()) {
				E e = outEdges.next();
				if (e.weight()< 0)
					throw new NegativeWeightEdgeException(e.toString());
				V v = neighbor(u,e);
				if (tree.contains(v)) {
					vData = (GTBD<V, E>) tree.get(v);
					if (vData.loc.inCollection())  //if v is still in the priority queue
						vData.consider(e, uData.cost, pq);   //consider if e gives better connection
				}
				else {  //just discovered v, so put into tree and queue
					vData = (GTBD<V,E>) tree.add(v, e, vData.getCost(e,uData.cost));
					vData.loc = pq.putTracked(vData.cost, v);
				} 

			}
		}
	}


	static class WSPD<V,E> extends GTBD<V,E> {
		WSPD(E edge, double cost) { super(edge, cost); }

/**
 * @param e an edge leading to
 * this vertex from a new possible parent vertex u
 * @param parentCost the cost associated with u
 * @return the cost for this vertex when reached by way
 * of edge e from u
**/

		double getCost(E e, double parentCost) {
			return parentCost + ((WeightedEdge) e).weight();
		}
	}

/**
 * @param s the
 * source vertex
 * @throws NegativeWeightEdgeException the
 * graph has a negative weight edge reachable from s
**/

	public InTree<V,E> weightedShortestPaths(V s) throws NegativeWeightEdgeException {
		InTree<V, E> tree = new InTree<V, E>(s) {
			GTBD<V, E> add(V v, E edgeFromParent, double cost) {  
				GTBD<V,E> data = new WSPD<V,E>(edgeFromParent, cost); 
				put(v, data);
				return data;
			}				
		};
		greedyTreeBuilder(tree, 0.0, new Comparator<Double>() {
			public int compare(Double e1, Double e2) {  //highest priority vertex
				return e2.compareTo(e1);                   //is one with minimum value tag
			}
		});
		return tree;
	}


	static class MSTD<V,E> extends GTBD<V,E> {
		 MSTD(E edge, double cost) {
			super(edge, cost);
		}

/**
 * @param e the edge connecting
 * this vertex with its parent
 * @param parentCost the cost
 * associated with the parent
 * @return the cost associated with
 * the edge
**/

		double getCost(E e, double parentCost) {
			return ((WeightedEdge) e).weight();
		}
	}

/**
 * @return a set of edges that is a minimum
 * spanning tree for this graph
 * @throws DisconnectedGraphException the
 * graph is not connected
 * @throws NegativeWeightEdgeException a negative
 * edge weight is encountered
 * @throws GraphException the
 * graph is directed
**/

	public Set<E> primMST()
			throws NegativeWeightEdgeException,DisconnectedGraphException {
		if (isDirected())
			throw new GraphException("undirected graphs expected");
		V seed = iterator().next();  //use an arbitrary vertex as the seed
		InTree<V, E> mstTree = new InTree<V, E>(seed) {
			GTBD<V, E> add(V v, E edgeFromParent, double cost) {    
				GTBD<V,E> data = new MSTD<V,E>(edgeFromParent, cost);
				put(v, data);
				return data;
			}				
		};
		greedyTreeBuilder(mstTree, 0.0, new Comparator<Double>() {
			public int compare(Double e1, Double e2) {
				return e2.compareTo(e1);
			}
		});
		Set<E> mst = new OpenAddressing<E>(numVertices()-1); //create set to return
		for (V v: this) 
			if (v != seed)
				try {
					mst.add(mstTree.get(v).edgeFromParent);
				} catch (NoSuchElementException nsee) {
					throw new DisconnectedGraphException();
				}
		return mst;
	}

/**
 * @return a set of edges that is a minimum
 * spanning tree for this graph
 * @throws DisconnectedGraphException the
 * graph is not connected
 * @throws NegativeWeightEdgeException a negative
 * edge weight is encountered
 * @throws GraphException the
 * graph is directed
**/

	public Set<E> kruskalMST()
			throws NegativeWeightEdgeException, DisconnectedGraphException {
		if (isDirected())
			throw new GraphException("undirected graphs expected");
		DynamicArray<E> edgeList = new DynamicArray<E>();
		for (V v: graph) {
			Iterator<E> edges = edgesFrom(v);
			while (edges.hasNext()) {
				E e = edges.next();
				if (e.weight()< 0)
					throw new NegativeWeightEdgeException(e.toString());
				edgeList.add(e);
			}
		}
		edgeList.quicksort(new Comparator<E>() {
			public int compare(E e1, E e2) {  //compare edges according to weight
				return Double.compare(e1.weight(), e2.weight());
			}
		});
		Mapping<V,UnionFindNode<V>> vertexMap = 
			new OpenAddressingMapping<V,UnionFindNode<V>>(numVertices());
		for (V v: graph)
			vertexMap.put(v,new UnionFindNode<V>(v));
		Set<E> mst = new OpenAddressing<E>(numVertices()-1);
		for (E edge : edgeList){
			UnionFindNode<V> u = vertexMap.get(edge.source());
			UnionFindNode<V> v = vertexMap.get(edge.dest());
			if (u.findRepresentative() != v.findRepresentative()) {
				mst.add(edge);
				u.union(v);
				if (mst.getSize() == numVertices()-1)
					return mst;
			}
		}
		throw new DisconnectedGraphException();
	}

/**
 * This method implements the Bellman-Ford single-source shortest path algorithm
 * @param source the source
 * vertex
 * @return a shortest path tree for the provided source
**/

    public InTree<V,E> generalShortestPathFromSource(V source) {
    	InTree<V,E> tree = new InTree<V, E>(source);
    	for (V v: this) {   //initialization
    		if (v == source)	   //place the source in the shortest path tree with cost 0
    			tree.add(source, null, 0);
    		else                   //place all other vertices in the shortest path tree with infinite cost
    			tree.add(v, null, INF);
    	}
    	Set<E> edges = edgeSet();  //set of edges in the graph to iterate over
    	for (int i = 1; i < numVertices(); i++) {  //repeat n-1 times
    		for (E e : edges) {         //consider edge e as a way to improve path from source to dest
    			TreeData<V,E> uData = tree.get(e.source());
    			TreeData<V,E> vData = tree.get(e.dest());
    			if (vData.cost > uData.cost + e.weight()) {  //if e provides a better path to dest
    				vData.cost = uData.cost + e.weight();        //update the cost for dest
    				vData.edgeFromParent = e;                    //and update parent edge for dest
    			}
    		}
    	}
    	//could stop here if it is known that there are no negative weight cycles
    	for (E e : edges){                   //check for a negative weight cycle using one more iteration
    		if (tree.get(e.dest()).cost > tree.get(e.source()).cost + e.weight())
    			tree.cycleStart = e.dest();  //record the fact that a cycle exists involving e.dest
    	}        
    	return tree;
    }

/**
 * @return a shortest path matrix.
**/

    public ShortestPathMatrix<V,E> allPairsShortestPaths() {
    	ShortestPathMatrix<V,E> shortestPaths = new ShortestPathMatrix<V,E>(this);
    	for (V mid: this) {                             //mid is being considered as an intermediate vertex
    		InTree<V, E> midT = shortestPaths.get(mid);
    		for (V source: this) {   
    			InTree<V, E> sourceT = shortestPaths.get(source);
    			for (V dest: this){    
    				double potentialCost = sourceT.get(mid).cost + midT.get(dest).cost;
    				if (potentialCost < sourceT.get(dest).cost) { //found better path
    					sourceT.get(dest).cost = potentialCost;
    					sourceT.get(dest).edgeFromParent = midT.get(dest).edgeFromParent;
    				}
    			}
    		}
    	}
    	shortestPaths.cycleStart = null;  //no negative weight cycle detected yet
    	for (V v: this) {  //check for a negative weight cycle in the graph
    		if (shortestPaths.get(v).get(v).cost < 0) {
    			shortestPaths.cycleStart = v;  //detected a negative weight cycle at v
    			break;
    		}
    	}
    	return shortestPaths;
    }


    public class FlowGraph extends WeightedAdjacencyMatrix<V,SimpleWeightedEdge<V>> {
    	FlowGraph() {
    		for (V v: AbstractWeightedGraph.this) 
    			addVertex(v);
    		for (E e: AbstractWeightedGraph.this.edgeSet()) {
    			if (!containsEdge(e.source(),e.dest()))
    				addEdge(new SimpleWeightedEdge<V>(e.source(), e.dest(), 0));
    			if (!containsEdge(e.dest(),e.source()))
    				addEdge(new SimpleWeightedEdge<V>(e.dest(), e.source(), 0));
    		}
    	}
    	void removeZeroWeightEdges() {
    		for (SimpleWeightedEdge<V> e: edgeSet()) {
    			if (e.weight() == 0)
    				removeEdge(e);
    		}
    	}
    }

/**
 * @param src the source vertex
 * @param dest the destination vertex
 * @param flow the flow graph
 * @return the weight of the edge from <code>src</code> to <code>dest</code> in
 * the residual graph defined by <code>flow</code>.
**/

    double weightInResidualGraph(V src, V dest, FlowGraph flow) {
    	E graphEdge = getEdge(src,dest);
    	SimpleWeightedEdge<V> reverseFlow = flow.getEdge(dest, src);
    	double residualWeight = 0;
    	if (graphEdge != null)  // unused forward capacity
    		residualWeight += graphEdge.weight() -  flow.getEdge(src, dest).weight;
    	if (reverseFlow != null)  // used backward capacity available to push back
    		residualWeight += reverseFlow.weight;
    	return residualWeight;
    }

/**
 * @param s the source vertex
 * @param t the sink vertex
 * @param flow the flow graph
**/

    InTree<V, SimpleWeightedEdge<V>> findAugmentingPath(V s, V t, FlowGraph flow) {
    	InTree<V, SimpleWeightedEdge<V>> residualFlowTree = 
    		new InTree<V, SimpleWeightedEdge<V>>(s);
    	residualFlowTree.add(s, null, 0); //source is the root for breadth-first search
    	Queue<V> q = new Queue<V>();      //create an empty queue of vertices
    	q.enqueue(s);                     //place source in the queue
    	while (!q.isEmpty()) {            //while some vertex is still in the queue
    		V u = q.dequeue();            //u is next vertex in the queue
    		Iterator<SimpleWeightedEdge<V>> outEdges = flow.edgesFrom(u);     
    		while (outEdges.hasNext()) {
    			SimpleWeightedEdge<V> e = outEdges.next(); //e is edge from u to v
    			V v = e.dest();                            //v is reachable from u by edge e
    			if (u != v && (weightInResidualGraph(u,v,flow) > 0)) {
    				if (!residualFlowTree.contains(v)) {   //v is newly discovered
    					residualFlowTree.add(v, e, 0);
    					if (v == t) 
    						return residualFlowTree;
    					q.enqueue(v);                      //put v at the end of the queue
    				}
    			}
    		}
    	}
    	return null;   //There is no augmenting path
    }

/**
 * @param flow the flow graph
 * @param s the source vertex
 * @param t the sink vertex
 * @param residualFlowTree the shortest path tree
 * from executing breadth-first search on the residual graph defined by <code>flow</code>
**/

    void augmentFlow(FlowGraph flow, V s, V t, 
    					InTree<V, SimpleWeightedEdge<V>> residualFlowTree) {
    	double bottleneck = INF;
    	V curVertex = t;
    	while (curVertex != s) {   //find minimum weight edge on path
    		SimpleWeightedEdge<V> e = residualFlowTree.get(curVertex).edgeFromParent;
    		bottleneck = Math.min(bottleneck,weightInResidualGraph(e.source(),curVertex,flow));
    		curVertex = e.source();
    	}
    	curVertex = t;   //now do the augmentation
    	while (curVertex != s) {
    		SimpleWeightedEdge<V> flowEdge = residualFlowTree.get(curVertex).edgeFromParent;
    		SimpleWeightedEdge<V> reverseEdge = flow.getEdge(curVertex, flowEdge.source());
    		double backflow = reverseEdge.weight;
    		double pushback = Math.min(backflow, bottleneck);
    		reverseEdge.weight -= pushback;
    		flowEdge.weight += (bottleneck - pushback);
    		curVertex = flowEdge.source();
    	}
    }

/**
 * @param s the source vertex
 * @param t the sink vertex
 * @return a flow graph with
 * the maximum flow from s to t, for which only edges with
 * non-zero flow are included.
**/

    public FlowGraph maximumFlow(V s, V t) {
    	FlowGraph flow = new FlowGraph();
    	InTree<V, SimpleWeightedEdge<V>> residualFlow;
    	while ((residualFlow = findAugmentingPath(s, t, flow)) != null)
    		augmentFlow(flow, s, t, residualFlow);
    	flow.removeZeroWeightEdges();
    	return flow;
    }


}
