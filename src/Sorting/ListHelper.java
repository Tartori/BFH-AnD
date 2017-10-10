package Sorting;

import java.util.List;

public class ListHelper {
    public static <E> void swap(List<E> data, int i, int j){
        E temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }
}
