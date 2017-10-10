// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered.digitized;
/**
 * The <code>CompressedTrieNode</code> interface must be supported by any
 * class defining objects to be used as nodes in a compressed trie.
**/

public interface CompressedTrieNode<E> extends TrieNode<E> {
/**
 * Returns the position of the digit used for branching
 * at this node.
**/

	public int bp();
}
