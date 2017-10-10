// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.priority;
import java.util.Comparator;
import goldman.Objects;
import goldman.collection.priority.LeftistHeap;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a leftist heap.
**/

public class TaggedLeftistHeap<T,V> extends TaggedPriorityQueueWrapper<T,V>
										implements TaggedPriorityQueue<T,V> {
	
	public TaggedLeftistHeap(Comparator<? super T> comp) {
		super(new LeftistHeap<TaggedElement<T,V>>
						(new TaggedElementComparator<T>(comp)));
	}


	public TaggedLeftistHeap() {
		this(Objects.DEFAULT_COMPARATOR);
	}

}

