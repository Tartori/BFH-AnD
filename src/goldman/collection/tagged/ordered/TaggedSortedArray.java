// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.
/**
 * @param comp the comparator that
 * defines the ordering among the tags
**/

package goldman.collection.tagged.ordered;
import java.util.Comparator;

import goldman.Objects;
import goldman.collection.ordered.SortedArray;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a sorted array.
**/

public class TaggedSortedArray<T,E> extends TaggedOrderedCollectionWrapper<T,E> {
	
	public TaggedSortedArray(Comparator<? super T> comp) {
		super(new SortedArray<TaggedElement<T,E>>
						(new TaggedElementComparator<T>(comp)));
	}


	public TaggedSortedArray() {
		super(new SortedArray<TaggedElement<T,E>>
			(new TaggedElementComparator<T>(Objects.DEFAULT_COMPARATOR)));
	}

}

