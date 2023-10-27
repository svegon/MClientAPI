package io.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface IntDoubleBiPredicate extends BiFunction<Integer, Double, Boolean>, BiPredicate<Integer, Double> {
    boolean test(int i, double j);

    @Deprecated
    @Override
    default Boolean apply(Integer aShort, Double aShort2) {
        return test(aShort, aShort2);
    }

    @Deprecated
    @Override
    default boolean test(Integer aShort, Double aShort2) {
        return test(aShort.intValue(), aShort2.doubleValue());
    }
}
