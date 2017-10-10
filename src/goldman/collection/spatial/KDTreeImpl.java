// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
import java.util.NoSuchElementException;
import goldman.collection.Collection;
import goldman.collection.ordered.*;
import goldman.collection.positional.DoublyLinkedList;

class KDTreeImpl<E> extends BinarySearchTree<E> {
/**
 * @param data the element to hold in the node
**/

	class KDNode extends BSTNode {

		KDNode(E data) {
			super(data);
		}

/**
 * @return the element held in the node
**/

		E getData() {
			return data;
		}

/**
 * @return the dimension along which search comparisons should be made with the element in this node
**/

		protected int getDiscriminator() {
			int depth = 0;
			KDNode x = this;
			while (x.parent != null) {
				x = (KDNode) x.parent;
				depth++;
			}
			int k = ((AlternatingComparator) KDTreeImpl.this.comp).getNumDimensions(); 
			return depth % k;
		}

/**
 * @param dimension the dimension along which a and b should be compared
 * @return the node a or b whose element is less according to the given discriminator.  When they
 * are equal with respect to the given dimension, node b is returned arbitrarily.  If node b is a frontier node,
 * node a is returned.
**/

		protected final KDNode min(KDNode a, KDNode b, int dimension) {
			if (b.isFrontier() ||
					((AlternatingComparator<E>) comp).compare(a.data, b.data, dimension) < 0)
				return a;
			else
				return b;
		}

/**
 * @param discriminator the dimension to be used for making comparisons
 * @param nodeDiscriminator the discriminator used during search at the level of this node
 * <BR> 
 * REQUIRES: 
 *  the parameter value of <code>nodeDiscriminator</code>
 * is the correct discriminator
 * for this node's level in the tree.
 * @return a node containing a minimum element in the subtree rooted at this node, where comparisons are
 * made with respect to the given discriminator
**/

		protected KDNode minimumInSubtree(int discriminator, int nodeDiscriminator) {
			boolean goLeft = (discriminator == nodeDiscriminator);
			if (isFrontier() || ( goLeft && ((KDNode) left).isFrontier()))
				return this;
			int nextLevelDiscriminator =
				((AlternatingComparator<E>) comp).nextDiscriminator(nodeDiscriminator);
			KDNode leftMin =
				((KDNode) left).minimumInSubtree(discriminator, nextLevelDiscriminator);
			if (goLeft)
				return leftMin;
			KDNode rightMin =
				((KDNode) right).minimumInSubtree(discriminator, nextLevelDiscriminator);
			return min( min(this, leftMin, discriminator), rightMin, discriminator);
		}

/**
 * <BR> 
 * REQUIRES: 
 *  the tree's comparator was last used to compare a node just above this level,
 * as would be the case if this node's parent had just been found for removal
 * @return a node containing a minimum element in the subtree rooted at this node, where comparisons are
 * made with respect to the discriminator of the node
**/

		protected KDNode minimumInSubtree() {
			AlternatingComparator<E> comparator = (AlternatingComparator<E>) comp;
			int discriminatorAtRoot = comparator.getLastDiscriminatorUsed();
			return minimumInSubtree(discriminatorAtRoot, 
					     comparator.nextDiscriminator(discriminatorAtRoot));
		}

/**
 * @param dimension the dimension along which a and b should be compared
 * @return the node a or b whose element is greater according to the given dimension.
 * When node b is a frontier node or
 * the nodes are equal with respect to the given dimension, node a is returned.
**/

		protected final KDNode max(KDNode a, KDNode b, int dimension) {
			if (b.isFrontier() ||
					((AlternatingComparator<E>) comp).compare(a.data, b.data, dimension) >= 0)
				return a;
			else
				return b;
		}

/**
 * @param discriminator the dimension to be used for making comparisons
 * @param nodeDiscriminator the discriminator used during search at the level of the current node
 * <BR> 
 * REQUIRES: 
 *  the parameter value of nodeDiscriminator is the correct discriminator
 * for this node's level in the tree.
 * @return a node containing a maximum element in the subtree rooted at this node, where comparisons are
 * made with respect to the given discriminator
**/

		protected KDNode maximumInSubtree(int discriminator, int nodeDiscriminator) {
			boolean goRight = (discriminator == nodeDiscriminator);
			if (isFrontier() || (goRight && ((KDNode) right).isFrontier()))
				return this;
			int nextLevelDiscriminator = 
				((AlternatingComparator<E>) comp).nextDiscriminator(nodeDiscriminator);
			KDNode rightMax = 
				((KDNode) right).maximumInSubtree(discriminator, nextLevelDiscriminator);
			if (goRight)
				return rightMax;
			KDNode leftMax = 
				((KDNode) left).maximumInSubtree(discriminator, nextLevelDiscriminator);
			return max( max(this, leftMax, discriminator), rightMax, discriminator);
		}


/**
 * @return a node containing a maximum element in the subtree rooted at this node, where comparisons are
 * made with respect to the discriminator of the node
**/

		protected KDNode maximumInSubtree() {
			AlternatingComparator<E> comparator = (AlternatingComparator<E>) comp;
			int discriminatorAtRoot = comparator.getLastDiscriminatorUsed();
			return maximumInSubtree(discriminatorAtRoot,
					comparator.nextDiscriminator(discriminatorAtRoot));
		}		


/**
 * The method removes this node from the tree, restructuring the tree as needed to
 * preserve the the property InOrder property.
 * @param disc the discriminator used during search at the level of the current node
 * <BR> 
 * REQUIRES: 
 * the parameter to be the correct discriminator for this node's level in the tree.
**/

		protected void remove(int disc) {
			KDNode right = (KDNode) this.right;
			KDNode left = (KDNode) this.left;

			if (right.isFrontier() && !left.isFrontier()) {   //special case: Only the right is empty
				this.right = right = left; 					  //move the only subtree to the right side
				this.left = FRONTIER_L; 
			}
			if (!right.isFrontierNode()) {  			//Case 1: There is a right subtree
				KDNode replacement = right.minimumInSubtree(disc,
						((AlternatingComparator) comp).nextDiscriminator(disc));
				KDTreeImpl.this.remove(replacement);   //remove min from right subtree
				substituteNode(replacement);		   //replace this node by the minimum
			} else { 								   //Case 2: This is a leaf node
				substituteNode(FRONTIER_L);
			}			
		}


//These methods expose some things for the visualizer.
		KDNode getChild(int i) {
			return (KDNode) child(i);
		}

		boolean isFrontierNode() {
			return isFrontier();
		}

		int getSize() {
			return size();
		}
	}
/**
 * Recall that an alternating comparator cycles through each of a fixed
 * number of dimensions, moving to the next dimension each time it is asked to make
 * a comparison.  In this way, the alternating comparator can be used as the
 * basis for searching within a k-d tree using the same search algorithm as
 * for a binary search tree.  That is, since the comparator takes care of cycling through
 * the various dimensions as the search proceeds down the tree, it is not necessary
 * to replace the logic of the <code>find</code> method inherited from BinarySearchTree.
 * @param comparator the alternating comparator for making comparisons
**/

	public KDTreeImpl(AlternatingComparator<E> comparator) {
		super(comparator);
	}

/**
 * @param data the element to be stored in the node
 * @return a newly created KDNode containing the data
**/

	protected KDNode createTreeNode(E data) {
		return new KDNode(data);
	}


	protected void resetComparator() {
		((AlternatingComparator<E>) comp).reset();
	}

/**
 * @param dimension the desired dimension along which the minimum should be found
 * @return a least element in the tree along the given dimension
 * @throws NoSuchElementException the tree is empty
 * @throws IllegalArgumentException the given dimension index is not supported by the tree's comparator
**/

	public E minimum(int dimension) {
		if (getSize() == 0)
			throw new NoSuchElementException();
		if (dimension < 0 ||
				dimension >= ((AlternatingComparator<E>) comp).getNumDimensions())
			throw new IllegalArgumentException("Illegal dimension " + dimension);
		return ((KDNode) root).minimumInSubtree(dimension, 0).getData();
	}

/**
 * @param dimension the desired dimension along which the maximum should be found
 * @return a greatest element in the tree along the given dimension
 * @throws NoSuchElementException the tree is empty
 * @throws IllegalArgumentException the given dimension index is not supported by the tree's comparator
**/

	public E maximum(int dimension) {
		if (getSize() == 0)
			throw new NoSuchElementException();
		if (dimension < 0 ||
				dimension >= ((AlternatingComparator<E>) comp).getNumDimensions())
			throw new IllegalArgumentException("Illegal dimension " + dimension);
		return ((KDNode) root).maximumInSubtree(dimension, 0).getData();
	}

/**
 * @param minCorner the corner of the range having the values in all dimensions at the low end of the range
 * @param maxCorner the corner of the range having the values in all dimensions at the high end of the range
 * <BR> 
 * REQUIRES: 
 * minCorner to be less than or equal to maxCorner along all
 * possible dimensions of the comparator
 * @return a collection of all the elements in the tree that fall
 * within the given range, inclusive
**/

	public Collection<E> withinBounds(E minCorner, E maxCorner) {
		Collection<E> elements = new DoublyLinkedList<E>();
		withinBounds((KDNode) root, minCorner, maxCorner, elements, 0);
		return elements;
	}

/**
 * @param node the root of a subtree in which to search
 * @param minCorner the corner of the range having the values in all dimensions at the low end of the range
 * @param maxCorner the corner of the range having the values in all dimensions at the high end of the range
 * @param elements a collection to which the elements within the range should be added
 * @param dimension the discriminator of the given node
 * <BR> 
 * REQUIRES: 
 * minCorner to be less than or equal to maxCorner along all
 * possible dimensions of the comparator, and that the given dimension be the
 * correct discriminator for the node according to its level in the tree
**/

	private void withinBounds(KDNode node, E minCorner, E maxCorner, 
			Collection<? super E> elements, int dimension) {
		if (node.isFrontierNode())
			return;
		E element = node.getData();
		AlternatingComparator<E> comp = (AlternatingComparator<E>) this.comp;
		int minCompare = comp.compare(element, minCorner, dimension);
		int maxCompare = comp.compare(element, maxCorner, dimension);

		if (minCompare >= 0 && maxCompare <= 0) {  //candidate
			if (comp.noGreaterThan(minCorner, element) &&
					comp.noGreaterThan(element, maxCorner))
				elements.add(element);
		}
		int nextDim = comp.nextDiscriminator(dimension);
		if (minCompare >= 0)
			withinBounds(node.getChild(0), minCorner, maxCorner, elements, nextDim);	
		if (maxCompare <= 0)
			withinBounds(node.getChild(1), minCorner, maxCorner, elements, nextDim);
	}


/**
 * @param element the element to be found
 * @return a reference to a tree node that contains an
 * occurrence of <code>element</code>.  If <code>element</code> is not in the collection,
 * then an insert position is returned, with the
 * <code>parent</code> field of the frontier node
 * set to the node that preceded it on the search path.
**/

	protected KDNode find(E element) {
		resetComparator();
		return (KDNode) super.find(element);
	}

/**
 * @param element the element to be found
 * @return a reference to a tree node that contains an
 * occurrence of <code>element</code>.  If <code>element</code> is not in the collection,
 * then an insert position is returned, with the
 * <code>parent</code> field of the frontier node
 * set to the node that preceded it on the search path.
**/

	protected BSTNode findLastInsertPosition(E element) {
		resetComparator();		
		return super.findLastInsertPosition(element);
	}
	

/**
 * @param element the
 * element to remove
 * @return true  if the element is
 * in the tree.  Otherwise, false  is returned.
**/

	public boolean remove(E element) {
		if (super.remove(element)) {
			version.increment();
			return true;
 		}
		return false;
	}

/**
 * Removes a given KDNode from the tree
 * @param x the node to be removed
**/

	protected void remove(TreeNode x) {
		KDNode toDelete = (KDNode) x;
		toDelete.remove(toDelete.getDiscriminator());
	}


//For visualizer:
	KDNode getRoot() {
		return (KDNode) root;
	}

	public void checkRep() {
		checkRep((KDTreeImpl.KDNode) root,0);
	}

	void checkRep(KDTreeImpl.KDNode node, int discriminator) {
		if (node.isFrontierNode())
			return;
		AlternatingComparator<E> c = (AlternatingComparator<E>) comp;
		int left = -1;
		int right = 0;
		if (!node.getChild(0).isFrontierNode()) {
			left = c.compare((E) node.getChild(0).getData(), (E) node.getData(),
					discriminator);
			if (left >= 0)
				throw new RuntimeException("rep invariant violation left of " +
						node.getData() + " with discriminator " + discriminator);
			checkRep(node.getChild(0), c.nextDiscriminator(discriminator));
		}
		if (!node.getChild(1).isFrontierNode()) {	
			right = c.compare((E) node.getData(), (E) node.getChild(1).getData(),
					discriminator);
			if (right > 0)
				throw new RuntimeException("rep invariant violation right of " +
						node.getData() + " with discriminator " + discriminator);
			checkRep(node.getChild(1), c.nextDiscriminator(discriminator));
		}	
	}
}
