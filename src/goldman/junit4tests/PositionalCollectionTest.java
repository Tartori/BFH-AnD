package goldman.junit4tests;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import goldman.Objects;
import goldman.collection.*;
import goldman.collection.positional.*;

public abstract class PositionalCollectionTest extends CollectionTest {
	
	static boolean REPORTING = false;
	
	public String delimLeft() { return "<"; }
	public String delimRight() { return ">"; }
	
	protected PositionalCollectionTest() {}
	
	@Test
	public void testLocatorOne() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(10);
		a.addLast(new Integer(1));
		a.addLast(new Integer(2));
		a.addLast(new Integer(4));
		a.addLast(new Integer(5));
		a.addLast(new Integer(6));
		a.addLast(new Integer(7));
		a.addLast(new Integer(8));
		a.addLast(new Integer(9));
		PositionalCollectionLocator<Comparable> loc1 = a.iterator();
		loc1.advance();
		assertEquals(1,loc1.get());
		loc1.advance();
		assertEquals(2,loc1.get());
		assertEquals(delimLeft() + "1, 2, 4, 5, 6, 7, 8, 9" + delimRight(),a.toString());
		loc1.addAfter(new Integer(3));
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6, 7, 8, 9" + delimRight(),a.toString());
		assertEquals(2,loc1.get());	
		loc1.advance();
		assertEquals(3,loc1.get());
		loc1.advance();
		assertEquals(4,loc1.get());
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6, 7, 8, 9" + delimRight(),a.toString());
		loc1.remove();
		assertEquals(delimLeft() + "1, 2, 3, 5, 6, 7, 8, 9" + delimRight(),a.toString());
		loc1.advance();
		assertEquals(5,loc1.get());
		assertEquals(delimLeft() + "1, 2, 3, 5, 6, 7, 8, 9" + delimRight(),a.toString());
		a.removeRange(4,7);
		assertEquals(delimLeft() + "1, 2, 3, 5" + delimRight(),a.toString());
		assertEquals(5,a.get(3));
		assertEquals(4,a.getSize());
	}
	
	@Test public void testLocatorTwo() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		a.addLast(new Integer(1));
		a.addLast(new Integer(3));
		a.addLast(new Integer(4));
		a.addLast(new Integer(5));
		PositionalCollectionLocator<Comparable> loc1 = a.iterator();
		loc1.advance();
		assertEquals(1,loc1.get());
		loc1.addAfter(new Integer(2));
		assertEquals(1,loc1.get());
		loc1.advance();
		assertEquals(2,loc1.get());
		loc1.advance();
		assertEquals(3,loc1.get());
		loc1.advance();
		assertEquals(4,loc1.get());
		loc1.remove();
		loc1.advance();
		assertEquals(5,loc1.get());
		assertEquals(delimLeft() + "1, 2, 3, 5" + delimRight(),a.toString());
		loc1.remove();
		loc1.advance();
		assertEquals(false, loc1.hasNext());
		loc1.retreat();
		assertEquals(3,loc1.get());
	}
	
	@Test
	public void testRemove() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		a.addLast(new Integer(1));
		a.addLast(new Integer(2));
		a.addLast(new Integer(3));
		a.addLast(new Integer(4));
		a.addLast(new Integer(5));
		a.addLast(new Integer(6));
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6" + delimRight(),a.toString());
		assertEquals(1, a.removeFirst());
		assertEquals(delimLeft() + "2, 3, 4, 5, 6" + delimRight(),a.toString());
		assertEquals(6, a.removeLast());
		assertEquals(delimLeft() + "2, 3, 4, 5" + delimRight(),a.toString());
		assertEquals(4, a.remove(2));
		assertEquals(delimLeft() + "2, 3, 5" + delimRight(),a.toString());
		assertEquals(5, a.remove(2));
		assertEquals(delimLeft() + "2, 3" + delimRight(),a.toString());
		assertEquals(2, a.remove(0));
		assertEquals(delimLeft() + "3" + delimRight(),a.toString());
		assertEquals(3, a.remove(0));
		assertEquals(delimLeft() + delimRight(),a.toString());
	}
	
	@Test
	public void testStaleLocator() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(10);
		a.addLast(new Integer(1));
		a.addLast(new Integer(2));
		a.addLast(new Integer(3));
		final PositionalCollectionLocator<Comparable> loc1 = a.iterator();
		a.swap(1, 2);
		try {
			loc1.advance();
			fail("expected ConcurrentModificationException");
		} catch (ConcurrentModificationException cme) {
			//OK
		}
	}
	
	@Test
	public void testForcedLocator() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(10);
		a.addLast(new Integer(1));
		a.addLast(new Integer(2));
		a.addLast(new Integer(3));
		final PositionalCollectionLocator<Comparable> loc = a.iterator();
		loc.advance();
		loc.advance();
		assertEquals(2, loc.get());
		a.swap(0, 2);
		loc.ignorePriorConcurrentModifications();
		loc.advance();
		assertEquals(1, loc.get());
	}
	
	@Test
	public void testIterationOrderBackToFront() {
		final PositionalCollection<Comparable> t = (PositionalCollection<Comparable>) createCollection(21);
		for (int i = 0; i <= 20; i++)
			t.add(i);
		PositionalCollectionLocator<Comparable> forward, backward;
		forward = t.iterator();
		backward = t.iteratorAtEnd();
		while (forward.hasNext()) {
			forward.advance();
			backward.retreat();
			assertEquals(20-((Integer) forward.get()), backward.get());
		}
	}
	
	@Test
	public void testToArray() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(7);
		a.addLast(new Integer(1));
		a.addLast(new Integer(2));
		a.addLast(new Integer(3));
		a.addLast(new Integer(4));
		a.addLast(new Integer(5));
		a.addLast(new Integer(6));
		Comparable[] array = new Comparable[6];
		Comparable[] result = a.toArray(array);
		assertEquals(result, array);
		assertEquals(6, result[5]);
		a.addLast(new Integer(7));
		result = a.toArray(array);
		assertEquals(false, array == result);
		assertEquals(6, result[5]);
		assertEquals(7, result[6]);
	}
	
	@Test
	public void testSortOnceMore() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(5);
		final Locator<Comparable> loc = a.iterator();
		a.addLast(new Integer(9));
		a.addLast(new Integer(11));
		a.addLast(new Integer(12));
		a.addLast(new Integer(10));
		a.addLast(new Integer(8));
		assertEquals(delimLeft() + "9, 11, 12, 10, 8" + delimRight(),a.toString());
		a.quicksort();
		assertEquals(delimLeft() + "8, 9, 10, 11, 12" + delimRight(),a.toString());
		try {
			loc.advance();
			fail("expected ConcurrentModificationException");
		} catch (ConcurrentModificationException cme) {
			//OK
		}
	}
	
	@Test
	public void testMediumSort() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(25);
		a.addLast(new Integer(23));
		a.addLast(new Integer(9));
		a.addLast(new Integer(14));
		a.addLast(new Integer(5));
		a.addLast(new Integer(24));
		a.addLast(new Integer(16));
		a.addLast(new Integer(20));
		a.addLast(new Integer(18));
		a.addLast(new Integer(0));
		a.addLast(new Integer(8));
		a.addLast(new Integer(7));
		a.addLast(new Integer(22));
		a.addLast(new Integer(6));
		a.addLast(new Integer(13));
		a.addLast(new Integer(1));
		a.addLast(new Integer(3));
		a.addLast(new Integer(17));
		a.addLast(new Integer(11));
		a.addLast(new Integer(12));
		a.addLast(new Integer(10));
		a.addLast(new Integer(4));
		a.addLast(new Integer(21));
		a.addLast(new Integer(2));
		a.addLast(new Integer(19));
		a.addLast(new Integer(15));
		assertEquals(delimLeft() + "23, 9, 14, 5, 24, 16, 20, 18, 0, 8, 7, 22, 6, 13, 1, 3, 17, 11, 12, 10, 4, 21, 2, 19, 15" + delimRight(),a.toString());
		a.quicksort();
		assertEquals(delimLeft() + "0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24" + delimRight(),a.toString());
	}
	
	@Test
	public void testSortAgain() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(5);
		a.addLast(new Integer(4));
		a.addLast(new Integer(1));
		a.addLast(new Integer(0));
		a.addLast(new Integer(3));
		a.addLast(new Integer(2));
		assertEquals(delimLeft() + "4, 1, 0, 3, 2" + delimRight(),a.toString());
		a.quicksort();
		assertEquals(delimLeft() + "0, 1, 2, 3, 4" + delimRight(),a.toString());
	}
	
	@Test
	public void testSort() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		a.addLast(new Integer(6));
		a.addLast(new Integer(4));
		a.addLast(new Integer(2));
		a.addLast(new Integer(1));
		a.addLast(new Integer(5));
		a.addLast(new Integer(3));
		assertEquals(delimLeft() + "6, 4, 2, 1, 5, 3" + delimRight(),a.toString());
		a.quicksort();
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6" + delimRight(),a.toString());
	}
	
	@Test
	public void testSortMore() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(5);
		a.addLast(new Integer(3));
		a.addLast(new Integer(2));
		a.addLast(new Integer(4));
		a.addLast(new Integer(0));
		a.addLast(new Integer(1));
		assertEquals(delimLeft() + "3, 2, 4, 0, 1" + delimRight(),a.toString());
		a.quicksort();
		assertEquals(delimLeft() + "0, 1, 2, 3, 4" + delimRight(),a.toString());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testMergeSort() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		Locator loc = null;
		a.addLast(new Integer(6));
		a.addLast(new Integer(4));
		a.addLast(new Integer(2));
		a.addLast(new Integer(1));
		if (a instanceof Tracked)
			loc = ((Tracked) a).addTracked(new Integer(5));
		else
			a.addLast(new Integer(5));
		a.addLast(new Integer(3));
		assertEquals(delimLeft() + "6, 4, 2, 1, 5, 3" + delimRight(),a.toString());
		a.mergesort();
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6" + delimRight(),a.toString());
		if (a instanceof Tracked)
			assertEquals(5,loc.get());
		assertEquals(6,a.get(a.getSize()-1));
		assertEquals(6,a.removeLast());
		assertEquals(5,a.getSize());
		assertEquals(5,a.removeLast());
	}
	
	@Test
	public void testSelect() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		a.addLast(new Integer(6));
		a.addLast(new Integer(4));
		a.addLast(new Integer(2));
		a.addLast(new Integer(1));
		a.addLast(new Integer(5));
		a.addLast(new Integer(3));
		assertEquals(1,a.repositionElementByRank(0));
		assertEquals(2,a.repositionElementByRank(1));
		assertEquals(3,a.repositionElementByRank(2));
		assertEquals(4,a.repositionElementByRank(3));
		assertEquals(5,a.repositionElementByRank(4));
		assertEquals(6,a.repositionElementByRank(5));
	}	

	@SuppressWarnings("unchecked")
	@Test
	public void testInsertionSortStability() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		Locator loc1 = null;
		Locator loc2 = null;
		if (a instanceof Tracked)
			loc1 = ((Tracked) a).addTracked(new Integer(5));
		else
			a.addLast(new Integer(5));
		a.addLast(new Integer(4));
		a.addLast(new Integer(2));
		a.addLast(new Integer(1));
		if (a instanceof Tracked)
			loc2 = ((Tracked) a).addTracked(new Integer(5));
		else
			a.addLast(new Integer(5));
		a.addLast(new Integer(3));
		assertEquals(delimLeft() + "5, 4, 2, 1, 5, 3" + delimRight(),a.toString());
		a.insertionsort();
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 5" + delimRight(),a.toString());
		assertEquals(5,a.get(a.getSize()-1));
		assertEquals(5,a.removeLast());
		if (a instanceof Tracked) {
			assertEquals(true,loc1.inCollection());
			assertEquals(false,loc2.inCollection());
		}
		assertEquals(5,a.getSize());
		assertEquals(5,a.removeLast());
		if (a instanceof Tracked)
			assertEquals(false,loc1.inCollection());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testInsertionSort() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		Locator loc = null;
		a.addLast(new Integer(6));
		a.addLast(new Integer(4));
		a.addLast(new Integer(2));
		a.addLast(new Integer(1));
		if (a instanceof Tracked)
			loc = ((Tracked) a).addTracked(new Integer(5));
		else
			a.addLast(new Integer(5));
		a.addLast(new Integer(3));
		assertEquals(delimLeft() + "6, 4, 2, 1, 5, 3" + delimRight(),a.toString());
		a.insertionsort();
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6" + delimRight(),a.toString());
		if (a instanceof Tracked)
			assertEquals(5,loc.get());
		assertEquals(6,a.get(a.getSize()-1));
		assertEquals(6,a.removeLast());
		assertEquals(5,a.getSize());
		assertEquals(5,a.removeLast());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHeapSort() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		Locator loc = null;
		a.addLast(new Integer(6));
		a.addLast(new Integer(4));
		a.addLast(new Integer(2));
		a.addLast(new Integer(1));
		if (a instanceof Tracked)
			loc = ((Tracked) a).addTracked(new Integer(5));
		else
			a.addLast(new Integer(5));
		a.addLast(new Integer(3));
		assertEquals(delimLeft() + "6, 4, 2, 1, 5, 3" + delimRight(),a.toString());
		a.heapsort();
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6" + delimRight(),a.toString());
		if (a instanceof Tracked)
			assertEquals(5,loc.get());
		assertEquals(6,a.get(a.getSize()-1));
		assertEquals(6,a.removeLast());
		assertEquals(5,a.getSize());
		assertEquals(5,a.removeLast());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTreeSort() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		Locator loc = null;
		a.addLast(new Integer(6));
		a.addLast(new Integer(4));
		a.addLast(new Integer(2));
		a.addLast(new Integer(1));
		if (a instanceof Tracked)
			loc = ((Tracked) a).addTracked(new Integer(5));
		else
			a.addLast(new Integer(5));
		a.addLast(new Integer(3));
		assertEquals(delimLeft() + "6, 4, 2, 1, 5, 3" + delimRight(),a.toString());
		a.treesort();
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6" + delimRight(),a.toString());
		if (a instanceof Tracked)
			assertEquals(5,loc.get());
		assertEquals(6,a.get(a.getSize()-1));
		assertEquals(6,a.removeLast());
		assertEquals(5,a.getSize());
		assertEquals(5,a.removeLast());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSorted() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		Locator loc = null;
		a.addLast(new Integer(1));
		a.addLast(new Integer(2));
		a.addLast(new Integer(3));
		a.addLast(new Integer(4));
		a.addLast(new Integer(6));
		if (a instanceof Tracked)
			loc = ((Tracked) a).addTracked(new Integer(5));
		else
			a.addLast(new Integer(5));

		assertEquals(delimLeft() + "1, 2, 3, 4, 6, 5" + delimRight(),a.toString());
		a.mergesort();
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6" + delimRight(),a.toString());
		if (a instanceof Tracked)
			assertEquals(5,loc.get());
		assertEquals(6,a.get(a.getSize()-1));
		assertEquals(6,a.removeLast());
		assertEquals(5,a.getSize());
		assertEquals(5,a.removeLast());
	}
	
	@Test
	public void testReversed() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(6);
		a.addLast(new Integer(6));
		a.addLast(new Integer(5));
		a.addLast(new Integer(4));
		a.addLast(new Integer(3));
		a.addLast(new Integer(2));
		a.addLast(new Integer(1));
		assertEquals(delimLeft() + "6, 5, 4, 3, 2, 1" + delimRight(),a.toString());
		a.quicksort();
		assertEquals(delimLeft() + "1, 2, 3, 4, 5, 6" + delimRight(),a.toString());
	}
	
	@Test
	public void testEmpty() {
		PositionalCollection<Comparable> a = 
			(PositionalCollection<Comparable>) createCollection(0);
		assertEquals(delimLeft() + "" + delimRight(),a.toString());
		assertEquals(false, a.iterator().hasNext());
		a.quicksort();
		assertEquals(delimLeft() + "" + delimRight(),a.toString());
		assertEquals(false, a.iterator().hasNext());		
	}
	
	static final int size = 8;
	static final int reps = 1;
	static final int skip = 0;

	@SuppressWarnings("unchecked")
	@Test
	public void testLargeSort() {
		ArrayList<Comparable> al = new ArrayList<Comparable>();
		for (int i = 0; i < size; i++)
			al.add(new Integer(i));
		java.util.Collections.shuffle(al);
		
		System.gc();
		long time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("n is " + size + ", and reps is " + reps + "\n");
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.");
		java.util.Collections.shuffle(al);
		
		testLargeSort(new Array<Comparable>(size), al);
		testLargeSort(new DynamicArray<Comparable>(), al);
		testLargeSort(new CircularArray<Comparable>(size), al);
		testLargeSort(new DynamicCircularArray<Comparable>(), al);
		testLargeSort(new SinglyLinkedList<Comparable>(), al);
		testLargeSort(new DoublyLinkedList<Comparable>(), al);
		testLargeSort(new TrackedArray<Comparable>(), al);
		System.gc();
		time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.\n");
		
	}

	public void testLargeSort(AbstractPositionalCollection<Comparable> a, java.util.Collection<Comparable> elements) {
		long sum = 0;
		for (int i = 0; i < reps+skip; i++){
			java.util.Collections.shuffle((List<?>) elements);
			a.clear();
			Iterator<Comparable> it = elements.iterator();
			while (it.hasNext())
				a.add(it.next());
			System.gc();
			long time = System.currentTimeMillis();
			a.quicksort();
			time = System.currentTimeMillis() - time;
			if (i==skip)
				sum = 0;
			sum += time;
			Locator<Comparable> l = a.iterator();
			for (int j = 0; j < size; j++) {
				l.advance();
				assertEquals(j,((Integer) l.get()).intValue());
			}
		}
		if (REPORTING) System.out.println("Quicksort avg for " + a.getClass().getName() + " took " + sum/reps + " milliseconds.");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLargeInsertionSort() {
		if (size <= 1000){
			ArrayList<Comparable> al = new ArrayList<Comparable>();
			for (int i = 0; i < size; i++)
				al.add(new Integer(i));
			java.util.Collections.shuffle(al);
			
			System.gc();
			long time = System.currentTimeMillis();
			Collections.sort(al);
			time = System.currentTimeMillis() - time;
			if (REPORTING) System.out.println("Java's sort for " + al.getClass().getName() + " took " + time + " milliseconds.");
			java.util.Collections.shuffle(al);
			
			testLargeInsertionSort(new Array<Comparable>(size), al);
			testLargeInsertionSort(new DynamicArray<Comparable>(), al);
			testLargeInsertionSort(new CircularArray<Comparable>(size), al);
			testLargeInsertionSort(new DynamicCircularArray<Comparable>(), al);
			testLargeInsertionSort(new SinglyLinkedList<Comparable>(), al);
			testLargeInsertionSort(new DoublyLinkedList<Comparable>(), al);
			testLargeInsertionSort(new TrackedArray<Comparable>(), al);
			
			System.gc();
			time = System.currentTimeMillis();
			Collections.sort(al);
			time = System.currentTimeMillis() - time;
			if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds./n");
		}
	}
	
	public void testLargeInsertionSort(AbstractPositionalCollection<Comparable> a, java.util.Collection<Comparable> elements) {
		Iterator<Comparable> it = elements.iterator();
		while (it.hasNext())
			a.add(it.next());
		System.gc();
		long time = System.currentTimeMillis();
		a.insertionsort();
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Insertion Sort for " + a.getClass().getName() + " took " + time + " milliseconds.");
		Locator<Comparable> l = a.iterator();
		for (int i = 0; i < size; i++) {
			l.advance();
			assertEquals(i,((Integer) l.get()).intValue());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLargeHeapSort() {
		ArrayList<Comparable> al = new ArrayList<Comparable>();
		for (int i = 0; i < size; i++)
			al.add(new Integer(i));
		java.util.Collections.shuffle(al);
		
		System.gc();
		long time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's sort for " + al.getClass().getName() + " took " + time + " milliseconds.");
		java.util.Collections.shuffle(al);
		
		testLargeHeapSort(new Array<Comparable>(size), al);
		testLargeHeapSort(new DynamicArray<Comparable>(), al);
		testLargeHeapSort(new CircularArray<Comparable>(size), al);
		testLargeHeapSort(new DynamicCircularArray<Comparable>(), al);
		testLargeHeapSort(new SinglyLinkedList<Comparable>(), al);
		testLargeHeapSort(new DoublyLinkedList<Comparable>(), al);
		testLargeHeapSort(new TrackedArray<Comparable>(), al);
		
		System.gc();
		time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.\n");
	}

	public void testLargeHeapSort(AbstractPositionalCollection<Comparable> a, java.util.Collection<Comparable> elements) {
		long sum = 0;
		for (int i = 0; i < reps+skip; i++){
			java.util.Collections.shuffle((List<?>) elements);
			a.clear();
			Iterator<Comparable> it = elements.iterator();
			while (it.hasNext())
				a.add(it.next());
			System.gc();
			long time = System.currentTimeMillis();
			a.heapsort();
			time = System.currentTimeMillis() - time;
			if (i==skip)
				sum = 0;
			sum += time;
			Locator<Comparable> l = a.iterator();
			for (int j = 0; j < size; j++) {
				l.advance();
				assertEquals(j,((Integer) l.get()).intValue());
			}
		}
		if (REPORTING) System.out.println("Heapsort avg for " + a.getClass().getName() + " took " + sum/reps + " milliseconds.");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLargeTreeSort() {
		ArrayList<Comparable> al = new ArrayList<Comparable>();
		for (int i = 0; i < size; i++)
			al.add(new Integer(i));
		java.util.Collections.shuffle(al);
		
		System.gc();
		long time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's sort for " + al.getClass().getName() + " took " + time + " milliseconds.");
		java.util.Collections.shuffle(al);
		
		testLargeTreeSort(new Array<Comparable>(size), al);
		testLargeTreeSort(new DynamicArray<Comparable>(), al);
		testLargeTreeSort(new CircularArray<Comparable>(size), al);
		testLargeTreeSort(new DynamicCircularArray<Comparable>(), al);
		testLargeTreeSort(new SinglyLinkedList<Comparable>(), al);
		testLargeTreeSort(new DoublyLinkedList<Comparable>(), al);
		testLargeTreeSort(new TrackedArray<Comparable>(), al);
		
		System.gc();
		time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.\n");
	}

	public void testLargeTreeSort(AbstractPositionalCollection<Comparable> a, java.util.Collection<Comparable> elements) {
		long sum = 0;
		for (int i = 0; i < reps+skip; i++){
			java.util.Collections.shuffle((List<?>) elements);
			a.clear();
			Iterator<Comparable> it = elements.iterator();
			while (it.hasNext())
				a.add(it.next());
			System.gc();
			long time = System.currentTimeMillis();
			a.treesort();
			time = System.currentTimeMillis() - time;
			if (i==skip)
				sum = 0;
			sum += time;
			Locator<Comparable> l = a.iterator();
			for (int j = 0; j < size; j++) {
				l.advance();
				assertEquals(j,((Integer) l.get()).intValue());
			}
		}
		if (REPORTING) System.out.println("treesort avg for " + a.getClass().getName() + " took " + sum/reps + " milliseconds.");
	}
		
	@SuppressWarnings("unchecked")
	@Test
	public void testLargeMergeSort() {
		ArrayList<Comparable> al = new ArrayList<Comparable>();
		for (int i = 0; i < size; i++)
			al.add(new Integer(i));
		java.util.Collections.shuffle(al);
		
		System.gc();
		long time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.");
		java.util.Collections.shuffle(al);
		
		testLargeMergeSort(new Array<Comparable>(size), al);
		testLargeMergeSort(new DynamicArray<Comparable>(), al);
		testLargeMergeSort(new CircularArray<Comparable>(size), al);
		testLargeMergeSort(new DynamicCircularArray<Comparable>(), al);
		testLargeMergeSort(new SinglyLinkedList<Comparable>(), al);
		testLargeMergeSort(new DoublyLinkedList<Comparable>(), al);
		testLargeMergeSort(new TrackedArray<Comparable>(), al);
		
		System.gc();
		time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.\n");
	}

	public void testLargeMergeSort(AbstractPositionalCollection<Comparable> a, java.util.Collection<Comparable> elements) {
		long sum = 0;
		for (int i = 0; i < reps+skip; i++){
			java.util.Collections.shuffle((List<?>) elements);
			a.clear();
			Iterator<Comparable> it = elements.iterator();
			while (it.hasNext())
				a.add(it.next());
			System.gc();
			long time = System.currentTimeMillis();
			a.mergesort();
			time = System.currentTimeMillis() - time;
			if (i==skip)
				sum = 0;
			sum += time;
			Locator<Comparable> l = a.iterator();
			for (int j = 0; j < size; j++) {
				l.advance();
				assertEquals(j,((Integer) l.get()).intValue());
			}
		}
		if (REPORTING) System.out.println("mergesort avg for " + a.getClass().getName() + " took " + sum/reps + " milliseconds.");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLargeRadixSort() {
		ArrayList<Comparable> al = new ArrayList<Comparable>(size);
		for (int i = 0; i < size; i++)
			al.add(new Integer(i));
		java.util.Collections.shuffle(al);
		
		System.gc();
		long time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.");
		java.util.Collections.shuffle(al);
		
		PositionalCollection<IndexedNumber> coll = createRadixCollection(size);
		Iterator<Comparable> it = al.iterator();
		while (it.hasNext())
			coll.add(new IndexedNumber((Integer) it.next()));
		System.gc();
		time = System.currentTimeMillis();
		coll.radixsort(new IndexedNumber.NumberDigitizer());
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Radix sort took " + time + " milliseconds.");
		PositionalCollectionLocator<IndexedNumber> l = coll.iterator();
		coll.toString();
		for (int i = 0; i < size; i++) {
			l.advance();
			assertEquals(""+i,l.get().toString());
		}
		
		System.gc();
		time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.\n");
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLargeBucketSort() {
		ArrayList<Comparable> al = new ArrayList<Comparable>(size);
		for (int i = 0; i < size; i++) {
			al.add(new Integer(i));
		}
		java.util.Collections.shuffle(al);
		
		System.gc();
		long time = System.currentTimeMillis();
		Collections.sort(al, Objects.DEFAULT_COMPARATOR);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort with comparator for " + al.getClass().getName() + " took " + time + " milliseconds.");
		java.util.Collections.shuffle(al);
		
		AbstractPositionalCollection<Comparable> coll = (AbstractPositionalCollection<Comparable>) createCollection(size);
		Iterator<Comparable> it = al.iterator();
		while (it.hasNext())
			coll.add(it.next());
		System.gc();
		time = System.currentTimeMillis();
		
		Bucketizer<Comparable> b = new DefaultBucketizer<Comparable>(coll,
				new Quantizer<Comparable>() {
					public double getDouble(Comparable x) {
						return (Integer) x;
					}
		});
		
		coll.bucketsort(b);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Bucket sort took " + time + " milliseconds.");
		Locator<Comparable> l = coll.iterator();
		for (int i = 0; i < size; i++) {
			l.advance();
			assertEquals(i,((Integer) l.get()).intValue());
		}
		System.gc();
		time = System.currentTimeMillis();
		Collections.sort(al);
		time = System.currentTimeMillis() - time;
		if (REPORTING) System.out.println("Java's Sort for " + al.getClass().getName() + " took " + time + " milliseconds.\n");
	}
	
	abstract protected PositionalCollection<IndexedNumber> createRadixCollection();
	abstract protected PositionalCollection<IndexedNumber> createRadixCollection(int capacity);
	
	@Test
	public void testRadixSort() {
		PositionalCollection<IndexedNumber> coll = createRadixCollection();
		coll.add(new IndexedNumber(5));
		coll.add(new IndexedNumber(2));
		coll.add(new IndexedNumber(8));
		coll.add(new IndexedNumber(7));
		coll.add(new IndexedNumber(4));
		coll.add(new IndexedNumber(7));
		coll.add(new IndexedNumber(3));
		coll.radixsort(new IndexedNumber.NumberDigitizer());
		assertEquals("<2, 3, 4, 5, 7, 7, 8>", coll.toString());
	}
	
	@Test
	public void testBucketSort() {
		AbstractPositionalCollection<Comparable> coll = (AbstractPositionalCollection<Comparable>) createCollection();
		coll.add(5);
		coll.add(2);
		coll.add(8);
		coll.add(7);
		coll.add(4);
		coll.add(7);
		coll.add(3);
		
		Bucketizer<Comparable> b = new DefaultBucketizer<Comparable>(coll,
				new Quantizer<Comparable>() {
					public double getDouble(Comparable x) {
						return (Integer) x;
					}
		});
		
		coll.bucketsort(b);
		assertEquals("<2, 3, 4, 5, 7, 7, 8>", coll.toString());
	}
	
	@Test
	public void testSwap() {
		AbstractPositionalCollection<Comparable> coll = (AbstractPositionalCollection<Comparable>) createCollection();
		coll.add(2);
		coll.add(1);
		coll.add(0);
		coll.add(4);
		coll.add(3);
		coll.add(6);
		coll.add(5);
		coll.swap(0, 1);  //1,2,0
		coll.swap(2, 1); //1,0,2
		coll.swap(1,0); //0,1,2
		coll.swap(3, 4);
		coll.swap(5,6);
		assertEquals("<0, 1, 2, 3, 4, 5, 6>", coll.toString());
	}
	
	@Test
	public void testSortsWhenCollectionDoesntStartAtZero() {
		AbstractPositionalCollection<Comparable> coll = prep();
		coll.quicksort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep();
		coll.heapsort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep();
		Bucketizer<Comparable> b = new DefaultBucketizer<Comparable>(coll,
				new Quantizer<Comparable>() {
					public double getDouble(Comparable x) {
						return (Integer) x;
					}
		});
		coll.bucketsort(b);
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep();
		coll.insertionsort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep();
		coll.treesort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep();
		coll.mergesort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
	}
	
	AbstractPositionalCollection<Comparable> prep() {
		AbstractPositionalCollection<Comparable> coll = (AbstractPositionalCollection<Comparable>) createCollection();
		coll.add(5);
		coll.add(2);
		coll.add(8);
		coll.add(7);
		coll.add(4);
		coll.add(7);
		coll.add(3);
		coll.remove(0);
		return coll;
	}
	
	@Test
	public void testSortsWhenLastElementHasBeenRemoved() {
		AbstractPositionalCollection<Comparable> coll = prep2();
		coll.quicksort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep2();
		coll.heapsort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep2();
		Bucketizer<Comparable> b = new DefaultBucketizer<Comparable>(coll,
				new Quantizer<Comparable>() {
					public double getDouble(Comparable x) {
						return (Integer) x;
					}
		});
		coll.bucketsort(b);
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep2();
		coll.insertionsort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep2();
		coll.treesort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
		coll = prep2();
		coll.mergesort();
		assertEquals("<2, 3, 4, 7, 7, 8>", coll.toString());
	}
	
	AbstractPositionalCollection<Comparable> prep2() {
		AbstractPositionalCollection<Comparable> coll = (AbstractPositionalCollection<Comparable>) createCollection();
		coll.add(3);
		coll.add(2);
		coll.add(8);
		coll.add(7);
		coll.add(4);
		coll.add(7);
		coll.add(5);
		coll.remove(6);
		return coll;
	}

}
