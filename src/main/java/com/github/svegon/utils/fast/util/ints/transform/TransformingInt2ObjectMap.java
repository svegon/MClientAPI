package com.github.svegon.utils.fast.util.ints.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingInt2ObjectMap<K_IN, V_IN, M extends Map<K_IN, V_IN>, V_OUT>
        extends AbstractInt2ObjectMap<V_OUT> {
    protected final M m;

    protected TransformingInt2ObjectMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry<V_OUT>> int2ObjectEntrySet();

    @Override
    public abstract V_OUT put(int key, V_OUT value);

    @Override
    public abstract V_OUT remove(int key);

    @Override
    public abstract V_OUT get(int key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>, V_OUT>
            implements Entry<V_OUT> {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Entry<?> intEntry
                    ? getIntKey() == intEntry.getIntKey() : Objects.equals(getKey(), entry.getKey()))
                    && Objects.equals(entry.getValue(), getValue());
        }

        @Override
        public final int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getIntKey() + "=" + getValue();
        }
    }
}
