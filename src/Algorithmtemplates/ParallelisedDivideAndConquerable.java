package Algorithmtemplates;

import java.util.Vector;

/**
 * Created by julia on 19-Sep-17.
 */
public interface ParallelisedDivideAndConquerable<T> extends DivideAndConquerable<T> {

    default T divideAndConquer() {
        if (isBase()) return baseFun();
        Vector<DivideAndConquerable<T>>
                subcomponents = decompose();
        Vector<T>
                intermediateresults =
                new Vector<T>(
                        subcomponents.size());
        subcomponents
                .parallelStream()
                .forEach(
                subcomponent -> intermediateresults.add(
                        subcomponent.divideAndConquer())
        );
        return recompose(intermediateresults);
    }

}
