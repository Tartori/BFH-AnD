// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.
/**
 * @param comparators an array of d comparators for
 * each of the d orderings.
**/

package goldman.collection.tagged.spatial;
import goldman.collection.spatial.KDTree;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedElementComparator;
import java.util.Comparator;
/**
 * A tagged version of a kd-tree.
**/

public class TaggedKDTree<T,E> extends TaggedSpatialCollectionWrapper<T,E> {

	public TaggedKDTree(Comparator<? super T> ...comparators) {
		super(new KDTree<TaggedElement<T,E>>(wrap(comparators)));
	}

/**
 * @param comparators an array of comparators of type T
 * @return an array of tagged element comparators that wrap the provided comparators
**/

	static <T> Comparator<? super TaggedElement<T,?>>[] 
	                                   wrap(Comparator<? super T>[] comparators) {
		Comparator<? super TaggedElement<T,?>>[] 
		                              wrapped = new Comparator[comparators.length];
		int i = 0;
		for (Comparator<? super T> comp : comparators)
			wrapped[i++] = new TaggedElementComparator<T>(comp);
		return wrapped;
	}
}

