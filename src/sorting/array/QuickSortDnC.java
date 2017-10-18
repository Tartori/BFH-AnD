package sorting.array;

import Algorithmtemplates.DivideAndConquerable;

import java.util.Comparator;
import java.util.Vector;

public class QuickSortDnC<E> implements DivideAndConquerable{
    Comparator<? super E> sorter;
    private E[] data;
    private QuickSortHelper<E> helper;
    private int left;
    private int right;

    public QuickSortDnC(Comparator<? super E> sorter) {
        this.sorter = sorter;
    }

    private QuickSortDnC(E[] data, int left, int right, Comparator<? super E> sorter, QuickSortHelper<E> helper){
        this.data=data;
        this.left=left;
        this.right=right;
        this.sorter=sorter;
        this.helper=helper;
    }

    public E[] sort(E[] data) {
        this.data = data.clone();
        helper=new QuickSortHelper<>(sorter, this.data);
        left=0;
        right=data.length-1;
        divideAndConquer();
        return this.data;
    }

    @Override
    public boolean isBase() {
        return left >= right;
    }

    @Override
    public Object baseFun() {
        return data;
    }

    @Override
    public Vector<DivideAndConquerable> decompose() {

        Vector<DivideAndConquerable> vector = new Vector<>();
        ArrayHelper.swap(data, helper.getMedianOfThree(left, right), right);
        int mid = helper.partition(left, right);

        vector.add(new QuickSortDnC<>(data, left, mid - 1, sorter, helper));
        vector.add(new QuickSortDnC<>(data, mid + 1, right, sorter, helper));
        return vector;
    }

    @Override
    public Object recompose(Vector intermediateResults) {
        return data;
    }
}

