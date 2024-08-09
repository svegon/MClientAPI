package io.github.svegon.utils.interfaces.function;

@FunctionalInterface
public interface ObjectLong2LongFunction<T> {
    long apply(T o, long l);
}
