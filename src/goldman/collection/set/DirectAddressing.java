// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.set;
import goldman.Objects;
import goldman.collection.AtBoundaryException;
import goldman.collection.AbstractCollection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import goldman.collection.Locator;
/**
 * This data structure provides excellent performance, but
 * O(|U|) space is required.  This data structure is the best
 * choice when at least a quarter of the elements in the universe U
 * are expected to be held in the collection.  When n is expected to
 * be much smaller than |U|/4, then either separate chaining or
 * open addressing should be used.
**/

public class DirectAddressing<E> extends AbstractCollection<E> implements Set<E> {

	Hasher<? super E> hasher;                   //computes hash codes
	Object[] table;                             //the underlying array

/**
 * The direct addressing hasher allocates slot 0 for the null element
 * and adds one to each of the naturally computed hash codes for the elements.
**/

	public static class DirectAddressingHasher<E> implements Hasher<E> {
		public int getHashCode(E element) { 
			return (element == null) ? 0 : 1+element.hashCode();
		}
		public int getTableSize(int capacity, double load) {
			return capacity + 1;
		}
	}

/**
 * Creates a hash table with the specified capacity (plus one to handle null values), and
 * with the given comparator and hasher
 * @param capacity the desired capacity
 * @param equivalenceTester the comparator that defines equivalence of objects
 * @param hasher the user-supplied hash code computation
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	protected DirectAddressing(int capacity, double load, 
			Comparator<? super E> equivalenceTester, Hasher<? super E> hasher) {
		super(equivalenceTester);
		this.hasher = hasher;
		table = new Object[hasher.getTableSize(capacity, load)];
		Arrays.fill(table, Objects.EMPTY);
		size = 0;
	}

/**
 * Creates a hash table with the specified capacity (plus one to handle null values), and
 * with the given comparator and default direct addressing hasher 
 * @param capacity the desired capacity
 * @param comp the comparator that defines equivalence of objects
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	protected DirectAddressing(int capacity, Comparator<? super E> comp) {
		this(capacity, 1, comp, new DirectAddressingHasher<E>());
	}	

/**
 * Creates a hash table with the specified capacity (plus one to handle null values), and
 * with the default comparator and direct addressing hasher
 * @param capacity the desired capacity
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public DirectAddressing(int capacity) {
		this(capacity, Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * @return the capacity of the
 * collection
**/

	public int getCapacity() {
		return table.length;
	}

/**
 * @param slot a valid slot of the table
 * @return true  if and only if <code>slot</code> references an element of the set.
 * @throws IllegalHashCodeException <code>slot</code> is
 * not a valid slot
**/

	boolean inUse(int slot){
		try {
			return table[slot] != Objects.EMPTY;
		} catch (ArrayIndexOutOfBoundsException exc){
			throw new IllegalHashCodeException(slot);
		}
	}

/**
 * @param element the target
 * @return the slot number in the table where <code>element</code>
 * is held, or the constant <code>NOT_FOUND</code> if <code>element</code>
 * @throws IllegalHashCodeException <code>hashCode</code> is
 * not a valid slot.
**/

	int locate(E element) {
		return hasher.getHashCode(element);
	}

/**
 * @param element the target
 * @return true  if and only if an equivalent element exists in the collection
**/

	public boolean contains(E element) {
		return inUse(locate(element));
	}

/**
 * @param target the target element
 * @return an equivalent element that is in the collection
 * @throws NoSuchElementException there is no equivalent element in the
 * collection.
**/

	@SuppressWarnings("unchecked")

	public E getEquivalentElement(E target) {
		int slot = locate(target);
		if (!inUse(slot))
			throw new NoSuchElementException();
		else
			return (E) table[slot];
	}
	

/**
 * @throws UnsupportedOperationException it
 * is called for direct addressing
**/

	public void ensureCapacity(int capacity) {
		throw new UnsupportedOperationException();
	}

/**
 * @throws UnsupportedOperationException it
 * is called for direct addressing
**/

	public void trimToSize() {
		throw new UnsupportedOperationException();
	}

/**
 * If an equivalent element <code>x</code> is in the set,
 * then <code>element</code> replaces <code>x</code>
 * @param element the element to add to the set
 * @throws IllegalHashCodeException <code>hashCode</code> is
 * not a valid slot in the table.
**/

	public void add(E element) {
		int slot = locate(element);
		if (!inUse(slot))  
			size++;
		table[slot] = element;
	}

/**
 * Removes the element (if any) held in that slot from
 * this set.
 * @param slot a valid slot number
 * @return <code>true</code>
 * @throws IllegalHashCodeException <code>slot</code> is
 * not a valid slot
**/

	boolean clearSlot(int slot){
		if (inUse(slot)) {
			table[slot] = Objects.EMPTY;
			size--;
			return true;
		}
		return false;
	}

/**
 * @param element the element (if it exists) to be remove from the set
 * @return true  if and only if the element had been in the set and
 * was therefore removed
**/

	public boolean remove(E element) {
		return clearSlot(locate(element)); 
	}

/**
 * Removes all elements from the collection.
**/

	public void clear() {
		Arrays.fill(table, Objects.EMPTY);
		size = 0;
	}

/**
 * Creates a new marker that is placed at FORE
**/

	public Locator<E> iterator() {
		return new Marker(FORE);
	}

/**
 * @param value the target
 * @return a marker initialized at an equivalent
 * element in the set
 * @throws NoSuchElementException there is no equivalent element in this set.
**/

	public Locator<E> getLocator(E value) {
		int location = locate(value);
		if (!inUse(location))
			throw new NoSuchElementException();
		return new Marker(location);
	}


	protected class Marker extends AbstractCollection<E>.AbstractLocator<E> implements Locator<E> {

		private int slot;           //marker location
	

/**
 * @param slot the slot to store within the marker.
 * @throws IllegalArgumentException <code>slot</code> is not FORE (-1),
 * AFT (m), or a valid slot of the table
**/

		Marker(int slot) {
			if (((slot < -1) || (slot >= table.length)))
				throw new IllegalArgumentException("slot is " + slot);
			this.slot = slot;
			versionNumber = version.getCount();
		}

/**
 * This method also updates <code>slot</code> so that it is either
 * FORE, AFT, or a valid slot that is in use.
 * @return true  if and only if the marker is at an element of the collection
**/

		public boolean inCollection() throws ConcurrentModificationException {
			checkValidity();
			if (slot != FORE && slot != table.length && !inUse(slot))
				retreat();
			return (slot != FORE && slot != table.length);
		}

/**
 * @return the element stored at the marker location.
 * @throws NoSuchElementException the marker is logically at FORE
 * or AFT.
**/

		@SuppressWarnings("unchecked")

		public E get() throws ConcurrentModificationException {
			if (!inCollection())
				throw new NoSuchElementException();
			return (E) table[slot];
		}

/**
 * Moves the marker to the next slot in use
 * @return true  if and only if there is an element at the updated marker location.
 * @throws AtBoundaryException the marker is at AFT since there is
 * no place to advance.
**/

		public boolean advance() throws ConcurrentModificationException {
			if (slot == table.length)
				throw new AtBoundaryException("Already after end.");
			checkValidity();
			while (++slot < table.length) //slot will be at AFT when loop exits
				if (inUse(slot))
					return true;
			return false;
		}

/**
 * Moves the marker to the previous slot in use, or to FORE if there is
 * no such slot.
 * @return true  if and only if it retreats to an inuse slot.
 * @throws AtBoundaryException the marker is already at FORE since then there is
 * no place to retreat.
**/

		public boolean retreat() throws ConcurrentModificationException {
			if (slot == FORE)
				throw new AtBoundaryException("Already before front.");
			checkValidity();
			while (--slot >= 0) //slot will be at FORE when loop exits
				if (inUse(slot))
					return true;
			return false;
		}	

/**
 * @return true  if there is some element after the current
 * marker location.
**/

		public boolean hasNext() throws ConcurrentModificationException {
			checkValidity();
			if (slot == table.length) //already at AFT
				return false;
			boolean ans = advance(); //true if there is a next item
			slot--;
			return ans;
		}

/**
 * Removes the element at the marker, and updates the marker to be
 * at the element in the iteration order that preceded the one removed.
 * @throws NoSuchElementException the marker is
 * logically at FORE or AFT
**/

		public void remove() throws ConcurrentModificationException {
			if (!inCollection())
				throw new NoSuchElementException();
			clearSlot(slot);
		}


	}
}
