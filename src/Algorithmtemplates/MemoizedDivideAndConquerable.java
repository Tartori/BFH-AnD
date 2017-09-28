package Algorithmtemplates;

import java.util.Vector;

/**
 * Created by julia on 19-Sep-17.
 */
public interface MemoizedDivideAndConquerable<T> extends DivideAndConquerable<T> {

    abstract void  memoize(DivideAndConquerable<T> input, T output);

    abstract T getMemoizedResult(DivideAndConquerable<T> input);

    abstract boolean hasMemoizedResult(DivideAndConquerable<T> input);

    default T divideAndConquer() {
        if (isBase()) return baseFun();
        Vector<DivideAndConquerable<T>>
                subcomponents = decompose();
        Vector<T>
                intermediateresults =
                new Vector<T>(
                        subcomponents.size());
        subcomponents
                .forEach(
                        subcomponent -> {
                            if(hasMemoizedResult(subcomponent))
                                intermediateresults.add(getMemoizedResult(subcomponent));
                            else {
                                T result = subcomponent.divideAndConquer();
                                intermediateresults.add(result);
                                memoize(subcomponent, result);
                            }
                        });
        return recompose(intermediateresults);
    }
}
