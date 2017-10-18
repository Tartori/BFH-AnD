package sorting.array;

import java.util.Comparator;

public class MergeSortTemplate<E> {

    public void sort(Object[] data, Comparator<?super E> comparator){
        mergesortImpl(data, data.clone(),0, data.length-1, comparator);
    }

    private void mergesortImpl(
            Object[] data, // input to sort / sorted output
            Object[] aux, // merged data ( initially none )
            int left, // data start ( initially 0)
            int right, // data end ( initially input size -1)
            Comparator<? super E> sorter) {
        if (left < right) { // else input data size is 1 (no sort )
            int mid = (left + right) / 2;
            int i = left; // sorted left - half start
            int j = mid + 1; // sorted right - half start
            int k = left; // sorted merged - halves start

            mergesortImpl(data, aux, left, mid, sorter); // l- DnC
            mergesortImpl(data, aux, mid + 1, right, sorter); // r- DnC
            while (i <= mid && j <= right) { // l-r- merge left & right
                if (sorter.compare((E) data[i], (E) data[j]) < 0)
                    aux[k++] = data[i++];
                else
                    aux[k++] = data[j++];
            }

            // copy possible sorted data left - over into sub -aux
            System.arraycopy(data, i, aux, k, mid - i + 1);
            // copy processed sub - aux into data for output
            System.arraycopy(aux, left, data, left, j - left);
        }
    }
}
