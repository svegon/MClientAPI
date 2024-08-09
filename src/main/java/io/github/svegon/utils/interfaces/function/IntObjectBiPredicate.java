package io.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface IntObjectBiPredicate<T> extends BiFunction<Integer, T, Boolean>, BiPredicate<Integer, T> {
    boolean test(int i, T o);

    @Deprecated
    @Override
    default Boolean apply(Integer integer, T t) {
        return test(integer, t);
    }

    @Deprecated
    @Override
    default boolean test(Integer integer, T t) {
        return test(integer.intValue(), t);
    }
}
