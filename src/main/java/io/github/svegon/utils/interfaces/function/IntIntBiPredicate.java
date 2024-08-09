package io.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface IntIntBiPredicate extends BiFunction<Integer, Integer, Boolean>, BiPredicate<Integer, Integer> {
    boolean test(int i, int j);

    @Deprecated
    @Override
    default Boolean apply(Integer integer, Integer integer2) {
        return test(integer, integer2);
    }

    @Deprecated
    @Override
    default boolean test(Integer integer, Integer integer2) {
        return test(integer.intValue(), integer2.intValue());
    }
}
