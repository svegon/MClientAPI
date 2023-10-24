package com.github.svegon.utils.interfaces.function;

import java.util.function.Function;

@FunctionalInterface
public interface Object2BooleanFunction<T> extends Function<T, Boolean>,
        it.unimi.dsi.fastutil.objects.Object2BooleanFunction<T> {
    boolean applyToBoolean(T o);

    @Deprecated
    @Override
    default Boolean apply(T t) {
        return applyToBoolean(t);
    }

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default boolean getBoolean(Object key) {
        return applyToBoolean((T) key);
    }
}
