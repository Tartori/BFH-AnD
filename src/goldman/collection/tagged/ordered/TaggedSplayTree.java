// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered;
import java.util.Comparator;

import goldman.Objects;
import goldman.collection.ordered.SplayTree;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a splay tree.
**/

public class TaggedSplayTree<T,E> extends TaggedOrderedCollectionWrapper<T,E> {
	
	public TaggedSplayTree(Comparator<? super T> comp) {
		super(new SplayTree<TaggedElement<T,E>>
						(new TaggedElementComparator<T>(comp)));
	}


	public TaggedSplayTree() {
		super(new SplayTree<TaggedElement<T,E>>
			(new TaggedElementComparator<T>(Objects.DEFAULT_COMPARATOR)));
	}

}

