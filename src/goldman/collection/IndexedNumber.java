// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;
import java.math.BigInteger;
/**
 * The IndexedNumber class illustrates a way to provide access to the
 * digits of a number.
**/

public class IndexedNumber {

	private char[] digits;

/**
 * Initializes the value of this indexed number to the provided integer.
**/

	public IndexedNumber(int value) {this(""+value);}

/**
 * Initializes the value of this indexed number to the provided long.
**/

	public IndexedNumber(long value) {this(""+value);}

/**
 * Initializes the value of this indexed number to the provided BigInteger.
**/

	public IndexedNumber(BigInteger value) {this(""+value);}


	private IndexedNumber(String rep) {digits = rep.toCharArray();}

/**
 * @return the number of
 * digits in the element
**/

	public int numDigits() {
		return digits.length;
	}

/**
 * @return false  since
 * this is not a prefix free digitizer
**/

	public boolean isPrefixFree() {
		return false;
	}

/**
 * @return the indexed number
 * represented as a character string
**/

	public String toString() {
		return new String(digits);
	}

/**
 * Returns the integer representation for the
 * digit at the given <code>place</code>.   This method treats each element
 * as if it was padded infinitely to the left with zeros.   For example,
 * 562 can be viewed as ...000562.
**/

	public int getDigit(int place) {
		if (place >= digits.length)  //pad infinitely to left with 0s
			return 0;
		else if (place < 0)
			throw new IllegalArgumentException("negative place " + place);
		return digits[digits.length - 1 - place] - '0';
	}
	

/**
 * Returns the string representation for the digit in the given place.
**/

/*
\tagcommentx{Returns the string representation for the digit in the given place.}
 */
	public String printDigit(int place) {
		if (place >= digits.length)
			return null;
		else if (place < 0)
			throw new IllegalArgumentException("negative place " + place);
		return "" + digits[digits.length - 1 - place];
	}
/**
 * This class provides a sample implementation for the <code>Digitizer</code>
 * interface for base 10 numbers that uses standard place value to report the value
 * for each digit.
**/

	public static class NumberDigitizer implements Digitizer<IndexedNumber> {

		public int getBase() {
			return 10;
		}

		public int numDigits(IndexedNumber x) {
			return x.numDigits();
		}

		public boolean isPrefixFree() {
			return false;
		}
		
		public int getDigit(IndexedNumber x, int place) {
			return x.getDigit(place);
		}	


		public String formatDigit(IndexedNumber x, int place){
			return "" + x.getDigit(place);
		}

	}


}
