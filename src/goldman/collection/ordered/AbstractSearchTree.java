// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import java.util.Comparator;
import java.util.NoSuchElementException;
import goldman.Objects;
import goldman.collection.AbstractCollection;
import goldman.collection.Locator;
import goldman.collection.Visitor;
/**
 * The AbstractSearchTree class is an abstract class that includes
 * the methods that are shared by all search trees.
**/

public abstract class AbstractSearchTree<E> extends AbstractCollection<E> {

	protected abstract class TreeNode {
/**
 * @return the number of elements held in that tree node
**/

		protected abstract int size();

/**
 * @return the maximum number of elements that are allowed
 * in the tree node.
**/

		protected abstract int capacity();

/**
 * @param index the index for the desired child
 * @return the tree node reference for that child
**/

		protected abstract TreeNode child(int index);

/**
 * @return the leftmost child
**/

		final TreeNode leftmostChild(){
			return child(0);
		}

/**
 * @return the rightmost child
**/

		final TreeNode rightmostChild(){
			return child(size());
		}

/**
 * @param index the index of the desired element
 * @return the element
 * @throws IllegalArgumentException <code>index</code> &lt; 0
 * or <code>index</code> &ge; <code>size()</code>
**/

		protected abstract E data(int index);

/**
 * @return true  if and only if the node is the frontier node
**/

		protected abstract boolean isFrontier();



	}

	protected TreeNode root;


	public AbstractSearchTree() { 
		this(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp the element comparator
**/

	public AbstractSearchTree(Comparator<? super E> comp) {
		super(comp);
	}

/**
 * @param target the element
 * to be located
 * @return a reference to a tree node that contains an
 * occurrence of the element if it is found.  If <code>target</code> is not in the collection,
 * then it returns the frontier node at the insert position.  In that case,
 * the parent of the returned frontier node must be the node that preceded it
 * on the search path.
**/

    protected abstract TreeNode find(E target);

/**
 * @param target the element being tested for membership in the collection
 * @return true  if and only if an equivalent value exists in the collection
**/

    public boolean contains(E target) {
    	return (!find(target).isFrontier());
    }

/**
 * @param r the desired rank
 * @return the r<sup>th</sup> element in the sorted order, where
 * r = 0 is the minimum.
 * @throws IllegalArgumentException r &lt; 0 or r &ge; n
**/

    public E get(int r) {
    	if (r < 0 || r >= getSize())
    		throw new IllegalArgumentException();
    	Locator<E> loc = iterator();
    	for (int j=0; j < r+1; j++, loc.advance());
    	return loc.get();
    }

/**
 * <BR> 
 * REQUIRES: 
 *  it is called only after a successful search
 * @return the index, within its
 * tree node, of the element returned in the most recent search.
**/

    protected int getLastNodeSearchIndex() {
    	return 0;
    }

/**
 * @param target the target element
 * @return an equivalent element that is in the collection
 * @throws NoSuchElementException there is no equivalent element in the
 * collection.
**/

    public E getEquivalentElement(E target) {
    	TreeNode t = find(target);
    	if (t.isFrontier())
    		throw new NoSuchElementException();
    	return t.data(getLastNodeSearchIndex());
    }

/**
 * @param x a reference to any tree node in
 * the collection
 * @return the leftmost tree node in the subtree rooted at <code>x</code>
**/

	TreeNode leftmost(TreeNode x) {
		while (!x.leftmostChild().isFrontier())
			x = x.leftmostChild();
		return x;
	}

/**
 * @return the
 * leftmost node in the tree
**/

	TreeNode leftmost() {
		return leftmost(root);
	}

/**
 * @return a smallest element
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public E min() {
		if (isEmpty())
			throw new NoSuchElementException();
		else
			return leftmost().data(0);
	}

/**
 * @param x a reference to any tree node in
 * the collection
 * @return the rightmost tree node in the subtree rooted at <code>x</code>
**/

	TreeNode rightmost(TreeNode x) {
		while (!x.rightmostChild().isFrontier())
			x = x.rightmostChild();
		return x;
	}

/**
 * @return the
 * rightmost node in the tree
**/

	TreeNode rightmost() {
		return rightmost(root);
	}

/**
 * @return a greatest element
 * in the collection.
 * @throws NoSuchElementException the collection is empty.
**/

	public E max() {
		if (isEmpty())
			throw new NoSuchElementException();
		else {
			TreeNode x = rightmost();
			return x.data(x.size()-1);
		}
	}

/**
 * Any exception thrown by the visitor propagates to the calling method.
 * @param v the visitor
 * @param x a reference to the tree node
**/

		void traverseForVisitor(Visitor<? super E> v, TreeNode x) throws Exception {
			if (!x.isFrontier()) {
				for (int i = 0; i < x.size(); i++){
					traverseForVisitor(v, x.child(i));
					v.visit(x.data(i));
				}
				traverseForVisitor(v, x.rightmostChild());
			}
		}

/**
 * Applies the visitor to each element of the collection in sorted
 * order
 * @param v the visitor
**/

		protected void traverseForVisitor(Visitor<? super E> v) throws Exception {
			traverseForVisitor(v, root);
		}

/**
 * Appends to the string
 * buffer a comma-separated string representation for the elements in the collection, in the
 * iteration order.
 * @param sb a string builder
**/

		protected void writeElements(final StringBuilder sb) { 
			if (!isEmpty()) {		//only visit the collection if it is not empty
				accept(new Visitor<E>() {
					public void visit(E item) throws Exception {
						sb.append(item);
						sb.append(", ");
					}
				});
				int extraComma = sb.lastIndexOf(","); //remove the comma and space characters
				sb.delete(extraComma,extraComma+2);   // after the last element
			}
		}

/**
 * Inserts it into the collection
 * @param element the
 * new element
 * @return a reference to the newly inserted element
**/

	protected abstract TreeNode insert(E element);

/**
 * Inserts <code>element</code> into the collection.
 * @param element the new element
**/

	public void add(E element) {
		insert(element);
		size++;
	}

/**
 * Removes the given node.
 * @param ptr a pointer to
 * an existing tree node
**/

	protected abstract void remove(TreeNode ptr);

/**
 * Removes an arbitrary element in the collection equivalent to
 * <code>element</code>, if any
 * @param element the element to be removed
 * @return true  if and only if an element is removed.
**/

	public boolean remove(E element) {
		TreeNode ptr = find(element); 
		if (ptr.isFrontier()) 
			return false;  
		remove(ptr);
		return true;
	}


}
