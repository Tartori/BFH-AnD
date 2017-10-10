// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.ordered;
import goldman.Objects;
import java.util.Comparator;

/**
 * The top down B-tree implements a variation of a B-tree
 * that uses top-down (versus bottom-up) insertion and deletion.
**/

public class TopDownBTree<E> extends BTree<E> implements OrderedCollection<E> {

	class TopDownBTreeNode extends BTreeNode {

//guaranteed that this node is not full
		BTreeNode addElement(int i, E element, BTreeNode rightChild){
			BTreeNode x = this; //pointer to node where element will be added
			if (x.size()-i > 0)  //need to make room for rightChild
				System.arraycopy(x.children,i+1,x.children,i+2,x.size()-i);
			x.data.add(i, element);  //add new element
			x.setChild(i+1,rightChild); //add new child
			return x;   //return the node where the new element was placed
		}
//guaranteed that this node is not minimum sized		
		void remove(int i) { 
			BTreeNode x = this;
			x.extract(i);
			if (x.size() - i > 0) //shift children right of index back by one
				System.arraycopy(x.children,i+2,x.children,i+1,x.size()-i);
			x.children[x.size()+1] = null; //reset child not in use to null
		}
	}
	
	public TopDownBTree() { 
		super(Objects.DEFAULT_COMPARATOR,2);
	}

	public TopDownBTree(int t) { 
		super(t);
	}

	public TopDownBTree(Comparator<? super E> comp, int t) {
		super(comp, t);
	}

//during search in insert, split any node reached that is full
	protected TreeNode insert(E element){
		if (isEmpty()) {
			createRoot();
			((BTreeNode) root).data.add(0, element);
			((BTreeNode) root).children[0] = (BTreeNode) FRONTIER;
			((BTreeNode) root).children[1] = (BTreeNode) FRONTIER;
			return root;
		} else {
			BTreeNode ptr = (BTreeNode) root;			//start search at the root
			while (ptr.atMaxSize() || !ptr.isLeaf() ) {
				curIndex = ptr.data.findLastInsertPosition(0,ptr.size()-1,element);
				if (ptr.atMaxSize()){
					BTreeNode newNode = ptr.split();
					if (curIndex >= t) {
						curIndex -= t;
						ptr = newNode;
					}
				}
				else
					ptr = ptr.child(curIndex);
			}
			curIndex = ptr.data.findLastInsertPosition(0,ptr.size()-1,element);
			version.increment();   //invalidate all markers for iteration
			return ptr.addElement(curIndex,element,(BTreeNode) FRONTIER);
		}
	}

//this method is used to ensure that if a minimum-sized node is encountered on the
//  search path to the predecessor (which will replace the element to remove), that
//  the tree is restructured so that node on which this method is called is not
//  minimum-sized.
	
	TreeNode moveToPredecessor(TreeNode ptr) {
		BTreeNode x = (BTreeNode) ptr;  //avoid need to cast elsewhere
		while (!x.rightmostChild().isFrontier()) {
			int pIndex = x.pIndex;
			int childIndex = x.size();        //before restructuring, predecessor in rightmost subtree
			if (x.atMinSize() && x != root) { //can't remove prior to restructing
				BTreeNode parent = x.parent();
				if (pIndex > 0 && !parent.child(pIndex-1).atMinSize()){ //Case 1, shift in new element from right
					x.shiftRight(parent,pIndex);
					childIndex++;    //desired child to move to next has moved to the right by one
				} else if (x.pIndex < parent.size() && 
						!parent.child(pIndex+1).atMinSize()) { //Case 2, shift in new element from left
					x.shiftLeft(parent,pIndex);  //don't change which child to move to
				} else {
					if (pIndex != parent.size())   //Case 3a, merge with right child
						x.merge(parent,pIndex);          //don't change which child to move to
					else {     				       //Case 3b, merge with left child
						x = parent.child(pIndex-1);      //move x to left sibling
						x.merge(parent, pIndex-1);       //now merge with right child
						childIndex = x.size();           //desired child rightmost of merged node
						
					}
				}
			}
			x = (BTreeNode) x.child(childIndex);  //move to desired child
		}
		return x;
	}
	
//modify public remove method so that find is replaced by a search that restructures
//  the tree when any minimum-sized node is on the path. 
	
	public boolean remove(E element) {
		if (isEmpty())
			return false;
		BTreeNode x = (BTreeNode) root;
		int i = 0;
		while (!x.isFrontier()) {
			curIndex = x.data.find(element);
			i = curIndex;
			int pIndex = x.pIndex;
			if (x.atMinSize() && x != root) { //can't remove prior to restructing
				BTreeNode parent = x.parent();
				if (pIndex > 0 && !parent.child(pIndex-1).atMinSize()){ //Case 1
					x.shiftRight(parent,pIndex);
					i++;  //element to be removed shifted right by one
				} else if (pIndex < parent.size() && 
						!parent.child(pIndex+1).atMinSize()) { //Case 2
					x.shiftLeft(parent,pIndex);			
				} else {
					if (pIndex != parent.size()) //Case 3a
						x.merge(parent,pIndex);
					else {     				   //Case 3b
						x = parent.child(pIndex-1);        //move x to left sibling
						x.merge(parent, pIndex-1);
						int offsetFromEnd = t-1-i;
						i = x.size() - offsetFromEnd; //update to index in joined node
					}
				}
			}
			if (i < x.size() && comp.compare(element, x.data.get(i)) == 0)
				break;
			((BTreeNode) FRONTIER).parent = x;
			x = x.child(i);
		}
		if (x.isFrontier()) 
			return false;  
		remove(x, i);
		version.increment();
		return true;
	}

//The remove method that takes the node and element to remove is modified so that
//    moveToPredecessor (versus rightmost) is used to ensure that the tree is
//	  restructured if any minimum-sized node on the search path to the predecessor is
//	  encountered.  In addition, if this restructuring causes the node x holding the
//    element to remove changes, then x and index are updated.
	
	void remove(BTreeNode x, int index) {
		if (x.isLeaf()) {    //Case 1
			x.remove(index);
			size--;
		} else {             //Case 2
			int xSize = x.size();
			int	xLeftSize = x.child(index).size();
			int	xRightSize = x.child(index+1).size();
			BTreeNode pred = (BTreeNode) moveToPredecessor(x.child(index)); 
			if (x.size() != xSize) {  //if element to remove was combined with children during merge
				x = x.child(index);      //update x to reference node with element to remove 
				index = t-1;             //after merge element to remove is at median position
			}
			else if (x.child(index).size() > xLeftSize) {  //if element to remove was moved left by shift left
				x = x.child(index);      //update x to reference node with element to remove 
				index = x.size()-1;      //after shift left, element to remove is rightmost in node
			}
			else if (x.child(index+1).size() > xRightSize){ //if element to remove was moved right by shift right
				x = x.child(index+1);    //update x to reference node with element to remove
				index = 0;               //after shift right, element to remove is leftmost in node
			}
			x.data.set(index,pred.data(pred.size()-1));
			remove(pred,pred.size()-1);
		}
	}
}

