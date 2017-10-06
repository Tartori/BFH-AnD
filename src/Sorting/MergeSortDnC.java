package Sorting;

import Algorithmtemplates.DivideAndConquerable;

import java.util.Comparator;
import java.util.Vector;

public class MergeSortDnC<E> implements DivideAndConquerable {

    private static final int BASELENGTH = 1;
    private final int left;
    private final int right;
    private final Comparator<? super E> comparator;

    private Object[] aux;
    private Object[] data;

    private MergeSortDnC( Object[] data, // input to sort / sorted output
                         Object[] aux, // merged data ( initially none )
                         int left, // data start ( initially 0)
                         int right, // data end ( initially input size -1)
                         Comparator<? super E> sorter){
        this.data=data;
        this.aux=aux;
        this.left=left;
        this.right=right;
        this.comparator=sorter;

    }

    public MergeSortDnC( Object[] data, // input to sort / sorted output
                          Comparator<? super E> sorter){
        this.data=data;
        this.aux=data.clone();
        this.left=0;
        this.right=data.length-1;
        this.comparator=sorter;
    }



    public void sort(){
        divideAndConquer();
    }



    @Override
    public boolean isBase() {
        return left>=right;
    }

    @Override
    public Object baseFun() {
        return data;
    }

    @Override
    public Vector<DivideAndConquerable> decompose() {
        int mid = (left + right) / 2;
        int i = left; // sorted left - half start
        int j = mid + 1; // sorted right - half start
        int k = left; // sorted merged - halves start
        Vector<DivideAndConquerable> vector = new Vector<>();

        vector.add(new MergeSortDnC<>(data, aux, left, mid, comparator));
        vector.add(new MergeSortDnC<>(data, aux, mid + 1, right, comparator));
        return vector;
    }

    @Override
    public Object recompose(Vector intermediateResults) {
        int mid = (left + right) / 2;
        int i = left; // sorted left - half start
        int j = mid + 1; // sorted right - half start
        int k = left; // sorted merged - halves start
        while (i <= mid && j <= right) { // l-r- merge left & right
            if (comparator.compare((E) data[i], (E) data[j]) < 0)
                aux[k++] = data[i++];
            else
                aux[k++] = data[j++];
        }

        // copy possible sorted data left - over into sub -aux
        System.arraycopy(data, i, aux, k, mid - i + 1);
        // copy processed sub - aux into data for output
        System.arraycopy(aux, left, data, left, j - left);
        return data;
    }
}
