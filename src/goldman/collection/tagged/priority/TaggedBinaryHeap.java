// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.
/**
 * @param comp the comparator that
 * defines the ordering among the tags
 * @param tracked which indicates
 * whether or not a track implementation is desired.
**/

package goldman.collection.tagged.priority;
import java.util.Comparator;
import goldman.Objects;
import goldman.collection.priority.BinaryHeap;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a binary heap.
**/

public class TaggedBinaryHeap<T,V> extends TaggedPriorityQueueWrapper<T,V>
										implements TaggedPriorityQueue<T,V> {
	
	public TaggedBinaryHeap(int initialCapacity, Comparator<? super T> comp, 
															boolean tracked) {
		super(new BinaryHeap<TaggedElement<T,V>>(initialCapacity,
					new TaggedElementComparator<T>(comp),tracked));
	}


	public TaggedBinaryHeap() {
		this(Objects.DEFAULT_COMPARATOR);
	}

	public TaggedBinaryHeap(Comparator<? super T> comp) {
		super(new BinaryHeap<TaggedElement<T,V>>(new TaggedElementComparator<T>(comp)));
	}
	
	public TaggedBinaryHeap(int initialCapacity, boolean tracked) {
		this(initialCapacity, Objects.DEFAULT_COMPARATOR, true);
	}

}

