// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
public interface TrieLeafNode<E> extends TrieNode<E> {
/**
 * The <code>TrieLeafNode</code> interface must be supported by any
 * class defining objects to be used as leaf nodes in a trie.
 * Inserts
 * this trie leaf node into the ordered leaf chain
 * immediately after the trie leaf node referenced by <code>ptr</code>.
**/

	void addAfter(TrieLeafNode<E> ptr);
/**
 * Returns true  if and only if this trie leaf node is
 * not in use.  By definition we say that FORE and AFT are deleted since they
 * do not hold an element in the collection.
**/

	boolean isDeleted();
/**
 * Marks this trie leaf node as
 * no longer in use.
**/

	void markDeleted();
/**
 * Returns a reference to the
 * next leaf node in the ordered leaf chain.
**/

	TrieLeafNode<E> next();
/**
 * Returns a reference to
 * previous leaf node in the ordered leaf chain.
**/

	TrieLeafNode<E> prev();
/**
 * Removes this trie leaf
 * node from the ordered leaf chain.
**/

	void remove();
/**
 * Sets the next
 * element in the ordered leaf chain to the leaf node referenced by <code>newNode</code>.
**/

	void setNext(TrieLeafNode<E> newNode);
/**
 * Sets the previous
 * element in the ordered leaf chain to <code>prevNode</code>.
**/

	void setPrev(TrieLeafNode<E> prevNode);
}
