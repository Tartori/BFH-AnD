// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.set;
import java.util.Comparator;
import static goldman.Objects.*;
/**
 * This data structure should also be considered when only
 * a small fraction of the elements in the universe will be stored in the collection.
 * As compared with separate chaining, for the same space usage, the primary methods
 * generally run faster.  However, the computation costs of the primary methods grow
 * as the number of deletions grows.
 * If deletions are frequent, then separate chaining (or if applicable,
 * direct addressing) should be considered.  Also, as compared with separate chaining,
 * resizing must be performed more frequently if the capacity provided in
 * the constructor (or via <code>ensureCapacity</code>) is not adequate.
**/

public class OpenAddressing<E> extends DirectAddressing<E> implements Set<E> {

	public static final double DEFAULT_LOAD = 0.5; //default value for target load
	double targetLoad; //the desired value for (size+d)/table.length


	int d; //number of deleted slots


	int minCapacity; // min capacity based on ensureCapacity
	int lowWaterMark; // min value for size (unless smaller than minCapacity)
	int highWaterMark; // max value for size

/**
 * The default open addressing hasher provides a default hasher for open
 * addressing that hashes null to slot 0, and uses the element's hash code
 * for all other elements.
**/

	public static class DefaultOpenAddressingHasher<E> implements Hasher<E> {
		
		public int getHashCode(E element) {
			return (element == null) ? 0 :  element.hashCode();
		}
		
		public int getTableSize(int capacity, double load) {
			return (int) Math.pow(2, Math.ceil(Math.log(capacity/load)/Math.log(2)));
		}
	}

/**
 * Creates a hash table with the specified capacity (plus one to handle null values), and
 * with the given comparator and hasher
 * @param capacity the desired capacity
 * @param load the target load
 * @param equivalenceTester the comparator that defines equivalence of objects
 * @param hasher the user-supplied hash code computation
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0 or <code>load</code> &ge; 1.0
**/

	public OpenAddressing(int capacity, double load, 
							Comparator<? super E> equivalenceTester, Hasher<E> hasher){
		super(capacity, load, equivalenceTester, hasher);
		if (load >= 1.0)
			throw new IllegalArgumentException("Load must be < 1");
		this.targetLoad = load;
		minCapacity = capacity;
		d = 0;
		updateResizingBounds();
	}

/**
 * Creates a hash table with the specified capacity (plus one to handle null values), and
 * with the given comparator and default open addressing hasher
 * @param capacity the desired capacity
 * @param load the target load
 * @param equivalenceTester the comparator that defines equivalence of objects
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0 or <code>load</code> &ge; 1.0
**/

	public OpenAddressing(int capacity, double load,
											Comparator<? super E> equivalenceTester) {
		this(capacity, load, equivalenceTester, new DefaultOpenAddressingHasher<E>());
	}

/**
 * Creates a new set with a default initial capacity, load,
 * equivalence tester, and hasher.
**/

	public OpenAddressing(){
		this(DEFAULT_CAPACITY, DEFAULT_LOAD, DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * Creates a new set with a default initial capacity, load, and hasher.
 * @param equivalenceTester the comparator defining equivalence
**/

	public OpenAddressing(Comparator<? super E> equivalenceTester) {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD, equivalenceTester);
	}


/**
 * Creates a new set with the given capacity, and the default load,
 * equivalence tester, and hasher.
 * @param capacity the desired capacity for the set
**/

	public OpenAddressing(int capacity) {
		this(capacity, DEFAULT_LOAD, DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * Creates a new set with the provided
 * capacity and equivalence tester, using the default load and hasher
 * @param capacity the desired capacity
 * @param equivalenceTester the comparator defining equivalence
**/

	public OpenAddressing(int capacity, Comparator<? super E> equivalenceTester) {
		this(capacity, DEFAULT_LOAD, equivalenceTester);
	}

/**
 * Creates a new set
 * with the provided capacity and load, using the
 * default equivalence tester and hasher.
 * @param capacity the desired capacity
 * @param load the desired load factor
 * @throws IllegalArgumentException <code>capacity</code> &lt; 0 or <code>load</code> &ge; 1.0
**/

	public OpenAddressing(int capacity, double load){
		this(capacity, load, DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * @return the capacity of the underlying representation for the
 * current hash table to meet the desired load factor
**/

	public int getCapacity() {
		return (int) Math.floor(table.length*targetLoad-d);
	}

/**
 * @param slot the desired
 * slot
 * <BR> 
 * REQUIRES: 
 *  <code>slot</code> is a valid slot number
 * (i.e., 0 &le; <code>slot</code> &lt; <code>table.length</code>)
 * @return true  if and only if <code>slot</code> is in use.
**/

	boolean inUse(int slot){
		return table[slot] != EMPTY && table[slot] != DELETED;
	}

/**
 * @param slot a slot to be
 * marked as deleted.
 * <BR> 
 * REQUIRES: 
 *  the slot is in use.
**/

	boolean clearSlot(int slot) {
		if (inUse(slot)) {
			table[slot] = DELETED;  //preserves Placement
			d++;                      //preserves MarkedAsDeleted
			size--;	                  //preserves Size
			return true;
		} else
			return false;
	}


	public static double A = (Math.sqrt(5.0)-1)/2; //multiplier for hash function

/**
 * @param x an arbitrary integer
 * @return an
 * integer between 0 and m-1
**/

	protected int hash(int x) {
		double frac = (x * A) - (int) (x * A);  //fractional part of x*A
		int hashValue = (int) (table.length * frac);  //multiply by m
		if (hashValue < 0) // if this is negative add m to get
			hashValue += table.length;  // hashValue mod m
		return hashValue;
	}


	int stepHash(int hashKey) {
		int s = (hashKey % (table.length/2 - 1));
		if (s < 0)
			s += (table.length/2 - 1);
		return 2*s + 1;
	}

/**
 * @param target the element to search for
 * @return the slot referencing an equivalent element to target,
 * or  the insert position if no equivalent element is
 * in the set.
**/

	@SuppressWarnings("unchecked")

	protected int locate(E target) {
		int hashCode = hasher.getHashCode(target);
		int index = hash(hashCode);      //first slot in probe sequence
		int step = stepHash(hashCode);   //step size between probes
		int insertPosition = FORE;
		while (table[index] != EMPTY) {  //continue until an empty slot found
			if (table[index] != DELETED) {
					if (equivalent((E) table[index], target))
						return index;
			} else if (insertPosition == FORE) {
				insertPosition = index;
			}
			index = (index + step) % table.length;  //move forward in probe sequence
		}
		if (insertPosition == FORE)
			insertPosition = index;
		return insertPosition;
	}



	void updateResizingBounds() {
		lowWaterMark = (int) Math.max(minCapacity*targetLoad,
										Math.ceil(table.length*targetLoad/2));
		highWaterMark = (int) (table.length*(1+targetLoad)/2);
	}

/**
 * Resizes the table to
 * preserve the property Proportionality
**/

	void resizeTableAsNeeded() {
		if (size < lowWaterMark || size + d > highWaterMark)
			resizeTable(Math.max(size, minCapacity));	
	}

/**
 * Increases the table size
 * when the number of elements held in the set plus the number of slots marked as deleted
 * has reached the high water mark.
**/

	void growTableAsNeeded() {
		if (size + d > highWaterMark)
			resizeTable(Math.max(size, minCapacity));		
	}

/**
 * Reduces the table size
 * when the number of elements held in the set drops to the lower water mark.
**/

	void shrinkTableAsNeeded() {
		if (size < lowWaterMark)
			resizeTable(Math.max(size, minCapacity));	
	}

/**
 * @param desiredCapacity the number of elements that can be placed in this set
 * while maintaining the load factor of <code>targetLoad</code>
**/

	void resizeTable(int desiredCapacity){
		OpenAddressing<E> newTable = new OpenAddressing<E>(desiredCapacity, 
																targetLoad, comp);
		for (E e: this)    //insert all elements
			newTable.add(e);      //into the new table
		this.table = newTable.table;
		d = 0;
		version.increment();   //invalidate all active markers
		updateResizingBounds();
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
 * Resizes the table if necessary so that it has the desired capacity
 * @param desiredCapacity the desired capacity
**/

	public void ensureCapacity(int desiredCapacity){
		minCapacity = desiredCapacity;
		updateResizingBounds();
		growTableAsNeeded();
	}

/**
 * Adjusts the size of the
 * hash table so that the desired load factor
 * is met as long as this does not increase the hash table size.
**/

	public void trimToSize(){
		minCapacity = Math.min(minCapacity, size); 
		updateResizingBounds();
		shrinkTableAsNeeded();
	}

/**
 * @param element the element to be inserted into this set
**/

	@SuppressWarnings("unchecked")

	public void add(E element) {
		int slot = locate(element);  // get insert position
		if (!inUse(slot)) { // element needs to be added
			size++;
			if (table[slot] == DELETED)
				d--;
		}
		table[slot] = element;
		growTableAsNeeded();  //invalidates active markers
	}

/**
 * Removes an equivalent element (if one exists) from the set
 * @param element the target
 * @return true  if and only if the element was in the set and hence removed.
**/

	public boolean remove(E element) {
		if (super.remove(element)){
			resizeTableAsNeeded();
			return true;
		}
		return false;
	}

/**
 * Removes all elements from the collection.
**/

	public void clear() {
		super.clear();
		d = 0;    //preserved MarkedAsDeleted
	}


}
