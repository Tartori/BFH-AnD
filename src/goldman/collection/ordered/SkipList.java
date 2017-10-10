// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import goldman.Objects;
import goldman.collection.AbstractCollection;
import goldman.collection.AtBoundaryException;
import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.Tracked;
/**
 * The skip list is a sorted list with additional structure
 * that supports finding an element in expected
 * logarithmic time. Unlike balanced search trees, a skip list achieves logarithmic performance
 * without rearranging the structure when elements are added or removed
 * from the collection.
 * Furthermore, once an element is located, all other skip list methods
 * (e.g., the insertion or removal of
 * an element, or moving forward or backwards in the list)
 * execute in expected constant time.  The
 * key drawback, as compared to a red-black tree, is that the search time is slower.
 * While the probability
 * of a search taking more than logarithmic time is extremely small, there is more variation in the search
 * time than with a red-black tree.
**/

public class SkipList<E> extends AbstractCollection<E> implements 
	OrderedCollection<E>, Tracked<E> {

	public static final int DEFAULT_HEIGHT = 2;
	public static final int MAX_HEIGHT = 32;
	int height;     //max height of any tower
	Tower<E> head;  //sentinel head, also serves role of FORE
	Tower<E> tail;  //sentinel tail, also serves role of AFT
	java.util.Random randseq; //for random number generation


	class Tower<T> {

		T element;  
		Tower<T>[] links; 

/**
 * @param element a reference to the
 * element
 * @param towerHeight the desired height for the tower
**/

@SuppressWarnings("unchecked")

		Tower(T element, int towerHeight) {
			this.element = element;
			links = (Tower<T>[]) new Tower[2 * towerHeight];
		}

/**
 * @return the height of the
 * tower
**/

		final int getTowerHeight() {
			return (links.length) >> 1;
		}


		public String toString() {
			return "" + element;
		}
/**
 * @param level the list to use
 * @return the previous tower in L_{\rm level}.
**/

		Tower<T> prev(int level) {
			return links[getTowerHeight()+level];
		}

/**
 * @param level the list to use
 * @param t a reference for the tower that is to be placed before this tower
 * in L_{\rm level}.
**/

		void setPrev(int level, Tower<T> t) {
			links[getTowerHeight()+level] = t;
		}

/**
 * @param level the list to use
 * @return the next tower in L_{\rm level}.
**/

		Tower<T> next(int level) {
			return links[level];
		}

/**
 * @param level the list to use
 * @param t a reference for the tower that is to be placed after this tower
 * in L_{\rm level}.
**/

		void setNext(int level, Tower<T> t) {
			links[level] = t;
			t.setPrev(level, this);
		}

/**
 * Marks that a tower has been deleted from the collection by
 * setting its level 0 predecessor to null.
**/

		void delete() {
			setPrev(0,null);
		}

/**
 * @return true  exactly when
 * an tower is marked as deleted.
**/

		boolean isDeleted() {
			return prev(0) == null;
		}


	}
/**
 * Creates an empty skip list
 * that uses the provided initial height for head and tail and the provided comparator.
 * @param size the initial size for
 * head and tail
 * @param comp the comparator that defines an ordering
 * among the elements
**/

	public SkipList(int size, Comparator<? super E> comp) {
		super(comp);                      
		randseq = new java.util.Random(); //initialize random number sequence
		height = 0;                       //no towers, so current height is 0
		head = new Tower<E>(null, size);
		tail = new Tower<E>(null, size);
		head.setPrev(0,head);                //satisfy InUse
		for (int i = 0; i < size; i++)       //In all lists, initially,
			head.setNext(i,tail);                //head points to tail
	}

/**
 * Creates an empty skip list
 * using the default height and comparator
**/

	public SkipList() {
		this(DEFAULT_HEIGHT, Objects.DEFAULT_COMPARATOR);
	}

/**
 * Creates an empty skip list using the default
 * comparator and the provided size for the height of the head and tail.
 * @param size the initial size for
 * head and tail,
**/

	public SkipList(int size) {
		this(size, Objects.DEFAULT_COMPARATOR);
	}

/**
 * Creates an empty skip list that uses
 * the given comparator and the default height for head and tail.
 * @param comp the comparator that defines an ordering
 * among the elements
**/

	public SkipList(Comparator<? super E> comp) {
		this(DEFAULT_HEIGHT, comp);
	}

/**
 * @param target the element to locate
 * @return a reference is the first tower in the
 * iteration order that holds an element equivalent to the target (if any).
 * If there is no equivalent element in the collection,
 * then this method is guaranteed to return the tower holding
 * the successor of the target.
**/

	Tower<E> findFirstOccurrence(E target) {	
		Tower<E> left = head;   //leftmost tower of range to search
		Tower<E> right = tail;  //rightmost tower of range to search
		int level = height - 1; //start search at max level in use
		while (level >= 0) {
			Tower<E> next = left.next(level);  //tower after left (at level)
			if (right == next)  //no towers between left and right (at level)
				level--;           //so drop down a level
			else {
				if (comp.compare(target,next.element) > 0) //target > next tower's element
					left = next;           //then update leftmost tower of range
				else {                 //target < next tower's element
					right = next;         //then rightmost tower of range moves
					level--;              //and drop down a level
				}
			}
		}
		return right; 
	}

/**
 * @param target the element
 * to locate
 * @return a reference to the last tower  in the iteration
 * order that holds an element equivalent to the target (if any).
 * If there is no equivalent element in the collection, then
 * this method is guaranteed to return the tower holding the predecessor of
 * the target.
**/

	Tower<E> findLastOccurrence(E target) {
		Tower<E> left = head;   //leftmost tower of range to search
		Tower<E> right = tail;  //rightmost tower of range to search
		int level = height - 1; //start search at max level in use
		while (level >= 0) {
			Tower<E> next = left.next(level);  //next tower at level
			if (right == next)  //no towers between left and right at level
				level--;           //so drop down a level
			else {
				if (comp.compare(target,next.element) >= 0) //target > next tower's element
					left = next;        //then update leftmost tower of range
				else {               //target < next tower's element
					right = next;       //then rightmost tower of range moves
					level--;            //and drop down a level and continue
				}
			}
		}
		return left;    //target (if there) or otherwise predecessor
	}


	public void viewStructure() {
		String out = "";
		for (int index = height-1; index >= 0; index--) { //from top to bottom
			Tower<E> cur = head.next(index);
			out += "Level " + index + ": ";
			while (cur != tail) { //as long as tail not yet reached
				out += cur.element + "  "; //  print the key
				cur = cur.next(index); //  move to the next item
			}
			out += "\n";
		}
		System.out.println("The structure is as follows: \n" + out);
	}
/**
 * @param target the element being tested for membership in the collection
 * @return true  if and only if an equivalent value exists in the collection
**/

	public boolean contains(E target) {
		Tower<E> t = findFirstOccurrence(target);
		return (t != tail && comp.compare(t.element, target) == 0);
	}

/**
 * @param r the desired rank
 * @return the r<sup>th</sup> element in the sorted order, where
 * r = 0 is the minimum.
 * @throws IllegalArgumentException r &lt; 0 or r &ge; n
**/

    	public E get(int r) {
    		if (r < 0 || r >= getSize())
    			throw new IllegalArgumentException();
    		Tower<E> ptr = head.next(0);
    		for (int j=0; j < r; j++, ptr = ptr.next(0));
    		return ptr.element;
    	}

/**
 * @param target the element for which an equivalent element is sought
 * @return an equivalent element in the collection
 * @throws NoSuchElementException there is no equivalent element in the
 * collection.
**/

	public E getEquivalentElement(E target) {
		Tower<E> t = findFirstOccurrence(target);
		if (t != tail && comp.compare(t.element,target) == 0)
			return t.element;
		else
			throw new NoSuchElementException();
	}

/**
 * @return a least element
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public E min() {
		return head.next(0).element;
	}

/**
 * @return a greatest element
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public E max() {
		return tail.prev(0).element;
	}

/**
 * This method does not require that <code>target</code>
 * be in the collection.
 * @param target the element for which to find
 * the predecessor
 * @return the largest
 * element in the ordered collection that is less than <code>target</code>.
 * @throws NoSuchElementException no element in the collection
 * is smaller than <code>target</code>
**/

	public E predecessor(E target) {
		Tower<E> t = findFirstOccurrence(target);
		t = t.prev(0);
		if (t == head)
			throw new NoSuchElementException();
		return t.element;
	}

/**
 * This method does not require that <code>target</code>
 * be in the collection.
 * @param target the element for which to find
 * the successor
 * @return the smallest
 * element in the ordered collection that is greater than <code>target</code>
 * @throws NoSuchElementException no element in the collection
 * is greater than <code>target</code>
**/

	public E successor(E target) {
		Tower<E> t = findLastOccurrence(target);
		t = t.next(0);
		if (t == tail)
			throw new NoSuchElementException();
		return t.element;
	}

/**
 * It resizes <code>head</code> and <code>tail</code> to be no more
 * than twice that of <code>minimumHeight</code> while maintaining the
 * same collection.
 * @param minimumHeight the minimum acceptable height for <code>head</code> and
 * <code>tail</code>
**/

	void resizeHeadAndTail(int minimumHeight) {
		int oldHeight = head.getTowerHeight();
		int newHeight = oldHeight * 2;
		while (newHeight < minimumHeight)
			newHeight *= 2;
			
		Tower<E>[] newHeadLinks = (Tower<E>[]) new Tower[newHeight*2];
		System.arraycopy(head.links, 0, newHeadLinks, 0, oldHeight);
		Arrays.fill(newHeadLinks, oldHeight, newHeight, tail);
		head.links = newHeadLinks;
		
		Tower<E>[] newTailLinks = (Tower<E>[]) new Tower[newHeight*2];
		System.arraycopy(tail.links, oldHeight, newTailLinks, newHeight, oldHeight);
		Arrays.fill(newTailLinks, newHeight + oldHeight, newHeight*2, head);
		tail.links = newTailLinks;

		head.setPrev(0,head);  // satisfy InUse
	}

/**
 * @return true  with probability
 * 1/4.
**/

	final boolean biasedCoinFlip() {
		 return (randseq.nextBoolean() & randseq.nextBoolean());
	}

/**
 * @return the height for
 * a new tower which is randomly selected as the
 * number of biased coin flips made until a true  is obtained
**/

	int selectTowerHeight() {
		int num = 1;
		while (biasedCoinFlip() && num < MAX_HEIGHT)
			num++;
		return num;
	}

/**
 * Inserts <code>element</code> into the collection
 * @param element the
 * new element
 * @return a reference to the newly created tower that holds the given
 * element
**/

	protected Tower<E> insert(E element) {
		int towerHeight = selectTowerHeight(); //pick random height
		int headHeight = head.getTowerHeight();
		if (towerHeight > height)   {    
			height = towerHeight;            //maintain Height property
			if (towerHeight > headHeight)    //must resize if new tower higher
				resizeHeadAndTail(towerHeight); //than current head and tail
		}
		Tower<E> newTower = new Tower<E>(element, towerHeight);
		Tower<E> left = head;   //left boundary of search range (initially head)
		Tower<E> right = tail;  //right boundary of search range (initially tail)
		int level = height - 1; //current level (start at max level in use)
		while (level >= 0) {    //until done with bottom level
			Tower<E> next = left.next(level);  //next tower at current level
			if (next != right && comp.compare(element,next.element) > 0) 
				left = next;                                        //left marker moves to right
			else {					          //element <= next elem.
				right = next;									   //right marker moves to left
				if (level < towerHeight) {   //splice in newTower between left and right at this level
					left.setNext(level,newTower);
					newTower.setNext(level,right);
				}
				level--;					  //move down to next level
			}
		}
		size++;							  //increment size to maintain Size property
		return newTower;
	}

/**
 * Inserts <code>element</code> into the collection.
 * @param element the new element
**/

	public void add(E element) {
		insert(element);
	}

/**
 * Inserts <code>element</code> into the collection.
 * @param element the new element
 * @return a tracker that tracks the new element
**/

	public Locator<E> addTracked(E element) {
		return (Locator<E>) new Tracker(insert(element));
	}

/**
 * @param c the collection for which the elements are to be merged into
 * this skip list
 * <BR> 
 * REQUIRES: 
 *  c is an ordered collection
**/

	void merge(Collection<? extends E> c) {
		int headHeight = head.getTowerHeight();
		Tower<E>[] succ = new Tower[headHeight];
		System.arraycopy(head.links, 0, succ, 0, headHeight);
		for (E element: c) {
			int towerHeight = selectTowerHeight();	            //select height for new tower
			if (towerHeight > height)  {    
				height = towerHeight;                           //maintain Height property
				if (towerHeight > head.getTowerHeight()) {      //must resize if new tower higher
					resizeHeadAndTail(towerHeight); 
					succ = resizePtr(succ);
				}
			}
			Tower<E> newTower = new Tower<E>(element, towerHeight);  //create new tower
			while (succ[0] != tail && comp.compare(succ[0].element, element) < 0)   
				System.arraycopy(succ[0].links, 0, succ, 0, succ[0].getTowerHeight()); 
			for (int i = 0; i < towerHeight; i++) {        //splice in new tower at each level
				succ[i].prev(i).setNext(i, newTower);      //insert new tower in L_i just before succ[i]
				newTower.setNext(i,succ[i]);
			}
		}
	}

/**
 * Resizes <code>succ</code> so that it has the same capacity as the head
 * @param succ an array of tower references
 * <BR> 
 * REQUIRES: 
 *  no new elements have been added since the head and tail were resized
**/

	Tower<E>[] resizePtr(Tower<E>[] succ) {
		int headHeight = head.getTowerHeight();
		Tower<E>[] newPtr = new Tower[headHeight];
		System.arraycopy(succ, 0, newPtr, 0, succ.length);
		Arrays.fill(newPtr, succ.length, headHeight, tail);
		return newPtr;
	}

/**
 * Iterates through <code>c</code> and adds each element in
 * <code>c</code> to the collection
 * @param c the collection to be added
**/

	public void addAll(Collection<? extends E> c) {
		if (c instanceof OrderedCollection && c.getComparator().equals(getComparator()))
			merge(c);
		else
			super.addAll(c);
	}

/**
 * Removes it
 * @param t a reference to a tower
 * in the collection
**/

	void remove(Tower<E> t) {
		int towerHeight = t.getTowerHeight();
		for (int i = 0; i < towerHeight; i++) 
			t.prev(i).setNext(i, t.next(i));  //remove from all levels it's in
		if (height == towerHeight){ //see if skip list height decreases
			while (height > 1 && head.next(height) == tail) //preserves Height property
				height--;
		}
		t.delete();  //preserves InUse property
		size--;      //preserves Size property
	}

/**
 * Removes the first occurrence of the target from the collection,
 * if an equivalent element exists in the collection.
 * @param element the element to remove
 * @return <code>true</code> if an element was removed, and <code>false</code> otherwise.
**/

	public boolean remove(E element) {
		Tower<E> t = findFirstOccurrence(element);
		if (t == tail || comp.compare(t.element, element) != 0)
			return false;
		remove(t);
		return true;
	}

/**
 * Updates the current collection to contain only
 * elements that are also in <code>c</code>.  When c is an ordered collection with
 * the same comparator, this method takes advantage of that fact for improved
 * efficiency.
 * @param c a collection
**/

	public void retainAll(Collection<E> c) {
		if (isEmpty())  //special case for efficiency
			return;
		if (c instanceof OrderedCollection && c.getComparator().equals(getComparator())) {
			Tower<E> ptr = head.next(0);
			for (E element: c) {  //c is sorted
				while (ptr != tail && comp.compare(element, ptr.element) > 0) {
					remove(ptr);
					ptr = ptr.next(0);
				}
				while (ptr != tail && comp.compare(element, ptr.element) == 0) 
					ptr = ptr.next(0);
			}
		}
		else
			super.retainAll(c);
	}

/**
 * Creates a new tracker at FORE.
**/

	public Tracker iterator() {
		return new Tracker(head);
	}

/**
 * Creates a new tracker at AFT.
**/

	public Tracker iteratorAtEnd() {
		return new Tracker(tail);
	}

/**
 * @param element an element to locate
 * @return a tracker to the specified element
 * @throws NoSuchElementException there is no equivalent element
 * in the ordered collection.
**/

	public Tracker getLocator(E element) {
		Tower<E> t = findFirstOccurrence(element);
		if (t == tail || comp.compare(element, t.element) != 0)
			throw new NoSuchElementException();
		return new Tracker(t);
	}


	protected class Tracker extends AbstractCollection<E>.AbstractLocator<E> {

		Tower<E> loc;  //reference to the tracked tower

/**
 * @param loc a reference to
 * the tower to track
**/

		Tracker(Tower<E> loc) {
			this.loc = loc;
		}

/**
 * @return true  if and only if the tracked element is
 * currently in the collection.
**/

		public boolean inCollection() {
			return !loc.isDeleted();
		}

/**
 * @return the tracked element
 * @throws NoSuchElementException tracker is not at an element in
 * the collection.
**/

		public E get() {
			if (!inCollection())
				throw new NoSuchElementException();
			return loc.element;
		}

/**
 * @param t a reference to a tower
 * @return a reference to the first tower in the
 * collection reached starting at <code>t</code>
 * and following the L_0 next
 * references.  If there is no such element, then
 * <code>tail</code> is returned.
**/

		private Tower<E> skipRemovedElements(Tower<E> t) {
			if (!t.isDeleted())
				return t;
			if (t.next(0).isDeleted())
				t.links[0] = skipRemovedElements(t.next(0));
			return t.next(0);
		}

/**
 * Moves the tracker to the next element in the
 * iteration order (or <code>tail</code> if
 * the last element is currently tracked).
 * @return true  if and only if after the update, the tracker is already at
 * an element of the collection.
 * @throws AtBoundaryException the tracker is at <code>tail</code> since there is
 * no place to advance.
**/

		public boolean advance() {
			if (loc == tail)
				throw new AtBoundaryException();
			if (loc.isDeleted())
				loc = skipRemovedElements(loc);
			else
				loc = loc.next(0);
			return loc != tail;
		}

/**
 * Moves the tracker to the previous element in the iteration order
 * (or <code>head</code> if the
 * first element is currently tracked).
 * @return true  if and only if after the update, the tracker is at an element of the collection.
 * @throws AtBoundaryException the tracker is at <code>head</code> since then there is
 * no place to retreat.
**/

		public boolean retreat() {
			if (loc == head)
				throw new AtBoundaryException();
			E oldData = loc.element;
			boolean wasDeleted = loc.isDeleted();
			if (wasDeleted)
				loc = skipRemovedElements(loc);
			loc = loc.prev(0);
			if (wasDeleted && loc != head)
				while (comp.compare(loc.element, oldData) > 0)
					loc = loc.prev(0);
			return loc != head;
		}

/**
 * @return true  if there is some element after the current tracker position.
**/

		public boolean hasNext() {
			if (loc.isDeleted())
				loc = skipRemovedElements(loc);
			return loc.next(0) != tail;
		}

/**
 * Removes the tracked element and updates the tracker to be
 * at the element in the iteration order that preceded the one removed.
 * @throws NoSuchElementException the tracker is at FORE or AFT
**/

		public void remove() {
			SkipList.this.remove(loc);
		}


	}
}
