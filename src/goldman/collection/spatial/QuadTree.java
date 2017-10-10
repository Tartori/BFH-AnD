// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
import java.util.NoSuchElementException;
import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.Visitor;
import goldman.collection.ordered.AbstractSearchTree;
import goldman.collection.positional.DoublyLinkedList;
import static java.lang.Double.*;
/**
 * A quad tree divides the subdomain into four regions at each
 * internal node.  In general, if there are k dimensions,
 * once can think of each node (in the k-dimensional extension of the quad tree)
 * as grouping k consecutive levels of a k-d tree, giving
 * a combined branching factor of 2<sup>k</sup>.  At each node, a comparison is made
 * along all k dimensions.   To do this compression,
 * a single k-dimensional point is used to partition the domain into
 * 2<sup>k</sup> subdomains.   Thus, the expected
 * height of a k-dimensional extension of quad tree is less
 * than the expected height of a k-d tree by a factor of k.  While the expected number of
 * comparisons is the same, the larger expected depth in a k-d tree slightly
 * increases the search time.  Furthermore, if there is support to make up to 3 comparisons
 * in parallel, then a quad tree (for k=2) or an oct tree (for k=3) leads
 * to significantly faster expected search times since all k comparisons can
 * be made in parallel whereas for a k-d tree the comparisons must be made
 * sequentially.  A k-d tree could also perform two levels of comparison in parallel, but
 * an extra comparison would then be made.
 * Each node in a quad tree contains an element representing an (x,y) point.
 * Each point divides the plane into four quadrants:
 * lower left, lower right, upper right, and upper left.
 * Each node has four subtrees
 * containing elements for points in these respective quadrants.
**/

public class QuadTree<E> extends AbstractSearchTree<E> implements SpatialCollection<E> {

	final QTNode FRONTIER = createTreeNode(null);
	QTNode root = FRONTIER;
	XYComparator<? super E> comp;


	double[] xDistance = new double[4];
	double[] yDistance = new double[4];
	Object[] candidates = new Object[4];
	Box dangerZone = new Box();


	class QTNode extends TreeNode {

		E data;
		Object[] children = new Object[4];
		QTNode parent;

/**
 * @param data the element to be stored in the node
**/

		protected QTNode(E data) {
			this.data = data;
			for (int i = 0; i < 4; i++)
				children[i] = FRONTIER;
		}		

/**
 * @return true  if this node is the frontier node
**/

		protected boolean isFrontier() {
			return this == FRONTIER;
		}

/**
 * @return 1 for non-frontier nodes, or 0 if this is the frontier node
**/

		protected int size() {
			return isFrontier()? 0 : 1;
		}

/**
 * @return 1 for non-frontier nodes, or 0 if this is the frontier node
**/

		protected int capacity() {
			return isFrontier()? 0 : 1;
		}
		

/**
 * @param index an ignored parameter
 * @return the element held in this node.
**/

		protected E data(int index) {
			return data;
		}

/**
 * @param index the index of the desired child (0-3)
 * <BR> 
 * REQUIRES: 
 *  the index is in the range 0 to 3, inclusive
 * @return the child quad tree node
 * with that index.  Note that the return value may be the frontier node.
**/

		protected QTNode child(int index) {
			return (QTNode) children[index];
		}

/**
 * @param child a quad tree node
 * @return the index of the given child within this node, or -1 if the given node is not a direct child of this node
**/

		int indexOf(QTNode child) {
			for (int q=0; q<4; q++)
				if (child(q) == child)
					return q;
			return -1;
		}

/**
 * @param index a child index
 * @param child a quad tree node
 * <BR> 
 * REQUIRES: 
 *  the index is in the range 0 to 3, inclusive, and that
 * this node is not in the subtree of the given child.
**/

		protected void setChild(int index, QTNode child) {
			children[index] = child;
			if (child != null)
				child.parent = this;
		}	


		public void resetParent() {
			if (parent != null) {
				parent.children[parent.indexOf(this)] = FRONTIER;
				parent = null;	
			}
		}

/**
 * @param replacement a replacement quad tree node
**/

		public void replaceBy(QTNode replacement) {
			if (parent == null) {
				root = replacement;
				replacement.parent = null;
			} else {
				parent.setChild(parent.indexOf(this), replacement);
			}
			if (replacement != FRONTIER) {
				for (int q = 0; q < 4; q++)
					replacement.setChild(q, child(q));
			}
			parent = null;
		}


/**
 * @param quadrant the desired search quadrant
 * @return the deepest non-frontier
 * node found by following the chain of children along that quadrant index, or this node itself if it happens to be the frontier node
**/

		protected QTNode deepestInSubtree(int quadrant) {
			if (isFrontier())
				return this;
			QTNode ptr = this;
			while (!ptr.child(quadrant).isFrontier())
				ptr = ptr.child(quadrant);
			return ptr;
		}		

/**
 * @param v a visitor
**/

		public void traverseForVisitor(Visitor<? super E> v) throws Exception {
			if (!isFrontier()) {
				v.visit(data);
				for (int q = 0; q < 4; q++)
					child(q).traverseForVisitor(v);				
			}
		}

/**
 * This method is used for testing the invariants.
 * This method returns a string representation of a quad tree.
**/

/*
\tagcommentx{This method is used for testing the invariants.}
 */
		void checkRep() {
			if (isFrontier())
				return;
			for (int q = 0; q < 4; q++) {
				if (children[q] == null)
					throw new RuntimeException("child " + q + " of " + this + " is null");
				if (!child(q).isFrontier()) {
					if (child(q).parent != this)
						throw new RuntimeException("child " + q + " of " + this + " has the wrong parent");
					int expectedQuadrant = comp.quadrant(data, child(q).data);
					if (expectedQuadrant%4 != q)
						throw new RuntimeException("child " + q + "(" + child(q) + ") of " + this + " is in the wrong quadrant");
				}
			}
			for (int q = 0; q < 4; q++)
				child(q).checkRep();
		}
/*
\tagcommentx{This method returns a string representation of a quad tree.}
 */		
		public String toString() {
			if (isFrontier())
				return "---";
			else
				return "QTNode("+data+")";
		}
	}

	class Box {

		double minx, miny, maxx, maxy;

/**
 * @param minx the minimum x coordinate
 * @param miny the minimum y coordinate
 * @param maxx the maximum x coordinate
 * @param maxy the maximum y coordinate
**/

		Box(double minx, double miny, double maxx, double maxy) {
			set(minx, miny, maxx, maxy);
		}


		Box() {	}

/**
 * @param a an arbitrary quad tree node
 * @param b a second quad tree node
**/

		Box(QTNode a, QTNode b) {
			setCorners(a.data, b.data);
		}

/**
 * @param a an arbitrary quad tree element
 * @param b a second quad tree element
**/

		Box(E a, E b) {
			setCorners(a,b);
		}

/**
 * @param minx the minimum x coordinate
 * @param miny the minimum y coordinate
 * @param maxx the maximum x coordinate
 * @param maxy the maximum y coordinate
 * @return this box
**/

		Box set(double minx, double miny, double maxx, double maxy) {
			this.minx = minx;
			this.miny = miny;
			this.maxx = maxx;
			this.maxy = maxy;
			return this;
		}

/**
 * @param a an arbitrary quad tree element
 * @param b a second quad tree element
**/

		void setCorners(E a, E b) {
			double ax = comp.getX(a), ay = comp.getY(a);
			double bx = comp.getX(b), by = comp.getY(b);
			double minx = Math.min(ax,bx), maxx = Math.max(ax,bx);
			double miny = Math.min(ay,by), maxy = Math.max(ay,by);
			set(minx, miny, maxx, maxy);
		}

/**
 * @param b another box
 * @return true  if and only if the given box has a non-empty intersection with this box.
**/

		final boolean intersects(Box b) {
			return b.contains(minx,miny) ||
				contains(b.minx, b.miny) || contains(b.minx, b.maxy) ||
				contains(b.maxx, b.miny) || contains(b.maxx, b.maxy);
		}

/**
 * @param e an element
 * @return true  if and only if the (x,y) point corresponding to the given element
 * is within, or on the boundary of, this box.
**/

		final boolean contains(E e) {
			return contains(comp.getX(e), comp.getY(e));
		}

/**
 * @param x an x coordinate
 * @param y a y coordinate
 * @return true  if and only if the given (x,y) point is within, or on
 * the boundary of, this box.
**/

		final boolean contains(double x, double y) {
			return (minx <= x && x <= maxx) && (miny <= y && y <= maxy);
		}		

/**
 * @param x an x coordinate
 * @param y a y coordinate
 * @return true  if and only if either the x coordinate is within the (inclusive) range
 * of the minimum and maximum x coordinates of the box, or
 * the y coordinate is within the (inclusive) range
 * of the minimum and maximum y coordinates of the box.
 * This is used in the remove algorithm to determine whether a given node in the tree must
 * be reinserted.
**/

		boolean inRange(double x, double y) {
			return (minx <= x && x <= maxx) || (miny <= y && y <= maxy);
		}

/**
 * @param node an arbitrary quad tree node
 * @return true  if and only if the node is not a frontier node and
 * the (x,y) point corresponding to the node
 * is within the infinite "cross" whose vertical and horizontal bands
 * are defined by the x and y ranges of this box.
**/

		boolean inRange(QTNode node) {
			if (node.isFrontier())
				return false;
			double cx = comp.getX(node.data);
			double cy = comp.getY(node.data);
			return inRange(cx, cy);
		}


	}
/**
 * The default constructor uses a default comparator,
 * under the assumption that type E implements the <code>XYPoint</code> interface.
 * If type E does not implement XYPoint, then an XYComparator for E should
 * be provided, using the other constructor.
 * Otherwise, a <code>ClassCastException</code> will occur
 * when the second element is added to the quad tree.
**/

	public QuadTree() {
		this(new DefaultXYComparator());
	}

/**
 * @param comp an XYComparator for comparing the elements of this collection
**/

	public QuadTree(XYComparator<? super E> comp) {
		super(comp);
		this.comp = (XYComparator<? super E>) comp;
	}	


	public void checkRep() {
		root.checkRep();
	}

	protected QTNode createTreeNode(E data) {
		return new QTNode(data);
	}

/**
 * @param q a given quadrant
 * @return the opposite quadrant
**/

	static final int conjugate(int q) {
		return (q+2) % 4;
	}

/**
 * @param q a given quadrant
 * @return the adjacent quadrant in the clockwise direction
**/

	static final int leftNeighbor(int q) {
		return (q+3) % 4;
	}

/**
 * @param q a given quadrant
 * @return the adjacent quadrant in the counterclockwise direction
**/

	static final int rightNeighbor(int q) {
		return (q+1) % 4;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(root, "", sb);
		return sb.toString();
	}

	void toString(QTNode node, String indent, StringBuilder sb) {
		sb.append(indent);
		sb.append(node);
		sb.append("\n");
		if (node.isFrontier())
			return;
		for (int q = 0; q < 4; q++)
			toString(node.child(q), indent+"   ", sb);
	}
/**
 * @param dimension where 0 is the x dimension and 1 is the y dimension
 * @return a maximum element along the given dimension
**/

	public E max(int dimension) {
		if (dimension < 0 || dimension > 1)
			throw new IllegalArgumentException("dimension 0 or 1 required");
		if (isEmpty())
			throw new NoSuchElementException();
		return max(root.data, root, dimension);
	}

/**
 * @param candidate the maximum found so far
 * @param subtree the root of the subtree in which to search
 * @param dimension where 0 is the x dimension and 1 is the y dimension
 * @return a maximum element among the given candidate and the elements in
 * the given subtree, along the given dimension of comparison
**/

	private E max(E candidate, QTNode subtree, int dimension) {
		if (!subtree.isFrontier()) {
			candidate = max(subtree.data, subtree.child(2), dimension);
			if (dimension == 0) { // x dimension
				E b = max(subtree.data, subtree.child(1), dimension);
				if (comp.compareX(b, candidate) > 0)
					candidate = b;
			} else { // y dimension
				E b = max(subtree.data, subtree.child(3), dimension);
				if (comp.compareY(b, candidate) > 0)
					candidate = b;
			}
		} 
		return candidate;
	}

/**
 * @param dimension where 0 is the x dimension and 1 is the y dimension
 * @return a minimum element along the given dimension
**/

	public E min(int dimension) {
		if (dimension < 0 || dimension > 1)
			throw new IllegalArgumentException("dimension 0 or 1 required");
		if (isEmpty())
			throw new NoSuchElementException();
		return min(root.data, root, dimension);
	}

/**
 * @param candidate the minimum found so far
 * @param subtree the root of the subtree in which to search
 * @param dimension where 0 is the x dimension and 1 is the y dimension
 * @return a minimum element among the given candidate and the elements in
 * the given subtree, along the given dimension of comparison
**/

	private E min(E candidate, QTNode subtree, int dimension) {
		if (!subtree.isFrontier()) {
			candidate = min(subtree.data, subtree.child(0), dimension);
			if (dimension == 0) { // x dimension
				E b = min(subtree.data, subtree.child(3), dimension);
				if (comp.compareX(b, candidate) < 0)
					candidate = b;
			} else { // y dimension
				E b = min(subtree.data, subtree.child(1), dimension);
				if (comp.compareY(b, candidate) < 0)
					candidate = b;
			}
		}
		return candidate;
	}

/**
 * The method adds, to the given collection of elements, all elements in the given subtree that lie within
 * the given bounds.
 * @param node the root of a given subtree
 * @param bounds the bounds within which the desired elements lie
 * @param space the extent of the search space covered by the subtree
 * @param elements a collection of elements that have been discovered within the bounds so far
 * <BR> 
 * REQUIRES: 
 *  all elements of the given subtree lie within the given space, which may or may not
 * intersect the desired bounding box
**/

	void withinBounds(QTNode node, Box bounds, Box space,
													Collection<? super E> elements) {
		if (!node.isFrontier() && bounds.intersects(space)) {
			if (bounds.contains(node.data))
				elements.add(node.data);
			double xMin = space.minx, yMin = space.miny;
			double xMax = space.maxx, yMax = space.maxy;
			double x = comp.getX(node.data), y = comp.getY(node.data);
			withinBounds(node.child(0), bounds, space.set(xMin, yMin, x, y), elements);
			withinBounds(node.child(1), bounds, space.set(x, yMin, xMax, y), elements);
			withinBounds(node.child(2), bounds, space.set(x, y, xMax, yMax), elements);
			withinBounds(node.child(3), bounds, space.set(xMin, y, x, yMax), elements);
		}
	}

/**
 * @param cornerA an element defining one corner of a bounding box
 * @param cornerB an element defining the opposite corner of the bounding box
 * @return a collection of all elements in this quad tree that lie within, or on
 * the boundary of, this bounding box, based on the XYComparator of this quad tree
**/

	public Collection<E> withinBounds(E cornerA, E cornerB) {
		Collection<E> elements = new DoublyLinkedList<E>();
		withinBounds(cornerA, cornerB, elements);
		return elements;
	}

/**
 * @param cornerA an element defining one corner of a bounding box
 * @param cornerB an element defining the opposite corner of the bounding box
 * @param elements the collection in which to place the elements that lie within, or on the boundary of, the bounding box
**/

	public void withinBounds(E cornerA, E cornerB, Collection<? super E> elements) {
		if (elements == this)
			throw new IllegalArgumentException("a different collection must be provided");
		Box bounds = new Box(cornerA, cornerB);
		Box searchSpace = new Box(NEGATIVE_INFINITY, NEGATIVE_INFINITY, 
										POSITIVE_INFINITY, POSITIVE_INFINITY);	
		withinBounds(root, bounds, searchSpace,  elements);
	}

/**
 * @param target any value of type E
 * @return the tree node containing an element equal to the target, or the frontier node if
 * no matching element is found.
 * If the frontier node is returned, then its parent will have been set to the last non-frontier node on the search path.
**/

	protected TreeNode find(E target) {
		QTNode ptr = root;
		FRONTIER.parent = null;
		while (!ptr.isFrontier()) {
			int quadrant = comp.quadrant(ptr.data, target);
			if (quadrant == 4)  // found a match
				return ptr;
			else {
				FRONTIER.parent = ptr;
				ptr = ptr.child(quadrant);
			}
		}
		return ptr;
	}

/**
 * @param subtree a quad tree node at the root of a subtree
 * @param element an element for insertion into the quad tree
 * @return the frontier node at the insert position of the target.  That is, the frontier node's parent is
 * set to the quad tree node that should become the parent of the target.
**/

	protected QTNode findInsertPosition(QTNode subtree, E element) {
		FRONTIER.parent = null;
		while (!subtree.isFrontier()) {
			FRONTIER.parent = subtree;
			subtree = subtree.child(comp.quadrant(subtree.data, element)%4);
		}
		return subtree;
	}

/**
 * @param element the element
 * to be inserted into the tree
**/

	protected QTNode insert(E element) {
		return insertNode(new QTNode(element), root);
	}

/**
 * @param node a quad tree node
 * @param subtree the root of the subtree into which the node should be inserted
**/

	protected QTNode insertNode(QTNode node, QTNode subtree) {
		E element = node.data;
		QTNode parent = findInsertPosition(subtree, element).parent;
		if (parent  == null)
			root = node;
		else {
			node.parent = parent;
			int quadrant = comp.quadrant(parent.data, element)%4;
			parent.children[quadrant] = node;
		}
		return node;
	}

/**
 * Removes the given element from the collection
 * @param element an element in the collection
 * @return true  if and only if the given element was found in the collection
**/

	public boolean remove(E element) {
		if (super.remove(element)) {
			version.increment();
			return true;
		}
		return false;
	}

/**
 * @param node the node to be deleted
**/

		void identifyReplacementCandidates(QTNode node) {
			int candidateCount = 0;
			for (int q = 3; q >= 0; q--) {
				QTNode deepest = node.child(q).deepestInSubtree(conjugate(q));
				candidates[q] = deepest;
				if (deepest.isFrontier()) {
					xDistance[q] = yDistance[q] = Double.POSITIVE_INFINITY;			
				} else  {
					xDistance[q] = Math.abs(comp.getX(node.data) - comp.getX(deepest.data));
					yDistance[q] = Math.abs(comp.getY(node.data) - comp.getY(deepest.data));
					candidateCount++;
				}
			}
			if (candidateCount > 1)
				filterCandidates();
		}


		void filterCandidates() {
			boolean found = false;  // true if a special candidate has been found
			int xOffset = 3;  // x comparisons between quadrants 0/3, 1/2, 2/1, and 3/0
			int yOffset = 1; //  y comparisons between quadrants 0/1, 1/0, 2/3, and 3/2
			for (int q = 0; q < 4; q++) {
				if (xDistance[q] < xDistance[(q+xOffset)%4] &&
						yDistance[q] < yDistance[(q+yOffset)%4]) {
					if (!found) { // the first special candidate
						found = true;
						for (int i = 0; i < q; i++) // filter out earlier candidates
							candidates[i] = null;
					}
				} else if (found) {  // not among special ones
					candidates[q] = null;
				}
				int temp = xOffset; xOffset = yOffset; yOffset = temp;
			}
		}

/**
 * @param node the node to be deleted
 * @return the number of the subquadrant containing the replacement node
**/

	int chooseReplacementNode(QTNode node) {
		identifyReplacementCandidates(node);
		int winningQuadrant = 0;
		double distance = java.lang.Double.MAX_VALUE;
		for (int q = 0; q < 4; q++) {
			if (candidates[q] != null) {
				double L1 = xDistance[q] + yDistance[q];
				if (L1 < distance) {
					distance = L1;
					winningQuadrant = q;
				}
			}
		}
		return winningQuadrant;
	}

/**
 * @param toDelete the quad tree node to be deleted
**/

	protected void remove(TreeNode toDelete) {
		QTNode node = (QTNode) toDelete;
		if (node.isFrontier()) return;

		int winningQuadrant = chooseReplacementNode(node);
		QTNode winner = (QTNode) candidates[winningQuadrant];

		if (!winner.isFrontier()) {
			dangerZone.setCorners(winner.data, node.data);
			node.data = winner.data; 
			winner.resetParent();	
			adjustSubtree(node, winner, winningQuadrant);		
			for (int q = 0; q < 4; q++)
				reinsertEntireSubtree(winner.child(q),node);
		}
		node.replaceBy(winner);
	}

/**
 * @param subtreeRoot the root of the subtree to be reinserted
 * @param destinationRoot the root of the subtree into which the nodes should be reinserted
 * <BR> 
 * REQUIRES: 
 *  the <code>destinationRoot</code> is not in the subtree of the <code>subtreeRoot</code>
**/

		void reinsertEntireSubtree(QTNode subtreeRoot, QTNode destinationRoot) {
			if (subtreeRoot != FRONTIER) {
				subtreeRoot.resetParent();
				insertNode(subtreeRoot, destinationRoot);
				for (int q = 0; q < 4; q++) {
					reinsertEntireSubtree(subtreeRoot.child(q), destinationRoot);
				}
			}
		}


		void adjustSubtree(QTNode deletedNode, QTNode winner, int winningQuadrant) {
			adjustSubquadrant(deletedNode, leftNeighbor(winningQuadrant),
					leftNeighbor(winningQuadrant), winningQuadrant, root);
			adjustSubquadrant(deletedNode, rightNeighbor(winningQuadrant),
					rightNeighbor(winningQuadrant), winningQuadrant, root);

			QTNode p = deletedNode.child(winningQuadrant);
			int conjugate = conjugate(winningQuadrant);
			while (!p.isFrontier()) {
				if (dangerZone.inRange(p)) {
					reinsertEntireSubtree(p, deletedNode);
					break;
				} else {
					adjustSubquadrant(p, winningQuadrant, 
							leftNeighbor(winningQuadrant), winningQuadrant, deletedNode);
					adjustSubquadrant(p, winningQuadrant,
							rightNeighbor(winningQuadrant), winningQuadrant, deletedNode);
					p = p.child(conjugate);
				}
			}
		}


/**
 * @param parent the parent of a subtree
 * @param quadrant the quadrant of the parent in relation to the deleted node
 * @param subquad the index of the parent's subquadrant that is to be adjusted
 * @param winningQuadrant the quadrant of the replacement node in relation to the deleted node
 * @param destination the root of the subtree into which any nodes from the subtree should be reinserted
**/

		void adjustSubquadrant(QTNode parent, int quadrant, int subquad,
				int winningQuadrant, QTNode destination) {
			QTNode subtreeRoot = parent.child(subquad);
			if (subtreeRoot != null && !subtreeRoot.isFrontier()) {
				if (dangerZone.inRange(subtreeRoot))
					reinsertEntireSubtree(subtreeRoot, destination);
				else {
					for (int q = 0; q < 4; q++)
						if (q != quadrant && q != winningQuadrant)
							adjustSubquadrant(subtreeRoot, quadrant, q,
									winningQuadrant,  destination);
				}
			}
		}

/**
 * @param v a visitor
**/

	protected void traverseForVisitor(Visitor<? super E> v) throws Exception {
		root.traverseForVisitor(v);
	}



	public Locator<E> iterator() {
		return new VisitingIterator();
	}

/**
 * The <code>iterator</code> method should be used to iterate through the collection.
**/

	public Locator<E> getLocator(E element) {
		throw new UnsupportedOperationException();
	}



}
