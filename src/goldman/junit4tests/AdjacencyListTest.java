package goldman.junit4tests;
import static org.junit.Assert.assertEquals;

import goldman.graph.*;
import org.junit.Test;

public class AdjacencyListTest extends GraphTest {
	
	public Graph<String,SimpleEdge<String>> createUnweightedGraph() {
		return new AdjacencyList<String,SimpleEdge<String>>();
	}
	
	public Graph<String,SimpleEdge<String>> createUnweightedGraph(boolean directed, boolean multigraph) {
		return new AdjacencyList<String,SimpleEdge<String>>(directed, multigraph, true);
	}
	
	public WeightedGraph<Integer,SimpleWeightedEdge<Integer>> createWeightedGraph() {
		return new WeightedAdjacencyList<Integer,SimpleWeightedEdge<Integer>>();
		
	}
	
	public WeightedGraph<Integer,SimpleWeightedEdge<Integer>> createWeightedGraph(boolean directed, boolean multigraph) {
		return new WeightedAdjacencyList<Integer,SimpleWeightedEdge<Integer>>(directed, multigraph, true);

	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createUnweightedGraph().numVertices());
	}

}
