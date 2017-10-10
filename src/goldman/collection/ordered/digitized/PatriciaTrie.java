// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;

import goldman.collection.Digitizer;
import goldman.collection.Tracked;
/**
 * The Patricia trie is a variation of a compressed trie that can
 * be used when the digitizer has base 2 and the collection is naturally prefix-free
 * (without adding an end of string character).  The most common application is for fixed-length
 * binary strings (such as ASCII codes or IP addresses).
 * A Patricia trie reduces the space usage of
 * the compressed trie by letting each node serve the role of both an internal node and
 * leaf node.
**/

public class PatriciaTrie<E> extends CompressedTrie<E> implements DigitizedOrderedCollection<E>,Tracked<E> {

	protected class Node extends CompressedTrie<E>.LeafNode 
				implements CompressedTrieNode<E>, TrieLeafNode<E> {

		int bp;                   //branch position
		TrieNode<E>[] children;   //array of child references

/**
 * @param bp the branch position
 * for this node
 * @param data the data for this node.
**/

		@SuppressWarnings("unchecked")

		Node(int bp, E data) { 
			super(data);      //call the compressed trie LeafNode constructor
			this.bp = bp;     //save the branch position
			children = new TrieNode[childCapacity]; //allocate the children array
		}

/**
 * @return the branch
 * position for the node
**/

		public int bp() {
			return bp;
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
 * @param element the target
 * @return the child that is <em>not</em>
 * on the search path to <code>element</code>
**/

		Node oppositeNode(E element) {
			int index = childIndex(element, bp);
			return (Node) child(1-index);
		}	

/**
 * @param element the element for
 * which the child index sought
 * @param bp the
 * branch position of
 * this node
 * @return the index of
 * the child determined by <code>sp(element)</code> when this node has
 * branch position <code>bp</code>
**/

		public int childIndex(E element, int bp){
			if (bp == -1) return 0;
			else return digitizer.getDigit(element, bp);
		}

/**
 * Gets the appropriate child index for the give element and branch
 * position and places the given child at that
 * index
 * @param child the new child to add
 * @param element the element defining the search path
 * @param bp the branch position of this node
 * @return the index at which the child is placed
**/

		int setChild(TrieNode<E> child, E element, int bp) {
			int i = childIndex(element, bp);
			children[i] = child;
			if (child == root || ((CompressedTrieNode<E>) child).bp() > bp) 
				child.setParent(this);
			return i;
		}

/**
 * @param child the child to add below this node
**/

		void setChild(TrieNode<E> child) {
			setChild(child, child.data(), bp);
		}


	}

	protected class PatriciaSearchData extends CompressedTrieSearchData {

		Node cameFrom;

/**
 * @return true  if and only if the search data object
 * is currently at a leaf node.
**/

		public boolean atLeaf(){
			return ((Node) ptr).bp() <= ((Node) cameFrom).bp();
		}

/**
 * @return true  if and only if the SearchData location
 * is at the search root in its role as an internal node.
**/

		public boolean atRoot() {
			return cameFrom == root;
		}

/**
 * @param childIndex the index
 * of the child to which to move
 * <BR> 
 * REQUIRES: 
 *  <code>childIndex</code> is not 1 if
 * <code>ptr</code> = <code>root</code>
 * @return the index for convenience
**/

		protected int moveDown(int childIndex){
			cameFrom = (Node) ptr;
			ptr = ptr.child(childIndex);
			bp = ((CompressedTrieNode<E>) ptr).bp();
			return childIndex;
		}	

/**
 * Moves this search data instance down
 * one level in the tree
 * @param element the element defining
 * the search path
 * @return the index of the child to which the search data object has moved.
**/

		protected int moveDown(E element){
			return moveDown(((Node) ptr).childIndex(element, bp));
		}

/**
 * Moves the search location to
 * the parent
 * @return the branch position for the new search location.
**/

		protected int moveUp() {
			if (atLeaf()) {   //Case 1
				bp = cameFrom.bp();
				ptr = cameFrom;
				cameFrom = (Node) ptr.parent();
				return bp;
			}
			else {            //Case 2
				cameFrom = (Node) ptr.parent();
				return super.moveUp();
			}
		}

/**
 * Moves the search location to the root of the left fork (if it exists)
 * on the search path defined by x.  If there is no left fork, then it moves the search
 * location to the root.
 * @param x the target
 * <BR> 
 * REQUIRES: 
 *  this SearchData instance is already positioned by a call to
 * <code>find(x)</code>
**/

		public void retraceToLastLeftFork(E x) {
			while (!atRoot()) {  //until search root is reached
				if (atLeaf() || digitizer.numDigits(x) == bp) //Case 1
					moveUp();						                  
				if (((Node) ptr).childIndex(x, bp) == 0)      //Case 2
					moveUp();
				else {					 				      //Case 3
					moveDown(0);			
					return;				
				}	
			}
		}


	}
/**
 * Creates an empty Patricia trie that uses
 * the given digitizer
 * @param digitizer the digitizer for extracting
 * digits from elements at each branch position
 * @throws Exception the base for
 * the provided digitizer is not 2.
**/

public PatriciaTrie(Digitizer<? super E> digitizer) throws Exception {
		super(digitizer);
		if (digitizer.getBase() != 2)
			throw new Exception("Base must be 2");
	}

/**
 * @param element the element to place in the new node
 * @param bp the branch position for the new node
 * @return a reference to a new Node holding the given element and
 * with the given branch position
**/

	Node newNode(E element, int bp) {
		if (isEmpty())
			return new Node(-1, element);
		else
			return new Node(bp, element);
	}

/**
 * @return a newly created and
 * initialized PatriciaSearchData instance
**/

	protected PatriciaSearchData createSearchData() {
		return new PatriciaSearchData();
	}

/**
 * @param sd the SearchData
 * instance to initialize to the search root
 * @return the initialized SearchData
 * instance
**/

	PatriciaSearchData initSearchData(SearchData sd){
		PatriciaSearchData psd = (PatriciaSearchData) sd;  //reduce casting
		psd.ptr = root.child(0);							//start at search root
		psd.cameFrom = (Node) root;	    //previous node is the sentinel root
		psd.bp = ((CompressedTrieNode<E>) psd.ptr).bp();
		psd.numMatches = 0;
		return psd;
	}

/**
 * @param newNode a trie node that
 * already holds the new element
 * @param sd a search data object
 * positioned by a call to <code>find</code> using the element held in <code>newNode</code>
 * <BR> 
 * REQUIRES: 
 *  the new node's branch position has been set to
 * the number of matches that occurred during the search
**/

	protected void addNewNode(TrieNode<E> newNode, SearchData sd){
		PatriciaSearchData psd = (PatriciaSearchData) sd; //to reduce casting
		((Node) newNode).setChild(newNode);               //make newNode and sd.ptr
		((Node) newNode).setChild(sd.ptr);                    //children of newNode
		psd.cameFrom.setChild(newNode);		  //make newNode a child of cameFrom
		psd.ptr = newNode;				      //reset psd to be at newNode (in leaf role)
		psd.cameFrom = (Node) newNode;           //so cameFrom is newNode in internal node role
	}

/**
 * @param element the
 * element to be added to
 * the collection
 * @return a reference to the new Patricia trie node that was inserted.
 * @throws IllegalArgumentException adding
 * <code>element</code> to the collection would violate the requirement that collection is
 * prefix free.
**/

	protected TrieLeafNode<E> insert(E element) {
		if (isEmpty()) {  //special case when collection is empty
			Node newNode = new Node(-1, element);
			root = newNode;
			newNode.children[0] = root;
			newNode.addAfter(FORE);
			size++;
			return newNode;
		}
		PatriciaSearchData sd = (PatriciaSearchData) pool.allocate();
		try {
			FindResult found = find(element,sd);
			if (found == FindResult.PREFIX || found == FindResult.EXTENSION
					|| found == FindResult.MATCHED)
				throw new IllegalArgumentException(element + " violates prefix-free requirement");
			Node newNode = newNode(element, sd.numMatches);
			addNewNode(newNode, sd);
			found = FindResult.MATCHED;
			if (moveToPred(element, sd, found))
				newNode.addAfter((TrieLeafNode<E>) sd.ptr);
			else
				newNode.addAfter(FORE);
			size++;
			return newNode;
		} finally {
			pool.release(sd);
		}
	}

/**
 * Call an internal <code>remove</code> method that takes
 * a reference to the node to remove and its leaf parent
 * @param sd a search data instance that has
 * been set by a search for the element to be removed
 * <BR> 
 * REQUIRES: 
 *  the structure of the Patricia trie has not
 * changed since <code>find</code> was called with <code>sd.ptr.data</code>
**/

	protected void removeImpl(SearchData sd) {
		remove(sd.ptr, ((PatriciaSearchData) sd).cameFrom);
	}

/**
 * @param x a reference to the trie
 * node to remove
**/

	protected void remove(TrieNode<E> x) {
		Node ptrPred = (Node) x;  //predecessor of ptr on search path
		Node ptr = (Node) ptrPred.child(ptrPred.childIndex(x.data(), ptrPred.bp));
		while (ptrPred.bp < ptr.bp) {  //until x is reached in leaf role
			ptrPred = ptr;
			ptr = (Node) ptr.child(ptr.childIndex(x.data(), ptr.bp));
		} 
		remove(x, ptrPred);
	}

/**
 * @param x a reference to the node to remove
 * @param leafParent a reference to the leaf parent of x
**/

	protected void remove(TrieNode<E> x, Node leafParent) {
		((Node) x).remove();  //preserves OrderedLeafChain
		if (size == 1) {  //special case when removing singleton element
			root = null;
			size--;
			return;
		}		
		if (x == root) {  //Case 1: x is sentinel root
			((Node) leafParent.parent()).setChild(leafParent.oppositeNode(x.data()));
			leafParent.bp = -1;
			leafParent.setChild(root.child(0));	
			leafParent.children[1] = null;	
			root = leafParent; 
		}
		else {
			Node leafSibling = leafParent.oppositeNode(x.data()); //sibling of x in its leaf role
			Node grandparent = (Node) leafParent.parent(); //grandparent of node in leaf role
			if (leafParent == x) //Case 2a
				grandparent.setChild(leafSibling);  //make sibling directly child of grandparent
			else {
				leafParent.bp = ((Node) x).bp;		       //Case 3
				((Node) x.parent()).setChild(leafParent);
				if (grandparent == x) {                     //Case 3a
					leafParent.setChild(((Node) x).oppositeNode(x.data()));  
					leafParent.setChild(leafSibling);
				}
				else {									   //Case 3b
					leafParent.setChild(x.child(0)); 
					leafParent.setChild(x.child(1));   
					grandparent.setChild(leafSibling);  
				}
			}
		}
		size--;
	}


}
