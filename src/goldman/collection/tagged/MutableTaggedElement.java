// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged;
/**
 * The MutableTaggedElement class is an extension of the
 * TaggedElement class that allows the tag to be modified.
**/

public class MutableTaggedElement<T,E> extends TaggedElement<T,E> {
	public TaggedElement<T,E> setTag(T t) {
		tag = t;
		return this;
	}
}

