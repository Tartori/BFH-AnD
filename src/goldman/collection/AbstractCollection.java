// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

import goldman.Objects;
import goldman.collection.ordered.OrderedCollection;
import goldman.collection.positional.Array;
/**
 * The AbstractCollection class implements methods that can be shared by all
 * data structures that implement a collection.  Some of these methods involve
 * maintaining and accessing the number of elements in the collection (e.g.
 * <code>size</code>, <code>isEmpty</code>), and others are implemented in
 * terms of abstract methods like <code>add</code>.  In some cases, the
 * implementations provided here are "brute-force," implementations
 * but they can be overridden
 * when a more efficient algorithm exists for a specific data structure.  Also, the methodology
 * and associated instance variable <code>version</code>,
 * for triggering a <code>ConcurrentModificationException</code>
 * when appropriate, is included
 * where to be inherited by all collection data structures.
**/

public abstract class AbstractCollection<E> implements Collection<E> {
/**
 * Checks, for the purposes of checking correctness,
 * that the correctness properties are preserved.
**/

	public void checkRep() {};

	protected static final int FORE = -1;  //precedes position 0 (logically)
	protected static final int NOT_FOUND = -2; //return value for position if not found
	public static final int DEFAULT_CAPACITY = 8; //default capacity
	protected int size = 0;  // the number of elements, n, in the collection
	protected Version version = new Version(); //keeps modification count
	public Comparator<? super E> comp; // for comparing user values
	
	public AbstractCollection(Comparator<? super E> comparator) {
		comp = comparator;
	}

/**
 * @return true  if and only if no elements are stored in
 * the collection
**/

	public boolean isEmpty() {
		return size == 0;
	}

/**
 * @return the number of elements stored in the
 * collection
**/

	public int getSize() {
		return size;
	}

/**
 * By default this method returns <code>Integer.MAX_VALUE</code>.
 * @return the current capacity of the collection.
**/

	public int getCapacity() {
		return Integer.MAX_VALUE;
	}

/**
 * @return the comparator used for this
 * collection
**/

	public Comparator<? super E> getComparator() {
		return comp;
	}

/**
 * @param e1 the first element to compare
 * @param e2 the second element to compare
 * @return a negative value when <code>e1</code> &lt; <code>e2</code>,
 * zero when <code>e1</code> equals <code>e2</code>, and
 * a positive value when <code>e1</code> &gt; <code>e2</code>.
**/

		protected int compare(E e1, E e2) {
			return comp.compare(e1, e2);
		}

/**
 * @param e1 one element to compare
 * @param e2 the second element to compare
 * @return true  if and only if <code>e1</code> and <code>e2</code> are
 * equivalent with respect to the set
**/

	protected boolean equivalent(E e1, E e2) {
		return comp.compare(e1, e2) == 0;
	}

/**
 * @return a Java primitive array of capacity n that holds
 * each element of the collection
**/

@SuppressWarnings("unchecked")

	public Object[] toArray() {
		Object[] result = new Object[getSize()];
		int i = 0;
		for (E e: this)
			result[i++] = e;
		return result;
	}

/**
 * @param array an array of the correct type into which the collection's elements
 * are to be placed
 * @return a Java primitive array of capacity at least n that holds
 * each element of the collection
**/

@SuppressWarnings("unchecked")

	public E[] toArray(E[] array) {
		if (array.length < size)
			array = (E[]) java.lang.reflect.Array.newInstance(
					array.getClass().getComponentType(), size);
		int i = 0;
		for (E e: this)
			array[i++] = e;
		return array;
	}

/**
 * This implementation for <code>contains</code> takes linear time, so it
 * is overridden by a more efficient method in most data structure
 * implementations.
 * @param value the element to be located
 * @return true  if and only if an equivalent element exists in the collection
**/

	public boolean contains(E value) {
		for (E e : this)
			if (equivalent(e, value))
				return true;
		return false;
	}

/**
 * @param target the target element
 * @return an equivalent element that is in the collection
 * @throws NoSuchElementException there is no equivalent element in the
 * collection.
**/

	public E getEquivalentElement (E target) {
		Locator<E> loc = iterator();
		while (loc.advance()) {
			if (equivalent(loc.get(), target))
				return loc.get();
		}
		throw new NoSuchElementException();
	}

/**
 * Appends, to the string
 * buffer a comma-separated string representation for each element in the collection, in the
 * iteration order.
 * @param s a string builder
**/

	protected void writeElements(StringBuilder s) {
		Locator<E> loc = iterator();
		while (loc.advance()) {
			s.append(loc.get());
			if (loc.hasNext())
				s.append(", ");
		}
	}

/**
 * @return a comma-separated string that shows the sequence held in the
 * collection, in the iteration order.  Braces mark the beginning
 * and the end of the collection.
**/

	public String toString() {
		StringBuilder s = new StringBuilder("{");
		writeElements(s);
		s.append("}");
		return s.toString();
	}

/**
 * Traverses the entire collection on behalf of a visitor.
 * @param v a visitor
 * @throws VisitAbortedException the traversal is aborted due to an
 * exception raised by the visitor,
 * in which case the cause held by the <code>VisitAbortedException</code> is the
 * exception thrown by the visitor.
**/

	public final void accept(Visitor<? super E> v) {
		try {
			traverseForVisitor(v);
		} catch (Throwable cause) {
			throw new VisitAbortedException(cause);
		}
	}

/**
 * Traverses the collection applying v to each element
 * @param v a visitor
**/

	protected void traverseForVisitor(Visitor<? super E> v) throws Exception {
		for (E e : this)
			v.visit(e);
	}

/**
 * This is a non-mutating method.
 * @param coll the collection to use
 * @param rank the rank of the desired element
 * @return the element that would be in position r if the collection
 * were sorted, where the smallest element is defined as
 * rank 0
**/

	public static<T> T getElementAtRank(Collection<T> coll, int rank) {
		return getElementAtRank(coll, rank, Objects.DEFAULT_COMPARATOR);
	}	

/**
 * This is a non-mutating method.
 * @param coll the collection to use
 * @param rank the rank of the desired element
 * @param comp the comparator that defines the ordering of the elements
 * @return the element that would be in position r if the collection
 * were sorted
**/

	public static<T> T getElementAtRank(Collection<T> coll, int rank,
											Comparator<? super T> comp) {
		if (coll instanceof OrderedCollection)
			return ((OrderedCollection<T>) coll).get(rank);
		Array<T> a = new Array<T>(coll.getSize());
		a.addAll(coll);
		return a.repositionElementByRank(rank, comp);
	}

/**
 * Increases the capacity of the collection, if necessary,
 * to ensure that it can hold at least <code>capacity</code> elements.
 * @param capacity the desired capacity for the collection
**/

	public void ensureCapacity(int capacity) { }

/**
 * Trims the capacity of
 * this collection to be its current size.  An application can use this
 * operation to minimize space usage.
**/

	public void trimToSize() { }

/**
 * Adds all elements in <code>c</code> to the collection.
 * @param c the collection to be added
**/

	public void addAll(Collection<? extends E> c) {
		for (E e : c)
			add(e);
	}

/**
 * Updates the current collection to contain only
 * elements that are also in <code>c</code>
 * @param c a collection
**/

	public void retainAll(Collection<E> c) {
		Locator<E> loc = iterator();
		while (loc.advance())
			if (!c.contains(loc.get()))
				loc.remove();
	}

/**
 * Removes all items from the collection.
**/

	public void clear() {
		Locator<E> loc = iterator();
		while (loc.advance())
			loc.remove();
	}


	public abstract class AbstractLocator<T extends E> implements Locator<E> {

		protected int versionNumber; //version number for locator
		
		protected AbstractLocator() {
			versionNumber = version.getCount();
		}

/**
 * Compares the version number
 * held by the locator with the current version number of the data structure object.
 * @throws ConcurrentModificationException the locator version number is less
 * than the modification count for the data structure object.
**/

		protected void checkValidity() {
			version.check(versionNumber);
		}

/**
 * Updates the version number for the
 * locator to be the current modification count for the data structure object.
**/

		protected final void updateVersion() {
			versionNumber = Math.max(versionNumber,version.getCount());			
		}

/**
 * Sets the version number of the locator to
 * <code>MAX_VALUE</code> when <code>ignore</code> is true, preventing
 * concurrent modification exceptions from occurring.
 * When <code>ignore</code> is false, it restores the
 * version number to the smaller of the locator's version number and the current
 * modification count for the data structure object
 * @param ignore a boolean flag indicating if concurrent modification exceptions
 * should be disabled
**/

		public void ignoreConcurrentModifications(boolean ignore) {
			if (ignore)
				versionNumber = Integer.MAX_VALUE;
			else
				versionNumber = Math.min(versionNumber, version.getCount());
		}

/**
 * Resets the version number of the locator to the current modification
 * count for the data structure object.
**/

		public void ignorePriorConcurrentModifications() {
			updateVersion();
		}

/**
 * Moves the locator forward
 * to the next element in the collection.
 * @return the element stored at the position
 * to which the locator is moved
 * @throws AtBoundaryException the locator is already at AFT
 * @throws NoSuchElementException the locator is at the last element in
 * the collection.
**/

		public E next() {
			advance();
			return get();
		}


}

	public class VisitingIterator extends AbstractCollection<E>.AbstractLocator<E>
		implements Visitor<E>, Locator<E>, Runnable {

		boolean hasItem = false; 
		E nextItem = null;
		boolean finished = false;
		boolean canceled = false;


		public VisitingIterator() {
			(new Thread(this)).start();  //start the visitor thread
		}

/**
 * Calls the <code>accept</code> method on the collection provided to the constructor
**/

		public synchronized void run() {
			try {
				AbstractCollection.this.accept(this); //begin accept method
			} catch (VisitAbortedException vae) {
				// iteration canceled
			}
			finished = true;  //only finished when it returns
			notify();
		}
		

/**
 * Aborts iteration to terminate
 * the visiting iterator thread when
 * iteration to completion is not required.
 * Subsequent calls to <code>hasNext</code> will return false.
**/

		public synchronized void cancel() {
			canceled = true;
			notify();
		}

/**
 * @param item the
 * element to visit
**/

		public synchronized void visit(E item) throws Exception {
			if (canceled)
				throw new Exception("further iteration canceled");
			nextItem = item; //store next item
			hasItem = true;  //item not yet consumed by application thread
			notify();    //release lock for hasNext
			try {
				wait();  //until notify from next
			} catch (InterruptedException ie) {}
		}

/**
 * @return true  if and only if iteration
 * has not reached the last element
**/

		public synchronized boolean hasNext() {
			if (canceled)
				return false;
			checkValidity();
			while (!hasItem && !finished)
				try { wait(); } catch (InterruptedException ie) {}
			return hasItem;
		}

/**
 * @return the next
 * element in the iteration order, and advances the locator
**/

		public synchronized E next() {
			if (!hasNext())  //has next element only when hasNext is true
				throw new NoSuchElementException();
			hasItem = false; //about to consume nextItem
			notify();        //unblock the visiting thread upon return
			return nextItem; //return the next item
		}


		public E get() { throw new UnsupportedOperationException(); }
		public boolean inCollection() { throw new UnsupportedOperationException(); }
		public boolean advance() { throw new UnsupportedOperationException(); }
		public boolean retreat() { throw new UnsupportedOperationException(); }
		public void remove() { throw new UnsupportedOperationException(); }


		public void ignoreConcurrentModifications(boolean ignore) {
			throw new UnsupportedOperationException();
		}

		public void ignorePriorConcurrentModifications() {
			throw new UnsupportedOperationException();
		}


	}
}
