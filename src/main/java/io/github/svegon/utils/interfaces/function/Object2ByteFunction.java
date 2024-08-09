package io.github.svegon.utils.interfaces.function;

@FunctionalInterface
public interface Object2ByteFunction<T> extends it.unimi.dsi.fastutil.objects.Object2ByteFunction<T> {
    byte applyAsByte(T o);

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    default byte getByte(Object o) {
        return applyAsByte((T) o);
    }

    @Deprecated
    @Override
    default Byte apply(T t) {
        return applyAsByte(t);
    }
}
