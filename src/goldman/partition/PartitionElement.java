// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.partition;
/**
 * A partition
 * is a division of a group of objects into disjoint sets that we call components.
 * We view a partition abstractly
 * as a collection of disjoint components whose union is
 * the total collection.
 * However, the actual data type we use to represent the partition
 * is not the partition as a whole but rather the individual components
 * of the collection.
 * Each component in the partition is defined by an (arbitrary) element of that component
 * known as the representative element, or simply the representative.
 * So the data type that we implement for the
 * Partition ADT is simply that of a
 * <code>PartitionElement</code> that represents one element of the partition.
**/

public interface PartitionElement<T> {
/**
 * Returns
 * the representative element for the component that includes this
 * partition element.
**/

	public PartitionElement<T> findRepresentative();
/**
 * Returns the data associated with this partition element.
**/

	public T get();
/**
 * Returns true 
 * if and only if this partition element and <code>x</code> are in the same component.
**/

	public boolean sameComponent(PartitionElement<T> x);
/**
 * Sets the associated data for this
 * partition element to the provided value.
**/
/**
 * Returns a string that describes the data associated
 * with this partition element.
**/

	public void set(T applicationData);
/*
\method{String toString()}{\tagcomment{Returns a string that describes the data associated
with this partition element.}}
*/
	String toString();
/**
 * Combines
 * the components holding this partition element and <code>x</code> into a single
 * component.  It returns the representative element for the resulting component.
 * If this element and <code>x</code> are already in the same component, no mutation occurs and
 * their representative is returned.
**/

	public PartitionElement<T> union(PartitionElement<T> x);

}
