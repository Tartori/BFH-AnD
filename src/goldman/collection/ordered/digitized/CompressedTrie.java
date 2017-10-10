// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
import goldman.collection.*;
/**
 * The compressed trie performs additional compression
 * on a compact trie.  While the compact trie ensures
 * that all leaves have at least one sibling, there can be an internal node
 * with a single child.  A compressed trie ensures that all nodes
 * have at least two children by replacing <em>any</em> node with a single child by
 * that child.
**/

public class CompressedTrie<E> extends CompactTrie<E> 
	implements DigitizedOrderedCollection<E>, Tracked<E> {

	protected class InternalNode extends Trie<E>.InternalNode implements CompressedTrieNode<E> {

		int bp;   //branch position
		E data;   //reference to the associated data

/**
 * @param bp the branch position
 * for the internal node
 * @param data the leaf node to reference for
 * the data.
**/

		InternalNode(int bp, E data) {
			this.bp = bp;
			this.data = data;
		}

/**
 * @return the data associated with this internal node
**/

		@SuppressWarnings("unchecked")

		public E data() {
			return data;
		}

/**
 * @return the branch position for this internal node.
**/

		public int bp() {
			return bp;
		}

/**
 * It adds <code>child</code>
 * as a child of this node.
 * @param child a reference to a trie node
**/

		protected void setChild(TrieNode<E> child) {
			setChild(child, child.data(), bp);
		}


	}

	protected class LeafNode extends Trie<E>.LeafNode implements CompressedTrieNode<E>, TrieLeafNode<E> {

		LeafNode(E data) {
			super(data);
		}

/**
 * @return the branch position for this LeafNode
**/

		public int bp() {
			return digitizer.numDigits(data());
		}


	}

	protected class CompressedTrieSearchData extends SearchData {
/**
 * @param childIndex the index for the desired child
 * @return the branch position for the specified child of this node
**/

		int childBranchPosition(int childIndex){
			return ((CompressedTrieNode<E>) ptr.child(childIndex)).bp();
		}

/**
 * @return the branch position for the parent
 * of this node
**/

		int parentBranchPosition(){
			return ((CompressedTrieNode<E>) ptr.parent()).bp();
		}

/**
 * @return the number of digits matched so far during a search
**/

		public int numMatches() {
			return numMatches;
		}


	}
/**
 * Creates an empty compressed trie that uses
 * the given digitizer.
 * @param digitizer the digitizer to be
 * used to define the digits for any element
**/

	public CompressedTrie(Digitizer<? super E> digitizer) {
		super(digitizer);
	}

/**
 * @return a newly created and
 * initialized compressed trie search data instance
**/

	protected CompressedTrieSearchData createSearchData() {
		return initSearchData(new CompressedTrieSearchData());
	}

/**
 * @param sd the SearchData
 * instance to initialize to the root
 * @return the initialized SearchData
 * instance
**/

	CompressedTrieSearchData initSearchData(SearchData sd){
		sd.ptr = root;         //place sd at root
		sd.numMatches = 0;     //initially no matches
		sd.bp = 0;             //root has branch position 0
		if (root != null) //only use sd.branchPosition when root isn't null
			((CompressedTrieSearchData) sd).bp = 
						((CompressedTrieNode<E>) root).bp();
		return (CompressedTrieSearchData) sd;
	}

/**
 * @param element the data element
 * @return a newly created compressed trie leaf node holding the given element.
**/

	TrieLeafNode<E> newLeafNode(E element){
		return new LeafNode(element);
	}

/**
 * @param data the associated data for the new node
 * @return a new internal compressed trie node with the
 * specified branch position and data
**/

	TrieNode<E> newInternalNode(int bp, E data) {
		return new InternalNode(bp, data);
	}

/**
 * @param target the target element
 * @return a reference to a leaf node that contains an
 * occurrence of the target if it is found.  Otherwise it returns a reference
 * to the internal node where the search ends.
**/

	FindResult find(E target, SearchData sd){
		if (isEmpty())  //for efficiency
			return FindResult.UNMATCHED;
		initSearchData(sd);
		CompressedTrieSearchData ctsd = (CompressedTrieSearchData) sd; //reduces casting
		int digitsInTarget = digitizer.numDigits(target); //last digit position to check
		for (int d = 0; d < digitsInTarget && !ctsd.atLeaf(); d++, ctsd.numMatches++) {
			if (d == ctsd.bp) { //branch at the current node
				if (ctsd.moveDown(target) == NO_CHILD) //move to next node
					return FindResult.UNMATCHED;          //no match if desired child is null
			} else {  //compare digit d of target and the associated data of current trie node
				int comparison =  digitizer.getDigit(target, d)  
									- digitizer.getDigit(ctsd.ptr.data(), d);	
				if (comparison < 0)  //when digit d of target < digit d of data for current node
						return FindResult.LESS; //target < data of current node
				else if (comparison > 0)
						return FindResult.GREATER;  //target > data of current node
				}
		}
		if 	(ctsd.numMatches == digitsInTarget && !ctsd.atLeaf())
			return FindResult.PREFIX; //processed all digits in target but at internal node
		else 
			return checkMatchFromLeaf(target, ctsd); //otherwise check remaining digits
	}

/**
 * @param newNode a trie node to add that
 * already holds the new element
 * @param sd a SearchData instance
 * <BR> 
 * REQUIRES: 
 *  <code>sd</code> has been
 * set by a call to <code>find</code> using the element associated with <code>newNode</code>,
 * and that the new element will preserve the prefix free requirement for
 * the collection
**/

	protected void addNewNode(TrieNode<E> newNode, SearchData sd) {
		if (isEmpty()) {     //Case 1: the collection is empty
			root = newNode;     //the root should directly reference the new node
		} else {
			CompressedTrieNode<E> searchLoc = (CompressedTrieNode<E>) sd.ptr;
			if (sd.atLeaf() || sd.numMatches() < searchLoc.bp()) {  //Cases 2 and 3
				InternalNode node = 
					(InternalNode) newInternalNode(sd.numMatches(), newNode.data());
				InternalNode searchLocParent = (InternalNode) searchLoc.parent();
				if (searchLocParent == null)          //preserve Parent property
					root = node;
				else
					searchLocParent.setChild(node);
				node.setChild(searchLoc);             //add old leaf as one child
				node.setChild(newNode);               //add the new node as the other child
			} else {                             //Case 4: search ended at a null child
				((InternalNode) searchLoc).setChild(newNode);  //just add the new node
			}
		}
		sd.ptr = newNode;   //set search location to the new node
	}


	protected void remove(TrieNode<E> node) {
		((LeafNode) node).remove();  //preserve OrderedLeafChain
		InternalNode parent = (InternalNode) node.parent();
		int childIndex = digitizer.getDigit(node.data(), parent.bp());
		parent.children[childIndex] = null; //remove node from the trie
		parent.numChildren--;  //preserve NumChildren
		if (parent.numChildren == 1) { //preserve NoSingletonNode
			TrieNode<E> sibling = null; //find only sibling
			for (int i = 0; i < childCapacity && sibling == null; i++) 
				if (parent.child(i) != null) 
					sibling = parent.child(i);
			if (parent == root) //if root was node with single child
				root = sibling;
			else //splice out node with a single child
				((InternalNode) parent.parent()).setChild(sibling); 
		}
		size--; //preserve Size
	}


}
