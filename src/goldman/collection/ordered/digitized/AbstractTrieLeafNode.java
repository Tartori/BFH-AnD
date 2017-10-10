// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
/**
 * The AbstractTrieLeafNode class implements methods that can be shared by all
 * implementations of a trie leaf node.
**/

public abstract class AbstractTrieLeafNode<E> 
		extends AbstractTrieNode<E> implements TrieLeafNode<E> {

	TrieLeafNode<E> next;  //refers to next node in ordered leaf chain
	TrieLeafNode<E> prev;  //refers to previous node in ordered leaf chain

/**
 * @return true
**/

	public boolean isLeaf() {
		return true;
	}

/**
 * @return a reference to the next leaf node
 * in the ordered leaf chain
**/

	public TrieLeafNode<E> next() {
		return next;
	}

/**
 * @return a reference to the previous
 * leaf node in the ordered leaf chain
**/

	public TrieLeafNode<E> prev() {
		return prev;
	}

/**
 * @param nextNode the value
 * to set the next pointer.
**/

	public void setNext(TrieLeafNode<E> nextNode) {
		next = (TrieLeafNode<E>) nextNode;
	}

/**
 * @param prevNode the value
 * to set the previous pointer
**/

	public void setPrev(TrieLeafNode<E> prevNode) {
		prev = (TrieLeafNode<E>) prevNode;
	}

/**
 * Marks this trie leaf node as no longer
 * being in the collection
**/

	public void markDeleted() {
		prev = null;
	}

/**
 * @return true  if and only if this
 * trie leaf node is not in use.
**/

	public boolean isDeleted(){
		return prev == null;
	}	

/**
 * @param ptr and places this trie leaf node after
 * the one referenced by <code>ptr</code> in the ordered leaf chain.
**/

	public void addAfter(TrieLeafNode<E> ptr){
		this.setNext(ptr.next());
		ptr.setNext(this);
		this.setPrev(ptr);
		next.setPrev(this);
	}

/**
 * Removes this leaf node from the
 * ordered leaf chain and marks the removed trie leaf node as deleted.
**/

	public void remove(){
		prev.setNext(next);  //removes from ordered leaf chain
		next.setPrev(prev);
		markDeleted();       //marks as deleted
	}



}
