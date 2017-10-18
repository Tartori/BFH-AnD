package sorting.array;

public class ArrayHelper {
    public static <E> void swap(E[] data, int i, int j){
        E temp = data[i];
        data[i]= data[j];
        data[j]=temp;
    }

    public static <E> E read(E[] data, int position){
        return data[position];
    }
}
