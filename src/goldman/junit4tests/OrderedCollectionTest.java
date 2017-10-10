package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import goldman.collection.*;
import goldman.collection.ordered.*;


public abstract class OrderedCollectionTest extends CollectionTest {
	boolean showTrees = false;
	
	protected OrderedCollectionTest() {}	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTracker() {
		Collection<Comparable> t = createTrackedCollection();
		t.add("a");
		t.add("m");
		Locator<String> ktracker = null;
		if (t instanceof Tracked)
			ktracker = ((Tracked) t).addTracked("k");
		else
			t.add("k");
		assertEquals(delimLeft() + 
				"a, k, m" + delimRight(),t.toString());
		if (t instanceof Tracked) {
			ktracker.remove();
			t.add("b");
			t.add("c");
			t.add("d");
			t.add("e");
			ktracker.advance();
			assertEquals(ktracker.get(),"m");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAdvanceAndRemove() {
		Collection<Comparable> t = createCollection();
		t.add(1);
		t.add(9);
		t.add(5);
		t.add(7);
		Locator<String> tracker = null;
		if (t instanceof Tracked)
			tracker = ((Tracked) t).addTracked(3);
		else
			t.add(3);
		assertEquals(delimLeft() + 
				"1, 3, 5, 7, 9" + delimRight(),t.toString());
		if (t instanceof Tracked) {
			t.remove(3);
			t.remove(5);
			t.add(6);
			tracker.advance();
			assertEquals(tracker.get(),7);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetreatAndRemove() {
		Collection<Comparable> t = createCollection();
		t.add("a");
		t.add("m");
		Locator<String> ctracker = null;
		if (t instanceof Tracked)
			ctracker = ((Tracked) t).addTracked("c");
		else
			t.add("c");
		assertEquals(delimLeft() + 
				"a, c, m" + delimRight(),t.toString());
		if (t instanceof Tracked) {
			ctracker.remove();
			t.add("d");
			t.add("e");
			t.add("f");
			t.add("g");
			ctracker.retreat();
			assertEquals(ctracker.get(),"a");
		}
	}
	
	@Test
	public void testAbstraction(){
		Collection<Comparable> t = createCollection();
		//if (t instanceof SearchTree)
		//	((SearchTree) t).showIntermediateSteps(true);
		t.add("a");
		assertEquals(delimLeft() + 
				"a" + delimRight(),t.toString());
		t.add("b");
		assertEquals(delimLeft() + 
				"a, b" + delimRight(),t.toString());
		t.add("s");
		assertEquals(delimLeft() + 
				"a, b, s" + delimRight(),t.toString());
		t.add("t");
		assertEquals(delimLeft() + 
				"a, b, s, t" + delimRight(),t.toString());
		t.add("r");
		assertEquals(delimLeft() + 
				"a, b, r, s, t" + delimRight(),t.toString());
		t.add("a");
		assertEquals(delimLeft() + 
				"a, a, b, r, s, t" + delimRight(),t.toString());
		t.add("c");
		assertEquals(delimLeft() + 
				"a, a, b, c, r, s, t" + delimRight(),t.toString());
		t.add("t");
		assertEquals(delimLeft() + 
				"a, a, b, c, r, s, t, t" + delimRight(),t.toString());
		t.add("i");
		assertEquals(delimLeft() + 
				"a, a, b, c, i, r, s, t, t" + delimRight(),t.toString());
		t.add("o");
		assertEquals(delimLeft() + 
				"a, a, b, c, i, o, r, s, t, t" + delimRight(),t.toString());
		t.add("n");
	
		assertEquals(delimLeft() + 
				"a, a, b, c, i, n, o, r, s, t, t" + delimRight(),t.toString());
		t.remove("c");
		assertEquals(delimLeft() + 
				"a, a, b, i, n, o, r, s, t, t" + delimRight(),t.toString());
		
		assertEquals(true,t.contains("b"));
		
		assertEquals(true,t.contains("r"));
		
		assertEquals(false,t.contains("j"));
		t.remove("i");
		assertEquals(delimLeft() + 
				"a, a, b, n, o, r, s, t, t" + delimRight(),t.toString());
		t.remove("t");
		assertEquals(delimLeft() + 
				"a, a, b, n, o, r, s, t" + delimRight(),t.toString());
	}
	
	@Test
	public void testThree() {
		Collection<Comparable> t = createCollection();
		t.add("b");
		t.add("a");
		t.add("c");
		assertEquals(delimLeft() + "a, b, c" + delimRight(),t.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTracked(){
		Collection<Comparable> t = createCollection();
		if (t instanceof Tracked){
			assertEquals(0, t.getSize());
			Locator btracker = ((Tracked) t).addTracked("b");
			assertEquals(delimLeft() + "b" + delimRight(),t.toString());
			assertEquals(1, t.getSize());
			Locator ctracker = ((Tracked) t).addTracked("c");
			assertEquals(2, t.getSize());
			assertEquals(true, t.contains("b"));
			assertEquals(true, t.contains("c"));
			if (t instanceof OrderedCollection)
				assertEquals(delimLeft() + "b, c" + delimRight(),t.toString());
			assertEquals(true, ctracker.inCollection());
			assertEquals(true, btracker.inCollection());
			t.remove("c");
			assertEquals(false, ctracker.inCollection());
			assertEquals(true, btracker.inCollection());
			assertEquals(true, t.contains("b"));
			assertEquals(false, t.contains("c"));
			assertEquals(delimLeft() + "b" + delimRight(),t.toString());
			assertEquals(1, t.getSize());
			Locator atracker = ((Tracked) t).addTracked("a");
			assertEquals(true, t.contains("b"));
			assertEquals(true, t.contains("a"));
			assertEquals("a", atracker.get());
			assertEquals(2, t.getSize());
			if (t instanceof OrderedCollection)
				assertEquals(delimLeft() + "a, b" + delimRight(),t.toString());
			t.clear();
			atracker.advance();
			atracker.retreat();
			assertEquals(0, t.getSize());
			assertEquals(delimLeft() + delimRight(),t.toString());
		}
	}
	
	@Test
	public void testRem() {
		Collection<Comparable> t = createCollection();
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("g");
		t.add("f");
		assertEquals(false,t.remove("j"));
		t.add("d");
		t.add("e");
		t.add("h");
		assertEquals(delimLeft() + "a, b, c, d, e, f, g, h" + delimRight(),t.toString());
		t.remove("f");
		assertEquals(false,t.contains("k"));
		assertEquals(delimLeft() + "a, b, c, d, e, g, h" + delimRight(),t.toString());
		t.remove("h");
		assertEquals(delimLeft() + "a, b, c, d, e, g" + delimRight(),t.toString());
		t.remove("b");
		assertEquals(delimLeft() + "a, c, d, e, g" + delimRight(),t.toString());
		t.remove("a");
		assertEquals(delimLeft() + "c, d, e, g" + delimRight(),t.toString());
		t.remove("d");
		assertEquals(delimLeft() + "c, e, g" + delimRight(),t.toString());
		t.remove("g");
		assertEquals(delimLeft() + "c, e" + delimRight(),t.toString());
		t.remove("c");
		assertEquals(delimLeft() + "e" + delimRight(),t.toString());
		t.remove("e");
		assertEquals(delimLeft() + "" + delimRight(),t.toString());
	}
	
	@Test
	public void testRemMin(){
		OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("g");
		t.add("f");
		assertEquals(false,t.remove("j"));
		t.add("d");
		t.add("e");
		t.add("h");
		assertEquals(delimLeft() + "a, b, c, d, e, f, g, h" + delimRight(),t.toString());
		t.remove(t.min());
		assertEquals(delimLeft() + "b, c, d, e, f, g, h" + delimRight(),t.toString());
		t.remove(t.min());
		assertEquals(delimLeft() + "c, d, e, f, g, h" + delimRight(),t.toString());
		t.remove(t.min());
		assertEquals(delimLeft() + "d, e, f, g, h" + delimRight(),t.toString());
		t.remove(t.min());
		assertEquals(delimLeft() + "e, f, g, h" + delimRight(),t.toString());
		t.remove(t.min());
		assertEquals(delimLeft() + "f, g, h" + delimRight(),t.toString());
		t.remove(t.min());
		assertEquals(delimLeft() + "g, h" + delimRight(),t.toString());
		t.remove(t.min());
		assertEquals(delimLeft() + "h" + delimRight(),t.toString());
		t.remove(t.min());
		assertEquals(delimLeft() + "" + delimRight(),t.toString());
	}
	
	@Test
	public void testDuplicates() {
		final Collection<Comparable> t = createCollection();
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("g");
		t.add("f");
		t.add("d");
		t.add("b");
		t.add("e");
		t.add("c");
		t.add("h");
		assertEquals(delimLeft() + "a, b, b, c, c, d, e, f, g, h" + delimRight(),t.toString());
		t.remove("f");
		assertEquals(delimLeft() + "a, b, b, c, c, d, e, g, h" + delimRight(),t.toString());
		t.remove("h");
		assertEquals(delimLeft() + "a, b, b, c, c, d, e, g" + delimRight(),t.toString());
		t.remove("b");
		assertEquals(delimLeft() + "a, b, c, c, d, e, g" + delimRight(),t.toString());
		t.remove("a");
		assertEquals(delimLeft() + "b, c, c, d, e, g" + delimRight(),t.toString()); //Triple black nodes at d-e-g along right?
		t.remove("d");

		try {
			t.getLocator("d");
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException nsee) {
			//OK
		}
		assertEquals(delimLeft() + "b, c, c, e, g" + delimRight(),t.toString());
		t.remove("g");
		assertEquals(delimLeft() + "b, c, c, e" + delimRight(),t.toString());
		t.remove("c");
		assertEquals(delimLeft() + "b, c, e" + delimRight(),t.toString());
		t.remove("e");
		assertEquals(delimLeft() + "b, c" + delimRight(),t.toString());
		t.remove("b");
		assertEquals(delimLeft() + "c" + delimRight(),t.toString());
		t.remove("c");
		assertEquals(delimLeft() + "" + delimRight(),t.toString());
	}

	@Test
	public void testFoo() {
		OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		assertEquals(0, t.getSize());
		t.add("c");
		assertEquals(delimLeft() + "c" + delimRight(),t.toString());
		assertEquals(1, t.getSize());
		t.add("d");
		assertEquals(2, t.getSize());
		assertEquals(delimLeft() + "c, d" + delimRight(),t.toString());
		t.remove("d");
		assertEquals(delimLeft() + "c" + delimRight(),t.toString());
		assertEquals(1, t.getSize());
		t.add("b");
		assertEquals(2, t.getSize());
		assertEquals(delimLeft() + "b, c" + delimRight(),t.toString());
		t.remove("b");
		assertEquals(delimLeft() + "c" + delimRight(),t.toString());
		assertEquals("c",t.successor("a"));
		assertEquals("c",t.predecessor("e"));
	}
	
	@Test
	public void testPred() {
		OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("h");
		t.add("g");
		t.add("d");
		t.add("e");
		t.add("i");
		assertEquals(delimLeft() + "a, b, c, d, e, g, h, i" + delimRight(),t.toString());
		assertEquals("a",t.predecessor("b"));
		assertEquals("b",t.predecessor("c"));
		assertEquals("c",t.predecessor("d"));
		assertEquals("d",t.predecessor("e"));
		assertEquals("e",t.predecessor("f"));
		assertEquals("e",t.predecessor("g"));
		assertEquals("g",t.predecessor("h"));
	}
	
	@Test
	public void testDuplicatesTestPred(){
		final OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		t.add("a");
		t.add("c");
		t.add("c");
		t.add("a");
		t.add("c");
		t.add("c");
		t.add("a");
		t.add("c");
		t.add("c");
		t.add("a");
		t.add("c");
		t.add("c");
		assertEquals("a",t.predecessor("b"));
		assertEquals("a",t.predecessor("c"));
		try {
			t.predecessor("a");
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException nsee) {
			//OK
		}
	}
	
	@Test
	public void testLargeDuplicatesTestPredSucc(){
		final OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		for (int i = 0; i < 2; i++){
			t.add("k");
			t.add("c");
			t.add("g");
			t.add("s");
			t.add("o");
			
			t.add("o");
			t.add("k");
			t.add("g");
			t.add("c");
			t.add("s");
		}
		for (int i = 0; i < 2; i++){
			t.add("b");
			t.add("d");
			t.add("f");
			t.add("j");
			t.add("h");
			t.add("l");
			t.add("n");
			t.add("p");
			t.add("r");
			t.add("t");
		}
		assertEquals(delimLeft() + "b, b, c, c, c, c, d, d, f, f, g, g, g, g, h, h, j, j, k, k, k, k, l, l, n, n, o, o, o, o, p, p, r, r, s, s, s, s, t, t" + delimRight(),t.toString());
		t.add("s");
		t.remove("k");
		t.remove("c");
		t.remove("g");
		t.remove("s");
		t.remove("o");
		t.add("a");
		
		assertEquals(delimLeft() + "a, b, b, c, c, c, d, d, f, f, g, g, g, h, h, j, j, k, k, k, l, l, n, n, o, o, o, p, p, r, r, s, s, s, s, t, t" + delimRight(),t.toString());
		assertEquals("a",t.predecessor("b"));
		assertEquals("b",t.predecessor("c"));
		assertEquals("c",t.predecessor("d"));
		assertEquals("d",t.predecessor("e"));
		assertEquals("d",t.predecessor("f"));
		assertEquals("f",t.predecessor("g"));
		assertEquals("g",t.predecessor("h"));
		assertEquals("h",t.predecessor("i"));
		assertEquals("h",t.predecessor("j"));
		assertEquals("j",t.predecessor("k"));
		assertEquals("k",t.predecessor("l"));
		assertEquals("l",t.predecessor("m"));
		assertEquals("l",t.predecessor("n"));
		assertEquals("n",t.predecessor("o"));
		assertEquals("o",t.predecessor("p"));
		assertEquals("p",t.predecessor("q"));
		assertEquals("p",t.predecessor("r"));
		assertEquals("r",t.predecessor("s"));
		assertEquals("s",t.predecessor("t"));
		assertEquals("t",t.predecessor("u"));
		
		try {
			t.predecessor("a");
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException nsee) {
			//OK
		}
		
		assertEquals("b",t.successor("a"));
		assertEquals("c",t.successor("b"));
		assertEquals("d",t.successor("c"));
		assertEquals("f",t.successor("d"));
		assertEquals("f",t.successor("e"));
		assertEquals("g",t.successor("f"));
		assertEquals("h",t.successor("g"));
		assertEquals("j",t.successor("h"));
		assertEquals("j",t.successor("i"));
		assertEquals("k",t.successor("j"));
		assertEquals("l",t.successor("k"));
		assertEquals("n",t.successor("l"));
		assertEquals("n",t.successor("m"));
		assertEquals("o",t.successor("n"));
		assertEquals("p",t.successor("o"));
		assertEquals("r",t.successor("p"));
		assertEquals("r",t.successor("q"));
		assertEquals("s",t.successor("r"));
		assertEquals("t",t.successor("s"));
		
		try {
			t.successor("t");
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException nsee) {
			//OK
		}

		try {
			t.successor("u");
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException nsee) {
			//OK
		}

	}
	
	@Test
	public void testSucc() {
		OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("g");
		t.add("f");
		t.add("d");
		t.add("e");
		t.add("i");
		assertEquals("b",t.successor("a"));
		assertEquals("c",t.successor("b"));
		assertEquals("d",t.successor("c"));
		assertEquals("e",t.successor("d"));
		assertEquals("f",t.successor("e"));
		assertEquals("g",t.successor("f"));
		assertEquals("i",t.successor("g"));
		assertEquals("i",t.successor("h"));
	}
	
	@Test
	public void testDuplicatesTestSucc(){
		final OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		t.add("c");
		t.add("a");
		t.add("a");
		t.add("a");
		t.remove("a");
		t.add("c");
		t.add("c");
		t.add("a");
		t.add("c");
		t.add("c");
		t.add("a");
		t.add("c");
		t.add("c");
		t.add("a");
		t.remove("a");
		t.add("c");
		t.add("c");

		assertEquals("c",t.successor("b"));
		assertEquals("c",t.successor("a"));
		
		try {
			t.successor("c");
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException nsee) {
			//OK
		}
	}
	
	@Test
	public void testSmallSearch() {
		OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		t.add("a");
		t.add("d");
		t.add("e");
		assertEquals(delimLeft() + "a, d, e" + delimRight(),t.toString());
		t.remove("e");
		t.add("b");
		assertEquals(delimLeft() + "a, b, d" + delimRight(),t.toString());
	}
	
	@Test
	public void testLarge(){
		final OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		//String out = delimLeft();
		for (int i = 0; i < 100; i++){
			Integer item = new Integer(i);
			t.add(item);
		}
		Integer item20 = new Integer(20);
		
		try {
			t.getLocator(new Integer(100));
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException nsee) {
			//OK
		}
		
		final Locator<Comparable> loc1 = t.getLocator(item20);
		assertEquals(item20,loc1.get());
		if (t instanceof BTree) { 
			try {
				loc1.remove();
				fail("expected UnsupportedOperationException");
			} catch (UnsupportedOperationException uoe) {
				//OK
			}
			return;
		}
		loc1.remove();
		assertEquals(false, t.contains(item20));
		loc1.advance();
		assertEquals(new Integer(21), loc1.get());
		
		final Locator<Comparable> loc2 = t.getLocator(new Integer(30));
		loc2.remove();
		loc2.retreat();
		loc2.remove();
		loc2.retreat();
		if (t instanceof Tracked)
			assertEquals(new Integer(28), loc2.get());
		else
			assertEquals(new Integer(26), loc2.get());
		
		final Locator<Comparable> loc = t.getLocator(new Integer(50));
		for (int i = 40; i <= 60; i++)
			t.remove(new Integer(i));
		if (t instanceof SortedArray) {
			try {
				loc.retreat();
				fail("expected ConcurrentModificationException");
			} catch (ConcurrentModificationException cme) {
				//OK
			}
			return;
		}
		loc.retreat();
		assertEquals(new Integer(39), loc.get());
		assertEquals(new Integer(61), loc.next());
	}
	

	@Test
	public void testIterator() {
		OrderedCollection<Comparable> t = (OrderedCollection<Comparable>) createCollection();
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("g");
		t.add("f");
		t.add("d");
		t.add("e");
		t.add("h");
		assertEquals("a", t.min());
		final Iterator it = t.iterator();
		assertEquals("a",it.next());
		assertEquals("b",it.next());
		if (t instanceof BTree) { 
			try {
				it.remove();
				fail("expected UnsupportedOperationException");
			} catch (UnsupportedOperationException uoe) {
				//OK
			}
			return;
		}
		it.remove();
		assertEquals(delimLeft() + "a, c, d, e, f, g, h" + delimRight(),t.toString());
		assertEquals("c",it.next());
		assertEquals("d",it.next());
		assertEquals("e",it.next());
		assertEquals("f",it.next());
		it.remove();
		assertEquals(delimLeft() + "a, c, d, e, g, h" + delimRight(),t.toString());
		assertEquals("g",it.next());
		assertEquals("h",it.next());
		it.remove();
		assertEquals(delimLeft() + "a, c, d, e, g" + delimRight(),t.toString());
	}
	
	@Test
	public void testLocator() {
		Collection<Comparable> t = createCollection();
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("g");
		t.add("f");
		t.add("d");
		t.add("e");
		t.add("h");
		Locator<Comparable> loc = t.getLocator("c");
		assertEquals("c",loc.get());
		loc.retreat();
		assertEquals("b",loc.get());
		loc.retreat();
		assertEquals("a",loc.get());
	}
	
	@Test
	public void testMediumRemove() {
		OrderedCollection<Comparable> td = (OrderedCollection<Comparable>) createCollection();
		int numDuplicates = 3;
		for (int i = 0; i < numDuplicates; i++){
			for (int j = 0; j < 5; j++){
				td.add(2*j);
			}
		}
		assertEquals(0,td.min());
		assertEquals(8,td.max());
		
		for (int i = 0; i < 5; i++)
			assertEquals(true,td.contains(2*i));
		
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < numDuplicates-1; j++){
				td.remove(2*i);
				assertEquals(true,td.contains(2*i));
			}
		}
		
		assertEquals(0,td.successor(-1));
		for (int j=0; j < 4; j++){
			assertEquals(2*(j+1),td.successor(2*j));
			assertEquals(2*(j+1),td.successor(2*j+1));
		}
		for (int j=0; j < 4; j++){
			assertEquals(2*j,td.predecessor(2*j+1));
			assertEquals(2*j,td.predecessor(2*j+2));
		}
		assertEquals(8,td.predecessor(9));
		assertEquals(delimLeft() + "0, 2, 4, 6, 8" + delimRight(),td.toString());
	}
	
	@Test
	public void testLargeRemove() {
		OrderedCollection<Comparable> td = (OrderedCollection<Comparable>) createCollection();
		int numDuplicates = 3;
		for (int i = 0; i < numDuplicates; i++){
			for (int j = 0; j < 20; j++){
				td.add(2*j);
			}
		}
		assertEquals(0,td.min());
		assertEquals(38,td.max());
		
		for (int i = 0; i < 20; i++)
			assertEquals(true,td.contains(2*i));
		
		for (int i = 0; i < 20; i++){
			for (int j = 0; j < numDuplicates-1; j++){
				td.remove(2*i);
				assertEquals(true,td.contains(2*i));
			}
		}
		
		assertEquals(0,td.successor(-1));
		for (int j=0; j < 19; j++){
			assertEquals(2*(j+1),td.successor(2*j));
			assertEquals(2*(j+1),td.successor(2*j+1));
		}
		for (int j=0; j < 19; j++){
			assertEquals(2*j,td.predecessor(2*j+1));
			assertEquals(2*j,td.predecessor(2*j+2));
		}
		assertEquals(38,td.predecessor(39));
		assertEquals(delimLeft() + "0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38" + delimRight(),td.toString());
	}
	
	@Test
	public void testLargeRemoveInReverse() {
		OrderedCollection<Comparable> td = (OrderedCollection<Comparable>) createCollection();
		int numDuplicates = 3;
		int numDistinctElements = 20;
		for (int i = 0; i < numDuplicates; i++){
			for (int j = 0; j < numDistinctElements; j++){ 
				td.add(2*j);
			}
		}
		assertEquals(0,td.min());
		assertEquals(2*(numDistinctElements-1),td.max());

		for (int i = 0; i < numDistinctElements; i++)
			assertEquals(true,td.contains(2*i));

		for (int i = numDistinctElements-1; i >= 0; i--){
			for (int j = 0; j < numDuplicates-1; j++){
				td.remove(2*i);
				assertEquals(true,td.contains(2*i));
			}
		}
		
		assertEquals(0,td.successor(-1));
		for (int j=0; j < numDistinctElements-1; j++){
			assertEquals(2*(j+1),td.successor(2*j));
			assertEquals(2*(j+1),td.successor(2*j+1));
		}
		for (int j=0; j < numDistinctElements-1; j++){
			assertEquals(2*j,td.predecessor(2*j+1));
			assertEquals(2*j,td.predecessor(2*j+2));
		}
		assertEquals(2*(numDistinctElements-1),td.predecessor(2*numDistinctElements-1));
		if (numDistinctElements == 20)
			assertEquals(delimLeft() + "0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38" + delimRight(),td.toString());
	}
	
	@Test
	public void testDuplicatesSuccPred() {
		OrderedCollection<Comparable> td = (OrderedCollection<Comparable>) createCollection();
		int numDuplicates = 100;
		int n = 20;
		for (int i = 0; i < numDuplicates; i++){
			for (int j = 0; j < n; j++){
				td.add(2*j);
			}
		}
		assertEquals(0,td.min());
		assertEquals(2*(n-1),td.max());
		assertEquals(0,td.successor(-1));
		for (int j=0; j < n-1; j++){
			assertEquals(2*(j+1),td.successor(2*j));
			assertEquals(2*(j+1),td.successor(2*j+1));
		}
		for (int j=0; j < n-1; j++){
			assertEquals(2*j,td.predecessor(2*j+1));
			assertEquals(2*j,td.predecessor(2*j+2));
		}
		assertEquals(2*(n-1),td.predecessor(2*n-1));
	
		int i = 0;
		int j = 0;
		Locator<Comparable> loc = td.iterator();
		while (loc.advance()) {
			assertEquals(i,loc.get());
			j++;
			if (j == numDuplicates){
				i = i + 2;
				j = 0;
			}
		}
		i = 2*(n-1);
		loc = td.iteratorAtEnd();
		while (loc.retreat()) {
			assertEquals(i,loc.get());
			j++;
			if (j == numDuplicates){
				i = i - 2;
				j = 0;
			}
		}
	}
	
	@Test
	public void testSimple() {
		OrderedCollection<Comparable> t1 = (OrderedCollection<Comparable>) createCollection();
		t1.add("b");
		t1.add("a");
		t1.add("f");
		t1.add("d");
		t1.add("c");
		t1.add("e");
		t1.add("h");
		t1.add("j");
		assertEquals("{a, b, c, d, e, f, h, j}",t1.toString());
		assertEquals("a",t1.min());
		assertEquals("j",t1.max());
		assertEquals("a",t1.predecessor("b"));
		assertEquals("c",t1.predecessor("d"));
		assertEquals("e",t1.predecessor("f"));
		assertEquals("f",t1.predecessor("h"));
		assertEquals("f",t1.predecessor("g"));
		assertEquals("c",t1.successor("b"));
		assertEquals("e",t1.successor("d"));
		assertEquals("h",t1.successor("f"));
		assertEquals("j",t1.successor("h"));
		assertEquals("h",t1.successor("g"));
	}
	
	@Test
	public void testSuccPred() {
		OrderedCollection<Comparable> t2 = (OrderedCollection<Comparable>) createCollection();
		int num = 20;
		for (int i = 0; i < num; i++){
			t2.add(2*i);
		}
		assertEquals(0,t2.min());
		assertEquals(2*(num-1),t2.max());
		assertEquals(0,t2.successor(-1));
		for (int j=0; j < num-1; j++){
			assertEquals(2*(j+1),t2.successor(2*j));
			assertEquals(2*(j+1),t2.successor(2*j+1));
		}
		for (int j=0; j < num-1; j++){
			assertEquals(2*j,t2.predecessor(2*j+1));
			assertEquals(2*j,t2.predecessor(2*j+2));
		}
		assertEquals(2*(num-1),t2.predecessor(2*num-1));
	}

}
