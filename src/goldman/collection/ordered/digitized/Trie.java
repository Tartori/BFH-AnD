// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
import goldman.Objects;
import goldman.Pool;
import goldman.collection.*;

import java.util.NoSuchElementException;
/**
 * The trie data structure is the simplest <code>DigitizedOrderedCollection</code>
 * implementation.  However,
 * it typically requires significantly more space than the other
 * data structures.  It should be used only when most prefixes p
 * that occur have at least two distinct elements
 * with that prefix.  The search path to any element is exactly
 * the number of digits (including the end of string character) in
 * the element.
**/

public class Trie<E> extends AbstractCollection<E> 
	implements DigitizedOrderedCollection<E>, Tracked<E> {

	static final int NO_CHILD = -1;
	final TrieLeafNode<E> FORE = new LeafNode(null);
	final TrieLeafNode<E> AFT = new LeafNode(null);


	TrieNode<E> root;
	int childCapacity; //maximum number of children a trie node can have
	Digitizer<? super E> digitizer;
	int base;   //the digitizer's getDigit method returns int in {0, ..., base-1}


	final Pool<SearchData> pool = new Pool<SearchData>() {
		protected SearchData create() {
			return createSearchData();
		}
	};


	protected class InternalNode extends AbstractTrieNode<E> implements TrieNode<E>{

		TrieNode<E>[] children;		//array of b child references
		int numChildren;			//number of non-null children

/**
 * Allocates an array with one
 * slot for each possible child and sets the current number of children to 0.
**/

		@SuppressWarnings("unchecked") 

		InternalNode() {
			children = new TrieNode[childCapacity]; 
			this.numChildren = 0;
		}

/**
 * @param i the index for the desired child
 * @return the i<sup>th</sup> child
 * @throws IllegalArgumentException i is
 * not between 0 and <code>childCapacity</code> -1 (inclusive).
**/

		public TrieNode<E> child(int i){
			if (i < 0 || i >= childCapacity)
				throw new IllegalArgumentException();
			return children[i];
		}

/**
 * @param element the element for
 * which the index of the child is sought
 * @param bp the
 * branch position of
 * the node on which this method is called
 * @return the index for
 * the next node on the search path defined by <code>element</code>.
**/

		public int childIndex(E element, int bp){
			return digitizer.getDigit(element, bp);
		}

/**
 * Sets the associated child for <code>element</code>
 * to <code>child</code>.
 * @param child the new child to add
 * @param element the element defining the search path
 * for the child
 * @param bp the branch position of
 * the node on which this method is called
 * <BR> 
 * REQUIRES: 
 *  <code>child</code> is
 * not null.
 * @return the index at which <code>child</code> is placed
**/

		protected int setChild(TrieNode<E> child, E element, int bp) {
			int i = childIndex(element, bp);
			if (children[i] == null)
				numChildren++;
			children[i] = child;
			child.setParent(this);
			return i;
		}


	}

	protected class LeafNode extends AbstractTrieLeafNode<E> implements TrieNode<E>, TrieLeafNode<E> {

		E data;


		LeafNode(E data) {this.data = data;}


		public E data() {return data;}	


		public String toString(){return "" + data;}
	}

protected class SearchData {

		TrieNode<E> ptr;  //references search data location
		int bp;           //branch position
		int numMatches;   //for use by compact trie

/**
 * @param childIndex the index
 * for the child of interest
 * @return the branch position of that child
**/

		int childBranchPosition(int childIndex) {
			return bp+1;
		}

/**
 * @return the branch position of the parent
**/

		int parentBranchPosition() {
			return bp-1;
		}

/**
 * @return true  if and only if the search data object
 * is currently at a leaf node.
**/

		public boolean atLeaf() {
			return ptr.isLeaf();
		}

/**
 * @return true  if and only if the search data object
 * is currently at the root.
**/

		public boolean atRoot() {
			return ptr == root;
		}

/**
 * @return the number of digits of the element
 * that have been matched so far.
**/

		public int numMatches() {
			return bp;
		}

/**
 * @param element an element to be considered on this search path
 * @return the index of the child branch that would be followed to reach
 * the given element from the current position of this search data object.
**/

		protected int childIndex(E element) {
			return ((InternalNode) ptr).childIndex(element, bp);
		}

/**
 * @param childIndex the index
 * of the child to which to move
 * @return the given index if the SearchData instance moves successfully or
 * <code>NO_CHILD</code> if the specified child does not exist.
**/

		protected int moveDown(int childIndex){
			TrieNode<E> child = ptr.child(childIndex);
			if (child == null)
				return NO_CHILD;
			bp = childBranchPosition(childIndex);
			ptr = child;
			return childIndex;
		}

/**
 * Moves this SearchData instance down one level in the tree
 * @param element the
 * target
 * @return the child index for the current search location, or
 * <code>NO_CHILD</code> if the SearchData instance did not move because a null\
 * child was encountered.
**/

		protected int moveDown(E element) {
			int childIndex = ((InternalNode) ptr).childIndex(element, bp);
			return moveDown(childIndex);
		}

/**
 * @param element the element that defines the search path on which the
 * child node should be placed
 * @param newChild the child node to be added
**/

		protected int extendPath(E element, TrieNode<E> newChild){
			return moveDown(((InternalNode) ptr).setChild(newChild, element, bp));
		}

/**
 * Moves up one level in the tree
 * @return the branch position for current search location.
**/

		protected int moveUp() {
			bp = parentBranchPosition();
			ptr = ptr.parent();
			return bp;
		}

/**
 * @param element the
 * target
 * @return true  if and only if the last step of the
 * search path processed the end of string character
**/

		protected boolean processedEndOfString(E element) {
		 	return (digitizer.isPrefixFree() &&    
		 			 ptr != root && ptr.parent().child(0) == ptr);
	    }

/**
 * Moves the search data position to the left fork root for x
 * @param x the target
 * <BR> 
 * REQUIRES: 
 * this SearchData instance is set by a call to
 * <code>find(x)</code>
**/

		public void retraceToLastLeftFork(E x) {
			while(true) {  
				if (!atLeaf()) {  //Checking if at a left fork
					int childIndex = ((InternalNode) ptr).childIndex(x, bp);
					for (int i = childIndex-1; i>= 0; i--)  //see sd has reached the left fork
						if (moveDown(i) != NO_CHILD)        //Case 1: reached left fork
							return;					       
				}  
				if (atRoot())    //Case 2: at root, and its not a left fork
					return;      
				else 		  
					moveUp();    //Case 3: move up and continue looking for left fork
			}
		}

/**
 * Moves this search data object
 * to the descendant of its current location that is last in the iteration order
 * among its descendants.
**/

		public void moveToMaxDescendant() {
			while (!atLeaf()) {
				int i = childCapacity - 1; //start at rightmost child
				while (moveDown(i) == NO_CHILD)  //moves to child i if it exist
					i--;						  
			}
		}

/**
 * @param c a collection to which all elements in the subtree rooted at this search location
 * are added.
**/

		void elementsInSubtree(Collection<? super E> c){
			if (atLeaf())
				c.add(((TrieLeafNode<E>) ptr).data());
			else {
				for (int i = 0; i < childCapacity; i++)
					if (moveDown(i) != NO_CHILD) {
						elementsInSubtree(c);  //recurse an child i
						moveUp();
					}
			} 
		}


	}

protected static enum FindResult
				{EXTENSION, GREATER, LESS, MATCHED, PREFIX, UNMATCHED}

/**
 * Creates an empty trie that uses
 * the given digitizer.
 * @param digitizer the digitizer to be
 * used
**/

	public Trie(Digitizer<? super E> digitizer) {
		super(Objects.DEFAULT_COMPARATOR);
		childCapacity = digitizer.getBase();
		this.digitizer = digitizer;
		root = null;
		FORE.setNext(AFT);
		AFT.setPrev(FORE);
	}

/**
 * @return a newly created and
 * initialized search data instance
**/

	protected SearchData createSearchData() {
		return initSearchData(new SearchData());
	}

/**
 * @param sd the SearchData
 * instance to initialize to the root
 * @return the initialized SearchData
 * instance
**/

	SearchData initSearchData(SearchData sd){
		sd.ptr = root;
		sd.bp = 0;
		return sd;
	}

/**
 * @param element the element to place in a new leaf node
 * @return a reference to a new leaf node holding the given element
**/

	TrieLeafNode<E> newLeafNode(E element){
		return new LeafNode(element);
	}

/**
 * @param element the target
 * @param sd a SearchData instance to hold the ending location for the search
 * @return the find result from the search
**/

	FindResult find(E element, SearchData sd){
		if (isEmpty())  //for efficiency
			return FindResult.UNMATCHED;
		initSearchData(sd);   //start sd at the root
		int numDigitsInElement = digitizer.numDigits(element);
		while (sd.ptr != null && !sd.atLeaf()) {  //while a child exists
			if (sd.bp == numDigitsInElement)      //Case 1
				return FindResult.PREFIX;       
			if (sd.moveDown(element) == NO_CHILD) //Case 2
				return FindResult.UNMATCHED;	  
		}
		if (sd.ptr != null && sd.bp != numDigitsInElement) //Case 3a
			return FindResult.EXTENSION;				 
		else											   //Case 3b
			return FindResult.MATCHED;
	}

/**
 * @param element the element being tested for membership in the collection
 * @return true  if and only if an equivalent element exists in the collection
**/

	public boolean contains(E element) {
		if (isEmpty()) //for efficiency
			return false;
		SearchData sd = pool.allocate(); //allocate sd from pool
		try {
			return find(element,sd) == FindResult.MATCHED;     
		} finally {
			pool.release(sd);		     //release sd to pool
		}
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
    		Locator<E> loc = iterator();
    		for (int j=0; j < r+1; j++, loc.advance());
    		return loc.get();
    	}

/**
 * @param element the target
 * @return an equivalent element in the collection
 * @throws NoSuchElementException there is no equivalent element in the
 * collection.
**/

	public E getEquivalentElement(E element) {
		SearchData sd = pool.allocate();        
		try {
			if (find(element,sd) == FindResult.MATCHED)
				return sd.ptr.data();
			else
				throw new NoSuchElementException();
		} finally {
			pool.release(sd);                   
		}
	}

/**
 * @return the least element
 * in the collection, as defined by the digitizer.
 * @throws NoSuchElementException the collection is empty.
**/

	public E min() {
		if (isEmpty())
			throw new NoSuchElementException();
		else
			return FORE.next().data();
	}

/**
 * @return the greatest element
 * in the collection, as defined by the digitizer.
 * @throws NoSuchElementException the collection is empty.
**/

	public E max() {
		if (isEmpty())
			throw new NoSuchElementException();
		else
			return AFT.prev().data();
	}

/**
 * If there is some element in the collection less than <code>element</code>
 * then <code>sd</code> is moved to the predecessor.  Otherwise, <code>sd</code> is not changed.
 * @param element the target
 * @param sd a SearchData instance that has already been set by <code>find(element)</code>
 * @param findStatus the return value from <code>find(element)</code>
 * @return true  if there is some element
 * in the collection less than <code>element</code> and otherwise returns false
**/

	protected boolean moveToPred(E element, SearchData sd, FindResult findStatus){
		if (sd.atLeaf() && (findStatus == FindResult.GREATER || 
				findStatus == FindResult.EXTENSION))
			return true;   //Case 1
		if (findStatus != FindResult.GREATER) //Case 2 and Case 3
			sd.retraceToLastLeftFork(element);    
		if (sd.atRoot())     //Rest of Cases 2,3 and Case 4
			return false; 
		else if (!sd.atLeaf())   
			sd.moveToMaxDescendant(); 
		return true;
	}

/**
 * This method does not require that <code>element</code>
 * be in the collection.
 * @param element the element for which to find
 * the predecessor
 * @return the maximum
 * element in the ordered collection that is less than <code>x</code>.
 * @throws NoSuchElementException no element in the collection
 * is less than <code>element</code>
**/

	public E predecessor(E element) {
		if (isEmpty())
			throw new NoSuchElementException();
		SearchData sd = pool.allocate();
		try {			
			if (moveToPred(element, sd, find(element, sd)))
				return sd.ptr.data();
			else
				throw new NoSuchElementException();
		} finally {
			pool.release(sd);
		}
	}

/**
 * This method does not require that <code>element</code>
 * be in the collection.
 * @param element the element for which to find
 * the successor
 * @return the least
 * element in the collection that is greater than <code>element</code>
 * @throws NoSuchElementException no element in the collection
 * is greater than <code>element</code>
**/

	public E successor(E element) {
		if (isEmpty())			//for efficiency
			throw new NoSuchElementException();
		SearchData sd = pool.allocate();
		try {
			FindResult findStatus = find(element,sd);		
			TrieLeafNode<E> succ = AFT;  //init. successor to AFT
			if (findStatus == FindResult.MATCHED)         //Case 1
				succ = ((TrieLeafNode<E>) sd.ptr).next();
			else if (moveToPred(element, sd, findStatus)) //Case 2
				succ = ((TrieLeafNode<E>) sd.ptr).next();
			if (succ == AFT)
				throw new NoSuchElementException();
			else
				return succ.data();
		} finally {
			pool.release(sd);
		}
	}

/**
 * This method has the side affect of
 * moving <code>sd</code> to its lowest ancestor for which the associated data is an extension of
 * <code>prefix</code>.
 * @param prefix the desired prefix
 * @param sd a SearchData
 * instance that has been set by <code>find(prefix)</code>
 * @param findStatus the FindResult value returned by the search used to
 * set <code>sd</code>
**/

    protected void moveToLowestCommonAncestor(E prefix, SearchData sd, 
    													FindResult findStatus) {
    		if (sd.processedEndOfString(prefix))
    			sd.moveUp();
	}

/**
 * @param prefix the desired
 * element for which all completions are sought
 * @param c a collection
 * to which all elements in the collection that consists of <code>prefix</code> followed by any number of
 * (possibly zero) additional digits are added.
**/

	public void completions(E prefix, Collection<? super E> c) {
		if (isEmpty()) //for efficiency
			return;
		SearchData sd = pool.allocate();
		try {
			FindResult findStatus = find(prefix, sd);
			int numDigits = digitizer.numDigits(prefix);
			if (digitizer.isPrefixFree()) {
				numDigits--;							
				if (sd.processedEndOfString(prefix))   //don't process end of string
					sd.moveUp();
			}
			if (findStatus == FindResult.PREFIX || findStatus == FindResult.MATCHED ||
				sd.numMatches() == numDigits)
				sd.elementsInSubtree(c);
			else
				return;
		} finally {
			pool.release(sd);
		}
	}

/**
 * @param prefix the desired prefix
 * @param c a collection to which all
 * elements in the collection that have a longest common
 * prefix with <code>element</code> are added.
**/

	public void longestCommonPrefix(E prefix, Collection<? super E> c) {
		if (isEmpty()) //for efficiency
			return;
		SearchData sd = pool.allocate();
		try {
			moveToLowestCommonAncestor(prefix, sd, find(prefix,sd));
			sd.elementsInSubtree(c);
		} finally {
			pool.release(sd);
		}
	}

/**
 * Modifies
 * the trie (excluding the ordered leaf chain) to include <code>newNode</code>
 * @param newNode a trie node to add that
 * already holds the new element
 * @param sd a SearchData instance
 * set by a call to <code>find(newNode.data())</code>
 * <BR> 
 * REQUIRES: 
 *  adding the new element will
 * preserve the property that the collection is prefix free
**/

	protected void addNewNode(TrieNode<E> newNode, SearchData sd){
		E element = ((LeafNode) newNode).data();
		if (sd.ptr == null) {  //collection is currently empty
			root = new InternalNode();
			sd.ptr = root;
		}
		while (sd.bp < digitizer.numDigits(element) - 1) {
			InternalNode internalNode = new InternalNode();
			((InternalNode) sd.ptr).setChild(internalNode, element, sd.bp++);
			sd.ptr = internalNode;
		}
		((InternalNode) sd.ptr).setChild(newNode, element, sd.bp++);
		sd.ptr = newNode;
	}

/**
 * @param element the element to be added to
 * the collection
 * @return a reference to the newly inserted leaf node.
 * @throws IllegalArgumentException adding
 * <code>element</code> to the collection would violate the requirement that no element in the
 * collection is a prefix of any other element in the collection.  Recall that two equal elements
 * are considered to be prefixes of each other.
**/

	protected TrieLeafNode<E> insert(E element) {
		SearchData sd = pool.allocate();
		try {
			FindResult found = find(element,sd);
			if (found == FindResult.MATCHED || (!digitizer.isPrefixFree() &&
						(found == FindResult.PREFIX || found == FindResult.EXTENSION)))
				throw new IllegalArgumentException(element + " violates prefix-free requirement");
			TrieLeafNode<E> newNode = newLeafNode(element);  //create the new leaf node
			addNewNode(newNode, sd);						  //add it to the trie (sd is updated)
			found = FindResult.MATCHED;  					  //update found (to avoid a new search)
			if (moveToPred(element, sd, found))			      //maintain OrderedLeafChain Property
				newNode.addAfter((TrieLeafNode<E>) sd.ptr);
			else
				newNode.addAfter(FORE);
			size++; 						              //preserve Size Property
			return newNode;								  //return reference to the new leaf node
		} finally {
			pool.release(sd);
		}
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
 * @return a locator that tracks the newly added element
**/

	public Locator<E> addTracked(E element) {
		return new Tracker(insert(element));
	}

/**
 * @param node a reference to the trie
 * node to remove
**/

	void remove(TrieNode<E> node){
		((LeafNode) node).remove();         //preserve OrderedLeafChain
		E element = ((LeafNode) node).data();  //element in node to remove
		int level = digitizer.numDigits(element); //current level in trie
		do {
			InternalNode parent = (InternalNode) node.parent(); //parent of current value for node
			level--;													  //adjust to parent's level
			parent.children[parent.childIndex(element,level)] = null;     //remove node from trie
			parent.numChildren--;								          //preserve NumChildren for parent
			node = parent;									              //move to parent
		} while (node != root && ((InternalNode) node).numChildren == 0); 
		size--;												// preserve Size property
	}

/**
 * It calls an internal method that
 * removes <code>sd.ptr</code> from the trie.
 * @param sd a search data instance that has
 * been set by a search for the element to be removed
 * <BR> 
 * REQUIRES: 
 *  the structure of the Patricia trie has not
 * changed since <code>find</code> was called with <code>sd.ptr.data</code>
**/

	protected void removeImpl(SearchData sd) {
		remove(sd.ptr);
	}

/**
 * Removes from the collection
 * the element (if any) equivalent to <code>element</code>.
 * @param element the element to remove
 * @return <code>true</code> if an element was removed, and <code>false</code> otherwise.
**/

	public boolean remove(E element) {
		if (isEmpty())			//for efficiency
			return false;
		SearchData sd = pool.allocate();
		try {
			if (find(element, sd) != FindResult.MATCHED)
				return false;
			removeImpl(sd);
			return true;
		} finally {
			pool.release(sd);
		}
	}

/**
 * Creates a new tracker that is at FORE.
**/

	public Locator<E> iterator() {
		return new Tracker(FORE);
	}

/**
 * Creates a new tracker that is at AFT.
**/

	public Locator<E> iteratorAtEnd() {
		return new Tracker(AFT);
	}

/**
 * @param x the element to track
 * @return a new tracker that is initialized to track
 * <code>x</code>.
 * @throws NoSuchElementException <code>x</code> is not in the collection.
**/

	public Locator<E> getLocator(E x) {
		SearchData sd = pool.allocate();
		if (find(x,sd) == FindResult.MATCHED) {
			TrieLeafNode<E> t = (TrieLeafNode<E>) sd.ptr;
			pool.release(sd);
			return new Tracker(t);
		}
		else
			throw new NoSuchElementException();
	}


	protected class Tracker extends AbstractCollection<E>.AbstractLocator<E> {

		TrieLeafNode<E> node;

/**
 * @param ptr a pointer to
 * the node to track
**/

		Tracker(TrieLeafNode<E> ptr) {
			this.node = ptr;
		}

/**
 * @return true  if and only if the tracked element is
 * currently in the collection.
**/

		public boolean inCollection() {
			if (node == FORE || node == AFT)
				return false;
			return !node.isDeleted();
		}

/**
 * @return the tracked element
 * @throws NoSuchElementException this tracker is not at an element in
 * the collection.
**/

		public E get() {
			if (!inCollection())
				throw new NoSuchElementException();
			return (E) node.data();
		}

/**
 * Similar to the path compression performed by
 * the union-find data structure (Section~\ref{sec:union-find}),
 * this method performs the optimization
 * of compressing the path of the redirect chain by
 * updating all next pointers to refer directly to the
 * returned element.
 * @param ptr reference to a trie node that is no longer
 * in the collection
 * @return a reference to the element in the collection that follows
 * the element referenced by <code>ptr</code> in
 * the iteration order. If there is no such element
 * AFT is returned.
**/

		protected TrieLeafNode<E> skipRemovedElements(TrieLeafNode<E> ptr) {
			if (ptr == FORE || ptr == AFT || !ptr.isDeleted()) 
				return ptr;
			ptr.setNext(skipRemovedElements(ptr.next()));
			return ptr.next();
		}

/**
 * Moves this tracker to the next element in the iteration order (or {\texttt AFT} if
 * the tracker is currently at the last element).
 * @return true  if and only if, after the update, the tracker is at
 * an element of the collection.
 * @throws AtBoundaryException this tracker is already at AFT since there is
 * no place to advance.
**/

		public boolean advance() {
			if (node == AFT)                      //Case 1
				throw new AtBoundaryException();
			if (node != FORE && node.isDeleted()) //Case 2
				node = skipRemovedElements(node);
			else   								  //Case 3
				node = node.next();	 
			return node != AFT;
		}

/**
 * Moves this tracker to the previous element in the iteration order (or FORE if the
 * tracker is currently at the first element).
 * @return true  if and only if, after the update, the tracker is at an element of the collection.
 * @throws AtBoundaryException this tracker is already at FORE since there is
 * no place to retreat.
**/

		public boolean retreat() {
			if (node == FORE)
				throw new AtBoundaryException();
			if (node != AFT && node.isDeleted())
				node = skipRemovedElements(node);
			node =  node.prev();		
			return node != FORE;
		}

/**
 * @return true  if there is some element in the collection after the currently
 * tracked element.
**/

		public boolean hasNext() {
			if (node.isDeleted())
				skipRemovedElements(node);
			return (node != AFT && node.next() != AFT);
		}

/**
 * Removes the tracked element, leaving the tracker logically between
 * the elements in the iteration order that preceded and followed the one removed.
 * @throws NoSuchElementException the tracker is at FORE or AFT
**/

		public void remove() {
			if (!inCollection())
				throw new NoSuchElementException();
			Trie.this.remove(node);
		}


	}
}
