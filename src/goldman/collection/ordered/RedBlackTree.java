// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;

import java.util.Comparator;
import goldman.Objects;
import goldman.collection.Tracked;
/**
 * The red-black tree is a balanced binary search tree in
 * which a single bit (a color of red or black) associated with each tree node is used
 * to ensure that the number of comparisons made when searching for any
 * element is at most 2 log<sub>2</sub> n.  Through partial analysis and simulations, it has
 * been shown that a search in a red-black tree constructed from n elements, inserted in a random
 * order, uses about 1.002 log<sub>2</sub> n comparisons, on average.
 * The red-black tree
 * is the best general-purpose ordered collection data structure since it has guaranteed
 * worst-case logarithmic bounds on the tree height and has very fast search time.
 * If it is important to have constant time methods to reach the previous or next elements
 * in the collection, additional structure can be added
 * for this purpose at the expense of increased execution time and
 * space usage.
**/

public class RedBlackTree<E> extends BalancedBinarySearchTree<E>
	implements OrderedCollection<E>, Tracked<E> {


	public class RBNode extends BSTNode {

		boolean colorIsRed;  //true when color is red, false when color is black

/**
 * Creates a new tree node
 * with the specified values
 * @param data the element to hold in this node
 * @param isRed the color for this node
**/

		RBNode(E data, boolean isRed) {
			super(data);
			colorIsRed = isRed;
		}


		final boolean isRed() { return colorIsRed; }


		final boolean isBlack() { return !colorIsRed; }


		final void setRed() { colorIsRed = true; }


		final void setBlack() { colorIsRed = false; }

/**
 * Checks if this node
 * is a black node with two red children, and if
 * so it reverses the colors of the three nodes.
 * @return true  if the recoloring was performed,
 * and false  otherwise
**/

		final boolean recolorRed() {
			if (isBlack() && ((RBNode) left).isRed() && ((RBNode) right).isRed()) {
				setRed();  
				((RBNode) left).setBlack();  
				((RBNode) right).setBlack();
				return true;
			}
			return false;
		}

/**
 * Replaces T(<code>this</code>) by T(<code>x</code>)
 * @param x a reference to a node
 * <BR> 
 * REQUIRES: 
 *  T(<code>left</code>) &le;
 * <code>x.element</code> &le; T(<code>right</code>)
 * @return the possibly updated value of
 * <code>x</code>
**/

		protected BSTNode deleteAndReplaceBy(BSTNode x) {
			x = super.deleteAndReplaceBy(x);
			if (isBlack())
				deleteFixUp((RBNode) x);
			return x;
		}

/**
 * Replaces the node on which this method is called by x
 * @param x a reference to a node
 * <BR> 
 * REQUIRES: 
 *  T(<code>left</code>) &le;
 * <code>x.element</code> &le; T(<code>right</code>)
**/

		protected void substituteNode(BSTNode x) {
			super.substituteNode(x);
			((RBNode) x).colorIsRed = colorIsRed;
		}


	}

	public RedBlackTree() { 
		this(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp the comparator to use to
 * order the elements
**/

	public RedBlackTree(Comparator<? super E> comp) {
		super(comp);
	}

/**
 * @return a reference to a new black frontier node
**/

	protected BSTNode createFrontierNode() {
		BSTNode t = new RBNode(null,false);
		return t;
	}

/**
 * @param data the
 * element to hold in the tree node
 * @return a reference to a new red tree node
**/

	protected BSTNode createTreeNode(E data) {
		return new RBNode(data,true);
	}

/**
 * @param extraRed a reference to
 * a red node that might have a red parent
**/

	void insertFixUp(BSTNode extraRed) {
		while (extraRed != root && ((RBNode) extraRed.parent).isRed()) { //still have violation
			if (((RBNode) extraRed.grandparent()).recolorRed()) //Case 1
				extraRed = extraRed.grandparent();
			else {
				if (extraRed.isLeftChild() != extraRed.parent.isLeftChild()) //Case 2a, zig-zag
					extraRed = liftUp(extraRed);
				((RBNode) extraRed.parent).setBlack();	 //Case 2b, zig-zig (and rest of 2a)
				((RBNode) extraRed.grandparent()).setRed();
				extraRed = liftUp(extraRed.parent);
			}
		}
		((RBNode) root).setBlack();  //preserve RootBlack
	}

/**
 * @param element the new element
 * @return a
 * reference to the newly added node
**/

	protected BSTNode insert(E element) {
		RBNode node = (RBNode) super.insert(element);
		insertFixUp(node);
		return node;
	}

/**
 * @param doubleBlack a reference to a node that is double black
 * <BR> 
 * REQUIRES: 
 *  the only violation of the red-black tree properties
 * is the existence of this one double black node
**/

	@SuppressWarnings("unchecked")

	 void deleteFixUp(RBNode doubleBlack){ // called on a node that has an "double" black
		while (doubleBlack != root && doubleBlack.isBlack()) { //stop if at root or red node
			RBNode sibling = (RBNode) doubleBlack.sibling();
			if (sibling.isRed()){  		       // Case 1
				sibling.setBlack();
				sibling = (RBNode) liftUp(sibling);
				sibling.setRed();
				sibling = (RBNode) sibling.otherChild(doubleBlack);
			}
			if (((RBNode) sibling.left).isBlack() 
					&& ((RBNode) sibling.right).isBlack()){ //Case 2a
				sibling.setRed();
				doubleBlack = (RBNode) doubleBlack.parent;
			} else {
				RBNode sameSideNiece = (RBNode) sibling.sameSideChild();
				if (sameSideNiece.isBlack()){             //Case 2b (excluding color changes)
				RBNode otherSideNiece = (RBNode) sibling.otherChild(sameSideNiece);
					liftUp(otherSideNiece);
					sibling = otherSideNiece;
				}
				sibling.colorIsRed = ((RBNode) sibling.parent).colorIsRed;   //Case 2c (+rest of 2b)
				((RBNode) sibling.parent).setBlack();
				((RBNode) sibling.sameSideChild()).setBlack();
				liftUp(sibling);
				break;	                                 //tree is legal with no doubleBlack remaining
			}
		}
		doubleBlack.setBlack();		//used when loop terminates with a red node as doubleBlack
	}

/**
 * This method is used for testing the invariants.
**/

	@SuppressWarnings("unchecked")
	public void checkRep() {
		if (((RBNode) root).isRed())
			throw new RuntimeException("Root is red.");
		int depth = computeDepth((RBNode) root);
		int maxDepth =  Math.max(1, (int) Math.ceil(Math.log(getSize())/Math.log(2)) + 1);
		if (depth > maxDepth) {
			throw new RuntimeException("Black height exceeded.");
		}
	}

	int computeDepth(RBNode ptr) {
		if (ptr.isFrontier())
			return 0;
		if (ptr.isRed() && ptr.parent != null && ((RBNode) ptr.parent).isRed())
			throw new RuntimeException("Red node " + ptr + "has red parent " + ptr.parent);
		int black = ptr.isBlack()? 1 : 0;
		int leftSide = computeDepth((RBNode) ptr.left);
		int rightSide = computeDepth((RBNode) ptr.right);
		if (leftSide != rightSide)
			throw new RuntimeException("Unbalanced below " + ptr);
		return black + leftSide;
	}
}	
