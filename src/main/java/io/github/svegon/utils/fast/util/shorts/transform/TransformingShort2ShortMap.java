package io.github.svegon.utils.fast.util.shorts.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.AbstractShort2ObjectMap;
import it.unimi.dsi.fastutil.shorts.AbstractShort2ShortMap;
import it.unimi.dsi.fastutil.shorts.Short2ShortMap;

import java.util.Map;
import java.util.Objects;

public abstract class TransformingShort2ShortMap<K_IN, V_IN, M extends Map<K_IN, V_IN>> extends AbstractShort2ShortMap {
    protected final M m;

    protected TransformingShort2ShortMap(M m) {
        this.m = Preconditions.checkNotNull(m);
    }

    @Override
    public final int size() {
        return m.size();
    }

    @Override
    public abstract ObjectSet<Entry> short2ShortEntrySet();

    @Override
    public abstract short put(short key, short value);

    @Override
    public abstract short remove(short key);

    @Override
    public abstract short get(short key);

    protected static abstract class TransformingEntry<K_IN, V_IN, E extends Map.Entry<K_IN, V_IN>> implements Entry {
        public final E e;

        protected TransformingEntry(E e) {
            this.e = e;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Entry shortEntry
                    ? getShortKey() == shortEntry.getShortKey() && getShortValue() == shortEntry.getShortValue()
                    : Objects.equals(getKey(), entry.getKey()) && Objects.equals(entry.getValue(), getValue()));
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getShortKey() + "=" + getValue();
        }
    }
}
