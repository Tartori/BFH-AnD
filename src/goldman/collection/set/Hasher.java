// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.set;
/**
 * The <code>Hasher</code> interface provides the application program the
 * flexibility to specify how the hash codes are to be computed over the elements of
 * the set.
**/

public interface Hasher<E> {
/**
 * @param element the element for which the hash code should be computed
 * @return the hash code for the given element, which may not necessarily be
 * related to the <code>element.hashCode</code>
**/

	public int getHashCode(E element);

/**
 * @param capacity the desired capacity of the collection
 * @param load the desired load factor of the table
 * @return the proper table size in accordance with the hashing algorithm and
 * the way hash codes are computed
**/

	public int getTableSize(int capacity, double load);


}
