// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.set;
import java.util.Arrays;
import java.util.Comparator;
import static goldman.Objects.*;
import java.util.NoSuchElementException;
import goldman.collection.AtBoundaryException;
import goldman.collection.AbstractCollection;
import goldman.collection.Locator;
/**
 * This data structure should be considered when only a
 * small fraction of the elements in U will be stored in the collection.
 * It is the right choice in this setting when deletions occur frequently, or
 * if n changes dramatically since it can be resized relatively
 * infrequently.  In the provided implementation, it is only resized when
 * n grows 4-fold, which keeps the expected load within a factor
 * of two of the desired value.  If the space usage can vary even more, then
 * without much degradation in performance, the implementation can be modified
 * to resize less frequently.  However, the space usage
 * required for separate chaining is
 * higher than that for the other data structures.
**/

public class SeparateChaining<E> extends AbstractCollection<E> implements Set<E> {

	ChainItem<E>[] table; // the hash table


	public static final double DEFAULT_LOAD = 1.0;  //default value for target load
	double targetLoad; // the desired value for size/table.length


	static final double MIN_RATIO = 0.5; //keep at or above half the target load
	static final double MAX_RATIO = 2.0; //keep at or below twice the target load
	int minCapacity; // min capacity to maintain based on ensureCapacity
	int lowWaterMark; // min value for size (unless smaller than minCapacity)
	int highWaterMark; // max value for size
	Hasher<? super E> hasher;  // for computing hash codes and the new table size


	static class ChainItem<E> {

		E element;  //element in the collection
		ChainItem<E> next;  //pointer to next item in the chain


/**
 * @param element the element to store in the chain item
 * @param next a reference to the next item
**/

		ChainItem(E element, ChainItem<E> next) {
			this.element = element;
			this.next = next;
		}

/**
 * Marks
 * the chain item as deleted
**/

		void markDeleted() {
			next = this;
		}

/**
 * @return true  if and only if this chain
 * item has been deleted
**/

		boolean isDeleted() {
			return next == this;
		}


	}
/**
 * The default separate chaining hasher provides a default hasher for separate
 * chaining that hashes null to slot 0, and uses the element's hash code
 * for all other elements.
**/

	public static class DefaultSeparateChainingHasher<E> implements Hasher<E> {
		public int getHashCode(E element) {
			return (element == null) ? 0 : element.hashCode();
		}		
		public int getTableSize(int capacity, double load) {
			return (int) Math.ceil(capacity/load);
		}
	}

/**
 * Creates a hash table with the specified capacity (plus one to handle null values), and
 * with the given comparator and hasher
 * @param capacity the desired capacity
 * @param load the target load
 * @param equivalenceTester the comparator that defines equivalence of objects
 * @param hasher the user-supplied hash code computation
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	@SuppressWarnings("unchecked")

	public SeparateChaining(int capacity, double load, 
			Comparator<? super E> equivalenceTester, Hasher<? super E> hasher){
		super(equivalenceTester);
		minCapacity = capacity;               //satisfy Capacity
		targetLoad = load;
		this.hasher = hasher;
		size = 0;
		table = (ChainItem<E>[]) new ChainItem[hasher.getTableSize(capacity, load)];
		size = 0;                             //satisfy Size
		updateResizingBounds();
	}

/**
 * Creates a hash table with the specified capacity (plus one to handle null values), and
 * with the given comparator and default separate chaining hasher
 * @param capacity the desired capacity
 * @param load the target load
 * @param equivalenceTester the comparator that defines equivalence of objects
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0.
**/

	public SeparateChaining(int capacity, double load, 
			Comparator<? super E> equivalenceTester) {
		this(capacity, load, equivalenceTester, new DefaultSeparateChainingHasher<E>());
	}

/**
 * Creates a new set with a default initial capacity, load,
 * equivalence tester, and hasher.
**/

	public SeparateChaining(){
		this(DEFAULT_CAPACITY, DEFAULT_LOAD, DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * Creates a new set with a default initial capacity, load, and hasher.
 * @param equivalenceTester the comparator defining equivalence
**/

	public SeparateChaining(Comparator<? super E> equivalenceTester) {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD, equivalenceTester);
	}

/**
 * Creates a new set with the given capacity, and the default load,
 * equivalence tester, and hasher.
 * @param capacity the desired capacity for the set
**/

	public SeparateChaining(int capacity) {
		this(capacity, DEFAULT_LOAD, DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * Creates a new set with the provided
 * capacity and equivalence tester, using the default load and hasher
 * @param capacity the desired capacity
 * @param equivalenceTester the comparator defining equivalence
**/

	public SeparateChaining(int capacity, Comparator<? super E> equivalenceTester) {
		this(capacity, DEFAULT_LOAD, equivalenceTester);
	}

/**
 * Creates a new set
 * with the provided capacity and load, using the
 * default equivalence tester and hasher.
 * @param capacity the desired capacity
 * @param load the desired load factor
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0
**/

	public SeparateChaining(int capacity, double load){
		this(capacity, load, DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * @return the capacity of the underlying representation for the
 * current hash table to meet the desired load factor
**/

	public int getCapacity() {
		return (int) (table.length*targetLoad);
	}
	


	public static final double A = (Math.sqrt(5.0)-1)/2;

/**
 * @param x an arbitrary integer
 * @return an
 * integer between 0 and m -1
**/

	int hash(int x) {
		double frac = (x * A) - (int) (x * A);  //fractional part of x*A
		int hashValue = (int) (table.length * frac);  //multiply by m
		if (hashValue < 0) // if this is negative add m to get
			hashValue += table.length;  // hashValue mod m
		return hashValue;
	}

/**
 * @param element the target
 * @return a reference to the chain item for an equivalent element in the set,
 * or null if no equivalent element exists.
**/

	ChainItem<E> locate(E element){
		ChainItem<E> x = table[hash(hasher.getHashCode(element))]; //chain to check
		while (x != null && !(equivalent(x.element,element)))
			x = x.next;
		return x;
	}

/**
 * @param element the target
 * @return true  if and only if an equivalent value exists in this set
**/

	public boolean contains(E element) {
		return locate(element) != null;
	}

/**
 * @param target the target element
 * @return an equivalent element that is in the collection
 * @throws NoSuchElementException there is no equivalent element in the
 * collection.
**/

	public E getEquivalentElement(E target) {
		ChainItem<E> ci = locate(target);
		if (ci != null)
			return ci.element;
		else
			throw new NoSuchElementException();
	}

/**
 * @param desiredCapacity the number of elements that can be placed in this set
 * while maintaining the load factor of <code>targetLoad</code>
**/

	void resizeTable(int desiredCapacity){
		if (hasher.getTableSize(desiredCapacity, targetLoad) != table.length) {
			SeparateChaining<E> newTable =
				new SeparateChaining<E>(desiredCapacity, targetLoad, comp, hasher);
			Locator<E> loc = this.iterator();  //iterate through all elements
			while (loc.advance()) {
				E e = loc.get();                         //get next element
				int slot = newTable.hash(hasher.getHashCode(e));  //place at front of its chain
				newTable.table[slot] = new ChainItem<E>(e,newTable.table[slot]); 
			}
			this.table = newTable.table;
			version.increment();   //invalidate all active locators
			updateResizingBounds(); //preserve Threshold
		}
	}


	void updateResizingBounds() {
		lowWaterMark = (int) Math.ceil(table.length*targetLoad*MIN_RATIO);
		highWaterMark = (int) (table.length*targetLoad*MAX_RATIO);		
	}

/**
 * Checks if the hash table is at or below its minimum allowed
 * load, and, if so, resizes within the limitations of the last call
 * to <code>ensureCapacity</code>
**/

	protected void shrinkTableAsNeeded(){
		if (size <= lowWaterMark)
			resizeTable(Math.max(size, minCapacity));
	}

/**
 * Checks if the hash table is at or above its maximum allowed
 * load, and, if so, resizes the table to accommodate the current size
**/

	protected void growTableAsNeeded(){
		if (size >= highWaterMark)   //table at maximum capacity
			resizeTable(size);
	}


	protected void resizeTableAsNeeded() {
		growTableAsNeeded();
		shrinkTableAsNeeded();
	}

/**
 * Resizes the table so that it has the desired load factor
 * @param load the desired load
**/

	public void setTargetLoad(double load){
		this.targetLoad = load;
		updateResizingBounds();
		resizeTableAsNeeded();
	}

/**
 * Resizes the table (if needed), so this set
 * can hold <code>desiredCapacity</code> elements at the target load factor
 * @param desiredCapacity the desired capacity
**/

	public void ensureCapacity(int desiredCapacity){
		minCapacity = desiredCapacity;
		growTableAsNeeded();
	}

/**
 * Adjust the size of the
 * hash table so it is the minimum size needed to maintain the desired
 * load factor.
**/

	public void trimToSize(){
		minCapacity = size;
		resizeTable(size);
	}

/**
 * @param element the element to be inserted into this set
**/

	public void add(E element){
		ChainItem<E> ci;						//check for equivalent element
		if ((ci = locate(element)) != null)  {// found equivalent element
			ci.element = element;   // replace by new element
		} 
		else {
			int slot = hash(hasher.getHashCode(element));  //place new element at front of
			table[slot] = new ChainItem<E>(element,table[slot]); //appropriate chain
			size++;
			growTableAsNeeded();
		}
	}

/**
 * Removes all elements from the collection.
**/

	public void clear() {
		Arrays.fill(table, 0, table.length, null);  //preserve Placement
		size = 0;              //preserve Size
		shrinkTableAsNeeded(); //preserve Proportionality and MinCapacity
		version.increment();   //invalidate markers 
	}

/**
 * Removes an equivalent element (if one exists) from the set
 * @param element the target
 * @return true  if and only if the element was in the set and therefore removed.
**/

	public boolean remove(E element) {
		version.increment();         //invalidate markers 
		int slot = hash(hasher.getHashCode(element));  //compute slot for chain
		ChainItem<E> toRemove = null;   //set to item if found
		ChainItem<E> ptr = table[slot]; //ptr traverses the chain
		if (ptr == null)   //chain empty which means element
			return false;  // is not in the set
		if (equivalent(ptr.element,element)) { //if element first in its chain
			toRemove = table[slot];
			table[slot] = table[slot].next;   //have head pointer bypass it
		}
		while (toRemove == null && ptr.next != null){  //traverse chain looking one ahead
			if (equivalent(ptr.next.element,element)) { //if its next
				toRemove = ptr.next;
				ptr.next = ptr.next.next;  //preserve Placement
			} else
				ptr = ptr.next; //otherwise, move forward in the chain
		}
		if (toRemove != null) {	 //if found perform needed updates
			size--;                  //preserve Size
			toRemove.markDeleted();  //preserve InUse
			shrinkTableAsNeeded();   //preserve Proportionality and MinCapacity
			return true;          
		}
		return false;  //if not found, return false
	}

/**
 * Creates a new marker that is initialized to FORE.
**/

	public Locator<E> iterator(){
		return new Marker(FORE);
	}

/**
 * @param element the target
 * @return a marker initialized to the first equivalent
 * element in the iteration order
 * @throws NoSuchElementException there is no equivalent element in this set.
**/

	public Locator<E> getLocator(E element) {
		ChainItem<E> node = locate(element);
		if (node == null)
			throw new NoSuchElementException();
		return new Marker(hash(hasher.getHashCode(element)), node);
	}


	protected class Marker extends AbstractCollection<E>.AbstractLocator<E> implements Locator<E> {

		private int slot;               //slot for current marker
		private ChainItem<E> chainLoc;  //location within chain table[slot]

/**
 * @param s the slot holding the chain item to mark
 * @param chainLoc a reference to the chain item itself
 * <BR> 
 * REQUIRES: 
 * s is FORE, AFT, or a valid slot in the hash table
**/

		Marker(int s, ChainItem<E> chainLoc){
			slot = s;
			this.chainLoc = chainLoc; 
			versionNumber = version.getCount();
		}	

/**
 * Initializes <code>slot</code> to the given value, and
 * initializes <code>chainLoc</code> to null.
 * @param s the slot to mark
**/

		Marker(int s){
			this(s, null);
		}

/**
 * @return true  if and only if the marker is at an element of the collection
**/

			public boolean inCollection(){
				checkValidity();    //check if marker has been invalidated
				if (chainLoc != null && chainLoc.isDeleted())
					chainLoc = null;     //move to start of current chain
				else if (slot != FORE && slot != table.length && chainLoc == null)
					retreat();
				return (slot != FORE && slot != table.length);
			}

/**
 * @return the element stored at the marker location.
 * @throws NoSuchElementException the marker is logically
 * before the first element or after the last element in the set.
**/

		public E get() {
			if (!inCollection())
				throw new NoSuchElementException();
			return chainLoc.element;
		}

/**
 * @throws AtBoundaryException the marker had already advanced past the end
 * of the collection via an earlier call to either <code>advance</code> or <code>next</code>
 * @throws AtBoundaryException the locator is at AFT since there is
 * no place to advance.
**/

		public boolean advance(){
			checkValidity();   //check if marker has been invalidated
			if (slot == table.length)  //marker is at AFT
				throw new AtBoundaryException("Already after end.");
			if (chainLoc == null || chainLoc.next == null) {	     //move to next slot
				while (++slot < table.length && table[slot] == null); //non-null slot
				if (slot == table.length)					//if at AFT
					return false;							// return false
				chainLoc = table[slot];						// otherwise, set chainLoc
			} else {                   //move to next element in chain
				chainLoc = chainLoc.next;                 
			}
			return true;
		}

/**
 * Moves the marker to the last chain item
 * in slot s
 * @param s the desired slot
**/

		private void toEnd(int s){
			ChainItem<E> ptr = table[s];
			while (ptr.next != null)
				ptr = ptr.next;
			chainLoc = ptr;
		}

/**
 * Moves the marker to the chain item in slot s that
 * precedes its current location, or null if the current location is at the
 * first chain item in slot s
 * @param s the desired slot
 * <BR> 
 * REQUIRES: 
 *  <code>chainLoc</code>
 * references a chain item in <code>table[s]</code>.
**/

		private boolean toPredecessor(int s) {
			if (slot != table.length) {   //marker not at AFT
				ChainItem<E> ptr = table[s];  // first item in current list
				if (chainLoc != ptr) {        //chainLoc not first in slot s
					while (ptr.next != chainLoc)
						ptr = ptr.next;
					chainLoc = ptr;	
					return true;
				} 
			}
			chainLoc = null;	 //chainLoc is first in slot s
			return false;
		}

/**
 * Moves the marker to the previous location
 * @return true  if and only if the updated value for <code>slot</code> is one
 * that is in use.
 * The user program can use this return value to
 * recognize when a marker has moved before the start of the set.
 * @throws AtBoundaryException the marker is at FORE because
 * retreating from FORE is impossible.
**/

		public boolean retreat(){
			checkValidity();  //check if marker has been invalidated
			if (slot == FORE)  //marker at FORE
				throw new AtBoundaryException("Already before front.");
			if (toPredecessor(slot))
				return true;         //there was a previous element in the chain
			while (--slot >= 0 && table[slot] == null); //move to previous non-empty chain
			if (slot == FORE)
				return false;
			toEnd(slot);   //position chainItem at end of chain
			return true;
		}	


		public boolean hasNext(){
			checkValidity();  //check if marker has been invalidated
			if (chainLoc != null && chainLoc.next != null) //next element in current chain
				return true;
			chainLoc = null;   //at end of current chain item
			while (++slot < table.length && table[slot] == null);  //find next non-empty chain
			slot--;  //move back to ``end'' of the preceding empty chain
			return (slot < table.length-1);
		}		

/**
 * Removes the item at the marker
 * @throws NoSuchElementException the marker is
 * logically at FORE or AFT
**/

		public void remove(){
			if (!inCollection())      //calls checkValidity and adjusts marker
				throw new NoSuchElementException();
			ChainItem<E> toRemove = chainLoc; //reference to element to remove
			toPredecessor(slot);
			if (chainLoc == null) {   //no predecessor
				table[slot] = table[slot].next; //remove it
				chainLoc = null;                //move marker to the end of
				slot--;                         //prior slot
			} else {                  //chainLoc references the predecessor
				chainLoc.next = chainLoc.next.next;  //remove it
			}
			shrinkTableAsNeeded();    //preserve Proportionality
			toRemove.markDeleted();   //preserve InUse
			size--;                   //preserve Size
			updateVersion();          //ensures that this locator is still valid
		}


	}

}
