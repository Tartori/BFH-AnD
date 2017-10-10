package goldman.junit4tests;
import static org.junit.Assert.*;

import org.junit.Test;
import goldman.graph.*;

public class AdjacencyMatrixTest extends GraphTest {

	public Graph<String,SimpleEdge<String>> createUnweightedGraph() {
		return new AdjacencyMatrix<String,SimpleEdge<String>>();
	}
	
	public Graph<String,SimpleEdge<String>> createUnweightedGraph(boolean directed, boolean multigraph) {
		return new AdjacencyMatrix<String,SimpleEdge<String>>(directed,multigraph);
	}
	
	public WeightedGraph<Integer,SimpleWeightedEdge<Integer>> createWeightedGraph() {
		return new WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>>();
		
	}
	
	public WeightedGraph<Integer,SimpleWeightedEdge<Integer>> createWeightedGraph(boolean directed, boolean multigraph) {
		return new WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>>(directed, multigraph);
	}
	
	@Test
	public void initializationTest() {
		assertEquals(0, createUnweightedGraph().numVertices());
	}
	
}
