package io.github.svegon.utils.interfaces.function;

@FunctionalInterface
public interface Object2CharFunction<T> extends it.unimi.dsi.fastutil.objects.Object2CharFunction<T> {
    char applyAsChar(T o);

    @Deprecated
    @Override
    default Character apply(T t) {
        return applyAsChar(t);
    }

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default char getChar(Object key) {
        return applyAsChar((T) key);
    }
}
