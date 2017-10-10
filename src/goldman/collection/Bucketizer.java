// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
import java.util.Comparator;
/**
 * The <code>Bucketizer</code> interface provides an application with a
 * mechanism to specify a way to partition all possible elements in a collection
 * into a set of groups (or buckets).   A combination of the ordering implicitly
 * defined between buckets and the comparator used within the buckets defines
 * a total order over the elements.
**/

public interface Bucketizer<E> extends Comparator<E> {
/**
 * Returns the number of buckets used.
**/

	int getNumBuckets();
/**
 * Returns the bucket to which element <code>x</code>
 * belongs.  The value returned must be an integer in the range 0 to
 * <code>getNumBuckets</code>() - 1.
**/

	int getBucket(E x);   // returns a value in the range 0 to getNumBuckets()-1 
}
