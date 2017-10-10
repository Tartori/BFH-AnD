// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged;
import java.util.Comparator;
/**
 * We compare tagged elements by wrapping a provided
 * comparator defined over only the tags.   That is, our comparator wraps a
 * comparator of type T and supports comparison of tagged elements whose
 * tag type is T.
**/

public class TaggedElementComparator<T> implements Comparator<TaggedElement<T,?>> {
	Comparator<? super T> comp;

	public TaggedElementComparator(Comparator<? super T> comp) {
		this.comp = comp;
	}
	
	public int compare(TaggedElement<T,?> a, TaggedElement<T,?> b) {
		return comp.compare(a.tag, b.tag);
	}
	
	public Comparator<? super T> getTagComparator() {
		return comp;
	}
}

