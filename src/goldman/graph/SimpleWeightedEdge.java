// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.graph;
/**
 * The simple weighted edge class provides a sample implementation for
 * a weighted edge.
**/

public class SimpleWeightedEdge<V> extends SimpleEdge<V> 
								implements WeightedEdge<V>  {
	double weight;

	public SimpleWeightedEdge(V head, V tail, double weight){
		super(head,tail);
		this.weight = weight;
	}
	public double weight() {return weight;}


	public String toString() {
		return super.toString() + "@" + weight;
	}

}

