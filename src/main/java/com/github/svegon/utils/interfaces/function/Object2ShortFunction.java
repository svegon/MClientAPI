package com.github.svegon.utils.interfaces.function;

import java.util.function.Function;

@FunctionalInterface
public interface Object2ShortFunction<T> extends Function<T, Short>,
        it.unimi.dsi.fastutil.objects.Object2ShortFunction<T> {
    short applyAsShort(T o);

    @Deprecated
    @Override
    default Short apply(T t) {
        return applyAsShort(t);
    }

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default short getShort(Object key) {
        return applyAsShort((T) key);
    }
}
