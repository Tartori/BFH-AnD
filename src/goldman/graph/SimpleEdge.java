// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
/**
 * The SimpleEdge class provides a sample implementation for an edge.
**/

public class SimpleEdge<V> implements Edge<V> {
	V head;
	V tail;

	public SimpleEdge(V head, V tail) {
		this.head = head;
		this.tail = tail;
	}
	public V source() {return head;}
	public V dest() {return tail;}


	public String toString() {
		return "(" + head + "," + tail + ")";
	}

}

