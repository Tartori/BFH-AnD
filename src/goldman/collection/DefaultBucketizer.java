// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;

/**
 * This class illustrates an implementation of
 * the <code>Bucketizer</code> interface.
**/

public class DefaultBucketizer<T> implements Bucketizer<T> {

	Collection<T> coll;               //collection on which it is defined
	Quantizer<? super T> quantizer;   //quantizer provided to convert element to double
	double min, max;                  //minimum and maximum quantized value in collection
	double range;                     //the difference of the maximum and minimum values
	int numBuckets;                   //the number of buckets

/**
 * @param coll the collection to which the bucketizer applies
 * @param quantizer for converting each element to a double
**/

	public DefaultBucketizer(Collection<T> coll, Quantizer<? super T> quantizer) {
		this.coll = coll;
		this.quantizer = quantizer;
		numBuckets = coll.getSize();
		computeRange();
	}

/**
 * @param o1 one element to compare
 * @param o2 the second element
 * to compare
 * @return a negative integer when
 * o1 is less than o2, zero when o1 is equivalent to o2, and a positive
 * integer when o1 is greater than o2.
**/

	public int compare(T o1, T o2) {
		double double1 = quantizer.getDouble(o1);
		double double2 = quantizer.getDouble(o2);
		if (double1 < double2)
			return -1;
		else if (double1 == double2)
			return 0;
		else
			return 1;
	}


/**
 * Computes the range of values created by the quantizer and stores
 * the minimum value in <code>min</code> and the maximum value in <code>max</code> to avoid
 * the need to recompute these values.
**/

	protected void computeRange() {
		if (coll instanceof Interval) {  //min and max available
			min = quantizer.getDouble(((Interval<T>) coll).getMin());
			max = quantizer.getDouble(((Interval<T>) coll).getMax());		
		} else {                 //compute min and max
			for (T element: coll){
				double x = quantizer.getDouble(element);
				if (x < min)
					min = x;
				else if (x > max)
					max = x;	
			}
		}
		range = max - min;		//range of quantized values
	}

/**
 * @return the number of buckets
 * among which the elements are placed.
**/

	public int getNumBuckets() {
		return numBuckets;
	}

/**
 * @param element the desired
 * element in the collection
 * @return the bucket where <code>element</code>
 * should be placed.  The return value is guaranteed to be an integer between 0 and
 * <code>numBuckets</code> - 1.
**/

	public int getBucket(T element) {
		double x = quantizer.getDouble(element);
		if (x == max)
			return numBuckets - 1;
		else
			return (int) (numBuckets * (x - min)/range);
	}


}
