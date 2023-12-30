package io.github.svegon.utils.interfaces.function;

import java.util.function.Function;

@FunctionalInterface
public interface Object2FloatFunction<T> extends it.unimi.dsi.fastutil.objects.Object2FloatFunction<T> {
    float applyAsFloat(T o);

    @Deprecated
    @Override
    default Float apply(T t) {
        return applyAsFloat(t);
    }

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default float getFloat(Object key) {
        return applyAsFloat((T) key);
    }

    static <T> Object2FloatFunction<T> cast(Function<T, Float> f) {
        if (f instanceof Object2FloatFunction) {
            return (Object2FloatFunction<T>) f;
        }

        if (f instanceof it.unimi.dsi.fastutil.objects.Object2FloatFunction<T> func) {
            return func::getFloat;
        }

        return f::apply;
    }
}
