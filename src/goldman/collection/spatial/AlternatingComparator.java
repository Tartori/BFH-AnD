// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.spatial;
import java.util.Comparator;
/**
 * The alternating comparator manages the
 * various comparators used by the k-d tree.
**/

public class AlternatingComparator<E> implements Comparator<E> {

	protected Comparator<? super E>[] comparators;        //one comparator for each dimension
	private int discriminator;              //the index of the current dimension in the cycle

/**
 * @param comparators a variable number of comparators defining the
 * dimensions of this comparator
 * <BR> 
 * REQUIRES: 
 *  at least two comparators are provided
**/

	public AlternatingComparator(Comparator<? super E> ... comparators) {
		if (comparators.length < 2)
			throw new IllegalArgumentException("at least two comparators are required");
		this.comparators = comparators;
		reset();
	}

/**
 * Returns the comparator to the beginning of the cycle of
 * dimensions, so that the next dimension used for unqualified comparison will be dimension 0.
**/

	public void reset() {
		discriminator = -1;
	}	

/**
 * Calling the compare method has the side-effect of advancing this alternating comparator to the next dimension in
 * the cycle, wrapping around to dimension 0 as needed.
 * The comparator then determines the relative ordering of two elements on the basis of that dimension of comparison.
 * It is important to note that two values are deemed equal only if they match along <em>all</em> dimensions.  If they match
 * along the current dimension in the cycle but differ in another dimension, then a positive value is returned.
 * This semantics is convenient for the KDTree implementation, in which ties along a node's dimension of comparison should
 * result in traversal of the right subtree.
 * It is assumed that the first parameter is always the reference point (e.g., the element in the tree) and the second parameter is the
 * element under consideration (e.g., the one to be placed to its left or right).
 * It is important to note that this unqualified compare method violates the usual symmetric convention for comparators when a tie
 * occurs along the dimension of comparison.
 * If the elements passed to this compare method are equal with respect to the current dimension of
 * comparison, but differ in some other dimension, then the method returns a positive value, regardless of the order in which the
 * actual parameters are passed to the method.
 * @param a the reference element
 * @param b the element to be compared against the reference
 * @return a negative value if  a &lt; b along the dimension of comparison, zero if a and b are equal along all dimensions,
 * and a positive value if a &gt; b along the dimension of comparison
**/

	public int compare(E a, E b) {
		discriminator = nextDiscriminator(discriminator);  // advance in the cycle
		int result = compare(a,b, discriminator);
		if (result == 0) {
			for (int i = 0; i < comparators.length; i++)
				if (i != discriminator && compare(a,b,i) != 0)
					return 1;
		}
		return result;
	}

/**
 * @param a the reference element
 * @param b the element to be compared against the reference
 * @param discriminator the index of the dimension along with the
 * elements should be compared
 * @return a negative value if  a &lt; b, zero if a = b, and a positive value if a &gt; b with respect to the specified dimension
**/

	public int compare(E a, E b, int discriminator) {
		return comparators[discriminator].compare(a,b);
	}


/**
 * Two calls to this method, using the maximum and minimum corners of a multidimensional bounding box,
 * are sufficient to determine whether a given element lies within the box.
 * @param a the candidate element
 * @param b the boundary element
 * @return true if and only if the candidate is less than or equal to the boundary along all dimensions
**/

	public boolean noGreaterThan(E a, E b) {
		for (int i = 0; i < comparators.length; i++) {
			if (compare(a, b, i) > 0)
				return false;
		}
		return true;
	}


/**
 * @param a the candidate element
 * @param b the target element
 * @return true if and only if the candidate is equal to the target along all dimensions
**/

	public boolean equalTo(E a, E b) {
		for (int i = 0; i < comparators.length; i++) {
			if (compare(a, b, i) != 0)
				return false;
		}
		return true;
	}

/**
 * @return the index of the most recently used dimension in this comparator's cycle, or -1 if the comparator has
 * not made an unqualified comparison since it was last reset
**/

	public int getLastDiscriminatorUsed() {
		return discriminator;
	}

/**
 * @param discriminator the index of a dimension of this comparator
 * @return the index of the next dimension on the cycle
**/

	public int nextDiscriminator(int discriminator) {
		return (discriminator+1) % comparators.length;
	}
	

/**
 * @return the number of dimensions used by this comparator, which is equal to the number of comparators provided to the constructor
**/

	public int getNumDimensions() {
		return comparators.length;
	}


}	
