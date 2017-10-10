// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
/**
 * The StringDigitizer class is an implementation of the
 * <code>Digitizer</code> interface for a string composed of only the 26 lower
 * case letters.
 * <BR> 
 * REQUIRES: 
 *  all strings provided as arguments
 * contain only the 26 lower case letters
**/

public class StringDigitizer implements PrefixFreeDigitizer<String> {
	int base; //base including the end of string character


	public StringDigitizer(int alphabetSize){base = alphabetSize + 1;}

/**
 * @return the base of the alphabet that
 * includes the end of string character.
**/

	public int getBase() {return base;}

/**
 * @return true  since
 * this is a prefix free digitizer.
**/

	public boolean isPrefixFree() {return true;}

/**
 * @param x the given string
 * @return the number of digits in the string including the end of string
 * character.
**/

	public int numDigits(String x) {return x.length() + 1;}

/**
 * @param x the given string
 * @param place the place of the desired digit, where the leftmost digit
 * is place 0
 * @return the integer value mapped to by
 * the digit in the
 * given place
**/

	public int getDigit(String x, int place) {
		if (place >= x.length())
			return 0;
		else 
			return x.toLowerCase().charAt(place) - 'a' + 1;
	}

/**
 * @param x the given string
 * @param place the place for the desired digit
 * @return a string with the digit in the given place where "\#" is used
 * %for the end of string character.
**/

	public String formatDigit(String x, int place) {
		if (place >= x.length())
			return "#";
		else 
			return "" + x.toLowerCase().charAt(place);
	}

}

