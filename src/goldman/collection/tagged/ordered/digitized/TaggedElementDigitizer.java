// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.tagged.ordered.digitized;

import goldman.collection.Digitizer;
import goldman.collection.tagged.TaggedElement;

/**
 * The tagged digitized ordered collection data structures require digitizers for tagged elements
 * that depend only on the tags.  This class wraps a digitizer
 * defined for a tag type to create a digitizer defined for a corresponding tagged element type.
**/

public class TaggedElementDigitizer<T> implements Digitizer<TaggedElement<T,?>> {
	Digitizer<? super T> digitizer;

	public TaggedElementDigitizer(Digitizer<? super T> digitizer) {
		this.digitizer = digitizer;
	}

	public int getBase() {
		return digitizer.getBase();
	}

	public int getDigit(TaggedElement<T, ?> x, int place) {
		return digitizer.getDigit(x.getTag(), place);
	}

	public boolean isPrefixFree() {
		return digitizer.isPrefixFree();
	}
	
	public int numDigits(TaggedElement<T, ?> x) {
		return digitizer.numDigits(x.getTag());
	}


	public String formatDigit(TaggedElement<T, ?> x, int place) {
		return digitizer.formatDigit(x.getTag(), place);
	}

}

