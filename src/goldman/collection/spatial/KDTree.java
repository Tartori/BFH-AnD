// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
import goldman.collection.AbstractCollection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import goldman.collection.Collection;
import goldman.collection.Locator;
import goldman.collection.Tracked;
/**
 * The k-d tree cycles among the k dimensions, dividing
 * each region by a halfspace with respect to the dimensions associated with that level.
 * Since the partition at each node uses just one dimension, a k-d tree has
 * the flexibility needed to efficiently partition the domain for certain
 * data distributions (e.g., points along the diagonal).  Because of this flexibility,
 * the space usage of a k-d tree scales well in the number of dimensions, and so it
 * is the spatial collection data structure of choice for k &gt; 3.
 * While the expected height of a k-d tree is roughly k times more than a k-dimensional
 * extension of a quad tree, each level only requires a single comparison in a k-d tree,
 * versus k comparisons in a k-dimensional extension of a quad tree.
 * Also, deletion is much less complex in a k-d tree than it is for a quad tree.
 * The internal representation for a k-d tree uses KDTreeImpl, which is
 * a special type of binary search tree data structure.
**/

public class KDTree<E> extends AbstractCollection<E> implements SpatialCollection<E>, Tracked<E> { 

	KDTreeImpl<E> tree;

/**
 * @param comparators a variable number of comparators defining the
 * dimensions of this spatial collection
 * <BR> 
 * REQUIRES: 
 *  at least two comparators are provided
**/

	public KDTree(Comparator<? super E> ... comparators) {
		super(new AlternatingComparator<E>(comparators));
		tree = new KDTreeImpl<E>((AlternatingComparator<E>) comp);
	}

/**
 * @return the size of the collection
**/

	public int getSize() {
		return tree.getSize();
	}

/**
 * Note that, in the case of a multidimensional data set,
 * equivalence is defined as the elements having
 * equal value along all dimensions.
 * @param a the first element to compare
 * @param b the second element to compare
 * @return true  if and only if <code>a</code> and <code>b</code> are
 * equivalent
**/

	protected boolean equivalent(E a, E b) {
		return ((AlternatingComparator<E>) comp).equalTo(a, b);
	}

/**
 * Because the data set is multidimensional,
 * the inherited <code>compare</code>
 * method does not make sense.
 * Comparison cannot occur without specifying a particular dimension.
**/

	protected int compare(E o1, E o2) {
		throw new UnsupportedOperationException();
	}

/**
 * @param dimension the dimension along which elements should be compared
 * @return a least element in the collection along the given dimension
**/

	public E min(int dimension) throws NoSuchElementException {
		return tree.minimum(dimension);
	}

/**
 * @param dimension the dimension along which elements should be compared
 * @return a greatest element in the collection along the given dimension
**/

	public E max(int dimension) {
		return tree.maximum(dimension);
	}

/**
 * @param element an element that may or may not be in the collection
 * @return true  if and only if an equivalent element exists in the collection
**/

	public boolean contains(E element) {
		return tree.contains(element);
	}

/**
 * @param minCorner the corner of the range having the values in all dimensions at the low end of the range
 * @param maxCorner the corner of the range having the values in all dimensions at the high end of the range
 * <BR> 
 * REQUIRES: 
 * minCorner to be less than or equal to maxCorner
 * @return a collection of all the elements in the tree that fall
 * within the given range
**/

	public Collection<E> withinBounds(E minCorner, E maxCorner) {
		return tree.withinBounds(minCorner, maxCorner);
	}

/**
 * @param x the element to track
 * @return a tracker that has been initialized
 * at the given element.
 * @throws NoSuchElementException the given element is not in the collection.
**/

	public Locator<E> getLocator(E x) {
		return tree.getLocator(x);
	}

/**
 * @param element an item to be added to the collection
**/

	public void add(E element) {
		tree.add(element);
	}

/**
 * @param element an item to be added to the collection
 * @return a tracker positioned at that element
**/

	public Locator<E> addTracked(E element) {
		return tree.addTracked(element);
	}

/**
 * Removes from the collection
 * an arbitrary element (if any) equivalent to <code>element</code>.
 * Note that equivalence is defined as being of equal value along all dimensions of the comparator.
 * @param element the element to remove
 * @return <code>true</code> if an element was removed, and <code>false</code> otherwise.
**/

	public boolean remove(E element) {
		return tree.remove(element);
	}

/**
 * Creates a new tracker at FORE.
 * Note that because the data set is multidimensional,
 * there is not a well-defined ordering of the elements.
 * Therefore, one should not rely upon any particular
 * ordering of the data during iteration.
**/

	public Locator<E> iterator() {
		return tree.iterator();
	}

/**
 * Creates a new tracker that is at AFT.
 * Again, because the data set is multidimensional,
 * there is not a well-defined ordering of the elements.
 * Therefore, one should not rely upon any particular
 * ordering during iteration.
**/

	public Locator<E> iteratorAtEnd() {
		return tree.iteratorAtEnd();
	}
	


	KDTreeImpl.KDNode getRoot() {
		return tree.getRoot();
	}
	
	public void checkRep() {
		tree.checkRep();
	}
}
