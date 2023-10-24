package com.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface IntLongBiPredicate extends BiFunction<Integer, Long, Boolean>, BiPredicate<Integer, Long> {
    boolean test(int i, long j);

    @Deprecated
    @Override
    default Boolean apply(Integer aShort, Long aShort2) {
        return test(aShort, aShort2);
    }

    @Deprecated
    @Override
    default boolean test(Integer aShort, Long aShort2) {
        return test(aShort.intValue(), aShort2.longValue());
    }
}
