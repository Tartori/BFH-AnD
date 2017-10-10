// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman;
import java.util.Comparator;
/**
 * The Objects abstract class holds a variety of utilities to support
 * many data structures.
**/

public abstract class Objects {

	public static final Object EMPTY = new EmptySlot(); //empty sentinel
	static class EmptySlot{}


	public static final Object DELETED = new DeletedElement(); //deleted sentinel
	static class DeletedElement{}

/**
 * Returns true  if and only if
 * <code>o1</code> and <code>o2</code> are equivalent.  This method
 * assumes the <code>equals</code> method is symmetric.   If that were
 * not the case then <code>o1.equals(o2)</code> would need to be
 * replaced by <code>o1.equals(o2) && o2.equals(o1)</code>, which would
 * make this method less efficient.
**/

	public static final boolean equivalent(Object o1, Object o2) {
		return o1 == o2 || (o1 != null && o1.equals(o2)); 
	}


	public static final Comparator<Object> DEFAULT_EQUIVALENCE_TESTER = 
		new DefaultEquivalenceTester<Object>();

/**
 * If no comparator is provided in the constructor for a data
 * structure that depends on the elements being comparable,
 * the following default equivalence tester is used.
**/

	public static class DefaultEquivalenceTester<E> implements Comparator<E> {
		public int compare(E o1, E o2) {
			if (Objects.equivalent(o1,o2))
				return 0;
			else
				return -1;
		}
	}


	static class NotComparableException extends RuntimeException {
		
		public NotComparableException(String message) {
			super(message);
		}
	}

	public static final Comparator<Object> DEFAULT_COMPARATOR = 
		new DefaultComparator<Object>();
	
	static class DefaultComparator<E> implements Comparator<E>{
		
		public int compare(E o1, E o2) {
			try {
				return ((Comparable<E>) o1).compareTo(o2);
			} catch (ClassCastException cce) {
				throw new NotComparableException("Can't compare " + o1 + " and " + o2);
			}
		}
	}


}
