// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;

import goldman.collection.Digitizer;
import goldman.collection.Tracked;
/**
 * The ternary search trie (often referred to as
 * a TST) is a hybrid between a trie and
 * a binary search tree that
 * combines the time efficiency of a trie with the space efficiency of
 * a binary search tree.
 * In a ternary search trie, each node has three children.  The branch to take
 * during search is determined by both a branch position and the digit in that position
 * for a distinguished descendant leaf node.  Elements with a smaller
 * digit use the left branch, elements with a equal digit use the middle branch,
 * and elements with a larger digit use the right branch.
**/

public class TernarySearchTrie<E> extends CompactTrie<E> 
	implements DigitizedOrderedCollection<E>,Tracked<E> {

protected class InternalNode extends Trie<E>.InternalNode {

		E comparisonElement; //the comparison character element

/**
 * @param node the leaf node
 * holding the comparison character.
**/

		InternalNode(LeafNode node) {
			comparisonElement = node.data();
		}

/**
 * @return the data associated with the LeafNode <code>dataPtr</code>
**/

		public E data() {
			return comparisonElement;
		}

/**
 * @param element the element
 * with respect to the index of the appropriate child is sought
 * @param bp the
 * branch position of
 * this node
 * @return the index of
 * the child followed by <code>sp(element)</code>, assuming the current node has
 * branch position <code>bp</code>
**/

		public int childIndex(E element, int bp){
			int elementDigit = digitizer.getDigit(element, bp);
			int dataDigit = digitizer.getDigit(data(), bp);
			if (elementDigit < dataDigit)
				return 0;
			else if (elementDigit == dataDigit)
				return 1;
			else 
				return 2;
		}


	}

	protected class TernarySearchTrieSearchData extends SearchData {
/**
 * @param childIndex the index
 * for the child of interest
 * @return the branch position of that child
**/

		int childBranchPosition(int childIndex){
			return (childIndex == 1) ? bp+1 : bp;
		}

/**
 * @return the branch position of the parent
**/

		int parentBranchPosition(){
			return (ptr.parent().child(1) == ptr) ? bp-1 : bp;
		}	

/**
 * @param element the element
 * defining the search path
 * @return true  if and only if the last step of the
 * search path occurred by processing the end of string character
**/

		protected boolean processedEndOfString(E element) {
		 	return (digitizer.isPrefixFree() && ptr != root &&
		 		 	  parentBranchPosition() == digitizer.numDigits(element)-1);
	    }


	}
/**
 * Creates an empty ternary search trie that uses
 * the given digitizer.
 * @param digitizer the digitizer to extract digits
 * from elements
**/

	public TernarySearchTrie(Digitizer<? super E> digitizer) {
		super(digitizer);
		childCapacity = 3;  //only three children per node, regardless of the base
	}

/**
 * @return a newly created and
 * initialized ternary search trie search data instance
**/

	protected SearchData createSearchData() {
		return initSearchData(new TernarySearchTrieSearchData());
	}

/**
 * @param element the data element
 * @return a newly created ternary search trie leaf node holding the given element.
**/

	protected TrieLeafNode<E> newLeafNode(E element, int level) {
		return new LeafNode(element);
	}

/**
 * @param o an object to initialize the data value for the new node
 * @return a new ternary search trie node holding <code>o</code>
**/

	protected TrieNode<E> newInternalNode(Object o) {
		return new InternalNode((LeafNode) o);
	}

/**
 * This method has the side affect of
 * moving <code>sd</code> so that it is at the lowest common ancestor
 * for which the associated data is an extension of <code>prefix</code>.
 * @param prefix a digitized element
 * @param sd a SearchData
 * instance that has been set by a search for <code>prefix</code>
 * @param found the FindResult value returned by the search that was used to
 * set <code>sd</code>
**/

	protected void moveToLowestCommonAncestor(E prefix, 
												SearchData sd, FindResult found) {	
   		while (!(sd.ptr == root || sd.ptr == sd.ptr.parent().child(1) ||
				(found == FindResult.MATCHED && !sd.processedEndOfString(prefix))))
    			sd.moveUp();
    }	


}
