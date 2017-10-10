// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman;
import java.util.Comparator;
/**
 * The ReverseComparator class wraps a provided comparator, reversing
 * the sign of the output.
**/

public class ReverseComparator<T> implements Comparator<T> {
	Comparator<? super T> comp;
	
	public ReverseComparator(Comparator<? super T> comp) {this.comp = comp;}
	
	public final int compare(T a, T b) {return -comp.compare(a,b);}
}

