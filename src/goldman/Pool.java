// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman;
import goldman.collection.positional.Stack;
/**
 * The abstract Pool class provides a way to reuse object to reduce
 * the overhead of performing garbage collection by supporting a reusable pool
 * of objects.
**/

public abstract class Pool<T> {
	Stack<T> available = new Stack<T>();

/**
 * Returns an available object from the pool, or creates one if the pool is empty.
**/

	public T allocate() {
		if (available.isEmpty())
			return create();
		else
			return available.pop();
	}

/**
 * Moves the given object to the pool of available objects for
 * reuse.
 * @param x an object that is no longer needed
 * <BR> 
 * REQUIRES: 
 *  the user does not retain a reference to the object released
**/

	public void release(T x) {
		available.push(x);
	}	

/**
 * @return a newly allocated object of type T
**/

	protected abstract T create();
}

