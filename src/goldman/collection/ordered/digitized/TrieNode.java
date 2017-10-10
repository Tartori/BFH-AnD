// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
/**
 * The <code>TrieNode</code> interface must be supported by any
 * class defining objects to be used as nodes in a trie.
**/

public interface TrieNode<E> {
/**
 * Returns a reference to the i<sup>th</sup>
 * child.
**/

	public TrieNode<E> child(int i);
/**
 * Returns the data (if any) associated with this trie node.
 * All data elements are held in leaf nodes, but for some trie implementations the internal
 * nodes hold a reference to an element that begins with the common prefix shared by all
 * of its descendants.
**/

	E data();
/**
 * Returns true  if and only if this trie node
 * is a leaf.
**/

	public boolean isLeaf();
/**
 * Returns a reference to the parent (or null for
 * the root).
**/

	public TrieNode<E> parent();
/**
 * Sets the parent
 * reference to be the given trie node.
**/

	public void setParent(TrieNode<E> parent);
}
