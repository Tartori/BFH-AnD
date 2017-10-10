// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered;
import java.util.Comparator;

import goldman.Objects;
import goldman.collection.ordered.BinarySearchTree;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a binary search tree.
**/

public class TaggedBinarySearchTree<T,E> 
						extends TaggedOrderedCollectionWrapper<T,E> {
	
	public TaggedBinarySearchTree(Comparator<? super T> comp) {
		super(new BinarySearchTree<TaggedElement<T,E>>
						(new TaggedElementComparator<T>(comp)));
	}


	public TaggedBinarySearchTree() {
		super(new BinarySearchTree<TaggedElement<T,E>>
			(new TaggedElementComparator<T>(Objects.DEFAULT_COMPARATOR)));
	}

}

