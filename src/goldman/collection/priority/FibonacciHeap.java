// Copyright Sally A. Goldman and Kenneth J. Goldman, 2007
// Absolutely no warranty.
// Unauthorized distribution of this source code is prohibited.
// Use subject to license agreement.

package goldman.collection.priority;
import java.util.Arrays;
import java.util.Comparator;
import goldman.Objects;
import goldman.collection.Tracked;
import static java.lang.Math.*;
/**
 * The Fibonacci heap is a more complex self-organizing data structure.
 * It has <em>amortized</em> and worst-case costs for <code>add</code>
 * and <code>merge</code> are constant.  Increasing the priority of
 * an element has amortized constant cost, but a worst-case logarithmic
 * cost.  Removing an element or lowering the priority of an element has
 * logarithmic amortized cost, but
 * the worst-case cost can be linear.  It is a more complex data
 * structure, so unless the priority is often increased, the overall
 * time complexity is higher than that of the pairing heap.
 * However, using a Fibonacci heap yields the asymptotically fastest
 * implementation of Prim's minimum spanning
 * tree algorithm and Dijkstra's shortest path algorithm.
**/

public class FibonacciHeap<E> extends PairingHeap<E> implements 
	PriorityQueue<E>, Tracked<E> {

	static class FibonacciHeapNode<E> extends HeapNode<E> {

		int degree = 0;             //number of children (initially 0)
		boolean marked = false;     //true iff child detached since parent changed
		FibonacciHeapNode<E> parent; //parent (null if in root chain)

/**
 * @param element the element to be held in this node
**/

		public FibonacciHeapNode(E element) {
			super(element);
		}	

/**
 * Sets <code>sibL</code> and <code>sibR</code> so that this node becomes the root.
**/

		void makeRoot() {
			sibL = sibR = this;  //satisfies RootChain
			marked = false;      //parent changes when moved to root chain
			parent = null;       //satisfies Parent for a root
		}

/**
 * @param newChild a reference
 * to the heap node to be added as a child of this node
**/

		void addChild(HeapNode<E> newChild) {
			if (degree == 0) {                             //newChild will be an only child
				child = newChild;                             //satisfy Child
				newChild.sibR = newChild.sibL = newChild;     //satisfy SiblingChain
			} else {                                      //this node already has a child 
				newChild.setLeft(child.sibL);                 //preserve SiblingChain
				child.setLeft(newChild);			
			}
			((FibonacciHeapNode<E>) newChild).parent = this;  //preserve Parent
			((FibonacciHeapNode<E>) newChild).marked = false; //preserve Marked
			degree++;                                         //preserve Degree
		}

/**
 * Removes this heap node from its
 * current sibling chain
**/

		void removeFromChain() {
			if (parent != null) {   //preserve Parent
				parent.degree--;    //preserve Degree
				if (parent.child == this) {
					if (this == sibR)
						parent.child = null; //it's an only child
					else
						parent.child = sibR;
				}
			}
			sibR.setLeft(sibL);    //Preserve SiblingChain (or RootChain)
			makeRoot();
		}


	}

	public FibonacciHeap(){
		super(Objects.DEFAULT_COMPARATOR);
	}

/**
 * @param comp a user-provided comparator
**/

	public FibonacciHeap(Comparator<? super E> comp) {
		super(comp);
	}

/**
 * @param element the element to be stores in this heap node
 * @return a new heap node holding the given element.
**/

	HeapNode<E> newHeapNode(E element) {
		return new FibonacciHeapNode<E>(element);
	}

/**
 * Removes T(x) from its current position, and adds it to
 * the root chain.
 * @param x the root of a Fibonacci heap to move to the root chain
 * <BR> 
 * REQUIRES: 
 * x has a parent (i.e., <code>x.parent</code> is not null)
**/

	void moveToRootChain(HeapNode<E> x) {
		x.removeFromChain();
		mergeWithRoot(x);
	}

/**
 * @param r1 the root of T(r1)
 * @param r2 the root of T(r2)
 * <BR> 
 * REQUIRES: 
 *  neither
 * r1 nor r2 are null.
 * @return a reference to
 * the node with the higher
 * priority element among r1 and r2
**/

	FibonacciHeapNode<E> link(FibonacciHeapNode<E> r1, FibonacciHeapNode<E> r2) {
		FibonacciHeapNode<E> parent = r1;
		FibonacciHeapNode<E> child = r2;
		if (comp.compare(r1.element,r2.element) < 0) {
			parent = r2;
			child = r1;
		}
		if (root == child)  
			root = parent;
		child.removeFromChain();
		parent.addChild(child);
		return parent;
	}	


	private final double PHI = (1 + sqrt(5))/2;   //golden ratio  
	private FibonacciHeapNode[] bucket = null;   //for consolidate
	private FibonacciHeapNode<E> stopSentinel = new FibonacciHeapNode<E>(null); 


	final void prepareToConsolidate() {
		stopSentinel.setLeft(root.sibL);  //marks where to stop
		root.setLeft(stopSentinel);
		int degreeUpperBound = (int) ceil(log(getSize())/log(PHI)) + 1;
		if (bucket == null || bucket.length < degreeUpperBound)
			bucket = new FibonacciHeapNode[degreeUpperBound+1]; //okay until n doubles
		else if (bucket.length > 2*degreeUpperBound && bucket.length > 8)
			bucket = new FibonacciHeapNode[degreeUpperBound];
	}

/**
 * <BR> 
 * REQUIRES: 
 *  a maximum
 * priority element is in the root list and that the property Root is the only property that
 * may not hold.
**/

	private void consolidate() {
		prepareToConsolidate();          //initialize
		FibonacciHeapNode<E> ptr = (FibonacciHeapNode<E>) root;
		do {
			if (comp.compare(ptr.element, root.element) > 0)  //restore Root
				root = ptr;
			int degree = ptr.degree;
			FibonacciHeapNode<E> next = (FibonacciHeapNode<E>) ptr.sibR;
			while (bucket[degree] != null) {
				ptr = link(ptr, bucket[degree]); //link existing under current
				bucket[degree] = null;           //move out of current bucket
				degree++;                        //degree has now grown by 1
			}
			bucket[degree] = ptr;  //store T(ptr) is bucket degree
			ptr = next;
		} while (ptr != stopSentinel);  //continue until stop sentinel is reached
		stopSentinel.removeFromChain(); //remove the stop sentinel
		Arrays.fill(bucket,null);
	}

/**
 * Splices together the chains that
 * contain a and b, to form a single chain.
 * @param a a reference to a heap node
 * @param b a reference to
 * a heap node in a different chain
**/

	void splice(HeapNode<E> a, HeapNode<E> b) {
		HeapNode<E> temp = b.sibL;
		b.setLeft(a.sibL);
		a.setLeft(temp);
	}

/**
 * @param rootA the root of one Fibonacci heap
 * @param rootB the root of the other Fibonacci heap
 * <BR> 
 * REQUIRES: 
 *  <code>T(rootA)</code> and <code>T(rootB)</code> are valid
 * Fibonacci heaps
 * @return a reference to the root of the merged Fibonacci heap
**/

	HeapNode<E> merge(HeapNode<E> rootA, HeapNode<E> rootB) {
		splice(rootA,rootB);
		if (comp.compare(rootA.element, rootB.element) > 0)
			root = rootA;
		else
			root = rootB;
		return root;
	}

/**
 * @param x a reference
 * to the heap node holding
 * element e
 * @param element the new element to replace e
 * <BR> 
 * REQUIRES: 
 *  <code>element</code> &ge; <code>x.element</code> and
 * that <code>x</code> is not null
**/

	protected void increasePriority(HeapNode<E> x, E element){
		x.element = element;
		FibonacciHeapNode<E> parent = ((FibonacciHeapNode<E>) x).parent;
		if (parent != null && comp.compare(x.element,parent.element) > 0) 
			cut((FibonacciHeapNode<E>) x);    //preserves Marked
		if (comp.compare(element,root.element) > 0)  //preserve Root
			root = x;
	}

/**
 * Moves all children of x into the root chain
 * @param x a reference
 * to a heap node
 * <BR> 
 * REQUIRES: 
 * <code>x</code> is not null
 * @return true  if and only if x had at least on
 * child
**/

	boolean moveChildrenToRootChain(HeapNode<E> x){
		FibonacciHeapNode<E> ptr = (FibonacciHeapNode<E>) x.child;
		if (ptr ==  null)       //by prop Child has no children
			return false;
		do {                                  //preserve Parent
			ptr = (FibonacciHeapNode<E>) ptr.sibR;
			ptr.parent = null;	
		} while (ptr  != x.child);
		x.child = null;                        //preserve Child
		((FibonacciHeapNode<E>) x).degree = 0; //preserve Degree
		splice(root, ptr);                     //preserves RootChain
		return true;
	}

/**
 * Performs cascading cuts to preserve
 * the property Marked
 * @param x a heap node that is to be cut
 * away from its parent
 * <BR> 
 * REQUIRES: 
 *  <code>x</code> is not null
**/

	private void cut(FibonacciHeapNode<E> x) {
		FibonacciHeapNode<E> parent = x.parent;
		if (parent != null) {           //if x not already in root list
			x.removeFromChain();            //remove it from where it is
			moveToRootChain(x);             //and put int root list
			if (!parent.marked)             //if parent is not marked
				parent.marked = true;          //then mark to denote a child has been cut
			else 
				cut(parent);                   //recursively cut parent
		}
	}

/**
 * @param x a reference to the Fibonacci
 * heap node holding element e
 * @param element the new element to replace e
 * <BR> 
 * REQUIRES: 
 *  <code>element</code> &le; <code>x.element</code>
 * <code>x</code> is not null
**/

	void decreasePriority(HeapNode<E> x, E element) {
		FibonacciHeapNode<E> node = (FibonacciHeapNode<E>) x;  //reduce casting
		node.element = element;                                //replace element
		if (moveChildrenToRootChain(node)) {   //Case 1: move children to root chain
			cut(node);                            //apply cascading cut to preserve Marked
			consolidate();                        //consolidate root chain, and preserve Root
		} else if (node == root)               //Case 2
			consolidate();                       //need to find max priority element in root list
	}

/**
 * Removes x from the Fibonacci heap
 * @param x a reference to the heap node to remove
 * <BR> 
 * REQUIRES: 
 *  x is not null
 * @return the
 * element held in x
**/

	E remove(HeapNode<E> x) {
		if (getSize() == 1) { //special case when singleton element is being removed
			root = null;
		} else {
			moveChildrenToRootChain(x);    //preserve HeapOrdered
			if (root == x)                 //make sure root is somewhere in root chain
				root = root.sibR;
			cut((FibonacciHeapNode<E>) x); //move x to root chain
			x.removeFromChain();           //remove x
			consolidate();                    
		}
		x.markDeleted();           //preserve Removed
		x.prev.setNext(x.next);    //preserve IterationList
		size--;                    //preserve Size
		return x.element;
	}


	public String showStructure(){
		return printHeap("");
	}

	private String printHeap(String indent) {
		FibonacciHeapNode<E> start = (FibonacciHeapNode<E>) root;
		String result = "";
		do {
			result += ("\n Node: " +start+", Degree: "+start.degree+", Marked: " + start.marked+"\n"+indent+
					"Left: "+start.sibL+"\n"+indent+"Right: "+start.sibR+"\n"+indent+"Parent: " +start.parent+"\n");
			result+=(printChildren(indent, start));
			start = (FibonacciHeapNode<E>) start.sibR;
		} while (start != root);
		return result;
	}
	
	private String printChildren(String indent, FibonacciHeapNode start) {
		FibonacciHeapNode<E> x = (FibonacciHeapNode<E>) start.child;
		String childIndent, result = "";
		if (x != null) {
			result +=(indent+"Children of "+start+"\n");
			childIndent = indent+="\t";
			do {
				result += (indent+"Child: "+ x +", Degree: " + x.degree + ", Marked: " + x.marked);
				indent +="\t";
				result += ("\n"+indent+"Left: "+ x.sibL+"\n"+indent+"Right: "+ x.sibR +"\n");
				if (x.child != null)
					result += (printChildren(indent,x));
				x = (FibonacciHeapNode<E>) x.sibR;
				indent = childIndent;
			} while (x != start.child);
		}
		return result;
	}
}
