package com.github.svegon.utils.fast.util.chars.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.chars.AbstractChar2ObjectMap;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingChar2ObjectMap<K_IN, V_IN, M extends Map<K_IN, V_IN>, V_OUT>
        extends AbstractChar2ObjectMap<V_OUT> {
    protected final M m;

    protected TransformingChar2ObjectMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry<V_OUT>> char2ObjectEntrySet();

    @Override
    public abstract V_OUT put(char key, V_OUT value);

    @Override
    public abstract V_OUT remove(char key);

    @Override
    public abstract V_OUT get(char key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>, V_OUT>
            implements Entry<V_OUT> {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Entry<?> charEntry
                    ? getCharKey() == charEntry.getCharKey() : Objects.equals(getKey(), entry.getKey()))
                    && Objects.equals(entry.getValue(), getValue());
        }

        @Override
        public final int hashCode() {
            return Character.hashCode(getCharKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getCharKey() + "=" + getValue();
        }
    }
}
