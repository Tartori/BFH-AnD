// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.priority;
import java.util.Comparator;
import goldman.Objects;
import goldman.collection.priority.FibonacciHeap;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
/**
 * A tagged version of a Fibonacci heap.
**/

public class TaggedFibonacciHeap<T,V> extends TaggedPriorityQueueWrapper<T,V>
										implements TaggedPriorityQueue<T,V> {
	
	public TaggedFibonacciHeap(Comparator<? super T> comp) {
					super(new FibonacciHeap<TaggedElement<T,V>>
							(new TaggedElementComparator<T>(comp)));
	}


	public TaggedFibonacciHeap() {
		this(Objects.DEFAULT_COMPARATOR);
	}

}

