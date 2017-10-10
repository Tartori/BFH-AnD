// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
/**
 * Unlike iterators that support external iteration of the internal structure
 * of the collection, a visitor can avoid these disadvantages by
 * turning the design "inside out."
 * Rather than provide an iterator for use by external code, the external code can
 * implement the following <code>Visitor</code> interface.
**/

public interface Visitor<T> {

/**
 * Called during traversal with each element of the collection
 * being passed as the parameter.
**/

	public void visit(T item) throws Exception;
}

