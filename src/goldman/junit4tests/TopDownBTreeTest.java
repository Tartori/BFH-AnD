package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;

import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.ordered.TopDownBTree;

public class TopDownBTreeTest extends BTreeTest {
	
	@Override
	public Collection<Comparable> createCollection() {
		return new TopDownBTree<Comparable>();
	}
	
	@Test
	public void testSuccPred() {
		for (int t = 2; t <= 2 ; t++){ //was t<=100
			TopDownBTree<Comparable> td = new TopDownBTree<Comparable>(t);
			int numDuplicates = 3; //was 10
			int n = 4; //was 20
			for (int i = 0; i < numDuplicates; i++){
				for (int j = 0; j < n; j++){
					td.add(2*j);
				}
			}
			assertEquals(td.min(),0);
			assertEquals(td.max(),2*(n-1));
			assertEquals(td.successor(-1),0);
			for (int j=0; j < n-1; j++){
				assertEquals(td.successor(2*j),2*(j+1));
				assertEquals(td.successor(2*j+1),2*(j+1));
			}
			for (int j=0; j < n-1; j++){
				assertEquals(td.predecessor(2*j+1),2*j);
				assertEquals(td.predecessor(2*j+2),2*j);
			}
			assertEquals(td.predecessor(2*n-1),2*(n-1));
		
			int i = 0;
			int j = 0;
			Locator<Comparable> loc = td.iterator();
			while (loc.advance()) {
				assertEquals(loc.get(),i);
				j++;
				if (j == numDuplicates){
					i = i + 2;
					j = 0;
				}
			}
			i = 2*(n-1);
			loc = td.iteratorAtEnd();
			while (loc.retreat()) {
				assertEquals(loc.get(),i);
				j++;
				if (j == numDuplicates){
					i = i - 2;
					j = 0;
				}
			}
		}
	}


}
