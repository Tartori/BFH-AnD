// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;

import java.util.Comparator;

import goldman.Objects;
import goldman.collection.Tracked;
/**
 * A balanced binary search tree uses rotations to maintain balance
 * when one path to a leaf becomes "to much longer" than another.  An interesting
 * property of rotations is that no comparisons are needed, and the relative order
 * for equivalent elements is not changed by a rotation.
**/

public abstract class BalancedBinarySearchTree<E> extends BinarySearchTree<E>
	implements OrderedCollection<E>, Tracked<E> {

	public BalancedBinarySearchTree() { 
		this(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp the comparator to use to
 * order the elements
**/

	public BalancedBinarySearchTree(Comparator<? super E> comp) {
		super(comp);
	}

/**
 * Modifies the structure of the tree so that y becomes the
 * left child of <code>y.right</code>
 * @param y reference to a tree node
**/

	void rotateLeft(BSTNode y){  
		BSTNode z = y.right;     // z is y's right child
		y.replaceSubtreeBy(z);   // z replaces y
		y.setRight(z.left);      // y's right child becomes z's old left child
		z.setLeft(y);            // z's left child is now y
	}

/**
 * Modifies the structure of the tree so that y becomes the
 * right child of <code>y.left</code>
 * @param y reference to a tree node
**/

	void rotateRight(BSTNode y){ 
		BSTNode x = y.left;      // x is y's left child
		y.replaceSubtreeBy(x);   // x replaces y
		y.setLeft(x.right);	     // y's left child becomes x's old right child
		x.setRight(y);	         // x's right child is now y
	}

/**
 * Uses a rotation
 * to bring y closer to the root
 * @param y a reference to a tree node.
 * @return a reference to the node
 * that moves from being y's parent to being y's child.
**/

	BSTNode liftUp(BSTNode y) {
		BSTNode parent = y.parent;
		if (y.isLeftChild())
			rotateRight(parent);
		else
			rotateLeft(parent);
		return parent;  //after rotation it's the child
	}


}
