package io.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface IntFloatBiPredicate extends BiFunction<Integer, Float, Boolean>, BiPredicate<Integer, Float> {
    boolean test(int i, float j);

    @Deprecated
    @Override
    default Boolean apply(Integer aShort, Float aShort2) {
        return test(aShort, aShort2);
    }

    @Deprecated
    @Override
    default boolean test(Integer aShort, Float aShort2) {
        return test(aShort.intValue(), aShort2.floatValue());
    }
}
