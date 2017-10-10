// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;

/**
 * The <code>TreeNode</code> interface must be supported by any
 * class defining objects to be used as nodes in a search tree.
**/

public interface TreeNode<E> {

/**
 * Returns true  if and only if this tree node is a frontier node.
**/

	public boolean isFrontier();

/**
 * Returns the i<sup>th</sup> child of this node.
**/

	public TreeNode child(int i);

/**
 * Returns the number of elements held in this tree node.
**/

	public int size();

/**
 * Returns the parent of this tree node.
**/

	public TreeNode<E> parent();
}

