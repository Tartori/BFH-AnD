// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
import java.util.Comparator;
import goldman.Objects;
import goldman.collection.AbstractCollection;
/**
 * The AbstractPositionalCollection provides
 * a basis for defining concrete positional collections by defining a useful
 * set of methods that can be implemented for
 * any positional collection in terms of the public methods from the
 * <code>PositionalCollection</code> interface.  The internal
 * representation can be any positional collection data structure.
**/

public abstract class AbstractPositionalCollection<E> extends AbstractCollection<E>
	implements PositionalCollection<E> {	

	public AbstractPositionalCollection() {
		this(Objects.DEFAULT_EQUIVALENCE_TESTER);
	}

/**
 * @param comp the function used
 * to compare two elements
**/

	public AbstractPositionalCollection(Comparator<? super E> comp) {
		super(comp);
	}

/**
 * @return a comma-separated string showing the elements in
 * the iteration order.  Angle brackets mark the beginning and the end
 * of the collection.
**/

	public String toString() {
		StringBuilder s = new StringBuilder("<");
		writeElements(s);
		s.append(">");
		return s.toString();
	}

/**
 * Inserts it at the front (position 0) of the collection
 * @param value the element to insert
**/

	public void addFirst(E value) {
		add(0, value);
	}

/**
 * Inserts it at the end (position <code>size</code>) of the collection
 * @param value the element to insert
**/

	public void addLast(E value) {
		add(value);
	}

/**
 * Creates a new positional collection locator that starts at FORE.
**/

	public abstract PositionalCollectionLocator<E> iterator(); 

/**
 * Returns a positional collection locator that is at the given position.
 * @param pos the user position of an element
 * @throws NoSuchElementException the given position is not a valid position.
**/

	public abstract PositionalCollectionLocator<E> iteratorAt(int pos); 

/**
 * Creates a new positional collection locator that starts at AFT.
**/

	public abstract PositionalCollectionLocator<E> iteratorAtEnd();


}
