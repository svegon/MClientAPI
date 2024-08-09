package io.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface IntShortBiPredicate extends BiFunction<Integer, Short, Boolean>, BiPredicate<Integer, Short> {
    boolean test(int i, short j);

    @Deprecated
    @Override
    default Boolean apply(Integer aShort, Short aShort2) {
        return test(aShort, aShort2);
    }

    @Deprecated
    @Override
    default boolean test(Integer aShort, Short aShort2) {
        return test(aShort.intValue(), aShort2.shortValue());
    }
}
