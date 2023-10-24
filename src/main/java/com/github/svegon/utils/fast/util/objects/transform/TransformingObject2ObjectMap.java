package com.github.svegon.utils.fast.util.objects.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.AbstractObject2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingObject2ObjectMap<K_IN, V_IN, M extends Map<K_IN, V_IN>, K_OUT, V_OUT>
        extends AbstractObject2ObjectMap<K_OUT, V_OUT> {
    protected final M m;

    protected TransformingObject2ObjectMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry<K_OUT, V_OUT>> object2ObjectEntrySet();

    @Override
    public abstract V_OUT put(K_OUT key, V_OUT value);

    @Override
    public abstract V_OUT remove(Object key);

    @Override
    public abstract V_OUT get(Object key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>, K_OUT, V_OUT>
            implements Entry<K_OUT, V_OUT> {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && Objects.equals(getKey(), entry.getKey())
                    && Objects.equals(entry.getValue(), getValue());
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }
}
