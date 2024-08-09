package io.github.svegon.utils.fast.util.tuple.triplet;

public final class ObjectObjectObjectImmutableTriplet<T, U, V> extends AbstractTriplet<T, U, V> {
    private final T first;
    private final U second;
    private final V third;

    public ObjectObjectObjectImmutableTriplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public T first() {
        return first;
    }

    @Override
    public U second() {
        return second;
    }

    @Override
    public V third() {
        return third;
    }
}
