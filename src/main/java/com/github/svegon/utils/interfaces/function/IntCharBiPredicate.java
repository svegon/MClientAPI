package com.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface IntCharBiPredicate extends BiFunction<Integer, Character, Boolean>, BiPredicate<Integer, Character> {
    boolean test(int i, char c);

    @Deprecated
    @Override
    default Boolean apply(Integer integer, Character integer2) {
        return test(integer, integer2);
    }

    @Deprecated
    @Override
    default boolean test(Integer integer, Character integer2) {
        return test(integer.intValue(), integer2.charValue());
    }
}
