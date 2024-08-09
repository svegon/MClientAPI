package io.github.svegon.utils.interfaces.function;

@FunctionalInterface
public interface ObjectHexaDoubleConsumer<T> {
    void consume(T o, double x1, double y1, double z1, double x2, double y2, double z2);
}
