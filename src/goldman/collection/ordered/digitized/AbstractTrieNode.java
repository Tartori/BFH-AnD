// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
/**
 * The AbstractTrieNode class implements methods that can be shared by all
 * implementations of a trie node.
**/

public abstract class AbstractTrieNode<E> implements TrieNode<E> {

	TrieNode<E> parent;

/**
 * @return false
**/

	public boolean isLeaf() {
		return false;
	}

/**
 * @return a reference to the parent trie node
**/

	public TrieNode<E> parent() {
		return parent;
	}

/**
 * @param parent a reference to the
 * new parent reference
**/

	public void setParent(TrieNode<E> parent) {
		this.parent = parent;
	}

/**
 * @param i the index for the desired child
**/

	public TrieNode<E> child(int i) {
		return null;
	}

/**
 * @return the data (if any) associated
 * with the trie node.
**/

	public E data() {
		return null;
	}


}
