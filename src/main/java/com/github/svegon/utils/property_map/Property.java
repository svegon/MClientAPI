package com.github.svegon.utils.property_map;

import java.util.Objects;

public interface Property<E> {
    E get();

    void set(E value);

    default E getAndSet(E value) {
        E prev = get();
        set(value);
        return prev;
    }

    default E setAndGet(E value) {
        set(value);
        return value;
    }

    default boolean compareAndSet(E value) {
        E prev = get();
        set(value);
        return Objects.equals(prev, value);
    }

    static <E> Property<E> simple() {
        return new SimpleProperty<>();
    }

    static <E> Property<E> simple(final E value) {
        return new SimpleProperty<>(value);
    }

    final class SimpleProperty<E> implements Property<E> {
        private E v;

        private SimpleProperty() {

        }

        private SimpleProperty(E value) {
            v = value;
        }

        @Override
        public E get() {
            return v;
        }

        @Override
        public void set(E value) {
            v = value;
        }
    }
}
