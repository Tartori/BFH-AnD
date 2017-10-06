package Sorting.Test;

import Sorting.MergeSortDnC;
import Sorting.MergeSortTemplate;
import org.junit.Assert;
import org.junit.Test;

public class MergeSortDnCTest {
    @Test
    public void TestSort() {
        Integer[] ints = new Integer[]{2, 5, 4, 3, 7, 8, 0, 1};

        MergeSortDnC<Integer> sorter = new MergeSortDnC<>(ints, Integer::compareTo);
        sorter.sort();

        Integer[] intsExpected = new Integer[]{0, 1, 2, 3, 4, 5, 7, 8};

        Assert.assertArrayEquals(ints,intsExpected);

    }
}
