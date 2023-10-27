package io.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntCharBiFunction<R> extends BiFunction<Integer, Character, R> {
    R apply(int i, char c);

    @Override
    default R apply(Integer integer, Character character) {
        return apply((int) integer, (char) character);
    }
}
