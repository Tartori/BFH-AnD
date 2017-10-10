package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import goldman.collection.*;
import goldman.collection.ordered.BTree;
import goldman.collection.ordered.BinarySearchTree;
import goldman.collection.ordered.BPlusTree;
import goldman.collection.ordered.OrderedCollection;
import goldman.collection.positional.Array;
import goldman.collection.positional.DynamicArray;
import goldman.collection.priority.BinaryHeap;
import goldman.collection.priority.LeftistHeap;
import goldman.collection.priority.PriorityQueue;
import goldman.collection.set.Set;
import goldman.collection.tagged.set.Mapping;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * An abstract JUnit test written in terms of the collection interface,
 * this class is intended to be extended by other tests for specific implementations.
 * These example tests place a heavy emphasis on boundary cases, and check that
 * certain exceptions are properly thrown.  However, no test suite is exhaustive, so
 * passing all tests is not a guarantee that any implementation is correct.
 * 
 * @author kjg
 */
public abstract class CollectionTest {
	
	protected CollectionTest() {}
	
	// The purpose of this abstract factory method is to create the collection under test.
	public abstract Collection<Comparable> createCollection();
	
	// An alternative abstract factory method takes an initial capacity for the collection.
	public Collection<Comparable> createCollection(int size) {
		return createCollection(); // ignore size by default
	}
	
	// Some tests require a separate method to create a tracked version of the collection.
	public Collection<Comparable> createTrackedCollection(){
		return createCollection(); 
	}
	
	// Subclasses may override these delimeters as appropriate for the classes under test.
	public String delimLeft() { return "{"; }
	public String delimRight() { return "}"; }
	
	protected void checkRep(Collection c) {
		// Subclasses may override this method to throw an exception of the rep invariants are violated.
		// Tests may call this method after operations to check the internal representation.
	}
	
	@Test
	public void testEmpty() {
		Collection<Comparable> a = createCollection();
		assertEquals(0, a.getSize());
		assertEquals(delimLeft() + delimRight(), a.toString());
		assertEquals(false, a.iterator().hasNext());
	}
	
	@Test
	public void testRemoveFromEmpty() {
		Collection<Comparable> a = createCollection();
		assertEquals(false, a.remove("b"));
	}
	
	@Test
	public void testGetEquivalentElement() {
		Collection<Comparable> a = createCollection();
		assertEquals(false, a.remove("b"));
		a.add("a"+"b");
		assertEquals(true, "ab".equals(a.getEquivalentElement("ab")));
		a.add("ac");
		assertEquals(true,a.remove("ab"));
		assertEquals(false,a.remove("ab"));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testNoSuchElement() {
		Collection<Comparable> a = createCollection();
		assertEquals(false, a.remove("b"));
		a.add("a"+"b");
		a.getEquivalentElement("a");
	}
	
	@Test
	public void testOne() {
		Collection<Comparable> t = createCollection();
		t.add("b");
		assertEquals(delimLeft() + "b" + delimRight(), t.toString());
	}
	
	@Test
	public void testEquivalent() {
		Collection<Comparable> t = createCollection();
		t.add("abc");
		assertEquals(true,t.contains("a"+"b"+"c"));
	}
	
	@Test
	public void testSize() {
		Collection<Comparable> t = createCollection();
		assertEquals(0,t.getSize());
		t.add("b");
		assertEquals(delimLeft() + "b" + delimRight(), t.toString());
		assertEquals(1,t.getSize());
		t.add("c");
		assertEquals(2,t.getSize());
		if (t instanceof Set) {
			t.add("c");
			assertEquals(2,t.getSize());
		}
		assertEquals(true, t.contains("b"));
		assertEquals(true, t.contains("c"));
		assertEquals("b", AbstractCollection.getElementAtRank(t,0));
		assertEquals("c", AbstractCollection.getElementAtRank(t,1));
		if (t instanceof OrderedCollection)
			assertEquals(t.toString(),delimLeft() + "b, c" + delimRight());
		boolean returnVal = t.remove("c");
		assertEquals(true, returnVal);
		assertEquals(false, t.remove("c"));
		assertEquals(true, t.contains("b"));
		assertEquals(false, t.contains("c"));
		assertEquals(delimLeft() + "b" + delimRight(), t.toString());
		assertEquals(1,t.getSize());
		t.add("a");
		assertEquals(true, t.contains("b"));
		assertEquals(true, t.contains("a"));
		assertEquals(2,t.getSize());
		if (t instanceof OrderedCollection)
			assertEquals(delimLeft() + "a, b" + delimRight(), t.toString());
		t.clear();
		assertEquals(0,t.getSize());
		assertEquals(false, t.contains("b"));
		assertEquals(false, t.contains("a"));
		assertEquals(false, t.contains("c"));
		assertEquals(delimLeft() + delimRight(), t.toString());
	}
	
	@Test
	public void testContains() {
		Collection<Comparable> t = createCollection();
		assertEquals(false, t.contains("b"));
		t.add("b");
		assertEquals(delimLeft() + "b" + delimRight(), t.toString());
		assertEquals(true, t.contains("b"));
		assertEquals(false, t.contains("c"));
		
		t.add("c");
		assertEquals(true, t.contains("c"));
		if (t instanceof OrderedCollection)
			assertEquals(delimLeft() + "b, c" + delimRight(), t.toString());
		
		t.remove("c");
		assertEquals(delimLeft() + "b" + delimRight(), t.toString());
		assertEquals(true, t.contains("b"));
		assertEquals(false, t.contains("c"));

		t.add("a");
		if (t instanceof OrderedCollection)
			assertEquals(delimLeft() + "a, b" + delimRight(), t.toString());
		assertEquals(true, t.contains("a"));
		assertEquals(true, t.contains("b"));
		assertEquals(false, t.contains("c"));
		
		t.clear();
		assertEquals(t.toString(),delimLeft() + "" + delimRight());
		assertEquals(false, t.contains("a"));
		assertEquals(false, t.contains("b"));
		assertEquals(false, t.contains("c"));
	}

	@Test
	public void testClearWithIterator() {
		final Collection<Comparable> t = createCollection(21);
		t.add(5);
		t.add(2);
		t.add(7);
		Locator<Comparable> loc = t.iterator();
		if (loc instanceof AbstractCollection.VisitingIterator)
			return;
		loc.advance();
		t.clear();
		assertEquals(0, t.getSize());
		
		// Check for expected exceptions:
		try {
			if (loc.inCollection())
				fail("Should return false or have ConcurrentModificationException");	
		} catch (ConcurrentModificationException cme) {
			//OK
		}
		try {
			if (loc.advance())   //should not give at Boundary Exception (instead returns false)
				fail("Should return false or have ConcurrentModificationException");	
		} catch (ConcurrentModificationException cme) {
			//OK
		}
	}
	
	@Test
	public void testClearFollowedByAdd() {
		final Collection<Comparable> t = createCollection(21);
		t.add(5);
		t.add(2);
		t.add(7);
		Locator<Comparable> loc = t.iterator();
		if (loc instanceof AbstractCollection.VisitingIterator)
			return;
		while (loc.advance());
		t.clear();
		assertEquals(0, t.getSize());
		t.add(0);
		try {
			loc.retreat();   //if clear was critical mutator, loc is invalidated
		} catch (ConcurrentModificationException cme) {
			return;
		}
		assertEquals(0, loc.get());
	}
	
	@Test
	public void testVisitIsInIterationOrder() {
		final Collection<Comparable> t = createCollection();
		for (int i = 0; i < 8; i+=2) {
			t.add(i);
		}
		for (int i = 1; i < 8; i+=2) {
			t.add(i);
		}
		t.accept(new Visitor<Comparable>() {
			Locator<Comparable> loc = t.iterator();
			public void visit(Comparable c) {
				assertEquals(loc.next(), c);
			}
		});
	}
	
	@Test
	public void testIterationOrderWhenRetreating() {
		final Collection<Comparable> t = createCollection(21);
		t.add(5);
		t.add(2);
		t.add(7);
		t.add(1);
		t.add(3);
		t.add(4);
		t.add(6);
		t.add(9);
		Locator<Comparable> forward, backward;
		forward = t.iterator();
		Array<Comparable> order = new Array<Comparable>(10);
		while (forward.hasNext())
			order.add(forward.next());
		assertEquals(8, order.getSize());
		backward = t.iterator();
		if (backward instanceof AbstractCollection.VisitingIterator)
			return;
		while (backward.advance());
		int i = 7;
		while (backward.retreat()) {
			assertEquals(order.get(i--), backward.get());
		}
	}
	
	
	@Test
	public void testIterationFollowingRemoveAfterIterator() {
		final Collection<Comparable> t = createCollection();
		if (t instanceof PriorityQueue)
			return;
		for (int i = 0; i < 8; i++) {
			t.add(i);
		}
		Locator<Comparable> loc = t.iterator();
		loc.advance();
		loc.advance();
		Comparable element1 = loc.get();
		loc.advance();
		Comparable element2 = loc.get();
		loc.advance();
		Comparable element3 = loc.get();
		loc.retreat();
		loc.retreat();
		loc.retreat();
		t.remove(element1);
		t.remove(element2);
		try {
			loc.advance();   //an exception should occur if remove is critical
		} catch (ConcurrentModificationException cme) {
			return;
		}
		assertEquals(element3, loc.get());
	}
	
	@Test
	public void testIterationFollowingRemoveAtIterator() {
		final Collection<Comparable> t = createCollection();
		if (t instanceof PriorityQueue)
			return;
		for (int i = 0; i < 8; i++) {
			t.add(i);
		}
		Locator<Comparable> loc = t.iterator();
		loc.advance();
		loc.advance();
		Comparable element1 = loc.get();
		loc.advance();
		Comparable element2 = loc.get();
		loc.advance();
		Comparable element3 = loc.get();
		loc.retreat();
		loc.retreat();
		t.remove(element1);
		t.remove(element2);
		try {
			loc.advance();   //should occur if remove is critical
		} catch (ConcurrentModificationException cme) {
			return;
		}
		assertEquals(element3, loc.get());
	}
	
	
	@Test
	public void testRetreatFollowingRemoveAtIterator() {
		final Collection<Comparable> t = createCollection();
		if (t instanceof PriorityQueue)
			return;
		for (int i = 0; i < 8; i++) {
			t.add(i);
		}
		Locator<Comparable> loc = t.iterator();
		loc.advance();
		Comparable element0 = loc.get();
		loc.advance();
		Comparable element1 = loc.get();
		loc.advance();
		Comparable element2 = loc.get();
		loc.advance();
		Comparable element3 = loc.get();
		t.remove(element1);
		t.remove(element2);
		t.remove(element3);
		try {
			loc.retreat();   //exception should occur if remove is critical
		} catch (ConcurrentModificationException cme) {
			return;
		}
		if (t instanceof Tracked)		
			assertEquals(element0, loc.get());
		else
			try {
				loc.get();
			} catch (AtBoundaryException cme) {
				//OK
			}
	}
	
	@Test
	public void testRetreatFollowingRemove2AtIterator() {
		final Collection<Comparable> t = createCollection();
		if (t instanceof PriorityQueue)
			return;
		for (int i = 0; i < 8; i++) {
			t.add(i);
		}
		Locator<Comparable> loc = t.iterator();
		loc.advance();
		loc.advance();
		Comparable element1 = loc.get();
		loc.advance();
		Comparable element2 = loc.get();
		loc.advance();
		Comparable element3 = loc.get();
		t.remove(element3);
		t.remove(element2);
		try {
			loc.retreat();   //should occur if remove is critical
		} catch (ConcurrentModificationException cme) {
			return;
		}
		assertEquals(element1, loc.get());
	}
		
	
	@Test
	public void testAddAll() {
		Collection<Comparable> t = createCollection(10);
		t.add("d");
		t.add("b");
		t.add("e");
		t.add("c");
		t.add("f");
		t.add("j");
		
		Collection<Comparable> t2 = new BinarySearchTree<Comparable>();
		t2.add("c");
		t2.add("g");
		t2.add("a");
		
		t.addAll(t2);
		
		if (t instanceof OrderedCollection)
			assertEquals(t.toString(),delimLeft() + "a, b, c, c, d, e, f, g, j" + delimRight());
		
		t.add("h");
		
		assertEquals(true, t.contains("a"));
		assertEquals(true, t.contains("b"));
		assertEquals(true, t.contains("c"));
		assertEquals(true,t.contains("d"));
		assertEquals(true,t.contains("e"));
		assertEquals(true,t.contains("f"));
		assertEquals(true,t.contains("g"));
		assertEquals(true,t.contains("h"));
		assertEquals(false,t.contains("i"));
		assertEquals(true, t.contains("j"));
		
		if (t instanceof OrderedCollection)
			assertEquals(t.toString(),delimLeft() + "a, b, c, c, d, e, f, g, h, j" + delimRight());
	}
	
	@Test
	public void testAddAll2() {
		Collection<Comparable> t = createCollection(10);
		t.add("d");
		t.add("b");
		t.add("e");
		t.add("c");
		t.add("f");
		t.add("j");
		
		Collection<Comparable> t2 = new DynamicArray<Comparable>();
		t2.add("c");
		t2.add("g");
		t2.add("a");
		
		t.addAll(t2);
		t.add("h");
		assertEquals(true, t.contains("a"));
		assertEquals(true, t.contains("b"));
		assertEquals(true, t.contains("c"));
		assertEquals(true,t.contains("d"));
		assertEquals(true,t.contains("e"));
		assertEquals(true,t.contains("f"));
		assertEquals(true,t.contains("g"));
		assertEquals(true,t.contains("h"));
		assertEquals(false,t.contains("i"));
		assertEquals(true, t.contains("j"));
		
		if (t instanceof OrderedCollection)
			assertEquals(t.toString(),delimLeft() + "a, b, c, c, d, e, f, g, h, j" + delimRight());
	}

	@Test
	public void testRetainAll() {
		Collection<Comparable> t = createTrackedCollection();
		if (!(t instanceof BTree || t instanceof BPlusTree || t instanceof LeftistHeap)) {
			t.add("d");
			t.add("b");
			t.add("e");
			t.add("c");
			t.add("c");
			t.add("g");
			
			Collection<Comparable> t2 = new BinarySearchTree<Comparable>();
			t2.add("c");
			t2.add("g");
			t2.add("a");
			
			t.retainAll(t2);
			t.add("h");
			
			assertEquals(false, t.contains("a"));
			assertEquals(false, t.contains("b"));
			assertEquals(true, t.contains("c"));
			assertEquals(false,t.contains("d"));
			assertEquals(false,t.contains("e"));
			assertEquals(false,t.contains("f"));
			assertEquals(true,t.contains("g"));
			assertEquals(true,t.contains("h"));
			assertEquals(false,t.contains("i"));

			if (t instanceof OrderedCollection)
				assertEquals(t.toString(),delimLeft() + "c, c, g, h" + delimRight());
		}
	}
	
	@Test
	public void testRetainAll2() {
		Collection<Comparable> t = createTrackedCollection();
		if (!(t instanceof BTree || t instanceof BPlusTree || t instanceof LeftistHeap)) {
			t.add("d");
			t.add("b");
			t.add("e");
			t.add("c");
			t.add("c");
			t.add("g");
			
			Collection<Comparable> t2 = new DynamicArray<Comparable>();
			t2.add("c");
			t2.add("g");
			t2.add("a");
			
			t.retainAll(t2);
			t.add("h");
			
			assertEquals(false, t.contains("a"));
			assertEquals(false, t.contains("b"));
			assertEquals(true, t.contains("c"));
			assertEquals(false,t.contains("d"));
			assertEquals(false,t.contains("e"));
			assertEquals(false,t.contains("f"));
			assertEquals(true,t.contains("g"));
			assertEquals(true,t.contains("h"));
			assertEquals(false,t.contains("i"));
			
			if (t instanceof OrderedCollection)
				assertEquals(t.toString(),delimLeft() + "c, c, g, h" + delimRight());
		}
	}
	
	
	@Test
	public void testRemove() {
		Collection<Comparable> t = createCollection();
		t.add("d");
		t.add("b");
		t.add("e");
		t.add("c");
		t.add("c");
		t.add("g");
		
		assertEquals(true, t.contains("d"));
		assertEquals(true, t.contains("b"));
		assertEquals(true, t.contains("e"));
		assertEquals(true, t.contains("c"));
		assertEquals(true, t.contains("g"));
		assertEquals(false, t.contains("h"));
		
		if (t instanceof Set) {
			assertEquals("b",AbstractCollection.getElementAtRank(t,0));
			assertEquals("c",AbstractCollection.getElementAtRank(t,1));
			assertEquals("d",AbstractCollection.getElementAtRank(t,2));
			assertEquals("e",AbstractCollection.getElementAtRank(t,3));
			assertEquals("g",AbstractCollection.getElementAtRank(t,4));
		} else {
			assertEquals("b",AbstractCollection.getElementAtRank(t,0));
			assertEquals("c",AbstractCollection.getElementAtRank(t,1));
			assertEquals("c",AbstractCollection.getElementAtRank(t,2));
			assertEquals("d",AbstractCollection.getElementAtRank(t,3));
			assertEquals("e",AbstractCollection.getElementAtRank(t,4));
			assertEquals("g",AbstractCollection.getElementAtRank(t,5));
		}
		if (t instanceof OrderedCollection)
			assertEquals(delimLeft() + "b, c, c, d, e, g" + delimRight(), t.toString());
		t.remove("d");
		if (t instanceof OrderedCollection)
			assertEquals(delimLeft() + "b, c, c, e, g" + delimRight(), t.toString());
		
		t.remove("c"); // remove the first c
		
		if (t instanceof Mapping || t instanceof Set) // duplicates not allowed
			assertEquals(false, t.contains("c"));
		else
			assertEquals(true, t.contains("c"));
		if (t instanceof OrderedCollection)
			assertEquals(t.toString(),delimLeft() + "b, c, e, g" + delimRight());
		assertEquals(false, t.contains("d"));
		assertEquals(true, t.contains("b"));
		assertEquals(true, t.contains("e"));
		assertEquals(true, t.contains("g"));
		assertEquals(false, t.contains("h"));

		t.remove("c"); // remove the other c
		
		assertEquals(false, t.contains("c"));
		assertEquals(false, t.contains("d"));
		assertEquals(true, t.contains("b"));
		assertEquals(true, t.contains("e"));
		assertEquals(true, t.contains("g"));
		assertEquals(false, t.contains("h"));	
	}
	
	// Tests below use Java's collections cross check the implementations.
	ArrayList<Comparable> createArrayList(Iterator<Comparable> it) {
		ArrayList<Comparable> a = new ArrayList<Comparable>();
		while (it.hasNext())
			a.add(it.next());
		return a;
	}
	
	
	@SuppressWarnings("unchecked")
	boolean sameMembership(Iterator<Comparable> it1,
						Iterator<Comparable> it2) {
		ArrayList<Comparable> a1 = createArrayList(it1);
		ArrayList<Comparable> a2 = createArrayList(it2);
		java.util.Collections.sort(a1);
		java.util.Collections.sort(a2);
		return a1.equals(a2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testMembershipWithIterator() {
		Collection<Comparable> t = createCollection();
		LinkedList<Comparable> comparisonList = new LinkedList<Comparable>();
		t.add("b");
		t.add("a");
		t.add("c");
		t.add("g");
		t.add("f");
		t.add("d");
		t.add("e");
		t.add("h");
		
		comparisonList.add("b");
		comparisonList.add("a");
		comparisonList.add("c");
		comparisonList.add("g");
		comparisonList.add("f");
		comparisonList.add("d");
		comparisonList.add("e");
		comparisonList.add("h");
		
		assertEquals(true, sameMembership(t.iterator(), comparisonList.iterator()));
		final Locator it = t.iterator();
		while (it.hasNext() && !it.next().equals("f")) {} // position iterator at g
		if (t instanceof BTree) {
			try {
				it.remove();
				fail("expected UnsupportedOperationException");
			} catch (UnsupportedOperationException uoe) {
				return;
			}
		}

		if (!(t instanceof LeftistHeap)) {
			it.remove(); // remove f
			comparisonList.remove("f");
			assertEquals(true, sameMembership(t.iterator(), comparisonList.iterator()));
			if (!(t instanceof BinaryHeap)) {
				if (t instanceof Tracked)
					assertEquals(false, it.inCollection());
				else
					assertEquals(true, it.inCollection());
			}
			
			if (t instanceof Tracked && !(t instanceof BinaryHeap)) {
				try {
					((Locator<Comparable>) it).get();
					fail("NoSuchElementException expected");
				} catch (NoSuchElementException nsee) {
					// expected
				}
			} else if (t instanceof OrderedCollection){
				assertEquals("e", ((Locator<Comparable>) it).get()); //e precedes f in iteration order
			} else if (!(t instanceof BinaryHeap)) {
				assertEquals("g", ((Locator<Comparable>) it).get()); //g precedes f in iteration order
			}
			// try removing the last element
			if (t instanceof OrderedCollection) {
				Locator<Comparable> end = ((OrderedCollection<Comparable>) t).iteratorAtEnd();
				end.retreat();
				Object last = end.get();
				end.remove();
				comparisonList.remove(last);
				assertEquals(true, sameMembership(t.iterator(), comparisonList.iterator()));
			}
			
			// try removing the first element
			Locator<Comparable> start = t.iterator();
			start.advance();
			Object first = start.get();
			start.remove();
			comparisonList.remove(first);
			assertEquals(true, sameMembership(t.iterator(), comparisonList.iterator()));
		}
	}
	
	
	// The following tests check that iterators work correctly at the boundaries.
	@Test(expected=AtBoundaryException.class)
	public void testBoundaryEmptyAft() {
		Collection<Comparable> t = createCollection();
		assertEquals(0, t.getSize());
		final Locator<Comparable> loc = t.iterator();
		if (loc instanceof AbstractCollection.VisitingIterator)
			return; // not applicable
		assertEquals(false, loc.advance()); // now at AFT
		loc.advance();   	
	}
	
	
	@Test(expected=AtBoundaryException.class)
	public void testBoundaryEmptyFore() {
		Collection<Comparable> t = createCollection();
		assertEquals(0, t.getSize());
		final Locator<Comparable> loc = t.iterator();
		if (loc instanceof AbstractCollection.VisitingIterator)
			return; // not applicable
		assertEquals(false, loc.advance()); // now at AFT
		assertEquals(false, loc.retreat()); // now at FORE
		loc.retreat();  // expect AtBoundaryException   	
	}
	
	
	@Test(expected=AtBoundaryException.class)	
	public void testBoundaryWithOneFore() {
		Collection<Comparable> t = createCollection();
		assertEquals(0, t.getSize());
		final Locator<Comparable> loc = t.iterator();
		if (loc instanceof AbstractCollection.VisitingIterator)
			return; // not applicable
		t.add(1);
		assertEquals(true, loc.advance()); // now at the element
		assertEquals(false, loc.advance()); // now at AFT
		assertEquals(true, loc.retreat()); // now at the element
		assertEquals(false, loc.retreat()); // now at FORE
		loc.retreat(); // expect AtBoundaryException
	}
	
	
	@Test(expected=AtBoundaryException.class)
	public void testBoundaryWithOneAft() {
		Collection<Comparable> t = createCollection();
		assertEquals(0, t.getSize());
		final Locator<Comparable> loc = t.iterator();
		if (loc instanceof AbstractCollection.VisitingIterator)
			return; // not applicable
		t.add(1);
		assertEquals(true, loc.advance()); // now at the element
		assertEquals(false, loc.advance()); // now at AFT
		loc.advance(); // expect AtBoundaryException
	}

}

