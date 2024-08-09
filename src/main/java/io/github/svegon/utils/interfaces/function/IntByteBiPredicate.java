package io.github.svegon.utils.interfaces.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface IntByteBiPredicate extends BiFunction<Integer, Byte, Boolean>, BiPredicate<Integer, Byte> {
    boolean test(int i, byte b);

    @Deprecated
    @Override
    default Boolean apply(Integer integer, Byte aByte) {
        return test(integer.intValue(), aByte.byteValue());
    }

    @Deprecated
    @Override
    default boolean test(Integer integer, Byte aByte) {
        return test(integer.intValue(), aByte.byteValue());
    }
}
