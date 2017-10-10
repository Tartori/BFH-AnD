// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
import goldman.collection.Digitizer;
import goldman.collection.Tracked;

/**
 * The compact trie data structure modifies the trie
 * by replacing any leaf that
 * has no siblings by its parent.  A compact trie reduces
 * the space complexity of a trie without any additional cost except
 * that the search, insert, and remove methods are slightly more complicated.
 * The length of the search path in a compact trie is at most
 * the number of digits in the elements.  More specifically, it is
 * the length of the prefix needed to distinguish the given element
 * from all other elements in the collection.
**/

public class CompactTrie<E> extends Trie<E> 
	implements DigitizedOrderedCollection<E>, Tracked<E> {
/**
 * Creates an empty compact trie that uses
 * the given digitizer.
 * @param digitizer the digitizer to be
 * used to define the digits for any element
**/

	public CompactTrie(Digitizer<? super E> digitizer) {
		super(digitizer);
	}

/**
 * @param o the data value for the new node
 * @return a new internal trie node holding <code>o</code>
**/

	protected TrieNode<E> newInternalNode(Object o) {
		return new InternalNode();
	}

/**
 * This method continues the matching process
 * @param e the target
 * @param sd a SearchData instance positioned at \ell
 * <BR> 
 * REQUIRES: 
 *  <code>sd</code> has been set by <code>find(e)</code>
 * @return the FindResult value for the search
**/

	FindResult checkMatchFromLeaf(E e, SearchData sd) {
		E leafData = ((TrieLeafNode<E>) sd.ptr).data();
		int stop = Math.min(digitizer.numDigits(e), digitizer.numDigits(leafData));
		while (sd.numMatches < stop) {	 //while digits remain in both e and leafData
			int targetDigit = digitizer.getDigit(e, sd.numMatches);
			int leafDigit = digitizer.getDigit(leafData, sd.numMatches);
			int comparison = targetDigit - leafDigit;	
			if (comparison < 0) {  //Case 1a: e < leafData (possibly a prefix)
				if (digitizer.isPrefixFree() && targetDigit == 0)
					return FindResult.PREFIX;  //prefix when end of string marker processed
				else
					return FindResult.LESS;    //otherwise e less than leafData
			}
			else if (comparison > 0) //Case 1b: e > leafData
				return FindResult.GREATER;
			else {                   //next digit matched
				sd.numMatches++;   
			}
		}
		if (sd.numMatches == digitizer.numDigits(e))  //Case 2a: all digits in e were matched
			if (sd.numMatches == digitizer.numDigits(leafData)) 
				return FindResult.MATCHED;  //if all digits in leafData matched then equal
			else
				return FindResult.PREFIX;   // otherwise, e is a prefix of leafData
		else //Case 2b: all digits in leafData (but not e) processed and matched 
			return FindResult.EXTENSION;  //so e is an extension of leafData
	}

/**
 * @param element the target
 * @param sd the SearchData instance to hold the ending location for the search
 * @return true  if and only if <code>element</code>
 * is in the collection
**/

	FindResult find(E element, SearchData sd){
		if (isEmpty())   //for efficiency
			return FindResult.UNMATCHED;
		initSearchData(sd);  //start sd at the root
		int numDigitsInElement = digitizer.numDigits(element);
		while (sd.bp < numDigitsInElement && !sd.ptr.isLeaf()) {
			if (sd.moveDown(element) == NO_CHILD)
				return FindResult.UNMATCHED;  //Case 1
		}
		if (sd.bp == numDigitsInElement)
			return sd.ptr.isLeaf()? FindResult.MATCHED : FindResult.PREFIX; //Case 2
		else { //Case 3
			sd.numMatches = sd.bp; //set numMatches in sd
			return checkMatchFromLeaf(element, sd); 
		}
	}

/**
 * @param newNode a new trie node holding
 * the element to be added
 * @param sd a SearchData instance
 * <BR> 
 * REQUIRES: 
 *  <code>sd</code> has been
 * positioned by a call to <code>find</code> using the element associated with <code>newNode</code>,
 * and that the new element will preserve the prefix free requirement for
 * the collection
**/

	protected void addNewNode(TrieNode<E> newNode, SearchData sd){
		E element = ((LeafNode) newNode).data();
		if (isEmpty()){  //Case 1: Collection is empty; create internal root node
			root = newInternalNode((LeafNode) newNode);
			sd.ptr = root;
		} else if (sd.atLeaf()) { //Case 2: search ended at leaf; need to split the path
			TrieLeafNode<E> leaf = (TrieLeafNode<E>) sd.ptr;  // save the leaf node
			sd.moveUp();   // begin extending the path from the parent
			while (sd.childIndex(element) == sd.childIndex(leaf.data()))
				sd.extendPath(element, newInternalNode(newNode));
			sd.extendPath(leaf.data(), leaf);  // put the leaf node back into the tree
			sd.moveUp();   // reposition at the parent for the new node
		}
		sd.extendPath(element, newNode); // Case 1, Case 2, and Case 3: add the new node
	}

/**
 * @param parent a reference
 * to the parent
 * @param child a reference to the desired child
 * @return the index
 * of <code>parent</code> the references <code>child</code>
**/

	int getIndexForChild(InternalNode parent,TrieNode<E> child){
		int index = 0;
		while (parent.child(index) != child && index < childCapacity)
			index++;
		return index;
	}

/**
 * @param node a reference to the trie
 * node to remove
**/

	void remove(TrieNode<E> node) {
		((TrieLeafNode<E>) node).remove(); //preserve OrderedLeafChain and InUse
		if (node == root) {  //special case for a collection with one element
			root = null;
		} else {
			InternalNode parent = (InternalNode) node.parent();
			int index = getIndexForChild(parent,node);
			parent.children[index] = null;     //remove reference to node
			parent.numChildren--;              //preserve NumChildren
			if (parent.numChildren == 1) {     //preserve NoSingletonLeaf
				TrieNode<E> onlyChild = null;  //find sibling of node (now an only child) 
				for (index = 0; index < childCapacity && onlyChild == null; index++)
					if (parent.child(index) != null)        //look for sibling of node
						onlyChild = parent.child(index);    //set when to other child when found
				if (onlyChild.isLeaf()) {       //can only collapse if the other child is a leaf
					InternalNode toRemove = null;
					while (parent != root && parent.numChildren == 1) { //find lowest ancestor
						toRemove = parent;							 //  with only 1 child
						parent = (InternalNode) parent.parent();
					}
					index = getIndexForChild(parent, toRemove);   //replace chain of nodes with
					parent.children[index] = onlyChild;		   //  one child by onlyChild
					onlyChild.setParent(parent);
				}
			}
		}
		size--;  //preserve Size
	}


}	
