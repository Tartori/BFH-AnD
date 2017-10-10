// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import java.util.Iterator;

/**
 * The TreeIterator class is an abstract class that implements the
 * <code>Iterator</code> interface for any search tree.
**/

public abstract class TreeIterator<E> implements Iterator<E> {
	TreeNode node;
	int visitedBranch;
	boolean consumed;
	
	public TreeIterator(TreeNode<E> node) {
		this.node = node;
		visitedBranch = -1;
		consumed = true;
	}
	
	boolean hasData(TreeNode child) {
		return child != null && !child.isFrontier();
	}
	
	boolean moveDown() {
		TreeNode descend = node.child(visitedBranch + 1);
		if (hasData(descend)) {
			while (hasData(descend.child(0)))
				descend = descend.child(0);
			node = descend;
			visitedBranch = 0;
			return true;
		} else
			return false;		
	}
	
	boolean advance() {
		if (visitedBranch + 1 < node.size()) {
			visitedBranch++;
			return true;
		} else
			return false;
	}
	
	boolean moveUp() {
		TreeNode<E> parent = node.parent();
		if (parent == null)
			return false;
		//visitedBranch = indexOf(node, parent);
		node = parent;
		return true;
	}

}

