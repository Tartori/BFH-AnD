package Sorting.Test;

import Sorting.QuickSortTemplate;
import org.junit.Assert;
import org.junit.Test;

public class QuickSortTemplateTest {

    @Test
    public void TestSort() {
        Integer[] ints = new Integer[]{2, 5, 4, 3, 7, 8, 1};

        QuickSortTemplate<Integer> sorter = new QuickSortTemplate<>(Integer::compareTo);

        Integer[] sorted = sorter.sort(ints);

        Integer[] intsExpected = new Integer[]{1, 2, 3, 4, 5, 7, 8};

        Assert.assertArrayEquals(sorted,intsExpected);
    }
}
