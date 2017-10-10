// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.spatial;
import goldman.collection.Collection;
import goldman.collection.spatial.SpatialCollection;
import goldman.collection.tagged.MutableTaggedElement;
import goldman.collection.tagged.TaggedElement;
import goldman.collection.tagged.TaggedCollectionWrapper;
/**
 * The tagged spatial collection wrapper can wrap any spatial collection
 * implementation, where each element in the collection is
 * a tagged element.   It is required that the comparator(s) depend(s) only
 * on the tag, and the tag is immutable.
**/

public abstract class TaggedSpatialCollectionWrapper<T,E> extends TaggedCollectionWrapper<T,E>
	implements TaggedSpatialCollection<T,E> {
/**
 * Since a target region is specified by two corners (each a d-dimensional
 * point), we use a second mutable tagged element <code>anchor</code> to specify the second
 * corner of a bounding box in an orthogonal range query where <code>target</code> specifies
 * the other corner.
**/

	protected MutableTaggedElement<T,E> anchor = new MutableTaggedElement<T,E>();


	public TaggedSpatialCollectionWrapper(Collection<TaggedElement<T, E>> pairs) {
		super(pairs);
	}

/**
 * @return a tagged element associated with the largest tag
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public TaggedElement<T,E> min(int dimension) {
		return ((SpatialCollection<TaggedElement<T,E>>) pairs).min(dimension);
	}

/**
 * @return a tagged element associated with the largest tag
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public TaggedElement<T,E> max(int dimension) {
		return ((SpatialCollection<TaggedElement<T,E>>) pairs).max(dimension);
	}

/**
 * @param minCorner the bottom left corner of
 * the target box
 * @param maxCorner the upper right corner of the target box
 * @return a collection of all tagged elements for which the tag is within (or on)
 * the target box
**/

	public Collection<TaggedElement<T,E>> withinBounds(T minCorner, T maxCorner) {
		return ((SpatialCollection<TaggedElement<T,E>>) 
				pairs).withinBounds(target.setTag(minCorner), anchor.setTag(maxCorner));
	}


}
