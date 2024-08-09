package io.github.svegon.utils.fast.util.objects.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.AbstractObject2ShortMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingObject2ShortMap<K_IN, V_IN, M extends Map<K_IN, V_IN>, K_OUT>
        extends AbstractObject2ShortMap<K_OUT> {
    protected final M m;

    protected TransformingObject2ShortMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry<K_OUT>> object2ShortEntrySet();

    @Override
    public abstract short put(K_OUT key, short value);

    @Override
    public abstract short removeShort(Object key);

    @Override
    public abstract short getShort(Object key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>, K_OUT>
            implements Entry<K_OUT> {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && Objects.equals(getKey(), entry.getKey())
                    && (entry instanceof Entry<?> shortEntry ? getShortValue() == shortEntry.getShortValue()
                    : getValue().equals(entry.getValue()));
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Short.hashCode(getShortValue());
        }

        @Override
        public final String toString() {
            return getKey() + "=" + getShortValue();
        }
    }
}
