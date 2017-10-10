// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;

/**
 * The BitDigitizer class implements the <code>Digitizer</code> interface
 * for a bit string.
**/

public class BitDigitizer implements Digitizer<String> {

	public int getBase() {
		return 2;
	}

	public boolean isPrefixFree() {
		return false;
	}
	
	public int numDigits(String x) {
		return x.length();
	}

	public int getDigit(String x, int place) {
		return x.toLowerCase().charAt(place) - '0';
	}
	
	public String formatDigit(String x, int place) {
		return "" + x.toLowerCase().charAt(place);
	}

}

