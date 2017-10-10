package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;
import goldman.collection.set.Set;
import goldman.graph.*;
import goldman.graph.AbstractWeightedGraph.FlowGraph;

public abstract class GraphTest {
	
	String a = new String("a");
	String b = new String("b");
	String c = new String("c");
	String d = new String("d");
	String e = new String("e");
	String f = new String("f");
	String g = new String("g");
	String h = new String("h");
	String i = new String("i");
	String j = new String("j");
	String k = new String("k");

	String s = new String("s");

	public abstract Graph<String,SimpleEdge<String>> createUnweightedGraph();
	public abstract Graph<String,SimpleEdge<String>> createUnweightedGraph(boolean directed, boolean multigraph);
	public abstract WeightedGraph<Integer,SimpleWeightedEdge<Integer>> createWeightedGraph();
	public abstract WeightedGraph<Integer,SimpleWeightedEdge<Integer>> createWeightedGraph(boolean directed, boolean multigraph);

	@Test
	public void testDirected() {
		final Graph<String,SimpleEdge<String>> g = createUnweightedGraph(true,false); 
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		g.addVertex(a);
		g.addEdge(new SimpleEdge<String>(a,b));
		g.addEdge(new SimpleEdge<String>(b,c));
		g.addEdge(new SimpleEdge<String>(c,d));
		g.addEdge(new SimpleEdge<String>(d,c));
		try {
			g.addEdge(new SimpleEdge<String>(d,c));
		} catch (AssertionError ae) {
			// OK
		}
		Graph<String,SimpleEdge<String>> g2 = createUnweightedGraph(true, true); 
		g2.addVertex(b);
		g2.addVertex(c);
		g2.addVertex(d);
		g2.addVertex(e);
		g2.addVertex(a);
		g2.addEdge(new SimpleEdge<String>(a,b));
		g2.addEdge(new SimpleEdge<String>(b,c));
		g2.addEdge(new SimpleEdge<String>(c,d));
		g2.addEdge(new SimpleEdge<String>(d,c));
		g2.addEdge(new SimpleEdge<String>(d,c));
		
	}

	@Test
	public void testUndirected() {
		final Graph<String,SimpleEdge<String>> g = createUnweightedGraph(false,false); 
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		g.addVertex(a);
		SimpleEdge<String> edge = new SimpleEdge<String>(c,d);
		g.addEdge(new SimpleEdge<String>(a,b));
		g.addEdge(new SimpleEdge<String>(b,c));
		g.addEdge(edge);
		try {
			g.addEdge(new SimpleEdge<String>(c,d));
		} catch (AssertionError ae) {
			// OK
		}
		try {
			g.addEdge(new SimpleEdge<String>(d,c));
		} catch (AssertionError ae) {
			// OK
		}
		assertEquals(g.removeEdge(edge),true);

		Graph<String,SimpleEdge<String>> g2 = createUnweightedGraph(false,true); 
		g2.addVertex(b);
		g2.addVertex(c);
		g2.addVertex(d);
		g2.addVertex(e);
		g2.addVertex(a);
		g2.addEdge(new SimpleEdge<String>(a,b));
		g2.addEdge(new SimpleEdge<String>(b,c));
		g2.addEdge(new SimpleEdge<String>(c,d));
		g2.addEdge(new SimpleEdge<String>(d,c));
		g2.addEdge(new SimpleEdge<String>(a,b));
	}

	@Test
	public void testAdd() throws Exception {
		Graph<String,SimpleEdge<String>> g = createUnweightedGraph(); 
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		g.addVertex(a);
		g.addEdge(new SimpleEdge<String>(a,b));
		g.addEdge(new SimpleEdge<String>(b,c));
		g.addEdge(new SimpleEdge<String>(c,d));
		g.addEdge(new SimpleEdge<String>(d,e));
		g.addEdge(new SimpleEdge<String>(a,c));
		g.addEdge(new SimpleEdge<String>(c,e));
		g.addEdge(new SimpleEdge<String>(b,d));
		g.addEdge(new SimpleEdge<String>(b,e));

		assertEquals("c: (c,d) (c,e)\n" +
				"e:\n"+
				"b: (b,c) (b,d) (b,e)\n"+
				"d: (d,e)\n"+
				"a: (a,b) (a,c)\n",
				g.toString());	
		assertEquals("<a, b, c, d, e>", g.topologicalOrder().toString());

		g.addVertex(f);
		g.addEdge(new SimpleEdge<String>(b,f));
		g.addEdge(new SimpleEdge<String>(f,c));
		assertEquals("f: (f,c)\n"+
				"c: (c,d) (c,e)\n" +
				"e:\n"+
				"b: (b,c) (b,d) (b,e) (b,f)\n"+
				"d: (d,e)\n"+
				"a: (a,b) (a,c)\n",
				g.toString());
		assertEquals("<a, b, f, c, d, e>", g.topologicalOrder().toString());
		assertEquals(false, g.hasCycle());
		
		SimpleEdge<String> e = new SimpleEdge<String>(d,a);
		g.addEdge(e);
		assertEquals("f: (f,c)\n"+
				"c: (c,d) (c,e)\n" +
				"e:\n"+
				"b: (b,c) (b,d) (b,e) (b,f)\n"+
				"d: (d,e) (d,a)\n"+
				"a: (a,b) (a,c)\n",
				g.toString());

		assertEquals("<(c,d), (d,a), (a,b), (b,c)>", g.getCycle().toString());
		assertEquals(true, g.hasCycle());
		
		g.removeEdge(e);
		assertEquals("f: (f,c)\n"+
				"c: (c,d) (c,e)\n" +
				"e:\n"+
				"b: (b,c) (b,d) (b,e) (b,f)\n"+
				"d: (d,e)\n"+
				"a: (a,b) (a,c)\n",
				g.toString());
		assertEquals("<a, b, f, c, d, e>", g.topologicalOrder().toString());
		assertEquals(false, g.hasCycle());
	}

	@Test
	public void testRemoveEdges() throws Exception {
		Graph<String,SimpleEdge<String>> g = createUnweightedGraph(); 
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		g.addVertex(a);
		SimpleEdge<String> cd, ac, be, de;
		g.addEdge(new SimpleEdge<String>(a,b));
		g.addEdge(new SimpleEdge<String>(b,c));
		g.addEdge(cd = new SimpleEdge<String>(c,d));
		g.addEdge(de = new SimpleEdge<String>(d,e));
		g.addEdge(ac = new SimpleEdge<String>(a,c));
		g.addEdge(new SimpleEdge<String>(c,e));
		g.addEdge(new SimpleEdge<String>(b,d));
		g.addEdge(be = new SimpleEdge<String>(b,e));

		assertEquals("c: (c,d) (c,e)\n" +
				"e:\n"+
				"b: (b,c) (b,d) (b,e)\n"+
				"d: (d,e)\n"+
				"a: (a,b) (a,c)\n",
				g.toString());

		g.removeEdge(cd);
		g.removeEdge(ac);
		g.removeEdge(be);
		g.removeEdge(de);
		assertEquals("c: (c,e)\n" +
				"e:\n"+
				"b: (b,c) (b,d)\n"+
				"d:\n"+
				"a: (a,b)\n",
				g.toString());
		assertEquals("(b,c)", g.edgesTo(c).next().toString());
		assertEquals("(b,d)", g.edgesTo(d).next().toString());
		assertEquals(false, g.edgesTo(a).hasNext());
	}
	
	@Test
	public void testIncomingEdges() {
		Graph<String,SimpleEdge<String>> g = createUnweightedGraph(); 
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		g.addVertex(a);
		SimpleEdge<String> r1, r2, r3;
		g.addEdge(new SimpleEdge<String>(a,b));
		g.addEdge(r1 = new SimpleEdge<String>(b,c));
		g.addEdge(new SimpleEdge<String>(c,d));
		g.addEdge(new SimpleEdge<String>(d,e));
		g.addEdge(new SimpleEdge<String>(a,c));
		g.addEdge(r2 = new SimpleEdge<String>(c,e));
		g.addEdge(r3 = new SimpleEdge<String>(b,d));
		g.addEdge(new SimpleEdge<String>(b,e));
		Iterator<SimpleEdge<String>> toA, toB, toC, toD, toE;
		toA = g.edgesTo(a);
		toB = g.edgesTo(b);
		toC = g.edgesTo(c);
		toD = g.edgesTo(d);
		toE = g.edgesTo(e);
		assertEquals(false, toA.hasNext());
		assertEquals("(a,b)", toB.next().toString());
		if ("(b,c)".equals(toC.next().toString()))
			assertEquals("(a,c)", toC.next().toString());
		else
			assertEquals("(b,c)", toC.next().toString());
		
		if ("(c,d)".equals(toD.next().toString()))
			assertEquals("(b,d)", toD.next().toString());
		else
			assertEquals("(c,d)", toD.next().toString());
		
		g.removeVertex(c);
		
		if ("(d,e)".equals(toE.next().toString()))
			assertEquals("(b,e)", toE.next().toString());
		else
			assertEquals("(d,e)", toE.next().toString());
	
		assertEquals(false, g.removeEdge(r2));
		assertEquals(true, g.removeEdge(r3));
		assertEquals(false, g.removeEdge(r3));		
		assertEquals(false, g.removeEdge(r1));
	}

	@Test
	public void testRemoveVertices() throws Exception {
		Graph<String,SimpleEdge<String>> g = createUnweightedGraph(); 
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		g.addVertex(a);
		g.addEdge(new SimpleEdge<String>(a,b));
		g.addEdge(new SimpleEdge<String>(b,c));
		g.addEdge(new SimpleEdge<String>(c,d));
		g.addEdge(new SimpleEdge<String>(d,e));
		g.addEdge(new SimpleEdge<String>(a,c));
		g.addEdge(new SimpleEdge<String>(c,e));
		g.addEdge(new SimpleEdge<String>(b,d));
		g.addEdge(new SimpleEdge<String>(b,e));

		assertEquals("c: (c,d) (c,e)\n" +
				"e:\n"+
				"b: (b,c) (b,d) (b,e)\n"+
				"d: (d,e)\n"+
				"a: (a,b) (a,c)\n",
				g.toString());

		g.removeVertex(d);
		assertEquals("c: (c,e)\n" +
				"e:\n"+
				"b: (b,c) (b,e)\n"+
				"a: (a,b) (a,c)\n",
				g.toString());
		g.removeVertex(e);
		assertEquals("c:\n" +
				"b: (b,c)\n"+
				"a: (a,b) (a,c)\n",
				g.toString());
		g.removeVertex(b);
		assertEquals("c:\n" +
				"a: (a,c)\n",
				g.toString());
		g.removeVertex(c);
		g.removeVertex(a);
		assertEquals("",g.toString());

	}

	@Test
	public void testUnweightedPaths() throws Exception {
		Graph<String,SimpleEdge<String>> g2 = createUnweightedGraph();

		g2.addVertex(d);
		g2.addVertex(c);
		g2.addVertex(a);
		g2.addVertex(b);
		g2.addVertex(e);
		g2.addVertex(s);
		g2.addEdge(new SimpleEdge<String>(s,a));
		g2.addEdge(new SimpleEdge<String>(s,b));
		g2.addEdge(new SimpleEdge<String>(c,d));
		g2.addEdge(new SimpleEdge<String>(d,e));
		g2.addEdge(new SimpleEdge<String>(a,c));
		g2.addEdge(new SimpleEdge<String>(c,e));
		g2.addEdge(new SimpleEdge<String>(b,a));

		assertEquals("s: (s,a) (s,b)\n"+
				"c: (c,d) (c,e)\n"+
				"e:\n"+	
				"b: (b,a)\n"+
				"d: (d,e)\n"+
				"a: (a,c)\n",
				g2.toString());

//		g2.computeShortestPaths(s);
		InTree<String,SimpleEdge<String>> tree = g2.unweightedShortestPaths(s);
		assertEquals("<(s,a)>", tree.getPathFromSource(a).toString());
		assertEquals("<(s,b)>", tree.getPathFromSource(b).toString());
		assertEquals("<(s,a), (a,c)>", tree.getPathFromSource(c).toString());
		assertEquals("<(s,a), (a,c), (c,d)>", tree.getPathFromSource(d).toString());
		assertEquals("<(s,a), (a,c), (c,e)>", tree.getPathFromSource(e).toString());

		InTree<String,SimpleEdge<String>> treeb = g2.unweightedShortestPaths(b);
		assertEquals("<(b,a), (a,c), (c,d)>", treeb.getPathFromSource(d).toString());

		g2.addEdge(new SimpleEdge<String>(b,e));
		assertEquals("s: (s,a) (s,b)\n"+
				"c: (c,d) (c,e)\n"+
				"e:\n"+	
				"b: (b,a) (b,e)\n"+
				"d: (d,e)\n"+
				"a: (a,c)\n",
				g2.toString());

		InTree<String,SimpleEdge<String>> tree2 = g2.unweightedShortestPaths(s);
		assertEquals("<(s,a)>", tree2.getPathFromSource(a).toString());
		assertEquals("<(s,b)>", tree2.getPathFromSource(b).toString());
		assertEquals("<(s,a), (a,c)>", tree2.getPathFromSource(c).toString());
		assertEquals("<(s,a), (a,c), (c,d)>", tree2.getPathFromSource(d).toString());
		assertEquals("<(s,b), (b,e)>", tree2.getPathFromSource(e).toString());

		Graph<String,SimpleEdge<String>> g3 = createUnweightedGraph();
		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);
		g3.addVertex(f);
		g3.addVertex(g);
		g3.addVertex(h);
		g3.addVertex(i);
		g3.addVertex(j);
		g3.addVertex(k);	
		g3.addEdge(new SimpleEdge<String>(b,c));
		g3.addEdge(new SimpleEdge<String>(b,i));
		g3.addEdge(new SimpleEdge<String>(c,d));
		g3.addEdge(new SimpleEdge<String>(c,i));
		g3.addEdge(new SimpleEdge<String>(i,a));
		g3.addEdge(new SimpleEdge<String>(i,h));
		g3.addEdge(new SimpleEdge<String>(a,h));
		g3.addEdge(new SimpleEdge<String>(d,a));
		g3.addEdge(new SimpleEdge<String>(d,e));
		g3.addEdge(new SimpleEdge<String>(d,g));
		g3.addEdge(new SimpleEdge<String>(g,e));
		g3.addEdge(new SimpleEdge<String>(e,f));
		g3.addEdge(new SimpleEdge<String>(g,f));
		g3.addEdge(new SimpleEdge<String>(h,g));
		g3.addEdge(new SimpleEdge<String>(k,j));
		
		tree = g3.unweightedShortestPaths(b);

		assertEquals("<(b,i), (i,a)>", tree.getPathFromSource(a).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(a));
		assertEquals("<>", tree.getPathFromSource(b).toString());
		assertEquals(0.0, tree.getPathDistanceFromSource(b));
		assertEquals("<(b,c)>", tree.getPathFromSource(c).toString());
		assertEquals(1.0, tree.getPathDistanceFromSource(c));
		assertEquals("<(b,c), (c,d)>", tree.getPathFromSource(d).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(d));
		assertEquals("<(b,c), (c,d), (d,e)>", tree.getPathFromSource(e).toString());
		assertEquals(3.0, tree.getPathDistanceFromSource(e));
		assertEquals("<(b,c), (c,d), (d,e), (e,f)>", tree.getPathFromSource(f).toString());
		assertEquals(4.0, tree.getPathDistanceFromSource(f));
		assertEquals("<(b,c), (c,d), (d,g)>", tree.getPathFromSource(g).toString());
		assertEquals(3.0, tree.getPathDistanceFromSource(g));
		assertEquals("<(b,i), (i,h)>", tree.getPathFromSource(h).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(h));
		assertEquals("<(b,i)>", tree.getPathFromSource(i).toString());
		assertEquals(1.0, tree.getPathDistanceFromSource(i));
		assertEquals(null, tree.getPathFromSource(j));
		assertEquals(INF, tree.getPathDistanceFromSource(j));
		assertEquals(null, tree.getPathFromSource(k));
		assertEquals(INF, tree.getPathDistanceFromSource(k));
	}

	@Test
	public void testUnweightedPathsWithTemporaryMultiEdges() throws Exception {
		Graph<String,SimpleEdge<String>> g2 = createUnweightedGraph(true,true);

		g2.addVertex(d);
		g2.addVertex(c);
		g2.addVertex(a);
		g2.addVertex(b);
		g2.addVertex(e);
		g2.addVertex(s);
		g2.addEdge(new SimpleEdge<String>(s,a));
		g2.addEdge(new SimpleEdge<String>(s,b));
		g2.addEdge(new SimpleEdge<String>(c,d));
		g2.addEdge(new SimpleEdge<String>(d,e));
		g2.addEdge(new SimpleEdge<String>(a,c));
		g2.addEdge(new SimpleEdge<String>(c,e));
		g2.addEdge(new SimpleEdge<String>(b,a));

		assertEquals("s: (s,a) (s,b)\n"+
				"c: (c,d) (c,e)\n"+
				"e:\n"+	
				"b: (b,a)\n"+
				"d: (d,e)\n"+
				"a: (a,c)\n",
				g2.toString());
		
		// NOW ADD STUFF TO THE GRAPH AND TAKE IT AWAY
		g2.addVertex(g);
		SimpleEdge<String> x,y;
		g2.addEdge(new SimpleEdge<String>(g,a));
		g2.addEdge(new SimpleEdge<String>(c,g));
		g2.addEdge(x = new SimpleEdge<String>(c,d)); // multi-edge
		g2.addEdge(new SimpleEdge<String>(a,g)); // another multi-edge, to be taken away with g
		g2.addEdge(y = new SimpleEdge<String>(s,b)); // another multi-edge
		
		g2.removeEdge(x);
		g2.removeVertex(g);
		g2.removeEdge(y);
		
		assertEquals("s: (s,a) (s,b)\n"+
				"c: (c,d) (c,e)\n"+
				"e:\n"+	
				"b: (b,a)\n"+
				"d: (d,e)\n"+
				"a: (a,c)\n",
				g2.toString());
		
//		g2.computeShortestPaths(s);
		InTree<String,SimpleEdge<String>> tree = g2.unweightedShortestPaths(s);
		assertEquals("<(s,a)>", tree.getPathFromSource(a).toString());
		assertEquals("<(s,b)>", tree.getPathFromSource(b).toString());
		assertEquals("<(s,a), (a,c)>", tree.getPathFromSource(c).toString());
		assertEquals("<(s,a), (a,c), (c,d)>", tree.getPathFromSource(d).toString());
		assertEquals("<(s,a), (a,c), (c,e)>", tree.getPathFromSource(e).toString());
		assertEquals("<(s,a), (a,c), (c,e)>", tree.getPathFromSource(e).toString());

		InTree<String,SimpleEdge<String>> treeb = g2.unweightedShortestPaths(b);
		assertEquals("<(b,a), (a,c), (c,d)>", treeb.getPathFromSource(d).toString());

		g2.addEdge(new SimpleEdge<String>(b,e));
		assertEquals("s: (s,a) (s,b)\n"+
				"c: (c,d) (c,e)\n"+
				"e:\n"+	
				"b: (b,a) (b,e)\n"+
				"d: (d,e)\n"+
				"a: (a,c)\n",
				g2.toString());

		InTree<String,SimpleEdge<String>> tree2 = g2.unweightedShortestPaths(s);
		assertEquals("<(s,a)>", tree2.getPathFromSource(a).toString());
		assertEquals("<(s,b)>", tree2.getPathFromSource(b).toString());
		assertEquals("<(s,a), (a,c)>", tree2.getPathFromSource(c).toString());
		assertEquals("<(s,a), (a,c), (c,d)>", tree2.getPathFromSource(d).toString());
		assertEquals("<(s,b), (b,e)>", tree2.getPathFromSource(e).toString());

		Graph<String,SimpleEdge<String>> g3 = createUnweightedGraph();
		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);
		g3.addVertex(f);
		g3.addVertex(g);
		g3.addVertex(h);
		g3.addVertex(i);
		g3.addVertex(j);
		g3.addVertex(k);	
		g3.addEdge(new SimpleEdge<String>(b,c));
		g3.addEdge(new SimpleEdge<String>(b,i));
		g3.addEdge(new SimpleEdge<String>(c,d));
		g3.addEdge(new SimpleEdge<String>(c,i));
		g3.addEdge(new SimpleEdge<String>(i,a));
		g3.addEdge(new SimpleEdge<String>(i,h));
		g3.addEdge(new SimpleEdge<String>(a,h));
		g3.addEdge(new SimpleEdge<String>(d,a));
		g3.addEdge(new SimpleEdge<String>(d,e));
		g3.addEdge(new SimpleEdge<String>(d,g));
		g3.addEdge(new SimpleEdge<String>(g,e));
		g3.addEdge(new SimpleEdge<String>(e,f));
		g3.addEdge(new SimpleEdge<String>(g,f));
		g3.addEdge(new SimpleEdge<String>(h,g));
		g3.addEdge(new SimpleEdge<String>(k,j));
		
		tree = g3.unweightedShortestPaths(b);

		assertEquals("<(b,i), (i,a)>", tree.getPathFromSource(a).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(a));
		assertEquals("<>", tree.getPathFromSource(b).toString());
		assertEquals(0.0, tree.getPathDistanceFromSource(b));
		assertEquals("<(b,c)>", tree.getPathFromSource(c).toString());
		assertEquals(1.0, tree.getPathDistanceFromSource(c));
		assertEquals("<(b,c), (c,d)>", tree.getPathFromSource(d).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(d));
		assertEquals("<(b,c), (c,d), (d,e)>", tree.getPathFromSource(e).toString());
		assertEquals(3.0, tree.getPathDistanceFromSource(e));
		assertEquals("<(b,c), (c,d), (d,e), (e,f)>", tree.getPathFromSource(f).toString());
		assertEquals(4.0, tree.getPathDistanceFromSource(f));
		assertEquals("<(b,c), (c,d), (d,g)>", tree.getPathFromSource(g).toString());
		assertEquals(3.0, tree.getPathDistanceFromSource(g));
		assertEquals("<(b,i), (i,h)>", tree.getPathFromSource(h).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(h));
		assertEquals("<(b,i)>", tree.getPathFromSource(i).toString());
		assertEquals(1.0, tree.getPathDistanceFromSource(i));
		assertEquals(null, tree.getPathFromSource(j));
		assertEquals(INF, tree.getPathDistanceFromSource(j));
		assertEquals(null, tree.getPathFromSource(k));
		assertEquals(INF, tree.getPathDistanceFromSource(k));
	}
	
	@Test
	public void testUnweightedPathsWithMultiEdges() throws Exception {
		Graph<String,SimpleEdge<String>> g2 = createUnweightedGraph(true,true);

		g2.addVertex(d);
		g2.addVertex(c);
		g2.addVertex(a);
		g2.addVertex(b);
		g2.addVertex(e);
		g2.addVertex(s);
		g2.addEdge(new SimpleEdge<String>(s,a));
		g2.addEdge(new SimpleEdge<String>(s,b));
		g2.addEdge(new SimpleEdge<String>(c,d));
		g2.addEdge(new SimpleEdge<String>(d,e));
		g2.addEdge(new SimpleEdge<String>(a,c));
		g2.addEdge(new SimpleEdge<String>(c,e));
		g2.addEdge(new SimpleEdge<String>(b,a));

		assertEquals("s: (s,a) (s,b)\n"+
				"c: (c,d) (c,e)\n"+
				"e:\n"+	
				"b: (b,a)\n"+
				"d: (d,e)\n"+
				"a: (a,c)\n",
				g2.toString());
		
		// add multiedges
		g2.addEdge(new SimpleEdge<String>(d,e));
		g2.addEdge(new SimpleEdge<String>(c,d));
		g2.addEdge(new SimpleEdge<String>(c,d)); 
		g2.addEdge(new SimpleEdge<String>(c,a)); 
		g2.addEdge(new SimpleEdge<String>(a,b)); 
		
		
//		g2.computeShortestPaths(s);
		InTree<String,SimpleEdge<String>> tree = g2.unweightedShortestPaths(s);
		assertEquals("<(s,a)>", tree.getPathFromSource(a).toString());
		assertEquals("<(s,b)>", tree.getPathFromSource(b).toString());
		assertEquals("<(s,a), (a,c)>", tree.getPathFromSource(c).toString());
		assertEquals("<(s,a), (a,c), (c,d)>", tree.getPathFromSource(d).toString());
		assertEquals("<(s,a), (a,c), (c,e)>", tree.getPathFromSource(e).toString());
		assertEquals("<(s,a), (a,c), (c,e)>", tree.getPathFromSource(e).toString());

		InTree<String,SimpleEdge<String>> treeb = g2.unweightedShortestPaths(b);
		assertEquals("<(b,a), (a,c), (c,d)>", treeb.getPathFromSource(d).toString());

		g2.addEdge(new SimpleEdge<String>(b,e));

		InTree<String,SimpleEdge<String>> tree2 = g2.unweightedShortestPaths(s);
		assertEquals("<(s,a)>", tree2.getPathFromSource(a).toString());
		assertEquals("<(s,b)>", tree2.getPathFromSource(b).toString());
		assertEquals("<(s,a), (a,c)>", tree2.getPathFromSource(c).toString());
		assertEquals("<(s,a), (a,c), (c,d)>", tree2.getPathFromSource(d).toString());
		assertEquals("<(s,b), (b,e)>", tree2.getPathFromSource(e).toString());

		Graph<String,SimpleEdge<String>> g3 = createUnweightedGraph();
		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);
		g3.addVertex(f);
		g3.addVertex(g);
		g3.addVertex(h);
		g3.addVertex(i);
		g3.addVertex(j);
		g3.addVertex(k);	
		g3.addEdge(new SimpleEdge<String>(b,c));
		g3.addEdge(new SimpleEdge<String>(b,i));
		g3.addEdge(new SimpleEdge<String>(c,d));
		g3.addEdge(new SimpleEdge<String>(c,i));
		g3.addEdge(new SimpleEdge<String>(i,a));
		g3.addEdge(new SimpleEdge<String>(i,h));
		g3.addEdge(new SimpleEdge<String>(a,h));
		g3.addEdge(new SimpleEdge<String>(d,a));
		g3.addEdge(new SimpleEdge<String>(d,e));
		g3.addEdge(new SimpleEdge<String>(d,g));
		g3.addEdge(new SimpleEdge<String>(g,e));
		g3.addEdge(new SimpleEdge<String>(e,f));
		g3.addEdge(new SimpleEdge<String>(g,f));
		g3.addEdge(new SimpleEdge<String>(h,g));
		g3.addEdge(new SimpleEdge<String>(k,j));
		
		tree = g3.unweightedShortestPaths(b);

		assertEquals("<(b,i), (i,a)>", tree.getPathFromSource(a).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(a));
		assertEquals("<>", tree.getPathFromSource(b).toString());
		assertEquals(0.0, tree.getPathDistanceFromSource(b));
		assertEquals("<(b,c)>", tree.getPathFromSource(c).toString());
		assertEquals(1.0, tree.getPathDistanceFromSource(c));
		assertEquals("<(b,c), (c,d)>", tree.getPathFromSource(d).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(d));
		assertEquals("<(b,c), (c,d), (d,e)>", tree.getPathFromSource(e).toString());
		assertEquals(3.0, tree.getPathDistanceFromSource(e));
		assertEquals("<(b,c), (c,d), (d,e), (e,f)>", tree.getPathFromSource(f).toString());
		assertEquals(4.0, tree.getPathDistanceFromSource(f));
		assertEquals("<(b,c), (c,d), (d,g)>", tree.getPathFromSource(g).toString());
		assertEquals(3.0, tree.getPathDistanceFromSource(g));
		assertEquals("<(b,i), (i,h)>", tree.getPathFromSource(h).toString());
		assertEquals(2.0, tree.getPathDistanceFromSource(h));
		assertEquals("<(b,i)>", tree.getPathFromSource(i).toString());
		assertEquals(1.0, tree.getPathDistanceFromSource(i));
		assertEquals(null, tree.getPathFromSource(j));
		assertEquals(INF, tree.getPathDistanceFromSource(j));
		assertEquals(null, tree.getPathFromSource(k));
		assertEquals(INF, tree.getPathDistanceFromSource(k));
	}

	@Test
	public void testSCC() throws Exception {

		Graph<String,SimpleEdge<String>> g3 = createUnweightedGraph();
		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);
		g3.addVertex(f);
		g3.addVertex(g);
		g3.addVertex(h);
		g3.addVertex(i);
		g3.addVertex(j);
		g3.addVertex(k);	
		g3.addEdge(new SimpleEdge<String>(b,c));
		g3.addEdge(new SimpleEdge<String>(b,i));
		g3.addEdge(new SimpleEdge<String>(c,d));
		g3.addEdge(new SimpleEdge<String>(c,i));
		g3.addEdge(new SimpleEdge<String>(i,a));
		g3.addEdge(new SimpleEdge<String>(i,h));
		g3.addEdge(new SimpleEdge<String>(a,h));
		g3.addEdge(new SimpleEdge<String>(d,a));
		g3.addEdge(new SimpleEdge<String>(d,e));
		g3.addEdge(new SimpleEdge<String>(d,g));
		g3.addEdge(new SimpleEdge<String>(g,e));
		g3.addEdge(new SimpleEdge<String>(e,f));
		g3.addEdge(new SimpleEdge<String>(g,f));
		g3.addEdge(new SimpleEdge<String>(h,g));
		g3.addEdge(new SimpleEdge<String>(k,j));
		g3.addEdge(new SimpleEdge<String>(f,g));
		g3.addEdge(new SimpleEdge<String>(j,k));
		g3.addEdge(new SimpleEdge<String>(h,b));

		assertEquals(g3.numStronglyConnectedComponents(),3);
		//Set<Set<String>> scc = g3.getStronglyConnectedComponents();
		//System.out.println("Strongly Connected Components are " + scc);
		//should be {{a,b,c,d,h,i},{e,f,g},{k,j}}		
	}

	public static final double INF = java.lang.Double.POSITIVE_INFINITY;
	
	@Test
	public void testWeightedPaths() throws NegativeWeightEdgeException, DisconnectedGraphException {
		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> g3 = createWeightedGraph();
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		Integer v7 = new Integer(7);
		Integer v8 = new Integer(8);
		Integer v9 = new Integer(9);
		Integer v10 = new Integer(10);
		g3.addVertex(v0);
		g3.addVertex(v1);
		g3.addVertex(v2);
		g3.addVertex(v3);
		g3.addVertex(v4);
		g3.addVertex(v5);
		g3.addVertex(v6);
		g3.addVertex(v7);
		g3.addVertex(v8);
		g3.addVertex(v9);
		g3.addVertex(v10);	
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v8,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v8,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v0,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v7,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v7,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v0,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v4,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,9.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v5,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v6,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v9,3.5));

		assertEquals("0: (0,7)@6.0\n"+
				"5:\n"+
				"10: (10,9)@3.5\n"+
				"2: (2,3)@8.0 (2,8)@11.0\n"+
				"7: (7,6)@2.0\n"+
				"4: (4,5)@9.0\n"+
				"9:\n"+
				"1: (1,2)@4.0 (1,8)@8.0\n"+
				"6: (6,4)@14.0 (6,5)@10.0\n"+
				"3: (3,0)@2.0 (3,4)@7.0 (3,6)@4.0\n"+
				"8: (8,0)@7.0 (8,7)@1.0\n",
				g3.toString());

		InTree<Integer,SimpleWeightedEdge<Integer>> tree = g3.weightedShortestPaths(v1);
		assertEquals("<(1,2)@4.0, (2,3)@8.0, (3,0)@2.0>", tree.getPathFromSource(v0).toString());
		assertEquals(14.0, tree.getPathDistanceFromSource(v0));
		assertEquals("<>", tree.getPathFromSource(v1).toString());
		assertEquals(0.0, tree.getPathDistanceFromSource(v1));
		assertEquals("<(1,2)@4.0>", tree.getPathFromSource(v2).toString());
		assertEquals(4.0, tree.getPathDistanceFromSource(v2));
		assertEquals("<(1,2)@4.0, (2,3)@8.0>", tree.getPathFromSource(v3).toString());
		assertEquals(12.0, tree.getPathDistanceFromSource(v3));
		assertEquals("<(1,2)@4.0, (2,3)@8.0, (3,4)@7.0>", tree.getPathFromSource(v4).toString());
		assertEquals(19.0, tree.getPathDistanceFromSource(v4));
		assertEquals("<(1,8)@8.0, (8,7)@1.0, (7,6)@2.0, (6,5)@10.0>", tree.getPathFromSource(v5).toString());
		assertEquals(21.0, tree.getPathDistanceFromSource(v5));

		InTree<Integer,SimpleWeightedEdge<Integer>> tree2 = g3.weightedShortestPaths(v8);
		assertEquals("<(8,7)@1.0, (7,6)@2.0>", tree2.getPathFromSource(v6).toString());
		assertEquals(3.0,  tree2.getPathDistanceFromSource(v6));

		assertEquals("<(1,8)@8.0, (8,7)@1.0, (7,6)@2.0>", tree.getPathFromSource(v6).toString());
		assertEquals(11.0, tree.getPathDistanceFromSource(v6));
		assertEquals("<(1,8)@8.0, (8,7)@1.0>", tree.getPathFromSource(v7).toString());
		assertEquals(9.0, tree.getPathDistanceFromSource(v7));
		assertEquals("<(1,8)@8.0>", tree.getPathFromSource(v8).toString());
		assertEquals(8.0, tree.getPathDistanceFromSource(v8));
		assertEquals(null, tree.getPathFromSource(v9));
		assertEquals(INF, tree.getPathDistanceFromSource(v9));
		assertEquals(null, tree.getPathFromSource(v10));
		assertEquals(INF,tree.getPathDistanceFromSource(v10));
	}
	
	@Test
	public void testWeightedPathsWithTemporaryMultiedges() throws NegativeWeightEdgeException, DisconnectedGraphException {
		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> 
			g3 = (WeightedGraph<Integer, SimpleWeightedEdge<Integer>>) createWeightedGraph(true, true);
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		Integer v7 = new Integer(7);
		Integer v8 = new Integer(8);
		Integer v9 = new Integer(9);
		Integer v10 = new Integer(10);
		Integer v11 = new Integer(11);
		g3.addVertex(v0);
		g3.addVertex(v1);
		g3.addVertex(v2);
		g3.addVertex(v3);
		g3.addVertex(v4);
		g3.addVertex(v11);
		g3.addVertex(v5);
		g3.addVertex(v6);
		g3.addVertex(v7);
		g3.addVertex(v8);
		g3.addVertex(v9);
		g3.addVertex(v10);
		SimpleWeightedEdge<Integer> w, x, y, z;
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v8,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,8.0));
		g3.addEdge(w = new SimpleWeightedEdge<Integer>(v2,v3,3.0)); // multi
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v8,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v0,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v7,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v11,1.0)); // to v11
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v7,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v0,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,7.0));
		g3.addEdge(x = new SimpleWeightedEdge<Integer>(v8,v0,2.0)); // multi
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v4,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v11,20.0)); // to v11
		g3.addEdge(new SimpleWeightedEdge<Integer>(v11,v6,20.0)); // from v11
		g3.addEdge(y = new SimpleWeightedEdge<Integer>(v4,v5,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,9.0));
		g3.addEdge(z = new SimpleWeightedEdge<Integer>(v4,v5,9.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v5,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v6,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v9,3.5));
		
		g3.removeEdge(w);
		g3.removeEdge(x);
		g3.removeEdge(y);
		g3.removeEdge(z);
		g3.removeVertex(v11);

		assertEquals("0: (0,7)@6.0\n"+
				"5:\n"+
				"10: (10,9)@3.5\n"+
				"2: (2,3)@8.0 (2,8)@11.0\n"+
				"7: (7,6)@2.0\n"+
				"4: (4,5)@9.0\n"+
				"9:\n"+
				"1: (1,2)@4.0 (1,8)@8.0\n"+
				"6: (6,4)@14.0 (6,5)@10.0\n"+
				"3: (3,0)@2.0 (3,4)@7.0 (3,6)@4.0\n"+
				"8: (8,0)@7.0 (8,7)@1.0\n",
				g3.toString());

		InTree<Integer,SimpleWeightedEdge<Integer>> tree = g3.weightedShortestPaths(v1);
		assertEquals("<(1,2)@4.0, (2,3)@8.0, (3,0)@2.0>", tree.getPathFromSource(v0).toString());
		assertEquals(14.0, tree.getPathDistanceFromSource(v0));
		assertEquals("<>", tree.getPathFromSource(v1).toString());
		assertEquals(0.0, tree.getPathDistanceFromSource(v1));
		assertEquals("<(1,2)@4.0>", tree.getPathFromSource(v2).toString());
		assertEquals(4.0, tree.getPathDistanceFromSource(v2));
		assertEquals("<(1,2)@4.0, (2,3)@8.0>", tree.getPathFromSource(v3).toString());
		assertEquals(12.0, tree.getPathDistanceFromSource(v3));
		assertEquals("<(1,2)@4.0, (2,3)@8.0, (3,4)@7.0>", tree.getPathFromSource(v4).toString());
		assertEquals(19.0, tree.getPathDistanceFromSource(v4));
		assertEquals("<(1,8)@8.0, (8,7)@1.0, (7,6)@2.0, (6,5)@10.0>", tree.getPathFromSource(v5).toString());
		assertEquals(21.0, tree.getPathDistanceFromSource(v5));

		InTree<Integer,SimpleWeightedEdge<Integer>> tree2 = g3.weightedShortestPaths(v8);
		assertEquals("<(8,7)@1.0, (7,6)@2.0>", tree2.getPathFromSource(v6).toString());
		assertEquals(3.0,  tree2.getPathDistanceFromSource(v6));

		assertEquals("<(1,8)@8.0, (8,7)@1.0, (7,6)@2.0>", tree.getPathFromSource(v6).toString());
		assertEquals(11.0, tree.getPathDistanceFromSource(v6));
		assertEquals("<(1,8)@8.0, (8,7)@1.0>", tree.getPathFromSource(v7).toString());
		assertEquals(9.0, tree.getPathDistanceFromSource(v7));
		assertEquals("<(1,8)@8.0>", tree.getPathFromSource(v8).toString());
		assertEquals(8.0, tree.getPathDistanceFromSource(v8));
		assertEquals(null, tree.getPathFromSource(v9));
		assertEquals(INF, tree.getPathDistanceFromSource(v9));
		assertEquals(null, tree.getPathFromSource(v10));
		assertEquals(INF,tree.getPathDistanceFromSource(v10));
		
		try {
			((WeightedGraph) g3).kruskalMST();
			fail("Should throw GraphException");
		} catch (GraphException dge) {
			//OK
		}
		
		try {
			((WeightedGraph) g3).primMST();
			fail("Should throw GraphException");
		} catch (GraphException dge) {
			//OK
		}
	}
	
	@Test
	public void testWeightedPathsWithMultiedges() throws NegativeWeightEdgeException, DisconnectedGraphException {
		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> 
			g3 = (WeightedGraph<Integer, SimpleWeightedEdge<Integer>>) createWeightedGraph(true, true);
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		Integer v7 = new Integer(7);
		Integer v8 = new Integer(8);
		Integer v9 = new Integer(9);
		Integer v10 = new Integer(10);
		Integer v11 = new Integer(11);
		g3.addVertex(v0);
		g3.addVertex(v1);
		g3.addVertex(v2);
		g3.addVertex(v3);
		g3.addVertex(v4);
		g3.addVertex(v11);
		g3.addVertex(v5);
		g3.addVertex(v6);
		g3.addVertex(v7);
		g3.addVertex(v8);
		g3.addVertex(v9);
		g3.addVertex(v10);
		SimpleWeightedEdge<Integer> w, x, y, z;
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v8,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,8.0));
		g3.addEdge(w = new SimpleWeightedEdge<Integer>(v2,v3,12.0)); // multi
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v8,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v0,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v7,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v11,1.0)); // to v11
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v7,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v0,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,7.0));
		g3.addEdge(x = new SimpleWeightedEdge<Integer>(v8,v0, 10.0)); // multi
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v4,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v11,20.0)); // to v11
		g3.addEdge(new SimpleWeightedEdge<Integer>(v11,v6,20.0)); // from v11
		g3.addEdge(y = new SimpleWeightedEdge<Integer>(v4,v5,10.0)); // multi
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,9.0));
		g3.addEdge(z = new SimpleWeightedEdge<Integer>(v4,v5,9.0)); // multi with the same weight
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v5,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v6,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v9,3.5));
		
		g3.removeVertex(v11);

		InTree<Integer,SimpleWeightedEdge<Integer>> tree = g3.weightedShortestPaths(v1);
		assertEquals("<(1,2)@4.0, (2,3)@8.0, (3,0)@2.0>", tree.getPathFromSource(v0).toString());
		assertEquals(14.0, tree.getPathDistanceFromSource(v0));
		assertEquals("<>", tree.getPathFromSource(v1).toString());
		assertEquals(0.0, tree.getPathDistanceFromSource(v1));
		assertEquals("<(1,2)@4.0>", tree.getPathFromSource(v2).toString());
		assertEquals(4.0, tree.getPathDistanceFromSource(v2));
		assertEquals("<(1,2)@4.0, (2,3)@8.0>", tree.getPathFromSource(v3).toString());
		assertEquals(12.0, tree.getPathDistanceFromSource(v3));
		assertEquals("<(1,2)@4.0, (2,3)@8.0, (3,4)@7.0>", tree.getPathFromSource(v4).toString());
		assertEquals(19.0, tree.getPathDistanceFromSource(v4));
		assertEquals("<(1,8)@8.0, (8,7)@1.0, (7,6)@2.0, (6,5)@10.0>", tree.getPathFromSource(v5).toString());
		assertEquals(21.0, tree.getPathDistanceFromSource(v5));

		InTree<Integer,SimpleWeightedEdge<Integer>> tree2 = g3.weightedShortestPaths(v8);
		assertEquals("<(8,7)@1.0, (7,6)@2.0>", tree2.getPathFromSource(v6).toString());
		assertEquals(3.0,  tree2.getPathDistanceFromSource(v6));

		assertEquals("<(1,8)@8.0, (8,7)@1.0, (7,6)@2.0>", tree.getPathFromSource(v6).toString());
		assertEquals(11.0, tree.getPathDistanceFromSource(v6));
		assertEquals("<(1,8)@8.0, (8,7)@1.0>", tree.getPathFromSource(v7).toString());
		assertEquals(9.0, tree.getPathDistanceFromSource(v7));
		assertEquals("<(1,8)@8.0>", tree.getPathFromSource(v8).toString());
		assertEquals(8.0, tree.getPathDistanceFromSource(v8));
		assertEquals(null, tree.getPathFromSource(v9));
		assertEquals(INF, tree.getPathDistanceFromSource(v9));
		assertEquals(null, tree.getPathFromSource(v10));
		assertEquals(INF,tree.getPathDistanceFromSource(v10));
		
		try {
			((WeightedGraph) g3).kruskalMST();
			fail("Should throw GraphException");
		} catch (GraphException dge) {
			//OK
		}
		
		try {
			((WeightedGraph) g3).primMST();
			fail("Should throw GraphException");
		} catch (GraphException dge) {
			//OK
		}
	}
	
	@Test
	public void testSpanningTreeAlgs() throws Exception {

		Graph<String,SimpleEdge<String>> g = createUnweightedGraph(); 
		String a = new String("a");
		String b = new String("b");
		String c = new String("c");
		String d = new String("d");
		String e = new String("e");

		g.addVertex(d);
		g.addVertex(c);
		g.addVertex(b);
		g.addVertex(e);
		g.addVertex(a);
		g.addEdge(new SimpleEdge<String>(a,b));
		g.addEdge(new SimpleEdge<String>(b,c));
		g.addEdge(new SimpleEdge<String>(c,d));
		g.addEdge(new SimpleEdge<String>(d,e));
		g.addEdge(new SimpleEdge<String>(a,c));
		g.addEdge(new SimpleEdge<String>(c,e));
		g.addEdge(new SimpleEdge<String>(b,d));
		g.addEdge(new SimpleEdge<String>(b,e));

		assertEquals("<a, b, c, d, e>", g.topologicalOrder().toString());
		String f = new String("f");
		g.addVertex(f);
		g.addEdge(new SimpleEdge<String>(b,f));
		g.addEdge(new SimpleEdge<String>(f,c));
		assertEquals("<a, b, f, c, d, e>", g.topologicalOrder().toString());
		assertEquals(false,g.hasCycle());
		
		g.addEdge(new SimpleEdge<String>(d,a));
		assertEquals(true,g.hasCycle());
//		assertEquals(("<c, d, a, b, c>" == g.getCycle().toString()) || ("<c, d, a, c>" == g.getCycle().toString()),true);
//		assertEquals("<c, d, a, c>", g.getCycle().toString());

		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> g3 =
			(WeightedGraph<Integer,SimpleWeightedEdge<Integer>>) createWeightedGraph(false,true);
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		Integer v7 = new Integer(7);
		Integer v8 = new Integer(8);
		Integer v9 = new Integer(9);
		Integer v10 = new Integer(10);
		g3.addVertex(v0);
		g3.addVertex(v1);
		g3.addVertex(v2);
		g3.addVertex(v3);
		g3.addVertex(v4);
		g3.addVertex(v5);
		g3.addVertex(v6);
		g3.addVertex(v7);
		g3.addVertex(v8);
		g3.addVertex(v9);
		g3.addVertex(v10);	
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v8,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v8,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v0,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v7,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v7,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v0,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v4,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,9.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v5,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v6,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v9,3.5));

		assertEquals(2, g3.numConnectedComponents());
		g3.addEdge(new SimpleWeightedEdge<Integer>(v9,v8,4.5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v10,8.5));
		assertEquals(1, g3.numConnectedComponents());
	
		Set<SimpleWeightedEdge<Integer>> mst = g3.kruskalMST();
		Iterator<SimpleWeightedEdge<Integer>> edges = mst.iterator();
		double weight = 0.0;
		while (edges.hasNext()) {
			SimpleWeightedEdge<Integer> nextEdge = edges.next();
			weight += nextEdge.weight();
		}
		assertEquals(45.0,weight);

		//		want an undirected graph
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v1,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v1,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v2,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v2,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v8,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v8,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v0,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v3,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v3,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v3,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v6,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v5,v4,9.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v5,v6,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v7,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v9,v10,3.5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v9,4.5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v0,8.5));
		Set<SimpleWeightedEdge<Integer>> mst2 = g3.primMST();
		edges = mst2.iterator();
		weight = 0.0;
		while (edges.hasNext()) {
			SimpleWeightedEdge<Integer> nextEdge = edges.next();
			weight += nextEdge.weight();
		}
		assertEquals(45.0,weight);
	}
	
	@Test
	public void testSpanningTreeAlgsWithMultiEdges() throws Exception {

		Graph<String,SimpleEdge<String>> g = createUnweightedGraph(true, true); 
		String a = new String("a");
		String b = new String("b");
		String c = new String("c");
		String d = new String("d");
		String e = new String("e");

		g.addVertex(d);
		g.addVertex(c);
		g.addVertex(b);
		g.addVertex(e);
		g.addVertex(a);
		g.addEdge(new SimpleEdge<String>(a,b));
		g.addEdge(new SimpleEdge<String>(b,c));
		g.addEdge(new SimpleEdge<String>(b,c)); // multi
		g.addEdge(new SimpleEdge<String>(c,d));
		g.addEdge(new SimpleEdge<String>(d,e));
		g.addEdge(new SimpleEdge<String>(a,c));
		g.addEdge(new SimpleEdge<String>(a,c)); // multi
		g.addEdge(new SimpleEdge<String>(c,e));
		g.addEdge(new SimpleEdge<String>(c,e)); // multi
		g.addEdge(new SimpleEdge<String>(c,e)); // multi
		g.addEdge(new SimpleEdge<String>(b,d));
		g.addEdge(new SimpleEdge<String>(b,e));
		g.addEdge(new SimpleEdge<String>(b,e)); // multi
		g.addEdge(new SimpleEdge<String>(b,d)); // multi

		assertEquals("<a, b, c, d, e>", g.topologicalOrder().toString());
		String f = new String("f");
		g.addVertex(f);
		g.addEdge(new SimpleEdge<String>(b,f));
		g.addEdge(new SimpleEdge<String>(f,c));
		assertEquals("<a, b, f, c, d, e>", g.topologicalOrder().toString());
		assertEquals(false,g.hasCycle());
		
		g.addEdge(new SimpleEdge<String>(d,a));
		assertEquals(true,g.hasCycle());
//		assertEquals(("<c, d, a, b, c>" == g.getCycle().toString()) || ("<c, d, a, c>" == g.getCycle().toString()),true);
//		assertEquals("<c, d, a, c>", g.getCycle().toString());

		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> g3 =
			(WeightedGraph<Integer,SimpleWeightedEdge<Integer>>) createWeightedGraph(false,true);
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		Integer v7 = new Integer(7);
		Integer v8 = new Integer(8);
		Integer v9 = new Integer(9);
		Integer v10 = new Integer(10);
		g3.addVertex(v0);
		g3.addVertex(v1);
		g3.addVertex(v2);
		g3.addVertex(v3);
		g3.addVertex(v4);
		g3.addVertex(v5);
		g3.addVertex(v6);
		g3.addVertex(v7);
		g3.addVertex(v8);
		g3.addVertex(v9);
		g3.addVertex(v10);	
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v8,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v8,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v0,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v7,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v7,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v0,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v4,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,9.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v5,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v6,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v9,3.5));

		assertEquals(2, g3.numConnectedComponents());
		g3.addEdge(new SimpleWeightedEdge<Integer>(v9,v8,4.5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v10,8.5));
		assertEquals(1, g3.numConnectedComponents());
	
		Set<SimpleWeightedEdge<Integer>> mst = g3.kruskalMST();
		Iterator<SimpleWeightedEdge<Integer>> edges = mst.iterator();
		double weight = 0.0;
		while (edges.hasNext()) {
			SimpleWeightedEdge<Integer> nextEdge = edges.next();
			weight += nextEdge.weight();
		}
		assertEquals(45.0,weight);

		//		want an undirected graph
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v1,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v1,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v2,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v2,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v8,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v8,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v0,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v3,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v3,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v3,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v6,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v5,v4,9.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v5,v6,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v7,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v9,v10,3.5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v9,4.5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v0,8.5));
		Set<SimpleWeightedEdge<Integer>> mst2 = g3.primMST();
		edges = mst2.iterator();
		weight = 0.0;
		while (edges.hasNext()) {
			SimpleWeightedEdge<Integer> nextEdge = edges.next();
			weight += nextEdge.weight();
		}
		assertEquals(45.0,weight);
	}
	
	@Test
	public void testSpanningTreeDisconnectedException() throws Exception {
		final WeightedGraph<Integer,SimpleWeightedEdge<Integer>> g3 =
			createWeightedGraph(false,true);
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		Integer v7 = new Integer(7);
		Integer v8 = new Integer(8);
		Integer v9 = new Integer(9);
		Integer v10 = new Integer(10);
		g3.addVertex(v0);
		g3.addVertex(v1);
		g3.addVertex(v2);
		g3.addVertex(v3);
		g3.addVertex(v4);
		g3.addVertex(v5);
		g3.addVertex(v6);
		g3.addVertex(v7);
		g3.addVertex(v8);
		g3.addVertex(v9);
		g3.addVertex(v10);	
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v8,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v8,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v0,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v7,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v7,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v0,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v4,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,9.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v5,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v6,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v9,3.5));

		assertEquals(2, g3.numConnectedComponents());
		try {
			g3.kruskalMST();
			fail("Should throw DisconnectedGraphException");
		} catch (DisconnectedGraphException dge) {
			//OK
		}
		
		try {
			((WeightedGraph) g3).primMST();
			fail("Should throw DisconnectedGraphException");
		} catch (DisconnectedGraphException dge) {
			//OK
		}
	}
	
	@Test
	public void testSpanningTreeNegativeWeightEdgeException() throws Exception {
		final WeightedGraph<Integer,SimpleWeightedEdge<Integer>> g3 = createWeightedGraph(false,true);
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		Integer v7 = new Integer(7);
		Integer v8 = new Integer(8);
		Integer v9 = new Integer(9);
		Integer v10 = new Integer(10);
		g3.addVertex(v0);
		g3.addVertex(v1);
		g3.addVertex(v2);
		g3.addVertex(v3);
		g3.addVertex(v4);
		g3.addVertex(v5);
		g3.addVertex(v6);
		g3.addVertex(v7);
		g3.addVertex(v8);
		g3.addVertex(v9);
		g3.addVertex(v10);	
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v1,v8,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v8,11.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,8.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v0,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v8,v7,1.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v7,6.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v0,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,7.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,4.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v4,14.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,9.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v6,v5,10.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v7,v6,2.0));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v10,v9,3.5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v9,v8,-4.5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(v0,v10,8.5));

		assertEquals(1, g3.numConnectedComponents());
	
		try {
			g3.kruskalMST();
			fail("Should throw NegativeWeightEdgeException");
		} catch (NegativeWeightEdgeException dge) {
			//OK
		}
		
		try {
			((WeightedGraph) g3).primMST();
			fail("Should throw NegativeWeightEdgeException");
		} catch (NegativeWeightEdgeException dge) {
			//OK
		}
		
	}
	
	@Test
	public void testMaxFlow() throws Exception {
		WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>> g4 = 
			new WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>>();
	
		Integer s = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer t = new Integer(5);
		g4.addVertex(s);
		g4.addVertex(v1);
		g4.addVertex(v2);
		g4.addVertex(v3);
		g4.addVertex(v4);
		g4.addVertex(t);
		
		g4.addEdge(new SimpleWeightedEdge<Integer>(s,v1,16));
		g4.addEdge(new SimpleWeightedEdge<Integer>(s,v2,13));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v2,v4,14));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v2,v1,4));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,10));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v3,12));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v3,t,20));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v4,v3,7));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v3,v2,9));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v4,t,4));
		FlowGraph maxFlow = g4.maximumFlow(s,t);
//		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> maxFlow = g4.maximumFlow(s,t);
//		System.out.println("The flow graph is: \n" + maxFlow);
		
		double flowAmount = 0.0;
		Iterator<SimpleWeightedEdge<Integer>> edgesFromSource = maxFlow.edgesFrom(s);     
		while (edgesFromSource.hasNext()) 
			flowAmount += edgesFromSource.next().weight();
		assertEquals(23.0,flowAmount);
	}

	@Test
	public void testMaxFlow2() throws Exception {
		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> 
				g3 = (WeightedGraph<Integer, SimpleWeightedEdge<Integer>>) createWeightedGraph();
		Integer a= new Integer(0);
		Integer b = new Integer(1);
		Integer c = new Integer(2);
		Integer d = new Integer(3);
		Integer e = new Integer(4);
		Integer f = new Integer(5);
		Integer g = new Integer(6);
		Integer h = new Integer(7);
		Integer i = new Integer(8);
		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);
		g3.addVertex(f);
		g3.addVertex(g);
		g3.addVertex(h);
		g3.addVertex(i);	
		g3.addEdge(new SimpleWeightedEdge<Integer>(b,c,4));
		g3.addEdge(new SimpleWeightedEdge<Integer>(i,b,8));
		g3.addEdge(new SimpleWeightedEdge<Integer>(i,c,11));
		g3.addEdge(new SimpleWeightedEdge<Integer>(c,d,8));
		g3.addEdge(new SimpleWeightedEdge<Integer>(a,i,7));
		g3.addEdge(new SimpleWeightedEdge<Integer>(a,h,6));
		g3.addEdge(new SimpleWeightedEdge<Integer>(h,i,1));
		g3.addEdge(new SimpleWeightedEdge<Integer>(h,g,6));
		g3.addEdge(new SimpleWeightedEdge<Integer>(f,g,10));
		g3.addEdge(new SimpleWeightedEdge<Integer>(g,e,2));
		g3.addEdge(new SimpleWeightedEdge<Integer>(d,e,12));
		g3.addEdge(new SimpleWeightedEdge<Integer>(e,f,9));
		g3.addEdge(new SimpleWeightedEdge<Integer>(a,d,2));
		g3.addEdge(new SimpleWeightedEdge<Integer>(d,g,4));
		g3.addEdge(new SimpleWeightedEdge<Integer>(f,d,3));
		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> maxFlow = g3.maximumFlow(a,d);
//		System.out.println("The flow graph is: \n" + maxFlow);
		
		double flowAmount = 0.0;
		Iterator<SimpleWeightedEdge<Integer>> edgesFromSource = maxFlow.edgesFrom(a);     
		while (edgesFromSource.hasNext()) 
			flowAmount += edgesFromSource.next().weight();
		assertEquals(12.0,flowAmount);
	}
	
	@Test
	public void testMaxFlow3() throws Exception {
		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> 
			g3 = (WeightedGraph<Integer, SimpleWeightedEdge<Integer>>) createWeightedGraph();
		Integer s = new Integer(0);
		Integer a = new Integer(1);
		Integer b = new Integer(2);
		Integer c = new Integer(3);
		Integer d = new Integer(4);
		Integer e = new Integer(5);
		Integer f = new Integer(6);
		Integer t = new Integer(7);
		g3.addVertex(a);
		g3.addVertex(b);
		g3.addVertex(c);
		g3.addVertex(d);
		g3.addVertex(e);
		g3.addVertex(f);
		g3.addVertex(s);
		g3.addVertex(t);	
		
		g3.addEdge(new SimpleWeightedEdge<Integer>(s,b,3));
		g3.addEdge(new SimpleWeightedEdge<Integer>(s,a,2));
		g3.addEdge(new SimpleWeightedEdge<Integer>(a,d,2));
		g3.addEdge(new SimpleWeightedEdge<Integer>(d,t,2));
		g3.addEdge(new SimpleWeightedEdge<Integer>(b,f,8));
		g3.addEdge(new SimpleWeightedEdge<Integer>(f,d,4));
		g3.addEdge(new SimpleWeightedEdge<Integer>(a,c,3));
		g3.addEdge(new SimpleWeightedEdge<Integer>(c,e,7));
		g3.addEdge(new SimpleWeightedEdge<Integer>(e,t,6));

		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> maxFlow = g3.maximumFlow(s,t);
		//System.out.println("The flow graph is: \n" + maxFlow);
		
		double flowAmount = 0.0;
		Iterator<SimpleWeightedEdge<Integer>> edgesFromSource = maxFlow.edgesFrom(s);     
		while (edgesFromSource.hasNext()) 
			flowAmount += edgesFromSource.next().weight();
		assertEquals(4.0,flowAmount);
	}
	
	@Test
	public void testMaxFlow4() throws Exception {
		WeightedGraph<Integer,SimpleWeightedEdge<Integer>>
				g3 = (WeightedGraph<Integer, SimpleWeightedEdge<Integer>>) createWeightedGraph();
		Integer s = new Integer(0);
		Integer vd = new Integer(1); //d
		Integer va = new Integer(2); //a
		Integer ve = new Integer(3); //e
		Integer vf = new Integer(4); //f
		Integer vc = new Integer(5); //c
		Integer vb = new Integer(6); //b
		Integer t = new Integer(7);
		g3.addVertex(vd);
		g3.addVertex(va);
		g3.addVertex(ve);
		g3.addVertex(vf);
		g3.addVertex(vc);
		g3.addVertex(vb);
		g3.addVertex(s);
		g3.addVertex(t);	
		
		g3.addEdge(new SimpleWeightedEdge<Integer>(s,va,5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(s,vd,2));
		g3.addEdge(new SimpleWeightedEdge<Integer>(vd,vf,6));
		g3.addEdge(new SimpleWeightedEdge<Integer>(vf,t,2));
		g3.addEdge(new SimpleWeightedEdge<Integer>(va,vb,8));
		g3.addEdge(new SimpleWeightedEdge<Integer>(vb,vf,4));
		g3.addEdge(new SimpleWeightedEdge<Integer>(vd,ve,5));
		g3.addEdge(new SimpleWeightedEdge<Integer>(ve,vc,1));
		g3.addEdge(new SimpleWeightedEdge<Integer>(vc,t,7));

		WeightedGraph<Integer,SimpleWeightedEdge<Integer>> maxFlow = g3.maximumFlow(s,t);
		//System.out.println("The flow graph is: \n" + maxFlow);
		
		double flowAmount = 0.0;
		Iterator<SimpleWeightedEdge<Integer>> edgesFromSource = maxFlow.edgesFrom(s);     
		while (edgesFromSource.hasNext()) 
			flowAmount += edgesFromSource.next().weight();
		assertEquals(3.0,flowAmount);
	}
	
	@Test
	public void testBellmanFord() throws Exception {
		WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>> g4 = 
			new WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>>();
	
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		g4.addVertex(v0);
		g4.addVertex(v1);
		g4.addVertex(v2);
		g4.addVertex(v3);
		g4.addVertex(v4);
		
		g4.addEdge(new SimpleWeightedEdge<Integer>(v0,v1,6));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v0,v2,7));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v3,5));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v3,v1,-2));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v4,v3,7));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v4,v0,2));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v2,v4,9));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v4,-4));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,-3));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,8));
		
		try {
			((WeightedGraph) g4).weightedShortestPaths(v0);
			fail("Should throw NegativeWeightEdgeException");
		} catch (NegativeWeightEdgeException dge) {
			//OK
		}
		
		InTree<Integer,SimpleWeightedEdge<Integer>> tree4 = g4.generalShortestPathFromSource(v0);
		assertEquals("<>",tree4.getPathFromSource(v0).toString());
		assertEquals(0.0, tree4.getPathDistanceFromSource(v0));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,1)@-2.0>", tree4.getPathFromSource(v1).toString());
		assertEquals(2.0, tree4.getPathDistanceFromSource(v1));
		assertEquals("<(0,2)@7.0>", tree4.getPathFromSource(v2).toString());
		assertEquals(7.0, tree4.getPathDistanceFromSource(v2));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0>", tree4.getPathFromSource(v3).toString());
		assertEquals(4.0, tree4.getPathDistanceFromSource(v3));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,1)@-2.0, (1,4)@-4.0>", tree4.getPathFromSource(v4).toString());
		assertEquals(-2.0, tree4.getPathDistanceFromSource(v4));
		
		ShortestPathMatrix<Integer,SimpleWeightedEdge<Integer>> matrix4 = g4.allPairsShortestPaths();
		assertEquals("<>", matrix4.getPath(v0,v0).toString());
		assertEquals(0.0, matrix4.getPathDistance(v0,v0));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,1)@-2.0>", matrix4.getPath(v0,v1).toString());
		assertEquals(2.0, matrix4.getPathDistance(v0,v1));
		assertEquals("<(0,2)@7.0>", matrix4.getPath(v0,v2).toString());
		assertEquals(7.0, matrix4.getPathDistance(v0,v2));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0>", matrix4.getPath(v0,v3).toString());
		assertEquals(4.0, matrix4.getPathDistance(v0,v3));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,1)@-2.0, (1,4)@-4.0>", matrix4.getPath(v0,v4).toString());
		assertEquals(-2.0, matrix4.getPathDistance(v0,v4));
			
		assertEquals(false,matrix4.hasNegativeWeightCycle());	
		g4.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,-10));
		matrix4 = g4.allPairsShortestPaths();
		assertEquals(true,matrix4.hasNegativeWeightCycle());
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,4)@-10.0, (4,0)@2.0>", matrix4.getNegativeWeightCycle().toString());
	}
	
	@Test
	public void testBellmanFordWithMultiEdges() throws Exception {
		WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>> g4 = 
			new WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>>(true, true);
	
		Integer v0 = new Integer(0);
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		g4.addVertex(v0);
		g4.addVertex(v1);
		g4.addVertex(v2);
		g4.addVertex(v3);
		g4.addVertex(v4);
		
		g4.addEdge(new SimpleWeightedEdge<Integer>(v0,v1,6));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v0,v2,7));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v3,5));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v3,6)); // multi
		g4.addEdge(new SimpleWeightedEdge<Integer>(v3,v1,-2));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v3,v1,0)); // multi
		g4.addEdge(new SimpleWeightedEdge<Integer>(v4,v3,8)); // multi
		g4.addEdge(new SimpleWeightedEdge<Integer>(v4,v3,7));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v4,v0,2));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v2,v4,9));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v4,0)); // multi
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v4,-4));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v2,v3,-3));
		g4.addEdge(new SimpleWeightedEdge<Integer>(v1,v2,8));
		
		try {
			((WeightedGraph) g4).weightedShortestPaths(v0);
			fail("Should throw NegativeWeightEdgeException");
		} catch (NegativeWeightEdgeException dge) {
			//OK
		}
		
		InTree<Integer,SimpleWeightedEdge<Integer>> tree4 = g4.generalShortestPathFromSource(v0);
		assertEquals("<>",tree4.getPathFromSource(v0).toString());
		assertEquals(0.0, tree4.getPathDistanceFromSource(v0));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,1)@-2.0>", tree4.getPathFromSource(v1).toString());
		assertEquals(2.0, tree4.getPathDistanceFromSource(v1));
		assertEquals("<(0,2)@7.0>", tree4.getPathFromSource(v2).toString());
		assertEquals(7.0, tree4.getPathDistanceFromSource(v2));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0>", tree4.getPathFromSource(v3).toString());
		assertEquals(4.0, tree4.getPathDistanceFromSource(v3));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,1)@-2.0, (1,4)@-4.0>", tree4.getPathFromSource(v4).toString());
		assertEquals(-2.0, tree4.getPathDistanceFromSource(v4));
		
		ShortestPathMatrix<Integer,SimpleWeightedEdge<Integer>> matrix4 = g4.allPairsShortestPaths();
		assertEquals("<>", matrix4.getPath(v0,v0).toString());
		assertEquals(0.0, matrix4.getPathDistance(v0,v0));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,1)@-2.0>", matrix4.getPath(v0,v1).toString());
		assertEquals(2.0, matrix4.getPathDistance(v0,v1));
		assertEquals("<(0,2)@7.0>", matrix4.getPath(v0,v2).toString());
		assertEquals(7.0, matrix4.getPathDistance(v0,v2));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0>", matrix4.getPath(v0,v3).toString());
		assertEquals(4.0, matrix4.getPathDistance(v0,v3));
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,1)@-2.0, (1,4)@-4.0>", matrix4.getPath(v0,v4).toString());
		assertEquals(-2.0, matrix4.getPathDistance(v0,v4));
			
		assertEquals(false,matrix4.hasNegativeWeightCycle());	
		g4.addEdge(new SimpleWeightedEdge<Integer>(v3,v4,-10));
		matrix4 = g4.allPairsShortestPaths();
		assertEquals(true,matrix4.hasNegativeWeightCycle());
		assertEquals("<(0,2)@7.0, (2,3)@-3.0, (3,4)@-10.0, (4,0)@2.0>", matrix4.getNegativeWeightCycle().toString());
	}
	
	@Test
	public void testFloydWarshall() throws Exception {
		WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>> g5 = 
			new WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>>(); //page 626 of CLRS
	
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		g5.addVertex(v1);
		g5.addVertex(v2);
		g5.addVertex(v3);
		g5.addVertex(v4);
		g5.addVertex(v5);
		g5.addVertex(v6);
		
		g5.addEdge(new SimpleWeightedEdge<Integer>(v1,v5,-1));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v4,v1,-4));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v2,v1,1));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v3,v2,2));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v6,v3,10));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,-8));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v6,v2,5));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v5,v2,7));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,3));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v2,v4,2));
	
		InTree<Integer,SimpleWeightedEdge<Integer>> tree5 = g5.generalShortestPathFromSource(v1);
		assertEquals("<>", tree5.getPathFromSource(v1).toString());
		assertEquals(0.0, tree5.getPathDistanceFromSource(v1));
		assertEquals("<(1,5)@-1.0, (5,2)@7.0>",  tree5.getPathFromSource(v2).toString());
		assertEquals(6.0,  tree5.getPathDistanceFromSource(v2));
		assertEquals(null, tree5.getPathFromSource(v3));
		assertEquals(INF,  tree5.getPathDistanceFromSource(v3));
		assertEquals("<(1,5)@-1.0, (5,2)@7.0, (2,4)@2.0>",  tree5.getPathFromSource(v4).toString());
		assertEquals(8.0,  tree5.getPathDistanceFromSource(v4));
		assertEquals("<(1,5)@-1.0>",  tree5.getPathFromSource(v5).toString());
		assertEquals(-1.0,  tree5.getPathDistanceFromSource(v5));
		assertEquals(null,  tree5.getPathFromSource(v6));
		assertEquals(INF,  tree5.getPathDistanceFromSource(v6));

		tree5 = g5.generalShortestPathFromSource(v3);		
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0, (4,1)@-4.0>", tree5.getPathFromSource(v1).toString());
		assertEquals(-5.0,  tree5.getPathDistanceFromSource(v1));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0>", tree5.getPathFromSource(v2).toString());
		assertEquals(-3.0,  tree5.getPathDistanceFromSource(v2));
		assertEquals("<>", tree5.getPathFromSource(v3).toString());
		assertEquals(0.0,  tree5.getPathDistanceFromSource(v3));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0>", tree5.getPathFromSource(v4).toString());
		assertEquals(-1.0,  tree5.getPathDistanceFromSource(v4));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0, (4,1)@-4.0, (1,5)@-1.0>", tree5.getPathFromSource(v5).toString());
		assertEquals(-6.0,  tree5.getPathDistanceFromSource(v5));
		assertEquals("<(3,6)@-8.0>", tree5.getPathFromSource(v6).toString());
		assertEquals(-8.0, tree5.getPathDistanceFromSource(v6));
		
		ShortestPathMatrix<Integer,SimpleWeightedEdge<Integer>> matrix = g5.allPairsShortestPaths();
		assertEquals("<>", matrix.getPath(v1,v1).toString());
		assertEquals(0.0, matrix.getPathDistance(v1,v1));
		assertEquals("<(1,5)@-1.0, (5,2)@7.0>", matrix.getPath(v1,v2).toString());
		assertEquals(6.0, matrix.getPathDistance(v1,v2));
		assertEquals(null, matrix.getPath(v1,v3));
		assertEquals(INF, matrix.getPathDistance(v1,v3));
		assertEquals("<(1,5)@-1.0, (5,2)@7.0, (2,4)@2.0>", matrix.getPath(v1,v4).toString());
		assertEquals(8.0, matrix.getPathDistance(v1,v4));
		assertEquals("<(1,5)@-1.0>", matrix.getPath(v1,v5).toString());
		assertEquals(-1.0, matrix.getPathDistance(v1,v5));
		assertEquals(null, matrix.getPath(v1,v6));
		assertEquals(INF, matrix.getPathDistance(v1,v6));
		
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0, (4,1)@-4.0>", matrix.getPath(v3,v1).toString());
		assertEquals(-5.0, matrix.getPathDistance(v3,v1));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0>", matrix.getPath(v3,v2).toString());
		assertEquals(-3.0, matrix.getPathDistance(v3,v2));
		assertEquals("<>", matrix.getPath(v3,v3).toString());
		assertEquals(0.0, matrix.getPathDistance(v3,v3));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0>", matrix.getPath(v3,v4).toString());
		assertEquals(-1.0, matrix.getPathDistance(v3,v4));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0, (4,1)@-4.0, (1,5)@-1.0>", matrix.getPath(v3,v5).toString());
		assertEquals(-6.0, matrix.getPathDistance(v3,v5));
		assertEquals("<(3,6)@-8.0>", matrix.getPath(v3,v6).toString());
		assertEquals(-8.0, matrix.getPathDistance(v3,v6));
		assertEquals(g5.numStronglyConnectedComponents(),2);
	}
	
	@Test
	public void testFloydWarshallWithMultiEdges() throws Exception {
		WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>> g5 = 
			new WeightedAdjacencyMatrix<Integer,SimpleWeightedEdge<Integer>>(true, true); //page 626 of CLRS
	
		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		g5.addVertex(v1);
		g5.addVertex(v2);
		g5.addVertex(v3);
		g5.addVertex(v4);
		g5.addVertex(v5);
		g5.addVertex(v6);
		
		g5.addEdge(new SimpleWeightedEdge<Integer>(v1,v5,0)); // multi
		g5.addEdge(new SimpleWeightedEdge<Integer>(v1,v5,-1));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v4,v1,1)); // multi
		g5.addEdge(new SimpleWeightedEdge<Integer>(v4,v1,-4));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v4,v1,10)); // multi
		g5.addEdge(new SimpleWeightedEdge<Integer>(v2,v1,1));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v3,v2,2));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v3,v2,4)); // multi
		g5.addEdge(new SimpleWeightedEdge<Integer>(v6,v3,10));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,-8));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v3,v6,-8)); // multi
		g5.addEdge(new SimpleWeightedEdge<Integer>(v6,v2,5));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v6,v2,5)); // multi
		g5.addEdge(new SimpleWeightedEdge<Integer>(v6,v2,6)); // multi
		g5.addEdge(new SimpleWeightedEdge<Integer>(v5,v2,7));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v4,v5,3));
		g5.addEdge(new SimpleWeightedEdge<Integer>(v2,v4,50)); // multi
		g5.addEdge(new SimpleWeightedEdge<Integer>(v2,v4,2));
	
		InTree<Integer,SimpleWeightedEdge<Integer>> tree5 = g5.generalShortestPathFromSource(v1);
		assertEquals("<>", tree5.getPathFromSource(v1).toString());
		assertEquals(0.0, tree5.getPathDistanceFromSource(v1));
		assertEquals("<(1,5)@-1.0, (5,2)@7.0>",  tree5.getPathFromSource(v2).toString());
		assertEquals(6.0,  tree5.getPathDistanceFromSource(v2));
		assertEquals(null, tree5.getPathFromSource(v3));
		assertEquals(INF,  tree5.getPathDistanceFromSource(v3));
		assertEquals("<(1,5)@-1.0, (5,2)@7.0, (2,4)@2.0>",  tree5.getPathFromSource(v4).toString());
		assertEquals(8.0,  tree5.getPathDistanceFromSource(v4));
		assertEquals("<(1,5)@-1.0>",  tree5.getPathFromSource(v5).toString());
		assertEquals(-1.0,  tree5.getPathDistanceFromSource(v5));
		assertEquals(null,  tree5.getPathFromSource(v6));
		assertEquals(INF,  tree5.getPathDistanceFromSource(v6));

		tree5 = g5.generalShortestPathFromSource(v3);		
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0, (4,1)@-4.0>", tree5.getPathFromSource(v1).toString());
		assertEquals(-5.0,  tree5.getPathDistanceFromSource(v1));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0>", tree5.getPathFromSource(v2).toString());
		assertEquals(-3.0,  tree5.getPathDistanceFromSource(v2));
		assertEquals("<>", tree5.getPathFromSource(v3).toString());
		assertEquals(0.0,  tree5.getPathDistanceFromSource(v3));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0>", tree5.getPathFromSource(v4).toString());
		assertEquals(-1.0,  tree5.getPathDistanceFromSource(v4));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0, (4,1)@-4.0, (1,5)@-1.0>", tree5.getPathFromSource(v5).toString());
		assertEquals(-6.0,  tree5.getPathDistanceFromSource(v5));
		assertEquals("<(3,6)@-8.0>", tree5.getPathFromSource(v6).toString());
		assertEquals(-8.0, tree5.getPathDistanceFromSource(v6));
		
		ShortestPathMatrix<Integer,SimpleWeightedEdge<Integer>> matrix = g5.allPairsShortestPaths();
		assertEquals("<>", matrix.getPath(v1,v1).toString());
		assertEquals(0.0, matrix.getPathDistance(v1,v1));
		assertEquals("<(1,5)@-1.0, (5,2)@7.0>", matrix.getPath(v1,v2).toString());
		assertEquals(6.0, matrix.getPathDistance(v1,v2));
		assertEquals(null, matrix.getPath(v1,v3));
		assertEquals(INF, matrix.getPathDistance(v1,v3));
		assertEquals("<(1,5)@-1.0, (5,2)@7.0, (2,4)@2.0>", matrix.getPath(v1,v4).toString());
		assertEquals(8.0, matrix.getPathDistance(v1,v4));
		assertEquals("<(1,5)@-1.0>", matrix.getPath(v1,v5).toString());
		assertEquals(-1.0, matrix.getPathDistance(v1,v5));
		assertEquals(null, matrix.getPath(v1,v6));
		assertEquals(INF, matrix.getPathDistance(v1,v6));
		
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0, (4,1)@-4.0>", matrix.getPath(v3,v1).toString());
		assertEquals(-5.0, matrix.getPathDistance(v3,v1));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0>", matrix.getPath(v3,v2).toString());
		assertEquals(-3.0, matrix.getPathDistance(v3,v2));
		assertEquals("<>", matrix.getPath(v3,v3).toString());
		assertEquals(0.0, matrix.getPathDistance(v3,v3));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0>", matrix.getPath(v3,v4).toString());
		assertEquals(-1.0, matrix.getPathDistance(v3,v4));
		assertEquals("<(3,6)@-8.0, (6,2)@5.0, (2,4)@2.0, (4,1)@-4.0, (1,5)@-1.0>", matrix.getPath(v3,v5).toString());
		assertEquals(-6.0, matrix.getPathDistance(v3,v5));
		assertEquals("<(3,6)@-8.0>", matrix.getPath(v3,v6).toString());
		assertEquals(-8.0, matrix.getPathDistance(v3,v6));
		assertEquals(g5.numStronglyConnectedComponents(),2);
	}
	
	@Test
	public void testIteratorDirected() {
		final Graph<String,SimpleEdge<String>> g = createUnweightedGraph(true,true); 
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		g.addVertex(a);
		SimpleEdge<String> e1, e2, e3, e4, e5, e6;
		g.addEdge(e1 = new SimpleEdge<String>(a,b));
		g.addEdge(e2 = new SimpleEdge<String>(b,c));
		g.addEdge(e3 = new SimpleEdge<String>(b,d));
		g.addEdge(e4 = new SimpleEdge<String>(b,e));
		g.addEdge(e5 = new SimpleEdge<String>(c,d));
		g.addEdge(e6 = new SimpleEdge<String>(d,c));
		
		final Iterator<SimpleEdge<String>> itA = g.edgesFrom(a);
		try {
			itA.remove();
		} catch (NoSuchElementException ae) {
			// OK
		}
		assertEquals(e1, itA.next());
		
		final Iterator<SimpleEdge<String>> itB = g.edgesFrom(b);
		while (itB.hasNext())
			if ((itB.next() != e3))
				itB.remove();
		
		final Iterator<SimpleEdge<String>> itB2 = g.edgesFrom(b);
		assertEquals(e3, itB2.next());
		
		final Iterator<SimpleEdge<String>> itC = g.edgesFrom(c);
		while (itC.hasNext())
			if (itC.next() == e6)
				fail("should be directed graph");		
	}
	
	@Test
	public void testIteratorUndirected() {
		final Graph<String,SimpleEdge<String>> g = createUnweightedGraph(false,true); 
		g.addVertex(b);
		g.addVertex(c);
		g.addVertex(d);
		g.addVertex(e);
		g.addVertex(a);
		SimpleEdge<String> e1, e2, e3, e4, e5, e6;
		g.addEdge(e1 = new SimpleEdge<String>(a,b));
		g.addEdge(e2 = new SimpleEdge<String>(b,c));
		g.addEdge(e3 = new SimpleEdge<String>(b,d));
		g.addEdge(e4 = new SimpleEdge<String>(b,e));
		g.addEdge(e5 = new SimpleEdge<String>(c,d));
		g.addEdge(e6 = new SimpleEdge<String>(d,c));
		
		final Iterator<SimpleEdge<String>> itA = g.edgesFrom(a);
		try {
			itA.remove();
		} catch (NoSuchElementException nsee) {
			// OK
		}
		assertEquals(e1, itA.next());
		
		final Iterator<SimpleEdge<String>> itB = g.edgesFrom(b);
		while (itB.hasNext())
			if ((itB.next() != e3))
				itB.remove();
		
		final Iterator<SimpleEdge<String>> itB2 = g.edgesFrom(b);
		assertEquals(e3, itB2.next());
		
		final Iterator<SimpleEdge<String>> itD = g.edgesFrom(d);
		while (itD.hasNext())
			if (itD.next() == e5)
				itD.remove();  // remove undirected edge from c to d
		
		final Iterator<SimpleEdge<String>> itC = g.edgesFrom(c);
		assertEquals(e6, itC.next());	
		assertEquals(false, itC.hasNext());
	}


	
}
