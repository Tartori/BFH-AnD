package goldman.junit4tests;
import static org.junit.Assert.*;
import goldman.partition.UnionFindNode;
import org.junit.Test;

public class UnionFindTest {

	@Test
	public void testUnionFind() {
		UnionFindNode<String> a = new UnionFindNode<String>("a");
		UnionFindNode<String> b = new UnionFindNode<String>("b");
		UnionFindNode<String> c = new UnionFindNode<String>("c");
		UnionFindNode<String> d = new UnionFindNode<String>("d");
		UnionFindNode<String> e = new UnionFindNode<String>("e");
		UnionFindNode<String> f = new UnionFindNode<String>("f");
		
		assertEquals(a, a.findRepresentative());
		assertEquals(b, b.findRepresentative());
		assertEquals(c, c.findRepresentative());
		assertEquals(d, d.findRepresentative());
		assertEquals(e, e.findRepresentative());
		assertEquals(f, f.findRepresentative());
		
		a.union(b);
		c.union(d);
		// Now have {a,b}, {c,d}, {e}, {f}
		UnionFindNode<String> repAB = a.findRepresentative();
		assertEquals(repAB, b.findRepresentative());
		UnionFindNode<String> repCD = d.findRepresentative();
		assertEquals(repCD, c.findRepresentative());
		assertEquals(e, e.findRepresentative());
		assertEquals(f, f.findRepresentative());
		
		b.union(e);
		// Now have {a,b,e}, {c,d}, {f}
		UnionFindNode<String> repABE = b.findRepresentative();
		assertEquals(repABE, a.findRepresentative());
		assertEquals(repABE, e.findRepresentative());
		assertEquals(repCD, c.findRepresentative());
		assertEquals(repCD, d.findRepresentative());
		assertEquals(f, f.findRepresentative());

		c.union(b);
		// Now have {a,b,e,c,d}, {f}
		UnionFindNode<String> repABECD = e.findRepresentative();
		assertEquals(repABECD, a.findRepresentative());
		assertEquals(repABECD, b.findRepresentative());
		assertEquals(repABECD, c.findRepresentative());
		assertEquals(repABECD, d.findRepresentative());
		assertEquals(repABECD, e.findRepresentative());
		assertEquals(f, f.findRepresentative());

		a.union(f);
		UnionFindNode<String> repAll = b.findRepresentative();
		assertEquals(repAll, a.findRepresentative());
		assertEquals(repAll, b.findRepresentative());
		assertEquals(repAll, c.findRepresentative());
		assertEquals(repAll, d.findRepresentative());
		assertEquals(repAll, e.findRepresentative());
		assertEquals(repAll, f.findRepresentative());
	}

}
