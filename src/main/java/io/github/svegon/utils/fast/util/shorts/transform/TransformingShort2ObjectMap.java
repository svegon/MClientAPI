package io.github.svegon.utils.fast.util.shorts.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.AbstractObject2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.AbstractShort2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingShort2ObjectMap<K_IN, V_IN, M extends Map<K_IN, V_IN>, V_OUT>
        extends AbstractShort2ObjectMap<V_OUT> {
    protected final M m;

    protected TransformingShort2ObjectMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry<V_OUT>> short2ObjectEntrySet();

    @Override
    public abstract V_OUT put(short key, V_OUT value);

    @Override
    public abstract V_OUT remove(short key);

    @Override
    public abstract V_OUT get(short key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>, V_OUT>
            implements Entry<V_OUT> {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Entry<?> shortEntry
                    ? getShortKey() == shortEntry.getShortKey() : Objects.equals(getKey(), entry.getKey()))
                    && Objects.equals(entry.getValue(), getValue());
        }

        @Override
        public final int hashCode() {
            return Short.hashCode(getShortKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getShortKey() + "=" + getValue();
        }
    }
}
