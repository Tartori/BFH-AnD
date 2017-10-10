// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered;
import java.util.Comparator;

import goldman.Objects;
import goldman.collection.ordered.BTree;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a B-tree.
**/

public class TaggedBTree<T,E> extends TaggedOrderedCollectionWrapper<T,E> {

	public TaggedBTree(Comparator<? super T> comp, int order) {
		super(new BTree<TaggedElement<T,E>>
						(new TaggedElementComparator<T>(comp), order));
	}


	public TaggedBTree() {
		super(new BTree<TaggedElement<T,E>>
						(new TaggedElementComparator<T>(Objects.DEFAULT_COMPARATOR), 2));
	}
	
	public synchronized E remove(T tag) {
		TaggedElement<T,E> te = pairs.getEquivalentElement(target.setTag(tag));
		pairs.remove(target);
		return te.getElement();
	}	

}

