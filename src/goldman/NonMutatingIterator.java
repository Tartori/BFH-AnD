// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman;
import java.util.Iterator;
/**
 * The NonMutatingIterator wraps an iterator to prevent the user
 * from calling the <code>remove</code> method.
**/

public class NonMutatingIterator<T> implements Iterator<T> {
	private Iterator<? extends T> wrapped;
	public NonMutatingIterator(Iterator<? extends T> iterator) {
		this.wrapped = iterator;
	}
	public boolean hasNext() { return wrapped.hasNext(); }
	public T next() { return wrapped.next(); }
	public void remove() { throw new UnsupportedOperationException(); }
}

