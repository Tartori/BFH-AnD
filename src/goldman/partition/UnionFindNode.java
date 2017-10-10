// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.partition;
/**
 * The UnionFindNode class provides an implementation of
 * the PartitionElement ADT using the union-find data structure.
**/

public class UnionFindNode<T> implements PartitionElement<T>{  

	T applicationData;
	UnionFindNode<T> parent;
	int rank;  //upper bound on tree height

/**
 * It creates a new component holding
 * a single union-find node associated with the given application data.
 * @param data the application data,
**/

	public UnionFindNode(T data) {
		this.applicationData = data;
		rank = 0;
		parent = this;
	}

/**
 * @return a String that shows the application data held within the node.
**/

	public String toString() {
		return " " + applicationData;
	}

/**
 * @return the application data held within the node.
**/

	public T get() {
		return applicationData;
	}

/**
 * Resets the application data to the given value.
 * @param data the new value for the
 * application data
**/

	public void set(T data) {
		this.applicationData = data;
	}

/**
 * @return the representative
 * node for the component containing this node.
**/

	public UnionFindNode<T> findRepresentative() {
		if (this != parent)
			parent = parent.findRepresentative(); //perform path compression
		return parent;
	}

/**
 * @param x a union-find node,
 * @return true  if and only if
 * <code>x</code> and the node on which this method is called are in the same
 * component.
**/

	public boolean sameComponent(PartitionElement<T> x) {
		return x != null && x.findRepresentative() == findRepresentative();
	}

/**
 * Combines the components of <code>x</code> and
 * this partition element into one component
 * If <code>x</code> and the node on which this method is called are in
 * the same component then no change is made, and the root of their component is returned.
 * @param x a node whose component is to be
 * combined with the component of this partition element.
 * @return the representative element of the resulting component.
 * @throws IllegalArgumentException <code>x</code> is not a union-find node
**/

	public PartitionElement<T> union(PartitionElement<T> x) {
		if (!(x instanceof UnionFindNode))
			throw new IllegalArgumentException("union-find node expected");
		UnionFindNode<T> r1 = (UnionFindNode<T>) x.findRepresentative();
		UnionFindNode<T> r2 = this.findRepresentative();
		if (r1 == r2)
			return r1;  //already in some component, no change needed
		else { // only merge if x is in a different component
			if (r1.rank > r2.rank) { //link the roots to preserve Rank
				r2.parent = r1;
				return r1;
			} else {
				r1.parent = r2;
				if (r1.rank == r2.rank) //preserve Rank for new root
					r2.rank++;
				return r2;
			}
		}
	}


}
