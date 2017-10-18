package sorting.array;

import java.util.*;

public class QuickSortTemplate<E> {

    Comparator<? super E> sorter;
    private E[] data;
    private QuickSortHelper<E> helper;


    public QuickSortTemplate(Comparator<? super E> sorter) {
        this.sorter = sorter;
    }

    public E[] sort(E[] data) {
        this.data = data.clone();
        helper=new QuickSortHelper<E>(sorter, this.data);
        quicksortImpl(0, data.length - 1);
        return this.data;
    }

    void quicksortImpl(int left, int right) {
        if (left < right) {
            ArrayHelper.swap(data, helper.getMedianOfThree(left, right), right);
            int mid = helper.partition(left, right);
            quicksortImpl(left, mid - 1);
            quicksortImpl(mid + 1, right);
        }
    }
}
