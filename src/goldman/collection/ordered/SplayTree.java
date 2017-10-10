// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import java.util.Comparator;
import java.util.NoSuchElementException;

import goldman.Objects;
import goldman.collection.Locator;
import goldman.collection.Tracked;
/**
 * A splay tree is a form of a balanced binary search tree in which
 * the nodes store no explicit information to enforce a balancing condition.  Instead,
 * a sequence of rotations is used to restructure the search tree so that the
 * element accessed in the last method call is brought to the root of the tree.  It can be shown that
 * if a random sequence of elements is accessed, then this restructuring leads to trees
 * with expected logarithmic height.  Thus, recently accessed elements are near the
 * root and can be located very efficiently.   The amortized complexity for
 * all splay tree methods is logarithmic.  However, as with a binary search tree, a splay tree can
 * degenerate into a sorted list.   The splay tree should be used if
 * the goal is to minimize the expected access time, provided that the access
 * pattern is stable and not uniform (i.e., some elements are accessed much more frequently than others),
 * and provided that elements recently accessed are more
 * likely to be accessed again soon.
**/

public class SplayTree<E> extends BalancedBinarySearchTree<E>
	implements OrderedCollection<E>,Tracked<E> {

	public SplayTree() { 
		this(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp the comparator to use to
 * order the elements
**/

	public SplayTree(Comparator<? super E> comp) {
		super(comp);
	}

/**
 * If <code>stop</code> is null, then <code>x</code> is
 * rotated to the root of the tree.  Otherwise, the method
 * requires that <code>stop</code> is an ancestor of x.
 * @param x a reference to the node  to be
 * moved up the tree
 * @param stop a reference to the node that
 * is the desired parent for <code>x</code> when this method terminates
**/

	private void splay(BSTNode x, BSTNode stop) {
		while (x.parent != stop) {
			if (x.parent.parent == stop){       //Case 1 (zig)
				liftUp(x);
			}
			else if (x.parent.sameSideChild() == x){ //Case 2 (zig-zig)
				liftUp(x.parent);
				liftUp(x);
			}
			else {                              //Case 3 (zig-zag)
				liftUp(x);
				liftUp(x);
			}
		}
	}

/**
 * If <code>element</code> is in the collection, it is brought
 * to the root using the splay method.  Otherwise, the last element
 * accessed on the search path for <code>element</code> is brought to the root
 * with the splay method
 * @param element the target
 * @return true  if and only if an equivalent value exists in the collection
**/

	public boolean contains(E element) {
		BSTNode node = find(element);
		if (!node.isFrontier()){
			splay(node,null);
			return true;
		}
		if (node != root) //if not found splay last node reached
			splay(node.parent, null); //(unless it's already the root)
		return false;
	}

/**
 * It uses splay to bring the minimum element to the root.
 * @return the smallest element in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public E min() {
		if (isEmpty())
			throw new NoSuchElementException();
		BSTNode min = (BSTNode) leftmost(root);
		splay(min,null);
		return min.data(0);
	}

/**
 * It uses splay to bring the maximum element to the root.
 * @return the largest element in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public E max() {
		if (isEmpty())
			throw new NoSuchElementException();
		BSTNode max = (BSTNode) rightmost(root);
		splay(max,null);
		return max.data(0);
	}

/**
 * It uses splay to bring the predecessor to the root.
 * @param element the element for which to find
 * the predecessor
 * @return the maximum value held in the collection that is less
 * than <code>element</code>
 * @throws NoSuchElementException no element in the collection
 * is less than <code>element</code>
**/

	public E predecessor(E element) {
		BSTNode x = findFirstInsertPosition(element);
		x = pred(x);
		if (x == FORE)
			throw new NoSuchElementException();
		else {
			splay(x,null);
			return x.data(0);
		}
	}

/**
 * It uses splay to bring the successor to the root.
 * @param element the element for which to find
 * the successor
 * @return the minimum value held in the collection that is less
 * than <code>element</code>
 * @throws NoSuchElementException no element in the collection
 * is larger than <code>element</code>
**/

	public E successor(E element) {
		BSTNode x = findLastInsertPosition(element);
		x = succ(x);
		if (x == AFT)
			throw new NoSuchElementException();
		else {
			splay(x,null);
			return x.data(0);
		}
	}

/**
 * Inserts <code>element</code> into the ordered collection and then uses
 * <code>splay</code> to bring the new element to the root of the tree.
 * @param element the new element
**/

	public void add(E element) {
		BSTNode node = insert(element);
		size++;
		splay(node,null);
	}

/**
 * Inserts <code>element</code> into the collection.
 * @param element the new element
 * @return a tracker that tracks the new element
**/

	public Locator<E> addTracked(E element) {
		BSTNode node = insert(element);
		splay(node,null);
		size++;
		return new Tracker(node);
	}

/**
 * Uses splay to both bring the successor of <code>x</code> (if
 * <code>x</code> has two children) to be a child of <code>x</code>, and then also uses splay to
 * bring <code>x</code>'s parent to the root.
 * @param x the tree node to remove
 * @throws NoSuchElementException node <code>x</code> has already been removed
**/

	protected void remove(TreeNode x) {
		BSTNode node = (BSTNode) x;
		if (node.isDeleted())		   
			throw new NoSuchElementException();
		BSTNode successor = succ(node);		   
		if (node.left.isFrontier()) 				   
			node.deleteAndReplaceBy(node.right);
		else if (node.right.isFrontier()) 	   	   
			node.deleteAndReplaceBy(node.left);
		else {							      
			splay(successor,node); //splay the successor to be the child of x
			successor.deleteAndReplaceBy(successor.right);
			node.substituteNode(successor);
		}
		if (node.parent != null) // If x's parent is not already at the root
			splay(node.parent,null); // use splay to bring x's parent to the root
		node.parent = successor; 
		node.markDeleted();  
		size--;  
	}

/**
 * As with the other accessors, this method uses splay to bring <code>x</code> to the root.
 * @param x the element to track
 * @return a locator that has been initialized
 * at the given element.
 * @throws NoSuchElementException the given element is not in the collection.
**/

	public Locator<E> getLocator(E x) {
		BSTNode t = find(x);
		if (t.isFrontier())
			throw new NoSuchElementException();
		splay(t,null);
		return new Tracker(t);
	}


}
