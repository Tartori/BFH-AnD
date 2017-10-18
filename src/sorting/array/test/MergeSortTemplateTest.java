
package sorting.array.test;
import sorting.array.MergeSortTemplate;
import org.junit.Assert;
import org.junit.Test;

public class MergeSortTemplateTest {

    @Test
    public void TestSort() {
        Integer[] ints = new Integer[]{2, 5, 4, 3, 7, 8, 1};

        MergeSortTemplate<Integer> sorter = new MergeSortTemplate<>();

        sorter.sort(ints, Integer::compareTo);

        Integer[] intsExpected = new Integer[]{1, 2, 3, 4, 5, 7, 8};

        Assert.assertArrayEquals(ints,intsExpected);

    }
}
