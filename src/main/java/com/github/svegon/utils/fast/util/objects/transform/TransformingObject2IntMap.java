package com.github.svegon.utils.fast.util.objects.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.AbstractObject2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingObject2IntMap<K_IN, V_IN, M extends Map<K_IN, V_IN>, K_OUT>
        extends AbstractObject2IntMap<K_OUT> {
    protected final M m;

    protected TransformingObject2IntMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry<K_OUT>> object2IntEntrySet();

    @Override
    public abstract int put(K_OUT key, int value);

    @Override
    public abstract int removeInt(Object key);

    @Override
    public abstract int getInt(Object key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>, K_OUT>
            implements Entry<K_OUT> {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && Objects.equals(getKey(), entry.getKey())
                    && (entry instanceof Entry<?> intEntry ? getIntValue() == intEntry.getIntValue()
                    : getValue().equals(entry.getValue()));
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public final String toString() {
            return getKey() + "=" + getIntValue();
        }
    }
}
