package Algorithmtemplates;
import java.util.Vector;

public interface DivideAndConquerable <OutputType > {
    boolean isBase();

    OutputType baseFun();

    Vector<DivideAndConquerable<OutputType>> decompose();

    OutputType recompose(Vector<OutputType> intermediateResults);


    default OutputType divideAndConquer() {
        if (isBase()) return baseFun();
        Vector<DivideAndConquerable<OutputType>>
                subcomponents = decompose();
        Vector<OutputType>
                intermediateresults =
                new Vector<OutputType>(
                        subcomponents.size());
        subcomponents.forEach(
                subcomponent -> intermediateresults.add(
                        subcomponent.divideAndConquer())
        );
        return recompose(intermediateresults);
    }
}
