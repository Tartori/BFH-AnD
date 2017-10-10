// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered;
import java.util.Comparator;

import goldman.Objects;
import goldman.collection.ordered.RedBlackTree;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a red-black tree.
**/

public class TaggedRedBlackTree<T,E> extends TaggedOrderedCollectionWrapper<T,E> {
	
	public TaggedRedBlackTree(Comparator<? super T> comp) {
		super(new RedBlackTree<TaggedElement<T,E>>
						(new TaggedElementComparator<T>(comp)));
	}


	public TaggedRedBlackTree() {
		super(new RedBlackTree<TaggedElement<T,E>>
			(new TaggedElementComparator<T>(Objects.DEFAULT_COMPARATOR)));
	}

}

