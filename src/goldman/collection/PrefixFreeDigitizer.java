// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;

/**
 * The <code>PrefixFreeDigitizer</code> interface adds the requirement that
 * the digitizer must enforce that no element is a prefix of another.  An easy way
 * to enforce this requirement is to introduce a special "end of string" character that
 * is placed at the end of every string.
**/

public interface PrefixFreeDigitizer<T> extends Digitizer<T> {
}

