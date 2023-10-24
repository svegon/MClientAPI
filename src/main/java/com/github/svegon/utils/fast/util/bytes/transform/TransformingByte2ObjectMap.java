package com.github.svegon.utils.fast.util.bytes.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.AbstractByte2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingByte2ObjectMap<K_IN, V_IN, M extends Map<K_IN, V_IN>, V_OUT>
        extends AbstractByte2ObjectMap<V_OUT> {
    protected final M m;

    protected TransformingByte2ObjectMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry<V_OUT>> byte2ObjectEntrySet();

    @Override
    public abstract V_OUT put(byte key, V_OUT value);

    @Override
    public abstract V_OUT remove(byte key);

    @Override
    public abstract V_OUT get(byte key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>, V_OUT>
            implements Entry<V_OUT> {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Entry<?> byteEntry
                    ? getByteKey() == byteEntry.getByteKey() : Objects.equals(getKey(), entry.getKey()))
                    && Objects.equals(entry.getValue(), getValue());
        }

        @Override
        public final int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getByteKey() + "=" + getValue();
        }
    }
}
