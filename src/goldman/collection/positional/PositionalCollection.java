// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.positional;

import java.util.Comparator;
import goldman.collection.Bucketizer;
import goldman.collection.Collection;
import goldman.collection.Digitizer;
/**
 * Often an application needs to maintain a collection of elements that
 * are accessed via their position in a line (with 0 being the position of the first
 * element in the line) or via their location relative to other elements in the line.
 * In a positional collection, the iteration order is given by:
 * position 0, position 1, ..., position n-1.
**/

public interface PositionalCollection<E> extends Collection<E>  {
/**
 * Inserts <code>value</code> into
 * position <code>p</code>.  The position of the elements that were at positions <code>p</code> to <code>size-1</code>
 * increase by one.
**/

	void add(int p, E value);
/**
 * Inserts <code>value</code> at the
 * front (position 0) of this collection. The positions
 * of the existing elements are increased by one.
**/

	void addFirst(E value);
/**
 * Inserts <code>value</code> at the
 * end of this collection (position <code>size</code>).
**/

	void addLast(E value);
/**
 * Returns the element at
 * position p.  It is required that 0 &le; p &le;
 * <code>size-1</code> (i.e., that the position exists in the collection).
 * Otherwise a <code>PositionOutOfBoundsException</code> is thrown.
**/

	E get(int p);
/**
 * Returns a positional
 * collection locator that has been initialized to FORE.
**/

	PositionalCollectionLocator<E> iterator();
/**
 * Returns a locator that has been initialized
 * to AFT.  As with the <code>iterator</code> method, this method also enables
 * navigation.
**/

	PositionalCollectionLocator<E> iteratorAtEnd();
/**
 * Returns the position
 * of the first occurrence (if any) of an element equivalent to <code>value</code>.
 * It throws a
 * <code>NoSuchElementException</code> if there is no equivalent element.
 * (The Java collections return -1 when there is no element with
 * the given value, but throwing an exception will better enable a
 * problem to be detected if an application program did not consider the possibility of -1
 * being returned.)
**/

	int positionOf(E value);
/**
 * Removes the element at
 * position <code>p</code> and returns it.  The positions of the elements
 * originally at positions p+1, ..., <code>size</code>-1 are decremented
 * by 1.  It is required that 0 &le; p &le;
 * <code>size</code>-1 (i.e., that the given position exists in the current collection).
 * Otherwise a <code>PositionOutOfBoundsException</code> is thrown.
**/

	E remove(int p);
/**
 * Removes the element at the front (position
 * 0) of this collection and returns it.
 * The positions of all other elements are decremented by 1.
 * If the collection is empty, a <code>NoSuchElementException</code> is thrown.
**/

	E removeFirst();
/**
 * Removes the element at the end (position
 * <code>n-1</code>) of this collection and returns it.  If the collection is empty, a
 * <code>NoSuchElementException</code> is thrown.
**/

	E removeLast();
/**
 * Removes
 * the elements from position
 * <code>fromPosition</code> to position <code>toPosition</code>, inclusive.  The positions for elements
 * <code>toPosition</code>+1, ..., <code>size</code>-1
 * are decremented by <code>toPosition</code> - <code>fromPosition</code> + 1
 * (the number of elements removed).
 * It is required that 0 &le; <code>fromPosition</code> &le; <code>toPosition</code> &le;
 * <code>size</code> - 1. A <code>PositionOutOfBoundsException</code> is thrown when
 * either of the arguments
 * is not a valid position, and an <code>IllegalArgumentException</code> is thrown when
 * <code>fromPos</code> is greater
 * than <code>toPos</code>.
**/

	void removeRange(int fromPosition, int toPosition);
/**
 * Replaces the element at
 * position <code>p</code> by the given <code>value</code>, and
 * returns the element that had been in position <code>p</code>.  It is required that 0 &le;
 * <code>p</code> &le; <code>size-1</code>.  Otherwise, a <code>PositionOutOfBoundsException</code> is thrown.
**/

	E set(int p, E value);
/**
 * Interchanges the element at position
 * <code>a</code> with the element at position <code>b</code>.  It is required that
 * 0 &le; <code>a</code> &le; <code>size-1</code> and 0 &le; <code>b</code> &le; <code>size-1</code>.
 * Otherwise,
 * a <code>PositionOutOfBoundsException</code> is thrown.
**/

	void swap(int a, int b);
/**
 * Sorts this collection using
 * bucket sort with the given bucketizer.
**/

	void bucketsort(Bucketizer<? super E> bucketizer);
/**
 * Sorts this collection with
 * heap sort using the default comparator.
**/

	void heapsort();
/**
 * Sorts this collection with
 * heap sort using the provided comparator.
**/

	void heapsort(Comparator<? super E> comp);
/**
 * Sorts this collection with
 * insertion sort using the default comparator.
**/

	void insertionsort();
/**
 * Sorts this collection with
 * insertion sort using the default comparator.
**/

	void insertionsort(Comparator<? super E> comp);
/**
 * Sorts the positional collection with
 * merge sort using the default comparator.
**/

	void mergesort();
/**
 * Sorts this collection with
 * merge sort using the default comparator.
**/

	void mergesort(Comparator<? super E> comp);
/**
 * Sorts this collection with
 * quicksort using the default comparator.
**/

	void quicksort();
/**
 * Sorts this collection with
 * quick sort using the default comparator.
**/

	void quicksort(Comparator<? super E> comp);
/**
 * Sorts
 * this collection using radix sort with the provided digitizer.
**/

	void radixsort(Digitizer<? super E> digitizer);
/**
 * Sorts this collection with
 * tree sort using the default comparator.
**/

	void treesort();
/**
 * Sorts this collection with
 * tree sort using the provided comparator.
**/

	void treesort(Comparator<? super E> comp);
/**
 * Modifies the positional collection so
 * element in position r is in its proper sorted order when using the default
 * comparator, and returns that element.  This
 * method does not completely sort the collection, but it may mutate it.
**/

	E repositionElementByRank(int r);
/**
 * Modifies
 * the positional collection so
 * element in position r is in its proper sorted order when using the provided
 * comparator, and returns this element.  This
 * method does not completely sort the collection, but it may mutate it.
**/

	E repositionElementByRank(int r, Comparator <? super E> comp);
}
