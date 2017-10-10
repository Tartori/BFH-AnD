// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.bucket;
import goldman.collection.Collection;
/**
 * A bucket factory is used by a tagged bucket collection to
 * create a new bucket for each newly added tag.
**/

public interface BucketFactory<E> {
	public Collection<E> createBucket();
}

