package sorting.array;

import java.util.Comparator;

public class QuickSortHelper <E>{

    Comparator<? super E> sorter;
    private E[] data;


    public QuickSortHelper(Comparator<? super E> sorter, E[] data) {
        this.data = data;
        this.sorter = sorter;
    }

    public  int getMedianOfThree(int left, int right) {
        if (right - left + 1 >= 3) {
            int mid = (left + right) / 2;
            E leftObject = ArrayHelper.read(data, left);
            E midObject = ArrayHelper.read(data, mid);
            E rightObject = ArrayHelper.read(data, right);
            if (sorter.compare(leftObject, midObject) <= 0) {
                if (sorter.compare(midObject, rightObject) <= 0)
                    return mid;
                else if (sorter.compare(rightObject, leftObject) <= 0)
                    return left;
            } else if (sorter.compare(midObject, rightObject) > 0) {
                return mid;
            }
        }
        return right;
    }

    public  int partition(int left, int right) {
        E pivot = ArrayHelper.read(data, right);
        int i = left;
        int j = right;
        while (i < j) {
            while (i < j && sorter.compare(ArrayHelper.read(data, i), pivot) < 0)
                i++; // move right ( paint green ) in left partition
            while (j > i && sorter.compare(ArrayHelper.read(data, j), pivot) >= 0)
                j--; // move left ( paint orange ) in right partition
            if (i < j) ArrayHelper.swap(data, i, j); // partition (" white swap ")
        }
        ArrayHelper.swap(data, i, right); // "orange - yellow swap "
        return i; // return mid - element
    }
}
