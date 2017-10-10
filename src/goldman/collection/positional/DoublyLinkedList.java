// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;
/**
 * The doubly linked list is the only positional collection data structure
 * that provides amortized constant time methods for all of the
 * <code>PositionalCollectionLocator</code> methods except <code>getCurrentPosition</code>.
 * Furthermore, it is a tracked implementation.  Since a doubly linked
 * list maintains a pointer from each element to the one that precedes it,
 * an element that is known to be located near the end of the collection can
 * be efficiently located by traversing backwards from the tail.
 * This list-based data structure extends SinglyLinkedList to have each list
 * item also maintain a reference to the previous list node.
 * The primary advantage of a doubly linked list, over all of the other data structures, is
 * that through a locator an element can be added or removed from the middle portion of the
 * list in constant time.  Also, it takes
 * O(min(p+1,n-p)) time, instead of O(p) time for SinglyLinkedList
 * to access the element at position p without
 * the use of a locator.  Furthermore, the <code>retreat</code> method takes constant time.
 * The primary disadvantage of DoublyLinkedList is that
 * including a previous reference adds space overhead and all
 * methods that modify the structure of the list must
 * adjust twice as many references.
**/

public class DoublyLinkedList<E> extends SinglyLinkedList<E> implements PositionalCollection<E> {

	DLListItem<E> tail; // tail sentinel


	protected static class DLListItem<E> extends ListItem<E> {

		DLListItem<E> prev;

		DLListItem(Object data) {
			super(data);
		}
		
		protected void setNext(ListItem<E> next) {
			this.next = next;
			((DLListItem<E>) next).prev = this;
		}


	}
/**
 * @param value the desired element
 * @return a new DLListItem holding the given element.
**/

	protected DLListItem<E> newListItem(E value) {
		return new DLListItem<E>(value);
	}	

/**
 * Updates this collection to be empty
**/

	void initialize(){
		tail = newListItem(null);
		super.initialize();
	}	


	protected ListItem<E> getTail() {
		return tail;
	}

/**
 * @return a reference to the last item in the list.
**/

    protected ListItem<E> getLast() {
    	return tail.prev;
    }

/**
 * @param p the position of the element to find
 * <BR> 
 * REQUIRES: 
 *  <code>p</code> is a valid
 * position in the collection.
 * @return a reference to the <code>DLListItem</code> for the element in
 * the collection at position <code>p</code>
**/

	private DLListItem<E> findFromBack(int p){
		DLListItem<E> current = tail.prev;
		for (int loc = size-1; loc > p; loc--)
			current = current.prev;
		return current;
	}

/**
 * @param p a valid user position or -1
 * @return a reference to the element in the collection at
 * position <code>p</code> or the head sentinel if <code>p</code> is -1
**/

	protected DLListItem<E> getPtr(int p){
		if (p >= size/2)
			return findFromBack(p);
		else
			return (DLListItem<E>) super.getPtr(p);
	}	

/**
 * @param ptr reference to an element in the collection
 * <BR> 
 * REQUIRES: 
 *  <code>ptr</code>
 * is a pointer to an element of the collection.
 * @return a reference to the list item just
 * before that referenced by <code>ptr</code> (possibly
 * the <code>head</code> sentinel).
**/

	protected ListItem<E> getPtrForPrevItem(ListItem<E> ptr){
		return ((DLListItem<E>) ptr).prev;
	}

/**
 * @param ptr a reference to the list item that
 * is now the last one in the list
**/

    protected void setLast(ListItem<E> ptr) {
    	tail.prev = (DLListItem<E>) ptr;
    }


}
