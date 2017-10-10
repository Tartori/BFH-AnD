// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection;

import java.util.ConcurrentModificationException;
/**
 * The Version class is used to manage a modification count for each class
 * that is used to determine when a concurrent modification has occurred.
 * Each data structure has one instance of the Version class.
 * The locator implementation has the option of updating its own copy of
 * the modification count when the locator calls a mutating operation on the data structure.
**/

public class Version {

	int modificationCount = 0;
	
	public int increment() {
		return ++modificationCount;
	}
	
	public int getCount() {
		return modificationCount;
	}
	
	public void restoreCount(int count) {
		modificationCount = count;
	}
	
	public void check(int v) {
		if (v < modificationCount)
			throw new ConcurrentModificationException("stale locator");
	}


}
