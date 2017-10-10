// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged;
/**
 * Each element and its associated tag is
 * stored in a tagged collection as a
 * tagged element.
**/

public class TaggedElement<T,E> {

	protected T tag;
	protected E element;

/**
 * Initializes the tagged element to hold these values
 * @param t the tag
 * @param e the element
 * <BR> 
 * REQUIRES: 
 *  the tag is not null.
**/

	public TaggedElement(T t, E e) {
		if (t == null) 
			throw new IllegalArgumentException("Tag cannot be null");
		tag = t;
		element = e;
	}


	protected TaggedElement() {}

/**
 * @return a hashcode based
 * on only the tag
**/

	public int hashCode() {return tag.hashCode();}

/**
 * @return the tag from this tagged element.
**/

	public T getTag() {return tag;}

/**
 * @return the element from this tagged
 * element.
**/

	public E getElement() {return element;}

/**
 * @param e the new
 * value for the element field
**/

	public void setElement(E e) {element = e;}

/**
 * @return a string of the form,
 * <code>tag</code> &#8594; <code>element</code>.
**/

	public String toString(){return tag + " --> " + element;}


}
