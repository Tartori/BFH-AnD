// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered;
import java.util.Comparator;

import goldman.Objects;
import goldman.collection.ordered.SkipList;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a skiplist.
**/

public class TaggedSkipList<T,E> extends TaggedOrderedCollectionWrapper<T,E> {
	
	public TaggedSkipList(int size, Comparator<? super T> comp) {
		super(new SkipList<TaggedElement<T,E>>
						(size, new TaggedElementComparator<T>(comp)));
	}


	public TaggedSkipList() {
		super(new SkipList<TaggedElement<T,E>>
						(8, new TaggedElementComparator<T>(Objects.DEFAULT_COMPARATOR)));
	}

}

